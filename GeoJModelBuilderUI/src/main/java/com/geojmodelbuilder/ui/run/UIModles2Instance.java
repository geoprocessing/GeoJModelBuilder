/**
 * Copyright (C) 2013 - 2016 Wuhan University
 * 
 * This program is free software; you can redistribute and/or modify it under 
 * the terms of the GNU General Public License version 2 as published by the 
 * Free Software Foundation.
 * 
 * This program is distributed WITHOUT ANY WARRANTY; even without the implied
 * WARRANTY OF MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 */
package com.geojmodelbuilder.ui.run;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.geojmodelbuilder.core.ILink;
import com.geojmodelbuilder.core.impl.DataFlowImpl;
import com.geojmodelbuilder.core.instance.IParameter;
import com.geojmodelbuilder.core.instance.IProcessInstance;
import com.geojmodelbuilder.core.instance.impl.WorkflowInstance;
import com.geojmodelbuilder.core.template.IPort;
import com.geojmodelbuilder.core.template.impl.SpatialMetadata;
import com.geojmodelbuilder.ui.models.ProcessInputArtifact;
import com.geojmodelbuilder.ui.models.ProcessOutputArtifact;
import com.geojmodelbuilder.ui.models.Workflow;
import com.geojmodelbuilder.ui.models.WorkflowCondition;
import com.geojmodelbuilder.ui.models.WorkflowProcess;
import com.geojmodelbuilder.ui.models.links.DataFlow;
import com.geojmodelbuilder.ui.models.links.NodeLink;

/**
 * @author Mingda Zhang
 * Condition is not considered.
 */
public class UIModles2Instance {
	private Workflow workflowRecipe;
	private WorkflowInstance workflowPlan;
	private StringBuffer errBuf;
	
	public UIModles2Instance(Workflow workflowRecipe){
		this.workflowRecipe = workflowRecipe;
		this.errBuf = new StringBuffer();
	}
	
	//map the value and mime type
	private boolean mapInput(ProcessInputArtifact inputPort, IParameter parameter){
		if(inputPort == null || parameter == null){
			this.errBuf.append("Neither InputPort nor Parmeter could be null.");
			return false;
		}
		
		if(parameter.getData() == null){
			this.errBuf.append("The parameter named "+ parameter.getName()+" must hold a data type.");
			return false;
		}
		
		Object value = null;
		String mimeType = null;
		value = inputPort.getValue();
		SpatialMetadata metadata = inputPort.getSptialDescription();
		if (metadata != null) {
			mimeType = metadata.getMimeType();
		}

		/*if(value == null){
			this.errBuf.append("There is no available value for parameter named "+ parameter.getName());
			return false;
		}*/
			
		parameter.getData().setValue(value);
		if(mimeType!=null && !mimeType.equals(""))
			parameter.getData().setType(mimeType);
		
		return true;
	}
	
	//map the mime type
	private boolean mapOutput(ProcessOutputArtifact outPutPort,IParameter parameter){
		if(outPutPort == null || parameter == null){
			this.errBuf.append("Neither OutputPort nor Parmeter could be null.");
			return false;
		}
		
		if(parameter.getData() == null){
			this.errBuf.append("The parameter named "+ parameter.getName()+" must hold a data type.");
			return false;
		}
		
		String mimeType;
		SpatialMetadata spatialMetadata = outPutPort.getSptialDescription();
		if(spatialMetadata != null){
			mimeType = spatialMetadata.getMimeType();
			if(mimeType!=null && !mimeType.equals("")){
				parameter.getData().setType(mimeType);
			}
		}
		
		return true;
	}
	
	
	public boolean transfer(){
		//There must be executable process candidates for all abstract processes
		if(!allHasCandidate())
			return false;
		
		Map<WorkflowProcess, IProcessInstance> temporalProcessMap = new HashMap<WorkflowProcess, IProcessInstance>();
		Map<IPort, IParameter> temporalParameterMap = new HashMap<IPort,IParameter>();
		
		workflowPlan= new WorkflowInstance();
		
		//Map inputs to executable process
		for(WorkflowProcess process:workflowRecipe.getAllProcess()){
			IProcessInstance processExec = process.getInstances().get(0);
			Map<IPort, IParameter> valueMap = process.getProcessExecMap(processExec);
			if(valueMap == null){
				this.errBuf.append("There is no mapping information from abstract process to executable one.");
				return false;
			}
			
			for(IPort artifact:valueMap.keySet()){
				IParameter parameter = valueMap.get(artifact);
				if(artifact instanceof ProcessInputArtifact){
					if(!mapInput((ProcessInputArtifact)artifact, parameter))
						return false;
				}else if (artifact instanceof ProcessOutputArtifact) {
					if(!mapOutput((ProcessOutputArtifact)artifact, parameter))
						return false;
				}
			}
			
			workflowPlan.addProcess(processExec);
			temporalProcessMap.put(process, processExec);
			temporalParameterMap.putAll(valueMap);
		}
		
		//parse the data flow
		for(WorkflowProcess process:workflowRecipe.getAllProcess()){
			
			//ignore the worklow condition temporally
			if(process instanceof WorkflowCondition)
				continue;
			
			List<ProcessOutputArtifact> outputArtifacts = process.getOutputArtifacts();
			
			for(ProcessOutputArtifact outputArtifact:outputArtifacts){
				List<NodeLink> nodeLinks =  outputArtifact.getOutLinks();
				
				for(NodeLink link:nodeLinks){
					if(!(link instanceof DataFlow))
						continue;
					
					DataFlow dataFlow = (DataFlow)link;
					IProcessInstance sourceExec = temporalProcessMap.get(dataFlow.getSourceProcess());
					IParameter sourceParameter = temporalParameterMap.get(dataFlow.getSourceExchange());
					IProcessInstance targetExec = temporalProcessMap.get(dataFlow.getTargetProcess());
					IParameter  targetParameter = temporalParameterMap.get(dataFlow.getTargetExchange());
					
					DataFlowImpl newDataflow = new DataFlowImpl(sourceExec, sourceParameter,targetExec,targetParameter );
					if(!this.containDataflow(sourceExec.getLinks(),newDataflow))
							sourceExec.addLink(newDataflow);
					
					if(!this.containDataflow(targetExec.getLinks(),newDataflow))
							targetExec.addLink(newDataflow);
				}
			}
			
		}
		
		return true;
	}

	public boolean containDataflow(List<ILink> links, DataFlowImpl dataflow ){
		
		for(ILink link :links){
			if(!(link instanceof DataFlowImpl))
				continue;
			
			DataFlowImpl existLink = (DataFlowImpl)link;
			if(existLink.getSourceProcess() == dataflow.getSourceProcess() && 
					existLink.getTargetProcess() == dataflow.getTargetProcess() && 
					existLink.getTargetExchange() == dataflow.getTargetExchange() &&
					existLink.getSourceExchange() == dataflow.getSourceExchange())
				return true;
		}
		
		return false;
	}
	public WorkflowInstance getExecutableWorkflow(){
		if(this.workflowPlan == null)
			transfer();
		
		return this.workflowPlan;
	}
	
	private boolean allHasCandidate(){
		for(WorkflowProcess process:workflowRecipe.getAllProcess()){
			List<? extends IProcessInstance> processExecs = process.getInstances();
			if (processExecs == null || processExecs.size() == 0) {
				this.errBuf.append( "There is no executable candidate for "+ process.getName() + "\n");
				return false;
			}
		}
		
		return true;
	}
	public String getErrInfo(){
		return this.errBuf.toString();
	}
}
