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

import com.geojmodelbuilder.ui.models.WorkflowNode;
/**
 * 
 * @author Mingda Zhang
 *
 */
public class NodeXYLayoutChangeCommand extends Command {
	private Rectangle layout;
	private WorkflowNode model;
	private Rectangle oldLayout;
	@Override
	public void execute() {
		super.execute();
		model.setLayout(layout);
	}
	
	public void setXYLayout(Rectangle layout){
		this.layout = layout;
	}
	
	public void setModel(WorkflowNode model){
		this.model = model;
		this.oldLayout = model.getLayout();
	}
	
	@Override
	public void redo() {
		super.redo();
		execute();
	}
	
	@Override
	public void undo() {
		super.undo();
		this.model.setLayout(this.oldLayout);
	}
	
/*	@Override
	public boolean canUndo() {
		return super.canUndo();
	}*/
}
