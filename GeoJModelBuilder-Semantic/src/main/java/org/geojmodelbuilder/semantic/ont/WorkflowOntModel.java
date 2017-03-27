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
package org.geojmodelbuilder.semantic.ont;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;

import org.apache.jena.ontology.DatatypeProperty;
import org.apache.jena.ontology.Individual;
import org.apache.jena.ontology.ObjectProperty;
import org.apache.jena.ontology.OntClass;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.ontology.OntModelSpec;
import org.apache.jena.ontology.OntologyException;
import org.apache.jena.rdf.model.ModelFactory;

/**
 * @author Mingda Zhang
 *
 */
public class WorkflowOntModel {
	private static WorkflowOntModel instance;
	private String owlName = "opso.owl";
	private OntModel ontModel;
	private HashMap<String, String> nsMap = null;
	private static String OPMW_PREFIX = "opmw:";
	private static String OPMO_PREFIX = "opmo:";
	private static String OPSO_PREFIX="opso:";
	private static String PROV_PREFIX="prov:";
	private static String RDF_PREFIX="rdf:";
	private static String RDFS_PREFIX="rdfs:";
	
	private static String OPSO_BASE= "http://www.gis.harvard.edu/opso/";
	private static String OPMW_BASE = "http://www.opmw.org/ontology/";
	private static String OPMO_BASE = "http://openprovenance.org/model/opmo#";
	private static String PROV_BASE = "http://www.w3.org/ns/prov#";
	private static String RDF_BASE = "http://www.w3.org/1999/02/22-rdf-syntax-ns#";
	private static String RDFS_BASE = "http://www.w3.org/2000/01/rdf-schema#";
	
	//Classes to represent workflow, process and artifact templates
	public static final String TEMPLATE_WORKFLOW = OPMW_PREFIX + "WorkflowTemplate";
	public static final String TEMPLATE_PROCESS = OPMW_PREFIX + "WorkflowTemplateProcess";
	public static final String TEMPLATE_ARTIFACT = OPMW_PREFIX + "WorkflowTemplateArtifact";
	
	//Object properties in templates
	public static final String OP_TEM_USES = OPMW_PREFIX + "uses";
	public static final String OP_TEM_ISGENERATEDBY = OPMW_PREFIX + "isGeneratedBy";
	
	//Data type properties in templates
	public static final String DP_TEM_HASVALUE= OPSO_PREFIX + "hasValue";
	public static final String DP_TEM_HASMIMETYPE= OPSO_PREFIX + "hasMimeType";
	
	
	//Classes to represent workflow, process and artifact instances
	public static final String INSTANCE_WORKFLOW = OPSO_PREFIX + "WorkflowInstance";
	public static final String INSTANCE_PROCESS = OPSO_PREFIX + "WorkflowInstanceProcess";
	public static final String INSTANCE_PROCESS_WPS = OPSO_PREFIX + "WPSProcess";
	public static final String INSTANCE_ARTIFACT = OPSO_PREFIX + "WorkflowInstanceArtifact";
	public static final String INSTANCE_DATA_VAR = OPSO_PREFIX + "InstanceDataVariable";
	public static final String INSTANCE_PARAMETER_VAR = OPSO_PREFIX + "InstanceParameterVariable";
	public static final String INSTANCE_WPSPROCESS = OPSO_PREFIX + "WPSProcessInstanceArtifact";
	
	//Properties in workflow instances
	public static final String OP_INS_HASSTEP = OPSO_PREFIX + "hasStep";
	public static final String DP_INS_INSTANCETYPE = OPSO_PREFIX + "instanceType";
		
	//Classes to represent executed workflow, process and artifact
	public static final String EXECUTION_WORKFLOW = OPMW_PREFIX + "WorkflowExecutionAccount";
	public static final String EXECUTION_PROCESS= OPMW_PREFIX + "WorkflowExecutionProcess";
	public static final String EXECUTION_ARTIFACT= OPMW_PREFIX + "WorkflowExecutionArtifact";
	
	//Properties among executed elements
	public static final String OP_EXE_USED= PROV_PREFIX + "used";
	public static final String OP_EXE_GENERATED= PROV_PREFIX + "generated";
	public static final String DP_EXE_HASLOCATION= OPMW_PREFIX + "hasLocation";
	public static final String DP_EXE_HASVALUE= OPMW_PREFIX + "hasValue";
	
	public static final String DP_EXE_STARTEDATTIME= PROV_PREFIX + "startedAtTime";
	public static final String DP_EXE_ENDEDATTIME= PROV_PREFIX + "endedAtTime";
	public static final String OP_EXE_ACCOUNT= OPMO_PREFIX + "account";
	public static final String DP_EXE_WOKFLOW_ENDTIME= OPMW_PREFIX + "overallEndTime";
	public static final String DP_EXE_WOKFLOW_STARTTIME= OPMW_PREFIX + "overallStartTime";
	
	//The data flow and control flow in template and instance
	public static final String IN_OUT_PORT= OPSO_PREFIX + "InOutPort";
	public static final String PORT_CONTROLLER= OPSO_PREFIX + "Controller";
	public static final String RULE_CONDITION = "http://spinrdf.org/sp#Ask";
	public static final String ELSEIF_CONTROLLER = OPSO_PREFIX + "ElseIf";
	public static final String WHILE_CONTROLLER = OPSO_PREFIX + "While";
	
	//Properties related to data flow
	public static final String OP_HAS_SRC_PROCESS = OPSO_PREFIX + "hasSourceProcess";
	public static final String OP_HAS_SRC_VAR = OPSO_PREFIX + "hasSourceVar";
	public static final String OP_HAS_TAR_PROCESS = OPSO_PREFIX + "hasTargetProcess";
	public static final String OP_HAS_TAR_VAR = OPSO_PREFIX + "hasTargetVar";
	
	//Properties related to control flow
	public static final String OP_CONTROLLED_BY = OPSO_PREFIX + "isControlledBy";
	public static final String OP_HAS_CONDITION = OPSO_PREFIX + "condition";
	public static final String OP_TRUE_THEN = OPSO_PREFIX + "trueThen";
	public static final String OP_FALSE_THEN = OPSO_PREFIX + "falseThen";
	public static final String OP_ISCONTROLLEDBY= OPSO_PREFIX + "isControlledBy";
	
	//Relations among templates (tem), instances (ins) and executions (exe)
	//Relations between executions and templates 
	public static final String OP_EXE_TO_TEM_WORKFLOW= OPMW_PREFIX + "correspondsToTemplate";
	public static final String OP_EXE_TO_TEM_PROCESS= OPMW_PREFIX + "correspondsToTemplateProcess";
	public static final String OP_EXE_TO_TEM_ARTIFACT= OPMW_PREFIX + "correspondsToTemplateArtifact";
	//Relations between templates and instances
	public static final String OP_TEM_TO_INS_WORKFLOW= OPSO_PREFIX + "hasInstanceWorkflow";
	public static final String OP_INS_TO_TEM_WORKFLOW= OPSO_PREFIX + "correspondsToTemplate";
	public static final String OP_TEM_TO_INS_PROCESS= OPSO_PREFIX + "hasInstanceProcess";
	public static final String OP_INS_TO_TME_PROCESS= OPSO_PREFIX + "correspondsToTemplateProcess";
	public static final String OP_TEM_TO_INS_ARTIFACT= OPSO_PREFIX + "hasInstanceArtifact";
	public static final String OP_INS_TO_TEM_ARTIFACT= OPSO_PREFIX + "correspondsToTemplateArtifact";
	//Relation between executions and instances
	public static final String OP_EXE_TO_INS_WORKFLOW= OPSO_PREFIX + "correspondsToInstance";
	public static final String OP_EXE_TO_INS_PROCESS= OPSO_PREFIX + "correspondsToInstanceProcess";
	public static final String OP_EXE_TO_INS_HASEXE= OPMW_PREFIX + "hasExecutableComponent";
	public static final String OP_EXE_TO_INS_ARTIFACT= OPSO_PREFIX + "correspondsToInstanceArtifact";
	
	//RDF 
	public static final String OP_RDF_TYPE= RDF_PREFIX + "type";
	
	//RDFS
//	public static final String DP_RDFS_LABEL= RDFS_PREFIX + "label";
//	public static final String DP_RDFS_COMMENT= RDFS_PREFIX + "comment";
	
	//WPS
	public static final String DP_WPS_HTTPPOST= OPSO_PREFIX + "httpPost";
	
	private WorkflowOntModel(){
		initialize();
	}
	
	public String completeURI(String uri){
		String[] parts = uri.split(":");
		if(parts.length == 0)
			return uri;
		
		String ns = nsMap.get(parts[0]);
		if (ns == null) 
			return uri;
		
		return ns + parts[1];
	}
	
	public static WorkflowOntModel getInstance(){
		if(instance == null){
			instance = new WorkflowOntModel();
		}
		return instance;
	}
	
	public OntClass getClass(String className){
		if(this.ontModel == null)
			throw new OntologyException("The base ontology model is not available");
			
		String uri = completeURI(className);
		return this.ontModel.getOntClass(uri);
	}
	
	public ObjectProperty getObjectProperty(String propertyName){
		if(this.ontModel == null)
			throw new OntologyException("The base ontology model is not available");
			
		String uri = completeURI(propertyName);
		ObjectProperty op = this.ontModel.getObjectProperty(uri);
		
		if(op == null)
			op = this.ontModel.createObjectProperty(uri);
		
		return op;
	}
	
	public DatatypeProperty getDatatypeProperty(String datatypeName){
		if(this.ontModel == null)
			throw new OntologyException("The base ontology model is not available");
			
		String uri = completeURI(datatypeName);
		DatatypeProperty dp = this.ontModel.getDatatypeProperty(uri);
		if (dp == null) {
			dp = this.ontModel.createDatatypeProperty(uri);
		}
		return dp;
	}
	
	/*public Individual createIndividual(String uri,Resource cls){
		return this.ontModel.createIndividual(uri, cls);
	}*/
	
	private void initialize(){
		nsMap = new HashMap<String, String>();
		nsMap.put(OPMW_PREFIX.substring(0, OPMW_PREFIX.length()-1),OPMW_BASE);
		nsMap.put(OPMO_PREFIX.substring(0, OPMO_PREFIX.length()-1),OPMO_BASE);
		nsMap.put(OPSO_PREFIX.substring(0, OPSO_PREFIX.length()-1),OPSO_BASE);
		nsMap.put(PROV_PREFIX.substring(0, PROV_PREFIX.length()-1),PROV_BASE);
		nsMap.put(RDF_PREFIX.substring(0, RDF_PREFIX.length()-1),RDF_BASE);
		nsMap.put(RDFS_PREFIX.substring(0, RDFS_PREFIX.length()-1),RDFS_BASE);
		
		ontModel = ModelFactory.createOntologyModel( OntModelSpec.OWL_MEM );
		InputStream in = this.getClass().getClassLoader().getResourceAsStream(this.owlName);
//		InputStream in = FileManager.get().open( owlName);
		if (in == null) {
			/*throw new IllegalArgumentException(
			                                 "File: " + owlName + " not found");*/
			throw new OntologyException("OWL is not available.");
		}
		
		ontModel.read(in,null);
		
		/*System.out.println("---------------List the Classes-----------------");
		Iterator<OntClass> ontClassIterator = ontModel.listClasses();
		int i = 0;
		for(;ontClassIterator.hasNext();){
			System.out.println("Class#"+(i++)+"-----");
			OntClass ontClass = ontClassIterator.next();
			System.out.println("name space:"+ontClass.getNameSpace());
			System.out.println("local name:"+ontClass.getLocalName());
			System.out.println("label:"+ontClass.getLabel(""));
			
		}*/
	}
	
	public HashMap<String, String> getNsPrefixes(){
		return this.nsMap;
	}
	
	public boolean existWorkflowTemplate(String path){
		return existWorkflow(path, WorkflowOntModel.TEMPLATE_WORKFLOW);
	}
	
	public boolean existWorkflowInstance(String path){
		return existWorkflow(path, WorkflowOntModel.INSTANCE_WORKFLOW);
	}
	
	public boolean existWorkflowProvenance(String path){
		return existWorkflow(path, WorkflowOntModel.EXECUTION_WORKFLOW);
	}
	
	private boolean existWorkflow(String path, String workflowClassURI){

		OntClass workflowCls = getInstance().getClass(workflowClassURI);
		OntModel targetOntModel = OntModelUtil.getInstance().read(path);
		if(targetOntModel == null)
			return false;
		
		Iterator<Individual> individuals = targetOntModel.listIndividuals(workflowCls);
		if(individuals == null || !individuals.hasNext())
			return false;
		
		targetOntModel.close();
		return true;
	}
	public static void main(String[] args){
		WorkflowOntModel workflowOntModel = WorkflowOntModel.getInstance();
		System.out.println("--------------Test Ontology class------------");
		OntClass testClass = workflowOntModel.getClass(IN_OUT_PORT);
		System.out.println(testClass.getNameSpace());
		System.out.println(testClass.getLocalName());
	}
}
