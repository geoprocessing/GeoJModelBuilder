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

import com.geojmodelbuilder.core.instance.IWorkflowInstance;
import com.geojmodelbuilder.core.template.examples.WaterExtraction;
import com.geojmodelbuilder.engine.impl.WorkflowExecutor;
import com.geojmodelbuilder.engine.impl.WorkflowExecutor.ExecutorStatus;
import com.geojmodelbuilder.xml.deserialization.XML2Instance;
import com.geojmodelbuilder.xml.serialization.Provenance2XML;


/**
 * @author Mingda Zhang
 *
 */
public class Provenance2XMLTest2 {
	public static void main(String[] args){
		//WaterExtraction workflowPlan = new WaterExtraction();
		String path = "D:/Workspace/water_extraction_vector_instance.xml";
		XML2Instance xml2Instance = new XML2Instance();
		IWorkflowInstance workflowInstance = xml2Instance.parse(new File(path));
		WorkflowExecutor executor2 = new WorkflowExecutor(workflowInstance);
		executor2.run();
		
		while (executor2.getStatus() == ExecutorStatus.RUNNING) {
			//System.err.println(executor2.getStatus());
			try {
				System.out.println("running");
				Thread.sleep(50);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		Provenance2XML instance2xml = new Provenance2XML(executor2.getEngine().getWorkflowTrace(),workflowInstance);
		
		instance2xml.save("D:/Workspace/water_extraction_vector_provenance.xml");
		
		System.out.println(instance2xml.xmlText());
		System.out.println("success");
	}
}
