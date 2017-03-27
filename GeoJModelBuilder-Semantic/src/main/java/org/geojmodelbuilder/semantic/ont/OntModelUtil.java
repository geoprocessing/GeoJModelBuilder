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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.jena.ontology.OntModel;
import org.apache.jena.ontology.OntModelSpec;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.util.FileManager;

import com.geojmodelbuilder.core.utils.ValidateUtil;

/**
 * @author Mingda Zhang
 *
 */
public class OntModelUtil {
	private static OntModelUtil instance;
	
	private OntModelUtil(){}
	
	public static OntModelUtil getInstance(){
		if(instance == null)
			instance = new OntModelUtil();
		
		return instance;
	}
	
	public boolean save(OntModel ontModel,String path){
		
		File file = new File(path);
		if(!file.exists()){
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
				return false;
			}
		}
		
		OutputStream writer = null;
		try {
			writer = new FileOutputStream(file);
			ontModel.write(writer);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return false;
			
		}finally{
			if(writer!=null)
				try {
					writer.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
		
		return true;
	}
	
	public OntModel read(String path){
		if(ValidateUtil.isStrEmpty(path))
			return null;
		
		OntModel ontModel = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM );
		InputStream in = FileManager.get().open(path);
		if (in == null) {
		    throw new IllegalArgumentException(
		                                 "File: " + path + " not found");
		}
		
		ontModel.read(in, "RDF/XML" );
		
		return ontModel;
	}
}
