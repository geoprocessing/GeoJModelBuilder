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
import java.util.List;

import org.eclipse.draw2d.ChopboxAnchor;
import org.eclipse.draw2d.ConnectionAnchor;
import org.eclipse.draw2d.EllipseAnchor;
import org.eclipse.draw2d.IFigure;
import org.eclipse.gef.ConnectionEditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.NodeEditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.RequestConstants;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;

import com.geojmodelbuilder.ui.editpolicies.LinkCreateEditPolicy;
import com.geojmodelbuilder.ui.editpolicies.NodeDeleteEditPolicy;
import com.geojmodelbuilder.ui.editpolicies.NodeRenameEditPolicy;
import com.geojmodelbuilder.ui.figures.WorkflowNodeFigure;
import com.geojmodelbuilder.ui.models.WorkflowNode;
/**
 * 
 * @author Mingda Zhang
 *
 */
public class WorkflowNodeEditPart extends AbstractGraphicalEditPart implements
		PropertyChangeListener, NodeEditPart {

	@Override
	protected IFigure createFigure() {
		return new WorkflowNodeFigure();
	}

	@Override
	protected void createEditPolicies() {
		installEditPolicy(EditPolicy.COMPONENT_ROLE, new NodeDeleteEditPolicy());
		installEditPolicy(EditPolicy.NODE_ROLE, new NodeRenameEditPolicy());
		installEditPolicy(EditPolicy.GRAPHICAL_NODE_ROLE,
				new LinkCreateEditPolicy());
	}

	@Override
	protected void refreshVisuals() {
		super.refreshVisuals();

		WorkflowNodeFigure figure = (WorkflowNodeFigure) getFigure();
		WorkflowNode model = (WorkflowNode) getModel();

		figure.setName(model.getName());
		figure.setLayout(model.getLayout());
		figure.setColor(model.getColor());
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		String propertyName = evt.getPropertyName();
		
		if (propertyName.equals(WorkflowNode.NODE_XYLAYOUT)
				|| propertyName.equals(WorkflowNode.PROPERTY_NAME)
				|| propertyName.equals(WorkflowNode.PROPERTY_COLOR)) {
			refreshVisuals();
		} else if (propertyName.equals(WorkflowNode.ADD_IN_LINK)||propertyName.equals(WorkflowNode.REMOVE_IN_LINK)) {
			refreshTargetConnections();
		} else if (propertyName.equals(WorkflowNode.ADD_OUT_LINK) || propertyName.equals(WorkflowNode.REMOVE_OUT_LINK)) {
			refreshSourceConnections();
		}

	}

	@Override
	public void activate() {
		super.activate();
		((WorkflowNode) getModel()).addListener(this);
	}

	@Override
	public void deactivate() {
		super.deactivate();
		((WorkflowNode) getModel()).removeListener(this);
	}

	@Override
	public void performRequest(Request req) {
		// TODO Auto-generated method stub
		super.performRequest(req);
		if (req.getType().equals(RequestConstants.REQ_OPEN)) {
			//
			/*try {
				IWorkbenchPage page = PlatformUI.getWorkbench()
						.getActiveWorkbenchWindow().getActivePage();
				page.showView(IPageLayout.ID_PROP_SHEET);
			} catch (PartInitException e) {
				e.printStackTrace();
			}*/
		}
	}

	@Override
	public List getModelSourceConnections() {
		WorkflowNode node = (WorkflowNode) getModel();
		return node.getOutLinks();
	}

	@Override
	public List getModelTargetConnections() {
		WorkflowNode node = (WorkflowNode) getModel();
		return node.getInLinks();
	}

	@Override
	public ConnectionAnchor getSourceConnectionAnchor(
			ConnectionEditPart connection) {
		IFigure figure = getFigure();
		if (figure instanceof WorkflowNodeFigure) {
			return ((WorkflowNodeFigure)figure).getSourceConnectionAnchor();
		}
		return new ChopboxAnchor(figure);
	}

	@Override
	public ConnectionAnchor getTargetConnectionAnchor(
			ConnectionEditPart connection) {
		IFigure figure = getFigure();
		if (figure instanceof WorkflowNodeFigure) {
			return ((WorkflowNodeFigure)figure).getTargetConnectionAnchor();
		}
		return new EllipseAnchor(figure);
	}

	@Override
	public ConnectionAnchor getSourceConnectionAnchor(Request request) {
		IFigure figure = getFigure();
		if (figure instanceof WorkflowNodeFigure) {
			return ((WorkflowNodeFigure)figure).getSourceConnectionAnchor();
		}
		return new ChopboxAnchor(figure);
	}

	@Override
	public ConnectionAnchor getTargetConnectionAnchor(Request request) {
		IFigure figure = getFigure();
		if (figure instanceof WorkflowNodeFigure) {
			return ((WorkflowNodeFigure)figure).getTargetConnectionAnchor();
		}
		return new EllipseAnchor(figure);
	}
	
}
