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
package org.geojmodelbuilder.semantic;

import java.io.InputStream;
import java.util.Iterator;

import org.apache.jena.ontology.Individual;
import org.apache.jena.ontology.OntClass;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.ontology.OntModelSpec;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.util.FileManager;
import org.apache.xmlbeans.XmlDateTime;
import org.apache.xmlbeans.impl.values.XmlDateTimeImpl;
import org.geojmodelbuilder.semantic.ont.WorkflowOntModel;

/**
 * @author Mingda Zhang
 *
 */
public class JenaOntologyAPI {
	public static void main(String[] args){
//		new JenaOntologyAPI().test2();
		new JenaOntologyAPI().Test1();
//		new JenaOntologyAPI().readLocalRDF();
	}
	
	public void test2(){
		String rdf = "https://raw.githubusercontent.com/apache/jena/master/jena-core/src-examples/data/eswc-2006-09-21.rdf";
		String SOURCE = "http://www.eswc2006.org/technologies/ontology";
		String NS = SOURCE + "#";
		OntModel base = ModelFactory.createOntologyModel( OntModelSpec.OWL_MEM );
		base.read( rdf, "RDF/XML" );

		
		base.write(System.out);
		
		System.out.println("----------");
		// create the reasoning model using the base
		OntModel inf = ModelFactory.createOntologyModel( OntModelSpec.OWL_MEM_MICRO_RULE_INF, base );

		// create a dummy paper for this example
		OntClass paper = base.getOntClass( NS + "Paper" );
		Individual p1 = base.createIndividual( NS + "paper1", paper );

		// list the asserted types
		/*for (Iterator<Resource> i = p1.listRDFTypes(); i.hasNext(); ) {
		    System.out.println( p1.getURI() + " is asserted in class " + i.next() );
		}*/

		// list the inferred types
		p1 = inf.getIndividual( NS + "paper1" );
		/*for (Iterator<Resource> i = p1.listRDFTypes(); i.hasNext(); ) {
		    System.out.println( p1.getURI() + " is inferred to be in class " + i.next() );
		}*/
		
		inf.write(System.out);
	}
	public void Test1(){

		OntModel ontModel = ModelFactory.createOntologyModel( OntModelSpec.OWL_MEM );
		String baseNS = "http://www.eswc2006.org/technologies/ontology#";
		
		ontModel.read("https://raw.githubusercontent.com/apache/jena/master/jena-core/src-examples/data/eswc-2006-09-21.rdf");
		ontModel.write(System.out);
		/*String inputFileName = "E:/Workspace/eswc-2006-09-21.rdf";
		InputStream in = FileManager.get().open( "" );
			if (in == null) {
			    throw new IllegalArgumentException(
			                                 "File: " + inputFileName + " not found");
			}
			// read the RDF/XML file
			ontModel.read(in, null);
			// write it to standard out
			ontModel.write(System.out);*/
		
		
		OntClass ontClass = ontModel.getOntClass(baseNS+"Artefact");
		Individual p1 = ontModel.createIndividual("Http://myselet/"+"artefact1", ontClass);
		ontModel.write(System.out);
		
		OntModel individuals = ModelFactory.createOntologyModel( OntModelSpec.OWL_MEM );
//		individuals.add();
	
		individuals.createResource(p1);
		individuals.createResource(p1);
		individuals.createResource(baseNS+"artefact2", ontClass);
		individuals.createResource(baseNS+"artefact2", ontClass);
		System.out.println("---------individual------------");
		individuals.write(System.out);
	}
	
	public void readLocalRDF(){
		String inputFileName = "E:/Workspace/eswc-2006-09-21.rdf";
		InputStream in = FileManager.get().open( inputFileName);
		if (in == null) {
			throw new IllegalArgumentException(
			                                 "File: " + inputFileName + " not found");
		}
		OntModel ontModel = ModelFactory.createOntologyModel( OntModelSpec.OWL_MEM );
		// read the RDF/XML file
		ontModel.read(in, null);
		// write it to standard out
		ontModel.write(System.out);
		
		System.out.println("---------------List the Classes-----------------");
		Iterator<OntClass> ontClassIterator = ontModel.listClasses();
		int i = 0;
		for(;ontClassIterator.hasNext();){
			System.out.println("Class#"+(i++)+"-----");
			OntClass ontClass = ontClassIterator.next();
			System.out.println("local name:"+ontClass.getLocalName());
			System.out.println("label:"+ontClass.getLabel(""));
		}
		
	}
	
	public void doubleCreateIndividual(){

		OntClass workflowCls = WorkflowOntModel.getInstance().getClass(WorkflowOntModel.EXECUTION_WORKFLOW);
		Individual individualWorkflow = workflowCls.createIndividual("http://cnfejifeurtysljfeife/");
		System.out.println(individualWorkflow.getURI());
		
		System.out.println("-----create double or once------------");
		OntModel individualOntModel = ModelFactory.createOntologyModel( OntModelSpec.OWL_MEM );
		Individual individual = individualOntModel.createIndividual("http://www.end.cn/e/tetejitejitjeij",workflowCls);
		individual.addLabel("dfefe","en");
		Individual individual2 = individualOntModel.createIndividual("http://www.end.cn/e/tetejitejitjeij", workflowCls);
		individual2.addComment("add comment by 2", "en");
		
		individualOntModel.write(System.out);
		System.out.println("----------");
		
		String xmlDateStr = "2017-03-02T14:43:20.311-05:00";
		XmlDateTime xmlDateTime = new XmlDateTimeImpl();
		xmlDateTime.setStringValue(xmlDateStr);
//		Date date = xmlDateTime.getDateValue();
	}
}
