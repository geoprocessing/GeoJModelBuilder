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

import com.geojmodelbuilder.ui.models.links.NodeLink;
/**
 * 
 * @author Mingda Zhang
 *
 */
public class LinkDeleteCommand extends Command {

	private NodeLink link;

	public void setLink(NodeLink link) {
		this.link = link;
	}

	@Override
	public boolean canExecute() {
		if (this.link == null)
			return false;

		return true;
	}

	@Override
	public void execute() {
		super.execute();
		this.link.disconnect();
	}

	@Override
	public boolean canUndo() {
		if (this.link == null) {
			return false;
		}

		return true;
	}

	@Override
	public void undo() {
		super.undo();
		this.link.connect();
	}

	@Override
	public boolean canRedo() {
		if (this.link == null)
			return false;

		return true;
	}

	@Override
	public void redo() {
		super.redo();
		this.link.disconnect();
	}
}
