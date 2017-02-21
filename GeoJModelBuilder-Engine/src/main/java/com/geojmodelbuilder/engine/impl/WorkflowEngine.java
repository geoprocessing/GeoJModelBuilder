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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
import com.geojmodelbuilder.engine.IProcessEvent;
import com.geojmodelbuilder.engine.IProcessEvent.EventType;
import com.geojmodelbuilder.engine.IListener;
import com.geojmodelbuilder.engine.IPublisher;
import com.geojmodelbuilder.engine.IRecorder;
/**
 * Event type: StepPerformed --> IProcessTrace
 * 
 * @author Mingda Zhang
 *
 */
public class WorkflowEngine implements IEngine, IListener,IPublisher {

	private IWorkflow workflowExec;
	// To execute
	private ProcessExecutors executors;
	// Executed
	private WorkflowTrace workflowTrace;
	
	private ThreadPoolExecutor executorPool;
	private Logger logger;
	private IRecorder recorder;
	
	private Map<EventType, Listeners> eventContainer = null;
	
	public WorkflowEngine(IWorkflow workflowExec, IRecorder recorder) {
		this.workflowExec = workflowExec;
		//traceProcesses = new ArrayList<IProcessTrace>();
		workflowTrace = new WorkflowTrace(workflowExec);
		
		this.recorder = recorder;
		
		executors = new ProcessExecutors(workflowExec.getProcesses());
		executorPool = new ThreadPoolExecutor(5, 10, 200,
				TimeUnit.MILLISECONDS, new ArrayBlockingQueue<Runnable>(5));
		
		eventContainer = new HashMap<IProcessEvent.EventType, Listeners>();
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
				recordMsg("--- sumbit the " + executor.getProcess().getName()
						+ " to execute.");
				}
		}
		return true;
	}

	public synchronized void onEvent(IProcessEvent event) {
		EventType eventType = event.getType();
		IProcess source = event.getSource();
		if (eventType == EventType.Succeeded) {
			if (source instanceof IProcessTrace) {
				recordMsg("Execute the " + source.getName()
						+ " successfully.");
				IProcess process = ((IProcessTrace) source).getProcess();
				printOutputs(process);
				this.workflowTrace.addProcessTrace((IProcessTrace)source);
				this.executors.remove(process);
				this.sendEvent(new ProcessEvent(EventType.StepPerformed, source));
			}
			
			if(isFinished()){
				this.dispose();
			}
			
		} else if (eventType == EventType.Failed) {
			recordMsg("Fail to execute " + source.getName());
			
			if (source instanceof IProcessTrace) {
				IProcess process = ((IProcessTrace)source).getProcess();
				
				if (process instanceof IProcessExec) {
					recordMsg("Error info:"
							+ ((IProcessExec) process).getErrInfo());
				}
				this.workflowTrace.addProcessTrace((IProcessTrace)source);
				this.executors.remove(process);
				
			}
			this.sendEvent(new ProcessEvent(EventType.StepPerformed, source));
			
			this.workflowTrace.setStatus(false);
			this.dispose();
			
		} else if (eventType == EventType.Ready) {
			ProcessExecutor executor = executors.getExecutor(source);
			this.executorPool.execute(executor);
			recordMsg("--- sumbit the " + source.getName() + " to execute.");
		}
		
	}

	private void dispose(){
		this.workflowTrace.setEndTime(new Date());
		
		this.executorPool.shutdownNow();
		
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd'T'HH:mm:ss.SSSZ");
		
		recordMsg("----------------The execution infomation of the workflow----------------");
		if(this.workflowTrace.getStatus()){
			recordMsg("The workflow was executed successfully.");
		}else {
			recordMsg("Failed to execute the workflow");
		}
		String startTime = dateFormat.format(this.workflowTrace.getStartTime());
		String endTime = dateFormat.format(this.workflowTrace.getEndTime());
		recordMsg("There are "+this.workflowTrace.getProcessTraces().size()+" processes executed and "+executors.size()+" left.");
		recordMsg("The workflow is executed from "+startTime+" to "+endTime);
		
		recordMsg("----------------The execution infomation of the processes----------------");
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
		
		this.sendEvent(new ProcessEvent(EventType.Stopped, null));
	}
	
	public WorkflowTrace getWorkflowTrace(){
		return this.workflowTrace;
	}
	
	//Not consider the workflow condition.
	public boolean isFinished(){
		if(this.executors.size()==0){
			this.workflowTrace.setStatus(true);
			return true;
		}
		return false;
	}
	
	private void recordMsg(String msg){
		this.recorder.appendMsg(msg);
		logger.info(msg);
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

	public void subscribe(IListener listener, EventType eventType) {
		Listeners listeners = eventContainer.get(eventType);
		if (listeners == null) {
			listeners = new Listeners();
			listeners.add(listener);
			eventContainer.put(eventType, listeners);
		} else {
			listeners.add(listener);
		}
	
	}

	public void unSubscribe(IListener listener, EventType eventType) {
		Listeners listeners = eventContainer.get(eventType);
		if (listeners == null)
			return;

		listeners.remove(listener);
	}

	public synchronized void sendEvent(IProcessEvent event) {
		Listeners listeners = eventContainer.get(event.getType());
		if (listeners == null)
			return;

		for (IListener listener : listeners) {
			listener.onEvent(event);
		}
	}
}
