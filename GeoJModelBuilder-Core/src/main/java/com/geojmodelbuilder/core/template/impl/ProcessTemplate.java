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
package com.geojmodelbuilder.core.template.impl;

import java.util.ArrayList;
import java.util.List;

import com.geojmodelbuilder.core.INamespaceDefault;
import com.geojmodelbuilder.core.impl.AbstractProcessImpl;
import com.geojmodelbuilder.core.instance.IProcessInstance;
import com.geojmodelbuilder.core.template.IInputPort;
import com.geojmodelbuilder.core.template.IOutPutPort;
import com.geojmodelbuilder.core.template.IProcessTemplate;
import com.geojmodelbuilder.core.utils.ValidateUtil;

/**
 * @author Mingda Zhang
 *
 */
public class ProcessTemplate extends AbstractProcessImpl<IInputPort, IOutPutPort> implements IProcessTemplate{

	private List<IProcessInstance> procExecs = null;
	
	public ProcessTemplate(){
		super();
		this.procExecs = new ArrayList<IProcessInstance>();
	}
	
	public ProcessTemplate(String name){
		this();
		setName(name);
	}
	
	public void addExecCandidate(IProcessInstance procExec){
		if(!this.procExecs.contains(procExec))
			this.procExecs.add(procExec);
	}
	
	public void removeExecCandidate(IProcessInstance procExec){
		if(this.procExecs.contains(procExec))
			this.procExecs.remove(procExec);
	}
	
	public List<IProcessInstance> getInstances() {
		return this.procExecs;
	}
	
	public String getID() {
		if (ValidateUtil.isStrEmpty(this.id)) {
			if(ValidateUtil.isStrEmpty(this.name))
				this.id = "process" + this.generatedAt;
			else {
				this.id = this.name + this.generatedAt;
			}
		}
		return this.id;
	}

	/**
	 * Do nothing
	 */
	public boolean execute() {
		return false;
	}
	
	@Override
	public String getNamespace() {
		if (ValidateUtil.isStrEmpty(this.namespace)) {
			this.namespace = INamespaceDefault.TEMPLATE_PROCESS;
		}
		return this.namespace;
	}
}
