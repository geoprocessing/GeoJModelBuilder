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

import org.geojmodelbuilder.semantic.serialization.Provenance2RDF;

import com.geojmodelbuilder.core.provenance.IWorkflowProv;
import com.geojmodelbuilder.core.provenance.examples.WaterExtractionProv;

/**
 * @author Mingda Zhang
 *
 */
public class Provenance2RdfTest {
	public static void main(String[] args){
		IWorkflowProv workflowTrace = new WaterExtractionProv().getWorkflow();
		Provenance2RDF trace2rdf = new Provenance2RDF(workflowTrace);
		trace2rdf.save("E:/Workspace/opmw-g-v5/flood_analysis_workflow_trace.rdf");
		System.out.println("end");
	}
}
