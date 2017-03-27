/**
 * Copyright (C) 2013 - 2016 Wuhan University
 * 
 * This program is free software; you can redistribute and/or modify it under 
 * the terms of the GNU General Public License version 2 as published by the 
 * Free Software Foundation.
 * 
 * This program is distributed WITHOUT ANY WARRANTY; even without the implied
 * WARRANTY OF MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 */
package com.geojmodelbuilder.engine.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.geojmodelbuilder.core.IProcess;
import com.geojmodelbuilder.core.instance.IWorkflowInstance;
import com.geojmodelbuilder.core.provenance.IProcessProv;
import com.geojmodelbuilder.engine.IListener;
import com.geojmodelbuilder.engine.IProcessEvent;
import com.geojmodelbuilder.engine.IProcessEvent.EventType;
/**
 * The executor is used to capture the status of the workflow execution.
 * 
 * @author Mingda Zhang
 *
 */
public class WorkflowExecutor implements IListener {

	private Logger logger;
	private WorkflowEngine workflowEngine;
	
	public WorkflowExecutor(IWorkflowInstance workflowExec){
		this.logger = LoggerFactory.getLogger(WorkflowExecutor.class);
		this.workflowEngine = new WorkflowEngine(workflowExec, new RecorderImpl());
		this.workflowEngine.subscribe(this, EventType.StepPerformed);
		this.workflowEngine.subscribe(this, EventType.Stopped);
	}
	
	public void run(){
		this.workflowEngine.execute();
	}
	public void onEvent(IProcessEvent event) {
		IProcess source = event.getSource();
		EventType eventType = event.getType();
		
		if(eventType == EventType.StepPerformed){
			if(source instanceof IProcessProv){
				IProcessProv processTrace = (IProcessProv)source;
				logger.info(processTrace.getName() + " is executed");
			}
		}else if (eventType == EventType.Stopped) {
			logger.info("Workflow engine is stopped");
		}
	}

}
