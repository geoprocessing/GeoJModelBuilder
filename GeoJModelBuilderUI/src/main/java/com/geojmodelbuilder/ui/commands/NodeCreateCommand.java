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

import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.commands.Command;

import com.geojmodelbuilder.ui.models.Workflow;
import com.geojmodelbuilder.ui.models.WorkflowNode;
/**
 * 
 * @author Mingda Zhang
 *
 */
public class NodeCreateCommand extends Command {
	private Workflow workflow = null;
	private WorkflowNode workflowNode = null;
	private Rectangle layout;
	
	public void setWorkflow(Workflow model){
		this.workflow = model;
	}
	
	public Workflow getWorkflow(){
		return this.workflow;
	}
	
	public void setWorkflowNode(WorkflowNode model){
		this.workflowNode = model;
	}
	
	
	public WorkflowNode getWorkflowNode(){
		return this.workflowNode;
	}
	                                    
	public void setLayout(Rectangle layout){
		this.layout = layout;
	}
	
	@Override
	public void execute() {
		super.execute();
		this.workflowNode.setLayout(this.layout);
		this.workflowNode.setWorkflow(workflow);
	}
}
