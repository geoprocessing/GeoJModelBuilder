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

import java.util.ArrayList;
import java.util.List;

import org.geojmodelbuilder.semantic.deserialization.RDF2Template;

import com.geojmodelbuilder.core.template.IWorkflowTemplate;

/**
 * @author Mingda Zhang
 *
 */
public class RDF2TemplateTest {

	public static void main(String[] args) {
		String templatePath = "E:/Workspace/opmw-g-v5/FloodAnalysisWorkflowTemplate.rdf";
		List<String> instanceList = new ArrayList<String>();
		instanceList.add("E:/Workspace/opmw-g-v5/FloodAnalysisWorkflowInstance.rdf");
		RDF2Template rdf2Template = new RDF2Template(templatePath,instanceList);
//		RDF2Template rdf2Template = new RDF2Template(templatePath);
		IWorkflowTemplate workflowTemplate = rdf2Template.parse();
		System.out.println(workflowTemplate.getName());
	}

}
