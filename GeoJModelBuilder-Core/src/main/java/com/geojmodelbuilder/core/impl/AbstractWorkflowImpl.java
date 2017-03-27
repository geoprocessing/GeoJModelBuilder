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
import com.geojmodelbuilder.core.utils.IDGenerator;
import com.geojmodelbuilder.core.utils.ValidateUtil;
/**
 * 
 * @author Mingda Zhang
 *
 */
public abstract class AbstractWorkflowImpl<T extends IProcess> implements IWorkflow<T> {
	private List<T> processes;
	private List<ICondition> conditions;
	protected String id;
	protected String namespace;
	private String description;
	private String name;
	protected String generatedAt;
	public AbstractWorkflowImpl(){
		this.processes = new ArrayList<T>();
		this.conditions = new ArrayList<ICondition>();
		this.generatedAt = IDGenerator.dateID();
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
	
	
	public void setID(String id){
		this.id = id;
	}
	
	public String getID(){
		if(ValidateUtil.isStrEmpty(this.id))
			this.id = "Workflow" + this.generatedAt;
 		
			
		return this.id;
	}
	
	public void setNamespace(String namespace) {
		this.namespace = namespace;
	}

	public String getNamespace() {
		return this.namespace;
	}
	
	public void setDescription(String description){
		this.description = description;
	}
	
	public String getDescription(){
		return this.description;
	}
	
	public void setName(String name){
		this.name = name;
	}
	
	public String getName(){
		return this.name;
	}
}
