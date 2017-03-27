/**
 * Copyright (C) 2013 - 2016 Wuhan University,
 *                           Center for Geographic Analysis, Harvard University
 * 
 * This program is free software; you can redistribute and/or modify it under 
 * the terms of the GNU General Public License version 2 as published by the 
 * Free Software Foundation.
 * 
 * This program is distributed WITHOUT ANY WARRANTY; even without the implied
 * WARRANTY OF MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 */
package org.geojmodelbuilder.semantic.serialization;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.jena.ontology.DatatypeProperty;
import org.apache.jena.ontology.Individual;
import org.apache.jena.ontology.ObjectProperty;
import org.apache.jena.ontology.OntClass;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.ontology.OntModelSpec;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.xmlbeans.XmlDateTime;
import org.apache.xmlbeans.impl.values.XmlDateTimeImpl;
import org.geojmodelbuilder.semantic.ont.OntModelUtil;
import org.geojmodelbuilder.semantic.ont.WorkflowOntModel;

import com.geojmodelbuilder.core.IDataFlow;
import com.geojmodelbuilder.core.ILink;
import com.geojmodelbuilder.core.INamespaceDefault;
import com.geojmodelbuilder.core.data.IComplexData;
import com.geojmodelbuilder.core.data.IData;
import com.geojmodelbuilder.core.instance.IInputParameter;
import com.geojmodelbuilder.core.instance.IOutputParameter;
import com.geojmodelbuilder.core.instance.IParameter;
import com.geojmodelbuilder.core.instance.IProcessInstance;
import com.geojmodelbuilder.core.provenance.IProcessProv;
import com.geojmodelbuilder.core.provenance.IWorkflowProv;
import com.geojmodelbuilder.core.utils.IDGenerator;
import com.geojmodelbuilder.core.utils.ValidateUtil;

/**
 * @author Mingda Zhang
 *
 */
public class Provenance2RDF {
	private IWorkflowProv workflowTrace;
	private WorkflowOntModel workflowOntModel;
	private OntModel individualOntModel;
	
	private Individual workflowIdl;
	
	private DatatypeProperty DP_Process_StartTime,DP_Process_EndTime,
								DP_Workflow_EndTime,DP_Workflow_StartTime,DP_EXE_hasValue,DP_Exe_hasLocation;
	
	private ObjectProperty OP_Process_RespondToInstance,Op_Exe_used,OP_Exe_generated,OP_Trace_hasExec,OP_Entity_RespondToInstance,OP_Exe_account;
	public Provenance2RDF(IWorkflowProv workflowTrace){
		this.workflowTrace = workflowTrace;
		this.workflowOntModel = WorkflowOntModel.getInstance();
		this.individualOntModel = ModelFactory.createOntologyModel( OntModelSpec.OWL_MEM );
		this.individualOntModel.setNsPrefixes(workflowOntModel.getNsPrefixes());
	}
	
	public boolean save(String path){
		System.out.println("save the workflow trace to "+ path);
		
		OntClass workflowCls = this.workflowOntModel.getClass(WorkflowOntModel.EXECUTION_WORKFLOW);
		workflowIdl = this.individualOntModel.createIndividual(IDGenerator.uri(this.workflowTrace),workflowCls);
		
		DP_Process_StartTime = this.workflowOntModel.getDatatypeProperty(WorkflowOntModel.DP_EXE_STARTEDATTIME);
		DP_Process_EndTime = this.workflowOntModel.getDatatypeProperty(WorkflowOntModel.DP_EXE_ENDEDATTIME);
		DP_Workflow_EndTime = this.workflowOntModel.getDatatypeProperty(WorkflowOntModel.DP_EXE_WOKFLOW_ENDTIME);
		DP_Workflow_StartTime = this.workflowOntModel.getDatatypeProperty(WorkflowOntModel.DP_EXE_WOKFLOW_STARTTIME);
		DP_EXE_hasValue = this.workflowOntModel.getDatatypeProperty(WorkflowOntModel.DP_EXE_HASVALUE);
		DP_Exe_hasLocation = this.workflowOntModel.getDatatypeProperty(WorkflowOntModel.DP_EXE_HASLOCATION);
		
		OP_Process_RespondToInstance = this.workflowOntModel.getObjectProperty(WorkflowOntModel.OP_EXE_TO_INS_PROCESS);
		OP_Trace_hasExec = this.workflowOntModel.getObjectProperty(WorkflowOntModel.OP_EXE_TO_INS_HASEXE);
		OP_Entity_RespondToInstance = this.workflowOntModel.getObjectProperty(WorkflowOntModel.OP_EXE_TO_INS_ARTIFACT);
		Op_Exe_used = this.workflowOntModel.getObjectProperty(WorkflowOntModel.OP_EXE_USED);
		OP_Exe_generated = this.workflowOntModel.getObjectProperty(WorkflowOntModel.OP_EXE_GENERATED);
		OP_Exe_account = this.workflowOntModel.getObjectProperty(WorkflowOntModel.OP_EXE_ACCOUNT);
		
		//information about the workflow
		String strValue = workflowTrace.getName();
		if(!ValidateUtil.isStrEmpty(strValue))
			workflowIdl.addLabel(strValue,"en");
		
		strValue = this.workflowTrace.getDescription();
		if(!ValidateUtil.isStrEmpty(strValue))
			workflowIdl.addComment(strValue, "en");;
		
		XmlDateTime xmlDate =new XmlDateTimeImpl();
		xmlDate.setDateValue(this.workflowTrace.getStartTime());
		workflowIdl.addLiteral(DP_Workflow_StartTime, xmlDate.getStringValue());
		xmlDate.setDateValue(this.workflowTrace.getEndTime());
		workflowIdl.addLiteral(DP_Workflow_EndTime, xmlDate.getStringValue());
		
		
		//information about the activity
		OntClass processCls = this.workflowOntModel.getClass(WorkflowOntModel.EXECUTION_PROCESS);
		List<IProcessProv> processTraces = this.workflowTrace.getProcesses();
		Map<IParameter, String> savedParamMap = new HashMap<IParameter, String>();
		//Activities
		for(IProcessProv processTrace:processTraces){
			Individual processIdl = this.individualOntModel.createIndividual(IDGenerator.uri(processTrace), processCls);
			xmlDate.setDateValue(processTrace.getStartTime());
			processIdl.addLiteral(DP_Process_StartTime, xmlDate.getStringValue());
			xmlDate.setDateValue(processTrace.getEndTime());
			processIdl.addLiteral(DP_Process_EndTime, xmlDate.getStringValue());
			
			processIdl.addProperty(OP_Exe_account, workflowIdl);
			
			if(!(processTrace.getProcess() instanceof IProcessInstance))
				continue;
					
			IProcessInstance processInstance = (IProcessInstance)processTrace.getProcess();
			processIdl.addProperty(OP_Process_RespondToInstance, IDGenerator.uri(processInstance));
			processIdl.addProperty(OP_Trace_hasExec, IDGenerator.uri(processInstance));
			

			//Artifact that associate with link
			List<ILink> links = processInstance.getLinks();
			for(ILink iLink: links){
				if (iLink.getSourceProcess() != processInstance) 
					continue;
				
				if(!(iLink instanceof IDataFlow))
					continue;
				
				IDataFlow dataFlow = (IDataFlow)iLink;
				IParameter output = (IParameter)dataFlow.getSourceExchange();
				IParameter input = (IParameter)dataFlow.getTargetExchange();
				
				Individual entityIdl = artifactCreator(output);
				entityIdl.addProperty(OP_Entity_RespondToInstance, IDGenerator.uri(input));
				entityIdl.addProperty(OP_Entity_RespondToInstance, IDGenerator.uri(output));
				entityIdl.addProperty(OP_Exe_account, workflowIdl);
				
				savedParamMap.put(input, entityIdl.getURI());
				savedParamMap.put(output, entityIdl.getURI());
			}
			
		}
		
		//Entities
		for(IProcessProv processTrace:processTraces){
			if(!(processTrace.getProcess() instanceof IProcessInstance))
				continue;
			Individual processIdl = this.individualOntModel.createIndividual(IDGenerator.uri(processTrace), processCls);
			IProcessInstance processInstance = (IProcessInstance) processTrace.getProcess();
			//generated
			List<IOutputParameter> outputs = processInstance.getOutputs();
			Individual entityIdl;
			for(IOutputParameter output:outputs){
				String uri = null;
				if(savedParamMap.containsKey(output)){
					uri = savedParamMap.get(output);
				}else {
					entityIdl = artifactCreator(output);
					uri = entityIdl.getURI();
					entityIdl.addProperty(OP_Entity_RespondToInstance, IDGenerator.uri(output));
					savedParamMap.put(output, uri);
				}
				processIdl.addProperty(OP_Exe_generated, uri);
			}
			
			//used 
			List<IInputParameter> inputs = processInstance.getInputs();
			for(IInputParameter input:inputs){
				String uri = null;
				
				if(savedParamMap.containsKey(input)){
					uri = savedParamMap.get(input);
				}else {
					entityIdl = artifactCreator(input);
					uri = entityIdl.getURI();
					entityIdl.addProperty(OP_Entity_RespondToInstance, IDGenerator.uri(input));
					savedParamMap.put(input, uri);
				}
				
				processIdl.addProperty(Op_Exe_used, uri);
			}
		}
		
		return OntModelUtil.getInstance().save(this.individualOntModel, path);
	}
	
	private Individual artifactCreator(IParameter parameter){
		OntClass artifactCls = this.workflowOntModel.getClass(WorkflowOntModel.EXECUTION_ARTIFACT);
		String ns = INamespaceDefault.EXECUTION_ARTIFACT + "Entity_" + IDGenerator.uuid();
		
		Individual artifactIdl = this.individualOntModel.createIndividual(ns, artifactCls);
		/*if(this.existArtifactList.contains(parameter.getID())){
			return artifactIdl;
		}else {
			this.existArtifactList.add(parameter.getID());
		}*/
		
		IData data = parameter.getData();
		String value = data.getValue().toString();
		String type = data.getType();
		
//		artifactIdl.addLabel(parameter.getName(), "en");
		if(!ValidateUtil.isStrEmpty(type))
			artifactIdl.addComment("type="+type,"en");
		
		if(data instanceof IComplexData){
			artifactIdl.addProperty(DP_Exe_hasLocation, value);
		}else {
			artifactIdl.addProperty(DP_EXE_hasValue, value);
		}
		
		artifactCls.addProperty(OP_Exe_account, workflowIdl);
		return artifactIdl;
	}
}
