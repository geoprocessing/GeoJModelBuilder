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

import com.geojmodelbuilder.core.template.examples.WaterExtractionAOI;
import com.geojmodelbuilder.core.template.examples.WaterExtractionToVector;
import com.geojmodelbuilder.core.template.impl.WorkflowTemplate;
import com.geojmodelbuilder.xml.serialization.Template2XML;


/**
 * @author Mingda Zhang
 *
 */
public class Template2XMLTest {
	public static void main(String[] args){
		/*WaterExtraction workflowPlan = new WaterExtraction();
		Template2XML template2xml = new Template2XML(workflowPlan.getTemplateWorkflow());
		template2xml.save("D:/Workspace/water_Extraction_workflow_template.xml");
		
		System.out.println(template2xml.xmlText());
		System.out.println("success");*/
		Template2XMLTest.run();
	}
	
	
	public static void run(){
		//WorkflowTemplate template = WaterExtractionAOI.Template();
		WorkflowTemplate template = WaterExtractionToVector.Template();
		Template2XML template2xml = new Template2XML(template);
//		template2xml.save("D:/Workspace/water_extraction_workflow_template_aoi.xml");
		template2xml.save("D:/Workspace/water_extraction_workflow_vector.xml");
		
		System.out.println(template2xml.xmlText());
		System.out.println("success");
	}
}
