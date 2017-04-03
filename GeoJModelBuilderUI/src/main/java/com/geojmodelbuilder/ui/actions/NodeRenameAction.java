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

import java.util.HashMap;
import java.util.List;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.ui.actions.SelectionAction;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.ActionFactory;

import com.geojmodelbuilder.ui.dialogs.RenameDialog;
import com.geojmodelbuilder.ui.editparts.WorkflowNodeEditPart;
import com.geojmodelbuilder.ui.models.WorkflowNode;
/**
 * 
 * @author Mingda Zhang
 *
 */
public class NodeRenameAction extends SelectionAction {

	public final static String REQUEST_NAME = "rename_request";
	public final static String REQEST_DATA_KEY = "newName";

	public NodeRenameAction(IWorkbenchPart part) {
		super(part);
		setLazyEnablementCalculation(false);
	}

	@Override
	protected void init() {
		super.init();
		setText("Rename...");
		setToolTipText("rename");
		
		setId(ActionFactory.RENAME.getId());
		setEnabled(false);
	}
	
	@Override
	protected boolean calculateEnabled() {
		Command cmd = createRenameCommand("");
		if(cmd == null)
			return false;
		
		return true;
	}

	@Override
	public void run() {
		super.run();
		
		WorkflowNodeEditPart nodeEditPart = getSelectedEditPart();
		String oldName = ((WorkflowNode)nodeEditPart.getModel()).getName();
		
		Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
		RenameDialog dialog = new RenameDialog(shell, oldName);
		if(dialog.open() == Window.OK){
			String newName = dialog.getNewName();
			if(!newName.equals(oldName)){
				Command cmd = createRenameCommand(newName);
				execute(cmd);
			}
		}
	}

	private Command createRenameCommand(String name) {
		
		EditPart editPart = getSelectedEditPart();
		if(editPart == null)
			return null;
		
		//construct request
		Request renameReq = new Request(REQUEST_NAME);
		HashMap<String, String> reqData = new HashMap<String, String>();
		reqData.put(REQEST_DATA_KEY, name);
		renameReq.setExtendedData(reqData);

		//obtain command from editpart
		return editPart.getCommand(renameReq);
	}

	private WorkflowNodeEditPart getSelectedEditPart() {
		List objects = getSelectedObjects();
		if (objects.size() == 0)
			return null;

		if (!(objects.get(0) instanceof EditPart))
			return null;

		EditPart editPart = (EditPart) objects.get(0);

		if (!(editPart instanceof WorkflowNodeEditPart))
			return null;

		return (WorkflowNodeEditPart) editPart;
	}
}
