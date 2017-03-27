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

import java.util.List;

import org.apache.jena.ontology.DatatypeProperty;
import org.apache.jena.ontology.Individual;
import org.apache.jena.ontology.ObjectProperty;
import org.apache.jena.ontology.OntClass;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.ontology.OntModelSpec;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Resource;
import org.geojmodelbuilder.semantic.ont.OntModelUtil;
import org.geojmodelbuilder.semantic.ont.WorkflowOntModel;

import com.geojmodelbuilder.core.IDataFlow;
import com.geojmodelbuilder.core.IExchange;
import com.geojmodelbuilder.core.ILink;
import com.geojmodelbuilder.core.IProcess;
import com.geojmodelbuilder.core.data.IData;
import com.geojmodelbuilder.core.data.ILiteralData;
import com.geojmodelbuilder.core.instance.IInputParameter;
import com.geojmodelbuilder.core.instance.IOutputParameter;
import com.geojmodelbuilder.core.instance.IParameter;
import com.geojmodelbuilder.core.instance.IProcessInstance;
import com.geojmodelbuilder.core.instance.IWorkflowInstance;
import com.geojmodelbuilder.core.instance.InstanceProcessType;
import com.geojmodelbuilder.core.resource.ogc.wps.WPSProcess;
import com.geojmodelbuilder.core.utils.IDGenerator;
import com.geojmodelbuilder.core.utils.ValidateUtil;

/**
 * @author Mingda Zhang
 * Ignore condition temporarily.
 */
public class Instance2RDF {
	private IWorkflowInstance workflowExec;
	private WorkflowOntModel workflowOntModel;
	private OntModel individualOntModel;
//	private String workflowNs,processNs,artifactNs;
	
	//Object property
	ObjectProperty OP_hasProcess,OP_uses,OP_isGeneratedBy,OP_hasSourceProcess,OP_hasSourceVar,OP_hasTargetVar,OP_hasTargetProcess;
	
	//Datatype property
	DatatypeProperty DP_hasValue,DP_hasMimeType;
	
	public Instance2RDF(IWorkflowInstance workflowExec){
		this.workflowExec = workflowExec;
		this.workflowOntModel = WorkflowOntModel.getInstance();
		
		OP_hasProcess = this.workflowOntModel.getObjectProperty(WorkflowOntModel.OP_INS_HASSTEP);
		OP_uses = this.workflowOntModel.getObjectProperty(WorkflowOntModel.OP_TEM_USES);
		OP_isGeneratedBy = this.workflowOntModel.getObjectProperty(WorkflowOntModel.OP_TEM_ISGENERATEDBY);
		OP_hasSourceProcess = this.workflowOntModel.getObjectProperty(WorkflowOntModel.OP_HAS_SRC_PROCESS);
		OP_hasSourceVar= this.workflowOntModel.getObjectProperty(WorkflowOntModel.OP_HAS_SRC_VAR);
		OP_hasTargetProcess= this.workflowOntModel.getObjectProperty(WorkflowOntModel.OP_HAS_TAR_PROCESS);
		OP_hasTargetVar= this.workflowOntModel.getObjectProperty(WorkflowOntModel.OP_HAS_TAR_VAR);
		
		DP_hasValue = this.workflowOntModel.getDatatypeProperty(WorkflowOntModel.DP_TEM_HASVALUE);
		DP_hasMimeType = this.workflowOntModel.getDatatypeProperty(WorkflowOntModel.DP_TEM_HASMIMETYPE);
	
	}
	
	private boolean transfer(){
		this.individualOntModel = ModelFactory.createOntologyModel( OntModelSpec.OWL_MEM );
		this.individualOntModel.setNsPrefixes(workflowOntModel.getNsPrefixes());
	
		OntClass workflowCls = this.workflowOntModel.getClass(WorkflowOntModel.INSTANCE_WORKFLOW);
		
		/*String ns = ValidateUtil.isStrEmpty(this.workflowNs)? 
							this.workflowPlan.getNamespace():this.workflowNs;*/
			
		Individual workflowRS = this.individualOntModel.createIndividual(IDGenerator.uri(workflowExec), workflowCls);
		
		//information about the workflow
		String strValue = this.workflowExec.getName();
		if(!ValidateUtil.isStrEmpty(strValue))
			workflowRS.addLabel(strValue, "en");
		
		strValue = this.workflowExec.getDescription();
		if(!ValidateUtil.isStrEmpty(strValue))
			workflowRS.addComment(strValue,"en");
		
		//processes in workflow
		List<IProcessInstance> processes = this.workflowExec.getProcesses();
		for(IProcessInstance process:processes){
			Resource processRS = processCreator(process,this.individualOntModel);
			workflowRS.addProperty(OP_hasProcess, processRS);
			
			/*//input artifacts
			List<IInputParameter> inputs = process.getInputs();
			for(IInputParameter input:inputs){
				Resource inputRS = artifactCreator(input,this.individualOntModel);
				processRS.addProperty(OP_uses, inputRS);
			}
			
			//output artifact
			List<IOutputParameter> outputs = process.getOutputs();
			for(IOutputParameter output:outputs){
				Resource outputRS = artifactCreator(output,this.individualOntModel);
				outputRS.addProperty(OP_isGeneratedBy, processRS);
			}*/
		}
		
		//data dependencies
		//based on the existing resources
		for(IProcessInstance process:processes){
			List<ILink> links = process.getLinks();
			for(ILink link:links){
				if(link.getSourceProcess() == process){
					linkCreator(link,this.individualOntModel);
				}
			}
		}
		return true;
	}
	
	public OntModel getOntModel(){
		if(this.individualOntModel == null)
			transfer();
		
		return this.individualOntModel;
	}
	public boolean save(String path){
		if(this.individualOntModel == null)
			transfer();
		
		return OntModelUtil.getInstance().save(this.individualOntModel, path);
	}
	
	
	public Resource linkCreator(ILink link, OntModel ontModel){
		OntClass linkCls = this.workflowOntModel.getClass(WorkflowOntModel.IN_OUT_PORT);
		Resource linkRes = ontModel.createResource(IDGenerator.uri(link),linkCls);
		
		IProcess processSrc = link.getSourceProcess();
		Resource processSrcRes = ontModel.getResource(IDGenerator.uri(processSrc));
		linkRes.addProperty(OP_hasSourceProcess, processSrcRes);
		
		IProcess processTar = link.getTargetProcess();
		Resource processTarRes = ontModel.getResource(IDGenerator.uri(processTar));
		linkRes.addProperty(OP_hasTargetProcess, processTarRes);
		
		if(link instanceof IDataFlow){
			IDataFlow dataFlow = (IDataFlow)link;
			
			IExchange exchangeSrc = dataFlow.getSourceExchange();
			Resource resExchangeSrc = ontModel.getResource(IDGenerator.uri(exchangeSrc));
			linkRes.addProperty(OP_hasSourceVar, resExchangeSrc);
			
			IExchange exchangeTar = dataFlow.getTargetExchange();
			Resource resExchangeTar = ontModel.getResource(IDGenerator.uri(exchangeTar));
			linkRes.addProperty(OP_hasTargetVar, resExchangeTar);
		}
		
		return linkRes;
	}
	
	//Only consider the WPS process
	public Individual processCreator(IProcessInstance process,OntModel ontModel){
		OntClass processClass = null;
		if( process instanceof WPSProcess){
			processClass = this.workflowOntModel.getClass(WorkflowOntModel.INSTANCE_PROCESS_WPS);
		}else {
			processClass = this.workflowOntModel.getClass(WorkflowOntModel.INSTANCE_PROCESS);
		}
		
		Individual resource =  ontModel.createIndividual(IDGenerator.uri(process), processClass);
		
		if(process instanceof WPSProcess){
			DatatypeProperty dp_instanceType = this.workflowOntModel.getDatatypeProperty(WorkflowOntModel.DP_INS_INSTANCETYPE);
			resource.addLiteral(dp_instanceType,InstanceProcessType.WPSProcess.toString());
			DatatypeProperty dp_httpPost = this.workflowOntModel.getDatatypeProperty(WorkflowOntModel.DP_WPS_HTTPPOST);
			resource.addLiteral(dp_httpPost, ((WPSProcess)process).getWPSUrl());
		}
		resource.addLabel(process.getName(), "en");
		
		//input artifacts
		List<IInputParameter> inputs = process.getInputs();
		for(IInputParameter input:inputs){
			Resource inputRS = artifactCreator(input,ontModel);
			resource.addProperty(OP_uses, inputRS);
		}
		
		//output artifact
		List<IOutputParameter> outputs = process.getOutputs();
		for(IOutputParameter output:outputs){
			Resource outputRS = artifactCreator(output,ontModel);
			outputRS.addProperty(OP_isGeneratedBy, resource);
		}
		
		return resource;
	}
	
	
	public Individual artifactCreator(IParameter parameter,OntModel ontModel){
		IData data = parameter.getData();
		OntClass artifactCls =null;
		if (data instanceof ILiteralData) {
			artifactCls = this.workflowOntModel.getClass(WorkflowOntModel.INSTANCE_PARAMETER_VAR);
			
		}else {
			artifactCls = this.workflowOntModel.getClass(WorkflowOntModel.INSTANCE_DATA_VAR);
		}
		Individual resArtifact = ontModel.createIndividual(IDGenerator.uri(parameter),artifactCls);
		
		resArtifact.addLabel(parameter.getName(), "en");
		if(data != null){
			Object objValue = data.getValue();
			if(objValue != null){
				resArtifact.addProperty(DP_hasValue, objValue.toString());
			}
			String type = data.getType();
			if(type!=null){
				resArtifact.addProperty(DP_hasMimeType, type);
			}
		}
		return resArtifact;
	}
	
}
