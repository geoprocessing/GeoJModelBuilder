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

import com.geojmodelbuilder.core.ILink;
import com.geojmodelbuilder.core.instance.IParameter;
import com.geojmodelbuilder.core.instance.IProcessInstance;
import com.geojmodelbuilder.core.instance.IWorkflowInstance;
import com.geojmodelbuilder.core.template.IInputPort;
import com.geojmodelbuilder.core.template.IOutPutPort;
import com.geojmodelbuilder.core.template.IPort;
import com.geojmodelbuilder.core.template.IProcessTemplate;
import com.geojmodelbuilder.core.template.IWorkflowTemplate;
import com.geojmodelbuilder.core.template.impl.SpatialMetadata;
import com.geojmodelbuilder.core.utils.IDGenerator;
import com.geojmodelbuilder.core.utils.ValidateUtil;

/**
 * @author Mingda Zhang
 *
 */
public class Template2RDF {

	private IWorkflowTemplate workflowTemplate;
	private WorkflowOntModel workflowOntModel;
	private OntModel individualOntModel;
	private Instance2RDF instance2rdf;
	
	//Object property
	ObjectProperty OP_hasProcess,OP_uses,OP_isGeneratedBy;
	ObjectProperty op_tem_hasInstance_workflow,op_tem_hasInstance_process,op_tem_hasInstance_artifact;
	
	//Datatype property
	DatatypeProperty DP_hasMimeType;
	
	private boolean saveInstance = false;
	
	public Template2RDF(IWorkflowTemplate workflowTemplate,boolean saveInstance){

		this.workflowTemplate = workflowTemplate;
		this.workflowOntModel = WorkflowOntModel.getInstance();
		this.instance2rdf = new Instance2RDF(null);
		this.saveInstance = saveInstance;
		
		OP_hasProcess = this.workflowOntModel.getObjectProperty(WorkflowOntModel.OP_INS_HASSTEP);
		OP_uses = this.workflowOntModel.getObjectProperty(WorkflowOntModel.OP_TEM_USES);
		OP_isGeneratedBy = this.workflowOntModel.getObjectProperty(WorkflowOntModel.OP_TEM_ISGENERATEDBY);
		
		op_tem_hasInstance_workflow = this.workflowOntModel.getObjectProperty(WorkflowOntModel.OP_TEM_TO_INS_WORKFLOW);
		op_tem_hasInstance_process= this.workflowOntModel.getObjectProperty(WorkflowOntModel.OP_TEM_TO_INS_PROCESS);
		op_tem_hasInstance_artifact= this.workflowOntModel.getObjectProperty(WorkflowOntModel.OP_TEM_TO_INS_ARTIFACT);
		
		DP_hasMimeType = this.workflowOntModel.getDatatypeProperty(WorkflowOntModel.DP_TEM_HASMIMETYPE);
	
	}
	
	private boolean transfer(){
		this.individualOntModel = ModelFactory.createOntologyModel( OntModelSpec.OWL_MEM );
		this.individualOntModel.setNsPrefixes(workflowOntModel.getNsPrefixes());
	
		OntClass workflowCls = this.workflowOntModel.getClass(WorkflowOntModel.TEMPLATE_WORKFLOW);
		
		Individual workflowRS = this.individualOntModel.createIndividual(IDGenerator.uri(workflowTemplate), workflowCls);
		
		//associated workflow instances, reference
		for(IWorkflowInstance instance:workflowTemplate.getInstances()){
			workflowRS.addProperty(op_tem_hasInstance_workflow, IDGenerator.uri(instance));
		}
		
		//information about the workflow
		String strValue = this.workflowTemplate.getName();
		if(!ValidateUtil.isStrEmpty(strValue))
			workflowRS.addLabel(strValue, "en");
		
		strValue = this.workflowTemplate.getDescription();
		if(!ValidateUtil.isStrEmpty(strValue))
			workflowRS.addComment(strValue,"en");
		
		//processes in workflow
		List<IProcessTemplate> processes = this.workflowTemplate.getProcesses();
		for(IProcessTemplate process:processes){
			Resource processRS = processCreator(process,this.individualOntModel);
			workflowRS.addProperty(OP_hasProcess, processRS);
			
			//input artifacts
			List<? extends IInputPort> inputs = process.getInputs();
			for(IInputPort input:inputs){
				Resource inputRS = artifactCreator(input,this.individualOntModel);
				processRS.addProperty(OP_uses, inputRS);
			}
			
			//output artifact
			List<? extends IOutPutPort> outputs = process.getOutputs();
			for(IOutPutPort output:outputs){
				Resource outputRS = artifactCreator(output,this.individualOntModel);
				outputRS.addProperty(OP_isGeneratedBy, processRS);
			}
		}
		
		//data dependencies
		//based on the existing resources
		for(IProcessTemplate process:processes){
			List<ILink> links = process.getLinks();
			for(ILink link:links){
				if(link.getSourceProcess() == process){
					linkCreator(link);
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
	
	
	private Resource linkCreator(ILink link){
		return this.instance2rdf.linkCreator(link, this.individualOntModel);
	}
	
	//Only consider the WPS process
	public Individual processCreator(IProcessTemplate process,OntModel ontModel){
		OntClass processClass  = this.workflowOntModel.getClass(WorkflowOntModel.TEMPLATE_PROCESS);
		Individual resource =  ontModel.createIndividual(IDGenerator.uri(process), processClass);
		
		for(IProcessInstance processInstance:process.getInstances()){
			if(this.saveInstance){
				this.instance2rdf.processCreator(processInstance, ontModel);
			}
			resource.addProperty(op_tem_hasInstance_process, IDGenerator.uri(processInstance));
		}
		
		if(!ValidateUtil.isStrEmpty(process.getName()))
			resource.addLabel(process.getName(), "en");
		
		return resource;
	}
	
	
	public Individual artifactCreator(IPort port,OntModel ontModel){
		OntClass artifactCls  = this.workflowOntModel.getClass(WorkflowOntModel.TEMPLATE_ARTIFACT);
		Individual resArtifact = ontModel.createIndividual(IDGenerator.uri(port),artifactCls);

		if(!ValidateUtil.isStrEmpty(port.getName()))
			resArtifact.addLabel(port.getName(), "en");
		
		SpatialMetadata metadata = port.getSptialDescription();
		
		if(metadata != null){
			if(!ValidateUtil.isStrEmpty(metadata.getDescription()))
				resArtifact.addComment(metadata.getDescription(), "en");
			
			if(!ValidateUtil.isStrEmpty(metadata.getMimeType()))
				resArtifact.addLiteral(DP_hasMimeType, metadata.getMimeType());
		}
		if (port.getInstances() != null) {
			for(IParameter parameter:port.getInstances()){
				if(this.saveInstance){
					this.instance2rdf.artifactCreator(parameter, ontModel);
				}
				resArtifact.addProperty(op_tem_hasInstance_artifact, IDGenerator.uri(parameter));
			}
		}
		
		return resArtifact;
	}
}
