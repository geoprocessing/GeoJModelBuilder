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
package com.geojmodelbuilder.core.instance.impl;

import com.geojmodelbuilder.core.INamespaceDefault;
import com.geojmodelbuilder.core.impl.AbstractWorkflowImpl;
import com.geojmodelbuilder.core.instance.IProcessInstance;
import com.geojmodelbuilder.core.instance.IWorkflowInstance;
import com.geojmodelbuilder.core.utils.ValidateUtil;

/**
 * @author Mingda Zhang
 *
 */
public class WorkflowInstance extends AbstractWorkflowImpl<IProcessInstance> implements IWorkflowInstance{
	@Override
	
	public String getNamespace() {
		if(ValidateUtil.isStrEmpty(this.namespace))
			this.namespace = INamespaceDefault.INSTANCE_WORKFLOW;
		
		return this.namespace;
	}
}
