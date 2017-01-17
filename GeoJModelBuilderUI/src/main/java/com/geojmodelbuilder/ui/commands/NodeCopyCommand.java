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
import org.eclipse.gef.ui.actions.Clipboard;

import com.geojmodelbuilder.ui.models.WorkflowNode;
/**
 * 
 * @author Mingda Zhang
 *
 */
public class NodeCopyCommand extends Command {

	private List<WorkflowNode> copyNodes;

	public NodeCopyCommand() {
		super();
		copyNodes = new ArrayList<WorkflowNode>();
	}

	public void addModel(WorkflowNode model) {
		this.copyNodes.add(model);
	}

	@Override
	public boolean canExecute() {
		return this.copyNodes.size() > 0 ? true : false;
	}

	@Override
	public void execute() {
		Clipboard.getDefault().setContents(copyNodes);
	}
	
	/*@Override
	public boolean canUndo() {
		return false;
	}*/
}
