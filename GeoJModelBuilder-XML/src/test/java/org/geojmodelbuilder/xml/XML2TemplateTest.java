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
package org.geojmodelbuilder.xml;

import java.io.File;

import com.geojmodelbuilder.core.template.IWorkflowTemplate;
import com.geojmodelbuilder.xml.deserialization.XML2Template;

/**
 * @author Mingda Zhang
 *
 */
public class XML2TemplateTest {
	public static void main(String[] args){
		String path = "D:/Workspace/water_Extraction_workflow_template.xml";
		XML2Template xml2Template = new XML2Template();
		IWorkflowTemplate workflowTemplate = xml2Template.parse(new File(path));
		
		System.out.println("process count:" + workflowTemplate.getProcesses().size());
	}
}
