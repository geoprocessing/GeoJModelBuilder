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

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.util.FileManager;
import org.apache.jena.vocabulary.VCARD;

/**
 * @author Mingda Zhang
 * https://jena.apache.org/tutorials/rdf_api.html
 */
public class JenaModel {

	public static void main(String[] args) {
		String personURI    = "http://somewhere/JohnSmith";
		String fullName     = "John Smith";
		Model model = ModelFactory.createDefaultModel();
		Resource johnSmith = model.createResource(personURI);
		johnSmith.addProperty(VCARD.FN, fullName);
		
		model.write(System.out);
		
		System.out.println("----------------------------------------");
		model.write(System.out, "RDF/XML-ABBREV");
		
		System.out.println("----------------------------------------");
		new JenaModel().readModel("E:/Workspace/rdf.txt");
		
	}

	
	public void readModel(String inputFileName){
		 // create an empty model
		 Model model = ModelFactory.createDefaultModel();

		 // use the FileManager to find the input file
		 InputStream in = FileManager.get().open( inputFileName );
		if (in == null) {
		    throw new IllegalArgumentException(
		                                 "File: " + inputFileName + " not found");
		}

		// read the RDF/XML file
		model.read(in, null);

		// write it to standard out
		model.write(System.out);
	}
}
