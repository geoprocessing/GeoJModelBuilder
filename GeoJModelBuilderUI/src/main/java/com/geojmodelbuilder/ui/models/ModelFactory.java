/**
 * Copyright (C) 2013 - 2016 Wuhan University,
 * 
 * This program is free software; you can redistribute and/or modify it under 
 * the terms of the GNU General Public License version 2 as published by the 
 * Free Software Foundation.
 * 
 * This program is distributed WITHOUT ANY WARRANTY; even without the implied
 * WARRANTY OF MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 */
package com.geojmodelbuilder.ui.models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.geojmodelbuilder.core.IDataFlow;
import com.geojmodelbuilder.core.IExchange;
import com.geojmodelbuilder.core.ILink;
import com.geojmodelbuilder.core.IProcess;
import com.geojmodelbuilder.core.data.IData;
import com.geojmodelbuilder.core.instance.IInputParameter;
import com.geojmodelbuilder.core.instance.IOutputParameter;
import com.geojmodelbuilder.core.instance.IParameter;
import com.geojmodelbuilder.core.instance.IProcessInstance;
import com.geojmodelbuilder.core.instance.IWorkflowInstance;
import com.geojmodelbuilder.core.template.IInputPort;
import com.geojmodelbuilder.core.template.IPort;
import com.geojmodelbuilder.core.template.IProcessTemplate;
import com.geojmodelbuilder.core.template.IWorkflowTemplate;
import com.geojmodelbuilder.core.template.impl.SpatialMetadata;
import com.geojmodelbuilder.core.utils.ValidateUtil;
import com.geojmodelbuilder.ui.models.links.DataFlow;
import com.geojmodelbuilder.ui.models.links.ProcessOutputLink;

/**
 * @author Mingda Zhang
 *
 */
public class ModelFactory {
	private static ModelFactory instance;
	
	private ModelFactory(){}
	
	public static ModelFactory getInstance(){
		if(instance == null)
			instance = new ModelFactory();
		
		return instance;
	}
	
	public Workflow createWorkflow(IWorkflowInstance workflowInstance){
		Workflow workflow = new Workflow();

		workflow.setName(workflowInstance.getName());
		workflow.setNamespace(workflowInstance.getNamespace());
		workflow.setId(workflowInstance.getID());
		
		//processes
		Map<IProcess, WorkflowProcess> templateInstanceMap = new HashMap<IProcess, WorkflowProcess>();
		for(IProcessInstance processTemplate:workflowInstance.getProcesses()){
			WorkflowProcess process = createProcess(processTemplate);
			workflow.addProcess(process);
			templateInstanceMap.put(processTemplate, process);
			process.setWorkflow(workflow);
		}
		
		//conditions
		
		//links
		for(IProcessInstance processTemplate:workflowInstance.getProcesses()){
			for(ILink link:processTemplate.getLinks()){
				if (link.getSourceProcess() != processTemplate) 
					continue;
				
				if(!(link instanceof IDataFlow))
					continue;
				
				IDataFlow idataFlow = (IDataFlow)link;
				IProcess processSrc = idataFlow.getSourceProcess();
				IProcess processTar = idataFlow.getTargetProcess();
				IExchange exchangeSrc = idataFlow.getSourceExchange();
				IExchange exchangeTar = idataFlow.getTargetExchange();
				
				if(processSrc == null || processTar == null || 
						exchangeSrc == null || exchangeTar == null)
					continue;
				
				WorkflowProcess workflowProcessSrc = templateInstanceMap.get(processSrc);
				WorkflowProcess workflowProcessTar = templateInstanceMap.get(processTar);
				
				if(workflowProcessSrc == null || workflowProcessTar == null)
					continue;
				
				ProcessOutputArtifact outputArtifact = workflowProcessSrc.getOutput(exchangeSrc.getName());
				ProcessInputArtifact inputArtifact = workflowProcessTar.getInput(exchangeTar.getName());
				
				DataFlow dataFlow = new DataFlow();
				dataFlow.setId(idataFlow.getID());
				dataFlow.setNamespace(idataFlow.getNamespace());
				
				dataFlow.setSourceArtifact(outputArtifact);
				dataFlow.setSourceProcess(workflowProcessSrc);
				dataFlow.setTargetArtifact(inputArtifact);
				dataFlow.setTargetProcess(workflowProcessTar);
				
				
				dataFlow.connect();
			}
		}
		
		ModelLayout.getInstance().addLayout(workflow);
		
		return workflow;
		
	}
	
	public Workflow createWorkflow(IWorkflowTemplate workflowTemplate){
		Workflow workflow = new Workflow();
		workflow.setName(workflowTemplate.getName());
		workflow.setNamespace(workflowTemplate.getNamespace());
		workflow.setId(workflowTemplate.getID());
		
		//processes
		Map<IProcess, WorkflowProcess> templateInstanceMap = new HashMap<IProcess, WorkflowProcess>();
		for(IProcessTemplate processTemplate:workflowTemplate.getProcesses()){
			WorkflowProcess process = createProcess(processTemplate);
			workflow.addProcess(process);
			templateInstanceMap.put(processTemplate, process);
			process.setWorkflow(workflow);
		}
		
		//conditions
		
		//links
		for(IProcessTemplate processTemplate:workflowTemplate.getProcesses()){
			for(ILink link:processTemplate.getLinks()){
				if (link.getSourceProcess() != processTemplate) 
					continue;
				
				if(!(link instanceof IDataFlow))
					continue;
				
				IDataFlow idataFlow = (IDataFlow)link;
				IProcess processSrc = idataFlow.getSourceProcess();
				IProcess processTar = idataFlow.getTargetProcess();
				IExchange exchangeSrc = idataFlow.getSourceExchange();
				IExchange exchangeTar = idataFlow.getTargetExchange();
				
				if(processSrc == null || processTar == null || 
						exchangeSrc == null || exchangeTar == null)
					continue;
				
				WorkflowProcess workflowProcessSrc = templateInstanceMap.get(processSrc);
				WorkflowProcess workflowProcessTar = templateInstanceMap.get(processTar);
				
				if(workflowProcessSrc == null || workflowProcessTar == null)
					continue;
				
				ProcessOutputArtifact outputArtifact = workflowProcessSrc.getOutput(exchangeSrc.getName());
				ProcessInputArtifact inputArtifact = workflowProcessTar.getInput(exchangeTar.getName());
				
				DataFlow dataFlow = new DataFlow();
				dataFlow.setId(idataFlow.getID());
				dataFlow.setNamespace(idataFlow.getNamespace());
				
				dataFlow.setSourceArtifact(outputArtifact);
				dataFlow.setSourceProcess(workflowProcessSrc);
				dataFlow.setTargetArtifact(inputArtifact);
				dataFlow.setTargetProcess(workflowProcessTar);
				
				
				dataFlow.connect();
			}
		}
		
		ModelLayout.getInstance().addLayout(workflow);
		return workflow;
	}
	
	private void copyValue(WorkflowArtifact artifact,IParameter parameter){
		IData data = parameter.getData();
		if(data == null)
			return;
		
		
		String value = null;
		if( data.getValue() != null)
			value = data.getValue().toString();
		
		if(!ValidateUtil.isStrEmpty(value))
			artifact.setValue(value);
		
		/*
		 String type = data.getType();
		 if(!ValidateUtil.isStrEmpty(type)){
			SpatialMetadata metadata = artifact.getSptialDescription();
			if(metadata == null){
				metadata = new SpatialMetadata();
				artifact.setSpatialDescription(metadata);
			}
			
			metadata.setMimeType(type);
		}*/
			
	}
	
	public WorkflowProcess createProcess(IProcessInstance processInstance){

		Map<IPort, IParameter> portParamMap = new HashMap<IPort, IParameter>();
		//process
		WorkflowProcess process = new WorkflowProcess();
		process.setName(processInstance.getName());
		process.setNamespace(processInstance.getNamespace());
		process.setId(processInstance.getID());
		
		//inputs 
		for(IInputParameter input:processInstance.getInputs()){
			ProcessInputArtifact inputArtifact = new ProcessInputArtifact();
			process.addInputArtifact(inputArtifact);
			inputArtifact.setOwner(process);
			copyArtifactInfo(inputArtifact, input);
			portParamMap.put(inputArtifact, input);
		}
		
		
		//outputs
		for(IOutputParameter outport:processInstance.getOutputs()){
			ProcessOutputArtifact outputArtifact = new ProcessOutputArtifact();
			process.addOutputArtifact(outputArtifact);
			outputArtifact.setOwner(process);
			copyArtifactInfo(outputArtifact, outport);
			
			ProcessOutputLink outputLink = new ProcessOutputLink(process, outputArtifact);
			outputLink.connect();
			
			portParamMap.put(outputArtifact, outport);
			
		}
		
		process.addProcessMap(processInstance, portParamMap);
		process.addExectableProcess(processInstance);
		
		return process;
	}
	
	public WorkflowProcess createProcess(IProcessTemplate processTemplate){
		
		Map<IParameter, WorkflowArtifact> paramPortMap = new HashMap<IParameter, WorkflowArtifact>();
		
		//process
		WorkflowProcess process = new WorkflowProcess();
		process.setName(processTemplate.getName());
		process.setNamespace(processTemplate.getNamespace());
		process.setId(processTemplate.getID());
		
		//inputs
		for(IInputPort inport:processTemplate.getInputs()){
			ProcessInputArtifact inputArtifact = new ProcessInputArtifact();
			process.addInputArtifact(inputArtifact);
			inputArtifact.setOwner(process);
			copyArtifactInfo(inputArtifact, inport);
			
			for(IParameter parameter:inport.getInstances()){
				paramPortMap.put(parameter, inputArtifact);
			}
			
			if(inport.getInstances()!=null && inport.getInstances().size()>0){
				copyValue(inputArtifact, inport.getInstances().get(0));
			}
		}
		
		//outputs
		for(IPort outport:processTemplate.getOutputs()){
			ProcessOutputArtifact outputArtifact = new ProcessOutputArtifact();
			process.addOutputArtifact(outputArtifact);
			outputArtifact.setOwner(process);
			copyArtifactInfo(outputArtifact, outport);
			
			ProcessOutputLink outputLink = new ProcessOutputLink(process, outputArtifact);
			outputLink.connect();
			
			for(IParameter parameter:outport.getInstances()){
				paramPortMap.put(parameter, outputArtifact);
			}
			
			if(outport.getInstances()!=null && outport.getInstances().size()>0){
				copyValue(outputArtifact, outport.getInstances().get(0));
			}
		}
		
		//instances and parameter maps
		for(IProcessInstance instance:processTemplate.getInstances()){
			process.addExectableProcess(instance);
			
			Map<IPort, IParameter> portParamMap = new HashMap<IPort, IParameter>();
			for(IParameter parameter:instance.getInputs()){
				IPort port = paramPortMap.get(parameter);
				portParamMap.put(port, parameter);
			}
			
			for(IParameter parameter:instance.getOutputs()){
				IPort port = paramPortMap.get(parameter);
				portParamMap.put(port, parameter);
			}
			
			process.addProcessMap(instance, portParamMap);
		}
		
		return process;
	}
	
	private void copyArtifactInfo(WorkflowArtifact artifact,IParameter parameter){
		if(!ValidateUtil.isStrEmpty(parameter.getName()))
			artifact.setName(parameter.getName());
		
		if(!ValidateUtil.isStrEmpty(parameter.getID()))
			artifact.setId(parameter.getID());
		
		if(!ValidateUtil.isStrEmpty(parameter.getNamespace()))
			artifact.setNamespace(parameter.getNamespace());
		
		SpatialMetadata metadataTar = artifact.getSptialDescription();
		if(metadataTar == null){
			metadataTar = new SpatialMetadata();
			artifact.setSpatialDescription(metadataTar);
		}
		
		IData idata = parameter.getData();
		if(idata != null ){
			if (idata.getValue() != null) {
				artifact.setValue(idata.getValue().toString());
			}
			if(idata.getType() != null){
				metadataTar.setMimeType(idata.getType());
			}
		}
			
	}
	
	private void copyArtifactInfo(WorkflowArtifact artifact,IPort port){
		if(!ValidateUtil.isStrEmpty(port.getName()))
			artifact.setName(port.getName());
		
		if(!ValidateUtil.isStrEmpty(port.getID()))
			artifact.setId(port.getID());
		
		if(!ValidateUtil.isStrEmpty(port.getNamespace()))
			artifact.setNamespace(port.getNamespace());
		
		SpatialMetadata metadataSrc = port.getSptialDescription();
		
		if (metadataSrc == null) 
			return;
		
		SpatialMetadata metadataTar = artifact.getSptialDescription();
		if(metadataTar == null){
			artifact.setSpatialDescription(metadataSrc);
			return;
		}
		
		metadataTar.setDescription(metadataSrc.getDescription());
		metadataTar.setKeyWords(metadataSrc.getKeyWords());
		metadataTar.setMimeType(metadataSrc.getMimeType());
	}
	
	/*
	public static void main(String[] args){
		String templatePath = "E:/Workspace/opmw-g-v5/FloodAnalysisWorkflowTemplate.rdf";
		List<String> instanceList = new ArrayList<String>();
		instanceList.add("E:/Workspace/opmw-g-v5/FloodAnalysisWorkflowInstance.rdf");
		RDF2Template rdf2Template = new RDF2Template(templatePath,instanceList);
//		RDF2Template rdf2Template = new RDF2Template(templatePath);
		IWorkflowTemplate workflowTemplate = rdf2Template.parse();
		Workflow workflow= ModelFactory.getInstance().createWorkflow(workflowTemplate);
		System.out.println(workflow.getName());
	}
	*/
}
