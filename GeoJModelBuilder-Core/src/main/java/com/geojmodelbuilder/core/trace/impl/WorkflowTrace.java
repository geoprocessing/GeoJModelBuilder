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

import java.util.Date;

import com.geojmodelbuilder.core.impl.WorkflowImpl;
import com.geojmodelbuilder.core.plan.IWorkflowExec;
import com.geojmodelbuilder.core.trace.IProcessTrace;
import com.geojmodelbuilder.core.trace.IWorkflowTrace;
/**
 * 
 * @author Mingda Zhang
 *
 */
public class WorkflowTrace extends WorkflowImpl<IProcessTrace> implements IWorkflowTrace {
	private Date startTime;
	private Date endTime;
	private boolean status;
	private IWorkflowExec workflow;
	
	public WorkflowTrace(IWorkflowExec workflow){
		this.workflow = workflow;
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


	public Date getStartTime() {
		return this.startTime;
	}

	public Date getEndTime() {
		return this.endTime;
	}

	public boolean getStatus() {
		return this.status;
	}

	/*public List<IProcessTrace> getProcesses() {
		return this.processTraces;
	}*/

	public IWorkflowExec getWorkflow() {
		return this.workflow;
	}

}
