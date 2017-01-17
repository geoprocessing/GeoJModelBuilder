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
package com.geojmodelbuilder.ui.commands;

import org.eclipse.gef.commands.Command;

import com.geojmodelbuilder.ui.models.WorkflowNode;
import com.geojmodelbuilder.ui.models.links.NodeLink;
/**
 * 
 * @author Mingda Zhang
 *
 */
public class LinkCreateCommand extends Command {
	private WorkflowNode sourceNode;
	private WorkflowNode targetNode;
	private NodeLink link;

	public void setLink(NodeLink link) {
		this.link = link;
	}

	protected NodeLink getLink() {
		return this.link;
	}

	public WorkflowNode getSourceNode() {
		return sourceNode;
	}

	public WorkflowNode getTargetNode() {
		return targetNode;
	}

	public void setSourceNode(WorkflowNode sourceNode) {
		this.sourceNode = sourceNode;
	}

	public void setTargetNode(WorkflowNode targetNode) {
		this.targetNode = targetNode;
	}

	@Override
	public boolean canExecute() {
		// The three necessary elements could not be null.
		// Source and target could not be the same.
		if (sourceNode == null || targetNode == null || link == null
				|| sourceNode == targetNode) {
			return false;
		}

		// Not permit to create the same link
		for (NodeLink link : getSourceNode().getOutLinks()) {
			if (link.getSourceNode() == this.getSourceNode()
					&& link.getTargetNode() == this.getTargetNode()) {
				System.out.println("Exist already.");
				return false;
			}
		}

		return true;
	}

	@Override
	public void execute() {
		link.setSourceNode(sourceNode);
		link.setTargetNode(targetNode);

		link.connect();
	}
}
