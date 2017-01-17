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

import com.geojmodelbuilder.core.IProcess;
import com.geojmodelbuilder.core.plan.impl.ProcessExec;
import com.geojmodelbuilder.core.trace.IProcessTrace;
/**
 * 
 * @author Mingda Zhang
 *
 */
public class ProcessTrace extends ProcessExec implements IProcessTrace {
	private Date startTime;
	private Date endTime;
	private boolean status;
	private IProcess process;
	
	public ProcessTrace(IProcess process){
		this.process = process;
	}
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getStartTime() {
		return this.startTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public Date getEndTime() {
		return this.endTime;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	public boolean getStatus() {
		return this.status;
	}
	
	public IProcess getProcess(){
		return this.process;
	}
	
	@Override
	public String getName() {
		return this.process.getName();
	}
	
	/**
	 * The id is same with the Process.
	 */
	@Override
	public String getID() {
		return this.process.getID();
	}
}
