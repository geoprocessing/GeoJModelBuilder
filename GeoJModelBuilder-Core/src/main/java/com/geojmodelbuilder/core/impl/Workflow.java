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
public class Workflow implements IWorkflow {
	private List<IProcess> processes;
	public Workflow(){
		this.processes = new ArrayList<IProcess>();
	}
	public List<IProcess> getProcesses() {
		return this.processes;
	}

	public void addProcess(IProcess process){
		if(!this.processes.contains(process))
			this.processes.add(process);
	}
	
	public void addCondition(ICondition condition){
		addProcess(condition);
	}
}
