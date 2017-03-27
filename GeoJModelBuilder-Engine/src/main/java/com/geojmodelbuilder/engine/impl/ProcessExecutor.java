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

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.geojmodelbuilder.core.IBranchControl;
import com.geojmodelbuilder.core.ICondition;
import com.geojmodelbuilder.core.IDataFlow;
import com.geojmodelbuilder.core.ILink;
import com.geojmodelbuilder.core.IProcess;
import com.geojmodelbuilder.core.data.IComplexData;
import com.geojmodelbuilder.core.data.IData;
import com.geojmodelbuilder.core.impl.Links;
import com.geojmodelbuilder.core.instance.IParameter;
import com.geojmodelbuilder.core.provenance.IProcessProv;
import com.geojmodelbuilder.core.provenance.impl.ProcessProv;
import com.geojmodelbuilder.engine.IProcessEvent;
import com.geojmodelbuilder.engine.IProcessEvent.EventType;
import com.geojmodelbuilder.engine.IListener;
import com.geojmodelbuilder.engine.IPublisher;

/**
 * Be responsible for invoking executable process. Play roles of both listener
 * and publisher.
 * Event type: succeeded, failed --> IProcessProv
 * Event type: ready --> IProcess(IProcessInstance)
 * 
 * @author Mingda Zhang
 *
 */
public class ProcessExecutor implements IListener, IPublisher,Runnable {

	private Map<EventType, Listeners> eventContainer = null;
	private IProcess process;
	private Date startTime;
	private Date endTime;
	
	/**
	 * include data flows and control flows
	 */
	private Links waitingSignals;

	public ProcessExecutor(IProcess process) {
		this.eventContainer = new HashMap<IProcessEvent.EventType, Listeners>();
		this.process = process;
		this.waitingSignals = new Links();
		initialize();
	}

	/**
	 * Initialization
	 */
	private void initialize() {
		// Initialize the waiting signals.
		List<ILink> links = this.process.getLinks();
		for (ILink link : links) {
			if (link.getTargetProcess() == this.process)
				this.waitingSignals.add(link);
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
	public synchronized void onEvent(IProcessEvent event) {
		switch (event.getType()) {
		case Succeeded:
			handleEvent(event);
			break;
		default:
			break;
		}

		if (canExecute()) {
//			execute();
			sendEvent(new ProcessEvent(EventType.Ready,this.process));
		}
	}

	/**
	 * Execute the process
	 * 
	 * @return
	 */
	/*public boolean execute() {
		ProcessTrace processTrace = new ProcessTrace();
		startTime = new Date();
		boolean flag = process.execute();
		endTime = new Date();
		processTrace.setStartTime(startTime);
		processTrace.setEndTime(endTime);
		processTrace.setStatus(flag);
		
		if (process.execute()) {
			sendEvent(new Event(EventType.Succeeded,this.getProcess()));
			return true;
		}

		sendEvent(new Event(EventType.Failed,this.getProcess()));
		return false;
	}*/

	/**
	 * When all the signals match, the process will be executed.
	 */
	public boolean canExecute() {
		if (this.waitingSignals.size() == 0)
			return true;

		return false;
	}

	/**
	 * Handles event according the link type.
	 */
	private void handleEvent(IProcessEvent event) {
		IProcess process = event.getSource();
		if(process instanceof IProcessProv)
			process = ((IProcessProv)process).getProcess();
		Links targetLinks = this.waitingSignals.getLinksWithSource(process);
		Links deleteLinks = new Links();

		for (ILink link : targetLinks) {
			if (link instanceof IDataFlow) {
				getValue((IDataFlow) link);
				deleteLinks.add(link);
			} else if (link instanceof IBranchControl) {
				IBranchControl branchControl = (IBranchControl) link;
				ICondition condition = branchControl.getSourceProcess();
				if (condition.isSatisfied() == branchControl.isTrue())
					deleteLinks.add(link);
			}
		}

		for (ILink link : deleteLinks) {
			this.waitingSignals.remove(link);
		}
	}

	/**
	 * Get the value from the source process
	 */
	private void getValue(IDataFlow dataFlow) {
		IParameter sourceParameter = (IParameter) dataFlow.getSourceExchange();
		IParameter targetParameter = (IParameter) dataFlow.getTargetExchange();
		IData sourceData = sourceParameter.getData();
		IData targetData = targetParameter.getData();
		targetData.setValue(sourceData.getValue());
		if((sourceData instanceof IComplexData) && (targetData instanceof IComplexData)){
			IComplexData sourceComplexData = (IComplexData)sourceData;
			String sourceMimeType = sourceComplexData.getMimeType();
			if(sourceMimeType!=null && !sourceMimeType.equals("")){
				IComplexData targetComplexData = (IComplexData)targetData;
				targetComplexData.setMimeType(sourceMimeType);
			}
		}
	}

	public IProcess getProcess() {
		return this.process;
	}

	public void run() {
		ProcessProv processTrace = new ProcessProv(this.process);
		startTime = new Date();
		boolean flag = process.canExecute();
		if(flag)
			flag = process.execute();
		
		endTime = new Date();
		processTrace.setStartTime(startTime);
		processTrace.setEndTime(endTime);
		processTrace.setStatus(flag);
		
		
		if (flag) {
			sendEvent(new ProcessEvent(EventType.Succeeded,processTrace));
		}else{
			sendEvent(new ProcessEvent(EventType.Failed,processTrace));
		}
	}

	
}
