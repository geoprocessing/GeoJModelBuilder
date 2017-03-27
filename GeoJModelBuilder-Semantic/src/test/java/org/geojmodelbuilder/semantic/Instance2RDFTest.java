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

import org.geojmodelbuilder.semantic.serialization.Instance2RDF;

import com.geojmodelbuilder.core.instance.examples.FloodAnalysisInstance;

/**
 * @author Mingda Zhang
 *
 */
public class Instance2RDFTest {
	public static void main(String[] args){
		FloodAnalysisInstance workflowPlan = new FloodAnalysisInstance();
//		WaterExtractionPlan workflowPlan = new WaterExtractionPlan();
//		BufferOverlayPlan workflowPlan = new BufferOverlayPlan();
		Instance2RDF plan2rdf = new Instance2RDF(workflowPlan.getWorkflow());
		
		plan2rdf.save("E:/Workspace/opmw-g-v5/flood_analysis_workflow_instance.rdf");
	}
}
