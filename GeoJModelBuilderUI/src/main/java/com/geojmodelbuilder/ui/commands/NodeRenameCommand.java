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
/**
 * 
 * @author Mingda Zhang
 *
 */
public class NodeRenameCommand extends Command {
	private String oldName;
	private WorkflowNode workflowNode;
	private String newName;
	
	public void setNewName(String name){
		this.newName = name;
	}
	
	public void setModel(WorkflowNode model){
		this.workflowNode = model;
		this.oldName = model.getName();
	}
	
	@Override
	public void execute() {
		// TODO Auto-generated method stub
		super.execute();
		this.workflowNode.setName(this.newName);
	}
	
	@Override
	public void undo() {
		// TODO Auto-generated method stub
		super.undo();
		this.workflowNode.setName(this.oldName);
	}
}
