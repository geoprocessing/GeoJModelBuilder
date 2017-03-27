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
package org.geojmodelbuilder.semantic.deserialization;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.jena.ontology.DatatypeProperty;
import org.apache.jena.ontology.Individual;
import org.apache.jena.ontology.ObjectProperty;
import org.apache.jena.ontology.OntClass;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.rdf.model.NodeIterator;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.ResIterator;
import org.apache.jena.rdf.model.Resource;
import org.geojmodelbuilder.semantic.ont.OntModelUtil;
import org.geojmodelbuilder.semantic.ont.WorkflowOntModel;

import com.geojmodelbuilder.core.impl.DataFlowImpl;
import com.geojmodelbuilder.core.instance.IParameter;
import com.geojmodelbuilder.core.instance.IProcessInstance;
import com.geojmodelbuilder.core.template.IInputPort;
import com.geojmodelbuilder.core.template.IOutPutPort;
import com.geojmodelbuilder.core.template.IPort;
import com.geojmodelbuilder.core.template.IProcessTemplate;
import com.geojmodelbuilder.core.template.IWorkflowTemplate;
import com.geojmodelbuilder.core.template.impl.InputPort;
import com.geojmodelbuilder.core.template.impl.OutputPort;
import com.geojmodelbuilder.core.template.impl.Port;
import com.geojmodelbuilder.core.template.impl.ProcessTemplate;
import com.geojmodelbuilder.core.template.impl.SpatialMetadata;
import com.geojmodelbuilder.core.template.impl.WorkflowTemplate;
import com.geojmodelbuilder.core.utils.ValidateUtil;

/**
 * @author Mingda Zhang
 *
 */
public class RDF2Template {
	private WorkflowOntModel workflowOntModel;
	private DatatypeProperty dp_mimeType;
	private ObjectProperty op_tem_uses,op_tem_port_hasInstance,op_tem_process_hasInstance,op_tem_isgeneratedby,op_hasSrcProc,op_hasSrcVar,op_hasTarProc,op_hasTarVar,op_isControlledBy;
	private OntModel ontModel;
	private Map<String, IProcessTemplate> mapProcess;
	private Map<String, IPort> mapParameter;
	private String templateRdf;
	private Map<ProcessTemplate, List<String>> processInstanceMap;
	private Map<Port, List<String>> portInstanceMap;
	private List<IProcessInstance> processInstances;
	//Instances associated with template
	private List<String> instanceRdfs;
	private List<OntModel> instanceOntModels;
	
	private RDF2Instance rdf2Instance = null;
	public RDF2Template(String templateRdf){
		this.templateRdf = templateRdf;
		
		this.processInstanceMap = new HashMap<ProcessTemplate, List<String>>();
		this.portInstanceMap = new HashMap<Port, List<String>>();
		this.processInstances = new ArrayList<IProcessInstance>();
		this.instanceOntModels = new ArrayList<OntModel>();
	}
	
	public RDF2Template(String templateRdf,List<String> instanceRdfs){
		this(templateRdf);
		this.instanceRdfs = instanceRdfs;
	}
	
	
	public IWorkflowTemplate parse(){
		
		this.workflowOntModel = WorkflowOntModel.getInstance();
		
		WorkflowTemplate workflowTemplate = new WorkflowTemplate();
		
//		WorkflowInstance workflowPlan = new WorkflowInstance();
		
		ontModel = OntModelUtil.getInstance().read(this.templateRdf);
		if(ontModel == null)
			return null;
		
		//read the instances
		if(this.instanceRdfs != null){
			for(String instanceRdf:this.instanceRdfs){
				OntModel instanceOntModel = OntModelUtil.getInstance().read(instanceRdf);
				if(instanceOntModel != null)
					this.instanceOntModels.add(instanceOntModel);
			}
		}
		
		mapProcess = new HashMap<String, IProcessTemplate>();
		mapParameter = new HashMap<String, IPort>();
		
		dp_mimeType = this.workflowOntModel.getDatatypeProperty(WorkflowOntModel.DP_TEM_HASMIMETYPE);
		
		op_tem_uses = this.workflowOntModel.getObjectProperty(WorkflowOntModel.OP_TEM_USES);
		op_tem_isgeneratedby = this.workflowOntModel.getObjectProperty(WorkflowOntModel.OP_TEM_ISGENERATEDBY);
		
		op_tem_port_hasInstance = this.workflowOntModel.getObjectProperty(WorkflowOntModel.OP_TEM_TO_INS_ARTIFACT);
		op_tem_process_hasInstance = this.workflowOntModel.getObjectProperty(WorkflowOntModel.OP_TEM_TO_INS_PROCESS);
		
		op_hasSrcProc = this.workflowOntModel.getObjectProperty(WorkflowOntModel.OP_HAS_SRC_PROCESS);
		op_hasSrcVar= this.workflowOntModel.getObjectProperty(WorkflowOntModel.OP_HAS_SRC_VAR);
		op_hasTarProc= this.workflowOntModel.getObjectProperty(WorkflowOntModel.OP_HAS_TAR_PROCESS);
		op_hasTarVar= this.workflowOntModel.getObjectProperty(WorkflowOntModel.OP_HAS_TAR_VAR);
		op_isControlledBy = this.workflowOntModel.getObjectProperty(WorkflowOntModel.OP_ISCONTROLLEDBY);
		
		OntClass workflowCls = this.workflowOntModel.getClass(WorkflowOntModel.TEMPLATE_WORKFLOW);
		Iterator<Individual> workflowIndividuals = ontModel.listIndividuals(workflowCls);
		
		if (!workflowIndividuals.hasNext()) 
			return null;
		
		Individual workflowIndividual = workflowIndividuals.next();
		
		
		ObjectProperty opHasStep = this.workflowOntModel.getObjectProperty(WorkflowOntModel.OP_INS_HASSTEP);
		NodeIterator nodeIterator = workflowIndividual.listPropertyValues(opHasStep);
		
		//Parse the steps including inputs and outputs
		while(nodeIterator.hasNext()){
			RDFNode nodeStep = nodeIterator.next();
			if(!(nodeStep instanceof Resource))
				continue;
			
			Resource resStep = (Resource)nodeStep;
			Individual indStep = ontModel.getIndividual(resStep.getURI());
			
			if(indStep == null)
				continue;
			
			IProcessTemplate processTemplate = parseProcess(indStep);
			workflowTemplate.addProcess(processTemplate);
			
		}
		
		//Parse the links between the steps
		OntClass inoutportCls = this.workflowOntModel.getClass(WorkflowOntModel.IN_OUT_PORT);
		Iterator<Individual> inoutportIterator = ontModel.listIndividuals(inoutportCls);
		while(inoutportIterator.hasNext()){
			Individual indInOutPort = inoutportIterator.next();
			parseLink(indInOutPort);
		}
		
		workflowTemplate.setID(workflowIndividual.getLocalName());
		workflowTemplate.setNamespace(workflowIndividual.getNameSpace());
		String label = workflowIndividual.getLabel("en");
		if(!ValidateUtil.isStrEmpty(label))
			workflowTemplate.setName(label);
		
		//parse process instance
		
		for(ProcessTemplate template:this.processInstanceMap.keySet()){
			List<String> instanceUris = this.processInstanceMap.get(template);
			for(String uri:instanceUris){
				IProcessInstance wpsProcess = getInstanceProcess(uri);
				if (wpsProcess!=null) {
					template.addExecCandidate(wpsProcess);
					this.processInstances.add(wpsProcess);
				}
				
			}
		}
		
		
		//parse port instances
		for(Port port:this.portInstanceMap.keySet()){
			List<String> instanceUris = this.portInstanceMap.get(port);
			for(String uri:instanceUris){
				IParameter parameter = getParameter(uri);
				if(parameter != null)
					port.addParameter(parameter);
			}
		}
		
		return workflowTemplate;
	}
	
	public IProcessInstance getInstanceProcess(String uri){
		if(this.rdf2Instance == null)
			this.rdf2Instance = new RDF2Instance();
		Individual individual = this.ontModel.getIndividual(uri);
		if(individual != null)
			return rdf2Instance.parseWPSProcess(individual, this.ontModel);
		
		for(OntModel ontModel:this.instanceOntModels){
			individual = ontModel.getIndividual(uri);
			if(individual != null)
				return rdf2Instance.parseWPSProcess(individual, ontModel);
		}
		
		return null;
	}
	
	private IParameter getParameter(String uri){
		for(IProcessInstance processInstance:this.processInstances){
			for(IParameter parameter:processInstance.getInputs()){
				String paramerUri = parameter.getNamespace() + parameter.getID();
				if (paramerUri.equals(uri)) {
					return parameter;
				}
			}
			
			for(IParameter parameter:processInstance.getOutputs()){
				String paramerUri = parameter.getNamespace() + parameter.getID();
				if (paramerUri.equals(uri)) {
					return parameter;
				}
			}
		}
		
		return null;
	}
	public void parseLink(Individual indLink){
		Resource resSrcProcess = indLink.getPropertyResourceValue(op_hasSrcProc);
		Resource resSrcVar = indLink.getPropertyResourceValue(op_hasSrcVar);
		Resource resTarProcess = indLink.getPropertyResourceValue(op_hasTarProc);
		Resource resTarVar = indLink.getPropertyResourceValue(op_hasTarVar);
		
		IProcessTemplate srcProcess = mapProcess.get(resSrcProcess.getURI());
		IPort srcVar = mapParameter.get(resSrcVar.getURI());
		IProcessTemplate tarProcess = mapProcess.get(resTarProcess.getURI());
		IPort tarVar = mapParameter.get(resTarVar.getURI());
		
		if (!indLink.hasProperty(op_isControlledBy)) {
			DataFlowImpl dataFlow = new DataFlowImpl(srcProcess, srcVar, tarProcess, tarVar);
			srcProcess.addLink(dataFlow);
			tarProcess.addLink(dataFlow);
			dataFlow.setID(indLink.getLocalName());
			dataFlow.setNamespace(indLink.getNameSpace());
		}
	}
	
	public IProcessTemplate parseProcess(Individual resStep){
		
		String label = resStep.getLabel("en");
		ProcessTemplate processTemplate = new ProcessTemplate(label);
		
		NodeIterator iteratorParameter = resStep.listPropertyValues(op_tem_uses);
		while (iteratorParameter.hasNext()) {
			RDFNode nodeParameter = iteratorParameter.next();
			if(!(nodeParameter instanceof Resource))
				continue;
			
			Resource resourceParameter = (Resource)nodeParameter;
			Individual indivParameter = this.ontModel.getIndividual(resourceParameter.getURI());
			IInputPort input = parseInputParameter(processTemplate,indivParameter);
			processTemplate.addInput(input);
		}
		
		ResIterator resOutputItr = this.ontModel.listResourcesWithProperty(op_tem_isgeneratedby, resStep);
		while(resOutputItr.hasNext()){
			Resource resOutput = resOutputItr.next();
			Individual indiOutput = this.ontModel.getIndividual(resOutput.getURI());
			IOutPutPort output = parseOutputParameter(processTemplate, indiOutput);
			processTemplate.addOutput(output);
		}
		
		processTemplate.setID(resStep.getLocalName());
		processTemplate.setNamespace(resStep.getNameSpace());
		
		this.mapProcess.put(resStep.getURI(), processTemplate);
		
		NodeIterator iteratorInstances= resStep.listPropertyValues(op_tem_process_hasInstance);
		while (iteratorInstances.hasNext()) {
			RDFNode nodeInstance= iteratorInstances.next();
			
			String uri;
			if(nodeInstance instanceof Resource){
				uri = ((Resource)nodeInstance).getURI();
			}else {
				uri = nodeInstance.asLiteral().toString();
			}
			
			this.addInstance(processTemplate, uri);
		}
		
		return processTemplate;
	}
	
	//Input or output port
	public void parsePort(Port<? extends IParameter> port, Individual portIdl){
		String label = portIdl.getLabel("en");
		if(!ValidateUtil.isStrEmpty(label))
			port.setName(label);
		
		SpatialMetadata metadata = new SpatialMetadata();
		port.setSpatialDescription(metadata);
		
		String comment = portIdl.getComment("en");
		if(!ValidateUtil.isStrEmpty(comment))
			metadata.setDescription(comment);
		
		RDFNode mimeTypeNode = portIdl.getPropertyValue(dp_mimeType);
		if(mimeTypeNode != null){
			metadata.setMimeType(mimeTypeNode.asLiteral().toString());
		}
		
		NodeIterator iteratorParameter = portIdl.listPropertyValues(op_tem_port_hasInstance);
		
		
		while (iteratorParameter.hasNext()) {
			RDFNode nodeParameter = iteratorParameter.next();
			String uri;
			if (nodeParameter instanceof Resource) {
				uri = ((Resource)nodeParameter).getURI();
			}else {
				uri = nodeParameter.asLiteral().toString();
			}
			
			this.addInstance(port, uri);
		}
		port.setID(portIdl.getLocalName());
		port.setNamespace(portIdl.getNameSpace());
		
		mapParameter.put(portIdl.getURI(), port);
	}
	
	private IInputPort parseInputParameter(IProcessTemplate process,Individual indivParameter){
		InputPort input = new InputPort(process);
		parsePort(input, indivParameter);
		return input;
	}
	
	private IOutPutPort parseOutputParameter(IProcessTemplate process, Individual indiOutput){
		OutputPort output = new OutputPort(process);
		parsePort(output, indiOutput);
		return output;
	}
	
	public void addInstance(Port port,String uri){
		if(!this.portInstanceMap.containsKey(port)){
			List<String> uris = new ArrayList<String>();
			this.portInstanceMap.put(port, uris);
		}
		
		List<String> uris = this.portInstanceMap.get(port);
		if(!uris.contains(uri))
			uris.add(uri);
	}
	
	public void addInstance(ProcessTemplate process,String uri){

		if(!this.processInstanceMap.containsKey(process)){
			List<String> uris = new ArrayList<String>();
			this.processInstanceMap.put(process, uris);
		}
		
		List<String> uris = this.processInstanceMap.get(process);
		if(!uris.contains(uri))
			uris.add(uri);
	}
}
