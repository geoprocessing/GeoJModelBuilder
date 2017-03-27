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
package com.geojmodelbuilder.ui.views.workflow;

import com.geojmodelbuilder.ui.views.tree.ITreeNode;

/**
 * @author Mingda Zhang
 *
 */
public class WorkflowNode implements ITreeNode{

	private WorkflowTypeNode workflowType;
	private String name;
	
	public WorkflowNode(WorkflowTypeNode workflowType,String name){
		this.workflowType = workflowType;
		this.name = name;
	}
	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public boolean hasChildren() {
		return false;
	}

	@Override
	public Object[] getChildren() {
		return null;
	}

	@Override
	public Object getParent() {
		return this.workflowType;
	}

}
