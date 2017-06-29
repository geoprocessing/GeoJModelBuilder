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
public class WorkflowExecution_ThreadExample{
	
	
	public static void main(String[] args){
		WaterExtractionInstance workflowPlan = new WaterExtractionInstance();
		WorkflowExecution_ThreadExample example = new WorkflowExecution_ThreadExample();
		Thread execThread = example.new ExecuteThread(workflowPlan.getWorkflow());
		
		System.out.println("Start");
		execThread.start();
		
		//wait for this thread
		try {
			execThread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		System.out.println("Over");
	}

	
	public class ExecuteThread extends Thread implements IListener{

		private WorkflowEngine workflowEngine;
		private Object blocked = new Object();
		public ExecuteThread(IWorkflowInstance workflowPlan){
			this.workflowEngine = new WorkflowEngine(workflowPlan, new RecorderImpl());
			this.workflowEngine.subscribe(this, EventType.Stopped);
		}
		
		@Override
		public void run() {
			super.run();
			this.workflowEngine.execute();
			synchronized (blocked) {
				try {
					blocked.wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			
			System.out.println("run finished");
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
				synchronized (blocked) {
					blocked.notify();
				}
			}
		}
	}
}
