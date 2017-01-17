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
public class LinkReconnectCommand extends Command {

	private WorkflowNode oldSourceNode;
	private WorkflowNode oldTargetNode;
	private WorkflowNode newSourceNode;
	private WorkflowNode newTargetNode;
	private NodeLink link;

	public LinkReconnectCommand(NodeLink link) {
		if (link != null) {
			this.link = link;
			this.oldSourceNode = link.getSourceNode();
			this.oldTargetNode = link.getTargetNode();
		}
	}

	public void setNewSourceNode(WorkflowNode node) {
		this.newSourceNode = node;
	}

	public void setNewTargetNode(WorkflowNode node) {
		this.newTargetNode = node;
	}

	@Override
	public boolean canExecute() {
		//source changed
		if (this.newSourceNode != null && this.newSourceNode!=this.oldTargetNode) 
			return true;

		if(this.newTargetNode!=null && this.newTargetNode != this.oldSourceNode)
			return true;
		
		return false;
	}

	@Override
	public void execute() {
		super.execute();
		if (this.newSourceNode == null) {
			this.newSourceNode = this.oldSourceNode;
		}
		
		if (this.newTargetNode == null) {
			this.newTargetNode = this.oldTargetNode;
		}
		this.link.reconnect(this.newSourceNode, this.newTargetNode);
	}
}
