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
package com.geojmodelbuilder.ui.actions;

import org.eclipse.gef.commands.Command;
import org.eclipse.gef.ui.actions.SelectionAction;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.ActionFactory;

import com.geojmodelbuilder.ui.commands.NodePasteCommand;
/**
 * 
 * @author Mingda Zhang
 *
 */
public class NodePasteAction extends SelectionAction {

	public NodePasteAction(IWorkbenchPart part) {
		super(part);
		setLazyEnablementCalculation(false);
	}

	@Override
	protected void init() {
		// TODO Auto-generated method stub
		super.init();
		super.init();
		ISharedImages sharedImages = PlatformUI.getWorkbench()
				.getSharedImages();
		setText("Paste");
		setId(ActionFactory.PASTE.getId());
		setHoverImageDescriptor(sharedImages
				.getImageDescriptor(ISharedImages.IMG_TOOL_PASTE));
		setImageDescriptor(sharedImages
				.getImageDescriptor(ISharedImages.IMG_TOOL_PASTE));
		setDisabledImageDescriptor(sharedImages
				.getImageDescriptor(ISharedImages.IMG_TOOL_PASTE_DISABLED));
		setEnabled(false);
	}

	public void getActiveEditor(){
		PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
	}
	
	@Override
	protected boolean calculateEnabled() {
		// TODO Auto-generated method stub
		Command cmd = new NodePasteCommand();
		return cmd.canExecute();
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		super.run();
		Command cmd = new NodePasteCommand();
		execute(cmd);
	}

}
