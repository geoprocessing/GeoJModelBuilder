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
package com.geojmodelbuilder.ui.editparts;

import org.eclipse.draw2d.IFigure;
import org.eclipse.gef.Request;
import org.eclipse.gef.RequestConstants;
import org.eclipse.jface.window.Window;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

import com.geojmodelbuilder.ui.dialogs.ProcessInputDialog;
import com.geojmodelbuilder.ui.figures.WorkflowProcessFigure;
import com.geojmodelbuilder.ui.models.WorkflowProcess;
/**
 * 
 * @author Mingda Zhang
 *
 */
public class WorkflowProcessEditPart extends WorkflowNodeEditPart {

	@Override
	protected IFigure createFigure() {
		return new WorkflowProcessFigure();
	}
	
	@Override
	public void performRequest(Request req) {
		// TODO Auto-generated method stub
		super.performRequest(req);
		
		if (req.getType().equals(RequestConstants.REQ_OPEN)) {
			WorkflowProcess process = (WorkflowProcess)getModel();
			IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();			
			ProcessInputDialog inputDialog = new ProcessInputDialog(window.getShell(), process);
			if (inputDialog.open() == Window.OK) {
				boolean changed = inputDialog.isChanged();
				if (changed) {
					System.out.println("Value changed.");
				}
			}
		}
	}
}
