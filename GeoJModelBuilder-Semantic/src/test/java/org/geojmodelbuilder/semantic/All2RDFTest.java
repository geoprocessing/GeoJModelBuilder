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
import org.geojmodelbuilder.semantic.serialization.Provenance2RDF;
import org.geojmodelbuilder.semantic.serialization.Template2RDF;

import com.geojmodelbuilder.core.instance.IWorkflowInstance;
import com.geojmodelbuilder.core.provenance.IWorkflowProv;
import com.geojmodelbuilder.core.template.IWorkflowTemplate;
import com.geojmodelbuilder.core.template.examples.FloodAnalysisTemplate;
import com.geojmodelbuilder.engine.IListener;
import com.geojmodelbuilder.engine.IProcessEvent;
import com.geojmodelbuilder.engine.IProcessEvent.EventType;
import com.geojmodelbuilder.engine.impl.RecorderImpl;
import com.geojmodelbuilder.engine.impl.WorkflowEngine;

/**
 * @author Mingda Zhang
 *
 */
public class All2RDFTest implements IListener{
	
	private WorkflowEngine workflowEngine;
	private static String basePath = "E:/Workspace/opmw-g-v5/";
	
	public static void main(String[] args){
		
		FloodAnalysisTemplate floodAnalysisTemplate = new FloodAnalysisTemplate();
		IWorkflowTemplate workflowTemplate = floodAnalysisTemplate.getTemplateWorkflow();
		new Template2RDF(workflowTemplate,true).save(basePath + "FloodAnalysisWorkflowTemplate.rdf");
		
		IWorkflowInstance workflowExec = floodAnalysisTemplate.getInstanceWorkflow();
		new Instance2RDF(workflowExec).save(basePath + "FloodAnalysisWorkflowInstance.rdf");
		
		new All2RDFTest().run(workflowExec);
	}
	
	public void run(IWorkflowInstance workflowPlan){
		this.workflowEngine = new WorkflowEngine(workflowPlan, new RecorderImpl());
		this.workflowEngine.subscribe(this, EventType.Stopped);
		this.workflowEngine.execute();
	}
	
	public void onEvent(IProcessEvent event) {
		if (event.getType().equals(EventType.Stopped)) {
			IWorkflowProv workflowTrace = this.workflowEngine.getWorkflowTrace();
			boolean succeeded = workflowTrace.getStatus();
			if(!succeeded){
				System.err.println("this workflow failed to execute.");
			}else {
				System.out.println("Successful!");
				
				new Provenance2RDF(workflowTrace).save(basePath+"FloodAnalysisWorkflowProvenance.rdf");
			}
		}
		
	}
}
