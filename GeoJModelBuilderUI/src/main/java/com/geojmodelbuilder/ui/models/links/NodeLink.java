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
package com.geojmodelbuilder.ui.models.links;

import com.geojmodelbuilder.ui.models.AbstractWorkflowElement;
import com.geojmodelbuilder.ui.models.WorkflowNode;

/**
 * @author Mingda Zhang
 *
 */
public class NodeLink extends AbstractWorkflowElement{
	private WorkflowNode sourceNode;
	private WorkflowNode targetNode;

	public NodeLink() {}
	
	public NodeLink(WorkflowNode sourceNode,WorkflowNode targetNode){
		this.sourceNode = sourceNode;
		this.targetNode = targetNode;
	}
	
	public WorkflowNode getSourceNode() {
		return sourceNode;
	}

	public void setSourceNode(WorkflowNode sourceNode) {
		this.sourceNode = sourceNode;
	}

	public WorkflowNode getTargetNode() {
		return targetNode;
	}

	public void setTargetNode(WorkflowNode targetNode) {
		this.targetNode = targetNode;
	}

	public void connect() {
		if (this.sourceNode == null || this.targetNode == null)
			return;

		this.sourceNode.addOutLink(this);
		this.targetNode.addInLink(this);
	}

	public void disconnect() {
		if (this.sourceNode == null || this.targetNode == null)
			return;

		this.sourceNode.removeOutLink(this);
		this.targetNode.removeInLink(this);
	}

	public void reconnect(WorkflowNode sourceNode, WorkflowNode targetNode) {
		if (sourceNode == null || targetNode == null
				|| sourceNode == targetNode) {
			return;
		}
		disconnect();
		this.sourceNode = sourceNode;
		this.targetNode = targetNode;
		connect();
	}
}
