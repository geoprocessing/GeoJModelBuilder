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

import org.geojmodelbuilder.semantic.deserialization.RDF2Instance;

import com.geojmodelbuilder.core.instance.IWorkflowInstance;
import com.geojmodelbuilder.engine.impl.WorkflowExecutor;

/**
 * @author Mingda Zhang
 *
 */
public class RDF2InstanceTest {
	public static void main(String[] args){
		String path = "E:/Workspace/opmw-g-v5/flood_analysis_workflow_instance.rdf";
		RDF2Instance rdf2Plan = new RDF2Instance();
		IWorkflowInstance workflowPlan = rdf2Plan.parse(path);
		WorkflowExecutor executor2 = new WorkflowExecutor(workflowPlan);
		executor2.run();
	}
}
