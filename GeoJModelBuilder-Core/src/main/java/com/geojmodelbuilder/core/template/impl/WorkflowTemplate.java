/**
 * Copyright (C) 2013 - 2016 Wuhan University,
 *                           Center for Geographic Analysis, Harvard University
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
import com.geojmodelbuilder.core.impl.AbstractWorkflowImpl;
import com.geojmodelbuilder.core.instance.IWorkflowInstance;
import com.geojmodelbuilder.core.template.IProcessTemplate;
import com.geojmodelbuilder.core.template.IWorkflowTemplate;
import com.geojmodelbuilder.core.utils.ValidateUtil;

/**
 * @author Mingda Zhang
 *
 */
public class WorkflowTemplate extends AbstractWorkflowImpl<IProcessTemplate> implements IWorkflowTemplate{

	private List<IWorkflowInstance> workflowInstances;
	
	public WorkflowTemplate(){
		super();
		this.workflowInstances = new ArrayList<IWorkflowInstance>();
	}
	
	@Override
	public String getNamespace() {
		if(ValidateUtil.isStrEmpty(this.namespace))
			this.namespace = INamespaceDefault.TEMPLATE_WORKFLOW;
		
		return this.namespace;
	}
	
	public void addInstance(IWorkflowInstance instance){
		if(!this.workflowInstances.contains(instance))
			this.workflowInstances.add(instance);
	}
	
	public List<IWorkflowInstance> getInstances(){
		return this.workflowInstances;
	}
}
