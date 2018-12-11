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

import java.util.ArrayList;
import java.util.List;

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
	private ExecutorStatus status;
	
	public WorkflowExecutor(IWorkflowInstance workflowExec){
		this.logger = LoggerFactory.getLogger(WorkflowExecutor.class);
		this.workflowEngine = new WorkflowEngine(workflowExec, new RecorderImpl());
		this.workflowEngine.subscribe(this, EventType.StepPerformed);
		this.workflowEngine.subscribe(this, EventType.Stopped);
	}
	
	public void run(){
		this.workflowEngine.execute();
		status = ExecutorStatus.RUNNING;
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
			boolean engineStatus = this.workflowEngine.getWorkflowTrace().getStatus();
			if(engineStatus)
				this.status = ExecutorStatus.SUCCEEDED;
			else 
				this.status = ExecutorStatus.FAILED;
		}
	}
	
	public ExecutorStatus getStatus(){
		return this.status;
	}
	
	public List<IProcess> getFailedIProcess()
	{
		if(this.status==ExecutorStatus.RUNNING || this.status == ExecutorStatus.SUCCEEDED)
			return null;
		
		List<IProcess> failedProcess = new ArrayList<IProcess>();
		for(IProcessProv processProv:this.workflowEngine.getWorkflowTrace().getProcesses()){
			if(!processProv.getStatus())
				failedProcess.add(processProv.getProcess());
		}
		
		return failedProcess;
	}
	
	public List<IProcess> getExecutedProcess()
	{
		List<IProcess> executedProcess = new ArrayList<IProcess>();
		for(IProcessProv processProv:this.workflowEngine.getWorkflowTrace().getProcesses()){
			if(!processProv.getStatus())
				executedProcess.add(processProv.getProcess());
		}
		
		return executedProcess;
	}
	
	public WorkflowEngine getEngine(){
		return this.workflowEngine;
	}
	
	/**
	 * the status of the workflow executor
	 * @author Mingda Zhang
	 *
	 */
	public enum ExecutorStatus {  
		  RUNNING, FAILED, SUCCEEDED  
	}
}
