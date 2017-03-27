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

import java.util.HashMap;
import java.util.Iterator;
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

import com.geojmodelbuilder.core.data.IData;
import com.geojmodelbuilder.core.data.impl.ComplexData;
import com.geojmodelbuilder.core.data.impl.LiteralData;
import com.geojmodelbuilder.core.impl.DataFlowImpl;
import com.geojmodelbuilder.core.instance.IInputParameter;
import com.geojmodelbuilder.core.instance.IOutputParameter;
import com.geojmodelbuilder.core.instance.IParameter;
import com.geojmodelbuilder.core.instance.IProcessInstance;
import com.geojmodelbuilder.core.instance.IWorkflowInstance;
import com.geojmodelbuilder.core.instance.InstanceProcessType;
import com.geojmodelbuilder.core.instance.impl.InputParameter;
import com.geojmodelbuilder.core.instance.impl.OutputParameter;
import com.geojmodelbuilder.core.instance.impl.Parameter;
import com.geojmodelbuilder.core.instance.impl.WorkflowInstance;
import com.geojmodelbuilder.core.resource.ogc.wps.WPSProcess;

/**
 * @author Mingda Zhang
 *
 */
public class RDF2Instance {
	private WorkflowOntModel workflowOntModel;
	private DatatypeProperty dp_httpPost,dp_hasType,dp_hasValue;
	private ObjectProperty op_tem_uses,op_tem_isgeneratedby,op_rdf_type,op_hasSrcProc,op_hasSrcVar,op_hasTarProc,op_hasTarVar,op_isControlledBy;
	private OntModel ontModel;
	private Map<String, IProcessInstance> mapProcess;
	private Map<String, IParameter> mapParameter;
	public RDF2Instance(){
		this.workflowOntModel = WorkflowOntModel.getInstance();
		
		mapProcess = new HashMap<String, IProcessInstance>();
		mapParameter = new HashMap<String, IParameter>();
		
		dp_httpPost = this.workflowOntModel.getDatatypeProperty(WorkflowOntModel.DP_WPS_HTTPPOST);
//		dp_rdfs_label = this.workflowOntModel.getDatatypeProperty(WorkflowOntModel.DP_RDFS_LABEL);
		dp_hasValue = this.workflowOntModel.getDatatypeProperty(WorkflowOntModel.DP_TEM_HASVALUE);
		dp_hasType= this.workflowOntModel.getDatatypeProperty(WorkflowOntModel.DP_TEM_HASMIMETYPE);
		
		op_tem_uses = this.workflowOntModel.getObjectProperty(WorkflowOntModel.OP_TEM_USES);
		op_tem_isgeneratedby = this.workflowOntModel.getObjectProperty(WorkflowOntModel.OP_TEM_ISGENERATEDBY);
		op_rdf_type = this.workflowOntModel.getObjectProperty(WorkflowOntModel.OP_RDF_TYPE);
		
		op_hasSrcProc = this.workflowOntModel.getObjectProperty(WorkflowOntModel.OP_HAS_SRC_PROCESS);
		op_hasSrcVar= this.workflowOntModel.getObjectProperty(WorkflowOntModel.OP_HAS_SRC_VAR);
		op_hasTarProc= this.workflowOntModel.getObjectProperty(WorkflowOntModel.OP_HAS_TAR_PROCESS);
		op_hasTarVar= this.workflowOntModel.getObjectProperty(WorkflowOntModel.OP_HAS_TAR_VAR);
		op_isControlledBy = this.workflowOntModel.getObjectProperty(WorkflowOntModel.OP_ISCONTROLLEDBY);
	}
	
	public IWorkflowInstance parse(String path){
		WorkflowInstance workflowPlan = new WorkflowInstance();
		/*ontModel = ModelFactory.createOntologyModel( OntModelSpec.OWL_MEM );
		InputStream in = FileManager.get().open(path);
		if (in == null) {
		    throw new IllegalArgumentException(
		                                 "File: " + path + " not found");
		}
		// read the RDF/XML file
		ontModel.read(in, "RDF/XML" );*/
		
		ontModel = OntModelUtil.getInstance().read(path);
		if(ontModel == null)
			return null;
		
		
		
		OntClass workflowCls = this.workflowOntModel.getClass(WorkflowOntModel.INSTANCE_WORKFLOW);
		Iterator<Individual> workflowIndividuals = ontModel.listIndividuals(workflowCls);
		
		
		if (!workflowIndividuals.hasNext()) 
			return null;
		
		Individual workflowIndividual = workflowIndividuals.next();
		
		DatatypeProperty dp_instanceType = this.workflowOntModel.getDatatypeProperty(WorkflowOntModel.DP_INS_INSTANCETYPE);
		
		ObjectProperty opHasStep = this.workflowOntModel.getObjectProperty(WorkflowOntModel.OP_INS_HASSTEP);
		NodeIterator nodeIterator = workflowIndividual.listPropertyValues(opHasStep);
		
		//Parse the steps including inputs and outputs
		while(nodeIterator.hasNext()){
			RDFNode nodeStep = nodeIterator.next();
			if(!(nodeStep instanceof Resource))
				continue;
			
			Resource resStep = (Resource)nodeStep;
			Individual indStep = ontModel.getIndividual(resStep.getURI());
			RDFNode node_type = indStep.getPropertyValue(dp_instanceType);
			
			if (node_type != null) {
				String instance_type = node_type.asLiteral().getString();
				if(instance_type.equals(InstanceProcessType.WPSProcess.toString())){
					WPSProcess wpsProcess = parseWPSProcess(indStep,this.ontModel);
					mapProcess.put(indStep.getURI(), wpsProcess);
					workflowPlan.addProcess(wpsProcess);
				}
			}
		}
		
		//Parse the links between the steps
		OntClass inoutportCls = this.workflowOntModel.getClass(WorkflowOntModel.IN_OUT_PORT);
		Iterator<Individual> inoutportIterator = ontModel.listIndividuals(inoutportCls);
		while(inoutportIterator.hasNext()){
			Individual indInOutPort = inoutportIterator.next();
			parseLink(indInOutPort);
		}
		
		workflowPlan.setID(workflowIndividual.getLocalName());
		workflowPlan.setNamespace(workflowIndividual.getNameSpace());
		return workflowPlan;
	}
	
	public void parseLink(Individual indLink){
		Resource resSrcProcess = indLink.getPropertyResourceValue(op_hasSrcProc);
		Resource resSrcVar = indLink.getPropertyResourceValue(op_hasSrcVar);
		Resource resTarProcess = indLink.getPropertyResourceValue(op_hasTarProc);
		Resource resTarVar = indLink.getPropertyResourceValue(op_hasTarVar);
		
		IProcessInstance srcProcess = mapProcess.get(resSrcProcess.getURI());
		IParameter srcVar = mapParameter.get(resSrcVar.getURI());
		IProcessInstance tarProcess = mapProcess.get(resTarProcess.getURI());
		IParameter tarVar = mapParameter.get(resTarVar.getURI());
		
		if (!indLink.hasProperty(op_isControlledBy)) {
			DataFlowImpl dataFlow = new DataFlowImpl(srcProcess, srcVar, tarProcess, tarVar);
			srcProcess.addLink(dataFlow);
			tarProcess.addLink(dataFlow);
			dataFlow.setID(indLink.getLocalName());
			dataFlow.setNamespace(indLink.getNameSpace());
		}
	}
	
	public WPSProcess parseWPSProcess(Individual resStep, OntModel ontModel){
		/*RDFNode node_label = resStep.getPropertyValue(dp_rdfs_label);
		if(node_label == null)
			return null;*/
		
		String label = resStep.getLabel("en");
		WPSProcess wpsProcess = new WPSProcess(label);
//		WPSProcess wpsProcess = new WPSProcess(node_label.asLiteral().toString());
		RDFNode node_httpurl = resStep.getPropertyValue(dp_httpPost);
		if(node_httpurl != null)
			wpsProcess.setWPSUrl(node_httpurl.toString());
		
		NodeIterator iteratorParameter = resStep.listPropertyValues(op_tem_uses);
		while (iteratorParameter.hasNext()) {
			RDFNode nodeParameter = iteratorParameter.next();
			if(!(nodeParameter instanceof Resource))
				continue;
			
			Resource resourceParameter = (Resource)nodeParameter;
			Individual indivParameter = ontModel.getIndividual(resourceParameter.getURI());
			IInputParameter input = parseInputParameter(wpsProcess,indivParameter);
			wpsProcess.addInput(input);
		}
		
		ResIterator resOutputItr = ontModel.listResourcesWithProperty(op_tem_isgeneratedby, resStep);
		while(resOutputItr.hasNext()){
			Resource resOutput = resOutputItr.next();
			Individual indiOutput = ontModel.getIndividual(resOutput.getURI());
			IOutputParameter output = parseOutputParameter(wpsProcess, indiOutput);
			wpsProcess.addOutput(output);
		}
		
		wpsProcess.setID(resStep.getLocalName());
		wpsProcess.setNamespace(resStep.getNameSpace());
		return wpsProcess;
	}
	
	public void parseParameter(Parameter parameter,Individual individualParameter){
		Resource rdfType = individualParameter.getPropertyResourceValue(op_rdf_type);
		IData data = null;
		if(rdfType == null || 
				rdfType.getURI().equals(this.workflowOntModel.completeURI(WorkflowOntModel.INSTANCE_DATA_VAR))){
			data = new ComplexData();
		}else {
			data = new LiteralData();
		}
		
		RDFNode nodeType = individualParameter.getPropertyValue(dp_hasType);
		if(nodeType != null){
			data.setType(nodeType.asLiteral().toString());
		}
		
		RDFNode nodeValue = individualParameter.getPropertyValue(dp_hasValue);
		if(nodeValue != null){
			data.setValue(nodeValue.asLiteral().toString());
		}
		
		parameter.setData(data);
		
		/*RDFNode nodeLabel = individualParameter.getPropertyValue(dp_rdfs_label);
		if(nodeLabel != null){
			parameter.setName(nodeLabel.asLiteral().toString());
		}*/
		String label = individualParameter.getLabel("en");
		parameter.setName(label);
		parameter.setID(individualParameter.getLocalName());
		parameter.setNamespace(individualParameter.getNameSpace());
		
		mapParameter.put(individualParameter.getURI(), parameter);
	}
	
	private IInputParameter parseInputParameter(IProcessInstance process,Individual indivParameter){
		InputParameter input = new InputParameter(process);
		parseParameter(input, indivParameter);
		return input;
	}
	
	private IOutputParameter parseOutputParameter(IProcessInstance process, Individual indiOutput){
		OutputParameter output = new OutputParameter(process);
		parseParameter(output, indiOutput);
		return output;
	}
}
