/**
 * Copyright (C) 2013 - 2016 Wuhan University,
 * 
 * This program is free software; you can redistribute and/or modify it under 
 * the terms of the GNU General Public License version 2 as published by the 
 * Free Software Foundation.
 * 
 * This program is distributed WITHOUT ANY WARRANTY; even without the implied
 * WARRANTY OF MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 */
package com.geojmodelbuilder.engine;

import com.geojmodelbuilder.core.instance.IWorkflowInstance;
import com.geojmodelbuilder.core.instance.examples.WaterExtractionInstance;
import com.geojmodelbuilder.core.provenance.IWorkflowProv;
import com.geojmodelbuilder.engine.IProcessEvent.EventType;
import com.geojmodelbuilder.engine.impl.RecorderImpl;
import com.geojmodelbuilder.engine.impl.WorkflowEngine;

/**
 * @author Mingda Zhang
 *
 */
public class WorkflowExecution_WaterExtraction implements IListener{
	
	private WorkflowEngine workflowEngine;
	
	public WorkflowExecution_WaterExtraction(IWorkflowInstance workflowPlan){
		this.workflowEngine = new WorkflowEngine(workflowPlan, new RecorderImpl());
		this.workflowEngine.subscribe(this, EventType.Stopped);
	}
	public static void main(String[] args){
		WaterExtractionInstance workflowPlan = new WaterExtractionInstance();
		new WorkflowExecution_WaterExtraction(workflowPlan.getWorkflow()).run();
		System.out.println("end");
	}

	public void run(){
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
			}
		}
		
	}
}
