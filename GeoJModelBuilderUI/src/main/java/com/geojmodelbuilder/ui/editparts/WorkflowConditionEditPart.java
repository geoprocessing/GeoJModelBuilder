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

import com.geojmodelbuilder.ui.dialogs.ConditionInputDialog;
import com.geojmodelbuilder.ui.figures.WorkflowConditionFigure;
import com.geojmodelbuilder.ui.models.WorkflowCondition;
/**
 * 
 * @author Mingda Zhang
 *
 */
public class WorkflowConditionEditPart extends WorkflowNodeEditPart{
	@Override
	protected IFigure createFigure() {
		return new WorkflowConditionFigure();
	}
	
	@Override
	public void performRequest(Request req) {
		super.performRequest(req);
		
		if (req.getType().equals(RequestConstants.REQ_OPEN)) {
			//WorkflowProcess process = (WorkflowProcess)getModel();
//			System.out.println(process.getName());
			WorkflowCondition condition = (WorkflowCondition)getModel();
			IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();			
			ConditionInputDialog inputDialog = new ConditionInputDialog(window.getShell(), condition);
			if (inputDialog.open() == Window.OK) {
				boolean changed = inputDialog.isChanged();
				if (changed) {
					System.out.println("Value changed.");
				}
			}
			
		}
	}
}
