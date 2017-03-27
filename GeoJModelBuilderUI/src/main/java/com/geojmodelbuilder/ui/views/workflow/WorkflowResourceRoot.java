/**
 * Copyright (C) 2013 - 2016 Wuhan University,
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
public class WorkflowResourceRoot implements ITreeNode {

	private WorkflowTypeNode workflowTemplate ;
	private WorkflowTypeNode workflowInstnace ;
	private WorkflowTypeNode workflowExecution;
	
	private String name;
	public WorkflowResourceRoot(String name){
		workflowTemplate = new WorkflowTypeNode(WorkflowType.TEMPLATE,this);
		workflowInstnace = new WorkflowTypeNode(WorkflowType.INSTANCE,this);
		workflowExecution = new WorkflowTypeNode(WorkflowType.EXECUTION,this);
		this.name = name;
	}
	
	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public boolean hasChildren() {
		return true;
	}

	@Override
	public Object[] getChildren() {
		return new Object[]{workflowTemplate,workflowInstnace,workflowExecution};
	}

	@Override
	public Object getParent() {
		return null;
	}

}
