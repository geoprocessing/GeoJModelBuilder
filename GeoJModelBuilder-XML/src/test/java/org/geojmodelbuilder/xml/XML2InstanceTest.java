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
import com.geojmodelbuilder.engine.impl.WorkflowExecutor;
import com.geojmodelbuilder.engine.impl.WorkflowExecutor.ExecutorStatus;
import com.geojmodelbuilder.xml.deserialization.XML2Instance;

/**
 * @author Mingda Zhang
 *
 */
public class XML2InstanceTest {
	public static void main(String[] args){
//		String path = "D:/Workspace/download.xml";
		String path = "D:/Workspace/water_Extraction_workflow_instance.xml";
		XML2Instance xml2Instance = new XML2Instance();
		IWorkflowInstance workflowInstance = xml2Instance.parse(new File(path));
		WorkflowExecutor executor2 = new WorkflowExecutor(workflowInstance);
		executor2.run();
		
		while (executor2.getStatus() == ExecutorStatus.RUNNING) {
			//System.err.println(executor2.getStatus());
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
		}
		
		
	}
}
