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
package com.geojmodelbuilder.ui.editors;

import org.eclipse.gef.ContextMenuProvider;
import org.eclipse.gef.EditPartViewer;
import org.eclipse.gef.ui.actions.ActionRegistry;
import org.eclipse.gef.ui.actions.GEFActionConstants;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.ui.actions.ActionFactory;

import com.geojmodelbuilder.ui.actions.InstanceBindAction;
import com.geojmodelbuilder.ui.actions.ProcessEditAction;
/**
 * 
 * @author Mingda Zhang
 *
 */
public class ModelEditorContextMenuProvider extends ContextMenuProvider {

	private ActionRegistry actionRegistry;

	public ModelEditorContextMenuProvider(EditPartViewer viewer,
			ActionRegistry registry) {
		super(viewer);
		this.actionRegistry = registry;
	}

	@Override
	public void buildContextMenu(IMenuManager menu) {
		// TODO Auto-generated method stub
		IAction action;
		GEFActionConstants.addStandardActionGroups(menu);
		action = this.actionRegistry.getAction(ActionFactory.UNDO.getId());
		menu.appendToGroup(GEFActionConstants.GROUP_UNDO, action);
		action = this.actionRegistry.getAction(ActionFactory.REDO.getId());
		menu.appendToGroup(GEFActionConstants.GROUP_UNDO, action);
		action = this.actionRegistry.getAction(ActionFactory.DELETE.getId());
		menu.appendToGroup(GEFActionConstants.GROUP_EDIT, action);
		
		action = this.actionRegistry.getAction(ActionFactory.RENAME.getId());
		menu.appendToGroup(GEFActionConstants.GROUP_EDIT, action);
		
		action = this.actionRegistry.getAction(ProcessEditAction.ID);
		menu.appendToGroup(GEFActionConstants.GROUP_EDIT, action);
		
		action = this.actionRegistry.getAction(InstanceBindAction.ID);
		menu.appendToGroup(GEFActionConstants.GROUP_EDIT, action);
	}
	
}
