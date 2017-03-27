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

import java.util.ArrayList;
import java.util.List;

import com.geojmodelbuilder.ui.views.tree.ITreeNode;

/**
 * @author Mingda Zhang
 *
 */
public class WorkflowTypeNode implements ITreeNode{

	private WorkflowType type;
	private List<WorkflowNode> workflowNodes;
	private WorkflowResourceRoot root;
	
	public WorkflowTypeNode(WorkflowType type,WorkflowResourceRoot root) {
		this.type = type;
		this.workflowNodes = new ArrayList<WorkflowNode>();
		this.root = root;
	}
	
	@Override
	public String getName() {
		switch (this.type) {
		case TEMPLATE:
			return "Templates";
			
		case INSTANCE:
			return "Instances";
			
		case EXECUTION:
			return "Executions";
			
		default:
			return "Workflow";
		}
	}

	@Override
	public boolean hasChildren() {
		return false;
	}

	@Override
	public Object[] getChildren() {
		return this.workflowNodes.toArray();
	}

	@Override
	public Object getParent() {
		return this.root;
	}

}
