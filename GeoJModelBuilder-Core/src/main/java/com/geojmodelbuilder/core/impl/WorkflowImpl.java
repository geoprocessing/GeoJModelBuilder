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
package com.geojmodelbuilder.core.impl;

import java.util.ArrayList;
import java.util.List;

import com.geojmodelbuilder.core.ICondition;
import com.geojmodelbuilder.core.IProcess;
import com.geojmodelbuilder.core.IWorkflow;
/**
 * 
 * @author Mingda Zhang
 *
 */
public abstract class WorkflowImpl<T extends IProcess> implements IWorkflow<T> {
	private List<T> processes;
	private List<ICondition> conditions;
	public WorkflowImpl(){
		this.processes = new ArrayList<T>();
		this.conditions = new ArrayList<ICondition>();
	}
	public List<T> getProcesses() {
		return this.processes;
	}

	public void addProcess(T process){
		if(!this.processes.contains(process))
			this.processes.add(process);
	}
	
	public void addCondition(ICondition condition){
		if(!this.conditions.contains(condition))
			this.conditions.add(condition);
	}
	
	public List<ICondition> getConditions() {
		return this.conditions;
	}
}
