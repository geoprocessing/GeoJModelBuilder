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
package com.geojmodelbuilder.core.trace.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.geojmodelbuilder.core.IWorkflow;
import com.geojmodelbuilder.core.trace.IProcessTrace;
import com.geojmodelbuilder.core.trace.IWorkflowTrace;
/**
 * 
 * @author Mingda Zhang
 *
 */
public class WorkflowTrace implements IWorkflowTrace {
	private Date startTime;
	private Date endTime;
	private boolean status;
	private List<IProcessTrace> processTraces;
	private IWorkflow workflow;
	
	public WorkflowTrace(IWorkflow workflow){
		this.workflow = workflow;
		processTraces = new ArrayList<IProcessTrace>();
		status = false;
	}
	
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}


	public void addProcessTrace(IProcessTrace trace){
		if(!this.processTraces.contains(trace))
			this.processTraces.add(trace);
	}

	public Date getStartTime() {
		return this.startTime;
	}

	public Date getEndTime() {
		return this.endTime;
	}

	public boolean getStatus() {
		return this.status;
	}

	public List<IProcessTrace> getProcessTraces() {
		return this.processTraces;
	}

	public IWorkflow getWorkflow() {
		return this.workflow;
	}

}
