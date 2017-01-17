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

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.draw2d.IFigure;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.Request;
import org.eclipse.gef.RequestConstants;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

import com.geojmodelbuilder.ui.editpolicies.NodeXYLayoutChangeEditPolicy;
import com.geojmodelbuilder.ui.editpolicies.NodeCreateEditPolicy;
import com.geojmodelbuilder.ui.figures.WorkflowFigure;
import com.geojmodelbuilder.ui.models.IWorkflowElement;
import com.geojmodelbuilder.ui.models.Workflow;
import com.geojmodelbuilder.ui.models.WorkflowProcess;
/**
 * 
 * @author Mingda Zhang
 *
 */
public class WorkflowEditPart extends AbstractGraphicalEditPart implements
		PropertyChangeListener {

	@Override
	protected IFigure createFigure() {
		return new WorkflowFigure();
	}

	@Override
	protected void createEditPolicies() {
		installEditPolicy(EditPolicy.CONTAINER_ROLE, new NodeCreateEditPolicy());
		installEditPolicy(EditPolicy.LAYOUT_ROLE,
				new NodeXYLayoutChangeEditPolicy());
		
	}

	@Override
	protected void refreshVisuals() {
		super.refreshVisuals();
		WorkflowFigure figure = (WorkflowFigure) getFigure();
		Workflow model = (Workflow) getModel();

		figure.setName(model.getName());
		figure.setCount(model.getProcessCount());
	}

	/**
	 * This method will determine the elements to display.
	 */
	@Override
	public List<IWorkflowElement> getModelChildren() {
		List<IWorkflowElement> childrenList = new ArrayList<IWorkflowElement>();
		
		Workflow model = (Workflow) getModel();
		childrenList.addAll(model.getProcessRecipe());
		for(WorkflowProcess process:model.getProcessRecipe()){
			childrenList.addAll(process.getOutputArtifacts());
		}
		childrenList.addAll(model.getArtifacts());
		childrenList.addAll(model.getConditions());
		
		//the connections will be maintained by the GEF
		//childrenList.addAll(model.getLinks());
		return childrenList;
	}

	
	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		String propertyName = evt.getPropertyName();
		if (propertyName.equals(Workflow.CHILD_REMOVE)
				|| propertyName.equals(Workflow.CHILD_ADD))
			refreshChildren();
	}

	@Override
	public void activate() {
		super.activate();
		Workflow workflow = (Workflow) getModel();
		workflow.addListener(this);
	}

	@Override
	public void deactivate() {
		super.deactivate();
		Workflow workflow = (Workflow) getModel();
		workflow.removeListener(this);
	}

	@Override
	public void performRequest(Request req) {
		super.performRequest(req);
		if (req.getType().equals(RequestConstants.REQ_OPEN)) {
			IWorkbenchWindow window = PlatformUI.getWorkbench()
					.getActiveWorkbenchWindow();
			MessageBox mb = new MessageBox(window.getShell(), SWT.OK);
			mb.setMessage("Why click me!");
			mb.setText("It's a joke.");
			mb.open();
		}
	}
}
