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
package org.geojmodelbuilder.semantic;

import org.geojmodelbuilder.semantic.serialization.Template2RDF;

import com.geojmodelbuilder.core.template.examples.FloodAnalysisTemplate;

/**
 * @author Mingda Zhang
 *
 */
public class Template2RDFTest {

	public static void main(String[] args){
		FloodAnalysisTemplate workflowTemplate = new FloodAnalysisTemplate();
		Template2RDF template2rdf = new Template2RDF(workflowTemplate.getTemplateWorkflow(),true);
		template2rdf.save("E:/Workspace/opmw-g-v5/flood_analysis_workflow_template.rdf");
		System.out.println("end");
	}

}
