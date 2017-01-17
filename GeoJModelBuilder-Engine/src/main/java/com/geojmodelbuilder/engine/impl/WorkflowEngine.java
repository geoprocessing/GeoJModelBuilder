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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.geojmodelbuilder.core.ILink;
import com.geojmodelbuilder.core.IProcess;
import com.geojmodelbuilder.core.IWorkflow;
import com.geojmodelbuilder.core.plan.IOutputParameter;
import com.geojmodelbuilder.core.plan.IProcessExec;
import com.geojmodelbuilder.core.trace.IProcessTrace;
import com.geojmodelbuilder.core.trace.impl.WorkflowTrace;
import com.geojmodelbuilder.engine.IEngine;
import com.geojmodelbuilder.engine.IEvent;
import com.geojmodelbuilder.engine.IEvent.EventType;
import com.geojmodelbuilder.engine.IListener;
/**
 * 
 * @author Mingda Zhang
 *
 */
public class WorkflowEngine implements IEngine, IListener {

	private IWorkflow workflowExec;
	// To execute
	private ProcessExecutors executors;
	// Executed
//	private List<IProcessTrace> traceProcesses;
	private WorkflowTrace workflowTrace;
	
	private ThreadPoolExecutor executorPool;
	private Logger logger;
	
	public WorkflowEngine(IWorkflow workflowExec) {
		this.workflowExec = workflowExec;
//		traceProcesses = new ArrayList<IProcessTrace>();
		workflowTrace = new WorkflowTrace(workflowExec);
		
		executors = new ProcessExecutors(workflowExec.getProcesses());
		executorPool = new ThreadPoolExecutor(5, 10, 200,
				TimeUnit.MILLISECONDS, new ArrayBlockingQueue<Runnable>(5));
		logger = LoggerFactory.getLogger(WorkflowEngine.class);
	}

	private List<ILink> getInLinks(IProcess process) {
		List<ILink> links = new ArrayList<ILink>();
		for (ILink link : process.getLinks()) {
			if (link.getTargetProcess() == process)
				links.add(link);
		}
		return links;
	}

	public IWorkflow getWorkflow() {
		return this.workflowExec;
	}

	/**
	 * Responsible for initialization and pick up the start process. The other
	 * processes will be triggered based on event mechanism.
	 */
	public boolean execute() {

		// Subscription
		for (ProcessExecutor executor : this.executors) {

			// This engine will be notified when the executor is executed
			// successfully or not.
			executor.subscribe(this, EventType.Succeeded);
			executor.subscribe(this, EventType.Failed);
			executor.subscribe(this, EventType.Ready);

			List<ILink> inLinks = getInLinks(executor.getProcess());
			/*
			 * if(inLinks == null) continue;
			 */

			for (ILink link : inLinks) {
				IProcess sourceProcess = link.getSourceProcess();
				ProcessExecutor sourceExecutor = executors
						.getExecutor(sourceProcess);
				sourceExecutor.subscribe(executor, EventType.Succeeded);
			}
		}

		this.workflowTrace.setStartTime(new Date());
		
		for (ProcessExecutor executor : this.executors) {
			if (executor.canExecute()) {
				executorPool.execute(executor);
				logger.info("--- sumbit the " + executor.getProcess().getName()
						+ " to execute.");
			}
		}
		return true;
	}

	public synchronized void onEvent(IEvent event) {
		EventType eventType = event.getType();
		IProcess source = event.getSource();
		if (eventType == EventType.Succeeded) {
			if (source instanceof IProcessTrace) {
				logger.info("Execute the " + source.getName()
						+ " successfully.");
				IProcess process = ((IProcessTrace) source).getProcess();
				printOutputs(process);
				this.workflowTrace.addProcessTrace((IProcessTrace)source);
				this.executors.remove(process);
			}
			
			if(isFinished()){
				this.dispose();
			}
			
		} else if (eventType == EventType.Failed) {
			logger.error("Fail to execute " + source.getName());
			if (source instanceof IProcessTrace) {
				IProcess process = ((IProcessTrace)source).getProcess();
				
				if (process instanceof IProcessExec) {
					logger.error("Error info:"
							+ ((IProcessExec) process).getErrInfo());
				}
				this.workflowTrace.addProcessTrace((IProcessTrace)source);
				this.executors.remove(process);
			}
			this.workflowTrace.setStatus(false);
			this.dispose();
			
		} else if (eventType == EventType.Ready) {
			ProcessExecutor executor = executors.getExecutor(source);
			this.executorPool.execute(executor);
			logger.info("--- sumbit the " + source.getName() + " to execute.");
		}
	}

	private void dispose(){
		this.workflowTrace.setEndTime(new Date());
		
		this.executorPool.shutdownNow();
		
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd'T'HH:mm:ss.SSSZ");
		
		logger.info("----------------The execution infomation of the workflow----------------");
		if(this.workflowTrace.getStatus()){
			logger.info("The workflow was executed successfully.");
		}else {
			logger.info("Failed to execute the workflow");
		}
		String startTime = dateFormat.format(this.workflowTrace.getStartTime());
		String endTime = dateFormat.format(this.workflowTrace.getEndTime());
		logger.info("There are "+this.workflowTrace.getProcessTraces().size()+" processes executed and "+executors.size()+" left.");
		logger.info("The workflow is executed from "+startTime+" to "+endTime);
		
		logger.info("----------------The execution infomation of the processes----------------");
		for(IProcessTrace trace:this.workflowTrace.getProcessTraces()){
			startTime = dateFormat.format(trace.getStartTime());
			endTime = dateFormat.format(trace.getEndTime());
			String status ="";
			if(trace.getStatus())
				status = "Succeeded: ";
			else {
				status = "Failed: ";
			}
			logger.info(status+trace.getName()+" executed from "+startTime+" to "+endTime);
		}
	}
	
	public WorkflowTrace getWorkflowTrace(){
		return this.workflowTrace;
	}
	
	public boolean isFinished(){
		if(this.executors.size()==0){
			this.workflowTrace.setStatus(true);
			return true;
		}
		return false;
	}
	
	private void printOutputs(IProcess process) {
		if (!(process instanceof IProcessExec)) 
			return;
		
		IProcessExec processExec = (IProcessExec)process;
		List<IOutputParameter> outputs = processExec.getOutputs();
		for(IOutputParameter output:outputs){
			logger.info(output.getName()+":"+output.getData().getValue());
		}
	}
}
