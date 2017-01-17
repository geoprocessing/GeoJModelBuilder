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

import java.util.ArrayList;
import java.util.List;

import org.eclipse.gef.commands.Command;

import com.geojmodelbuilder.ui.models.WorkflowNode;
import com.geojmodelbuilder.ui.models.links.NodeLink;
/**
 * 
 * @author Mingda Zhang
 *
 */
public class NodeDeleteCommand extends Command {
	private WorkflowNode workflowNode;
	private List<NodeLink> removedLinks = new ArrayList<NodeLink>();

	public void setWorkflowNode(WorkflowNode node) {
		this.workflowNode = node;
	}

	@Override
	public void execute() {
		super.execute();
		removedLinks.clear();
		
		for(NodeLink link:this.workflowNode.getInLinks()){
			removedLinks.add(link);
		}
		for(NodeLink link:this.workflowNode.getOutLinks()){
			removedLinks.add(link);
		}
		for(NodeLink link:removedLinks){
			link.disconnect();
		}
//		this.workflowNode.getWorkflow().removeNode(this.workflowNode);
		this.workflowNode.removeSelf();
	}

	@Override
	public void undo() {
		super.undo();
//		this.workflowNode.getWorkflow().addNode(this.workflowNode);
		this.workflowNode.reloadSelf();
		for(NodeLink link:removedLinks){
			link.connect();
		}
	}
}
