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
package com.geojmodelbuilder.ui.editpolicies;

import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.ContainerEditPolicy;
import org.eclipse.gef.requests.CreateRequest;

import com.geojmodelbuilder.ui.commands.ArtifactCreateCommand;
import com.geojmodelbuilder.ui.commands.ConditionCreateCommand;
import com.geojmodelbuilder.ui.commands.NodeCreateCommand;
import com.geojmodelbuilder.ui.commands.ProcessCreateCommand;
import com.geojmodelbuilder.ui.editparts.WorkflowEditPart;
import com.geojmodelbuilder.ui.models.StandaloneArtifact;
import com.geojmodelbuilder.ui.models.Workflow;
import com.geojmodelbuilder.ui.models.WorkflowCondition;
import com.geojmodelbuilder.ui.models.WorkflowNode;
import com.geojmodelbuilder.ui.models.WorkflowProcess;
/**
 * 
 * @author Mingda Zhang
 *
 */
public class NodeCreateEditPolicy extends ContainerEditPolicy {

	@Override
	protected Command getCreateCommand(CreateRequest request) {
		if (!request.getType().equals(REQ_CREATE))
			return null;

		EditPart editPart = getHost();
		if (!(editPart instanceof WorkflowEditPart))
			return null;

		Workflow workflow = (Workflow) editPart.getModel();

		Object object = request.getNewObject();
		if (!(object instanceof WorkflowNode))
			return null;
		
		Point location = request.getLocation();
		int x = location.x - WorkflowNode.WIDTH_DEFAULT / 2;
		int y = location.y - WorkflowNode.HEIGHT_DEFAULT / 2;
		x = x < 0 ? 0 : x;
		y = y < 0 ? 0 : y;
		Rectangle layout = new Rectangle(x, y, WorkflowNode.WIDTH_DEFAULT,
				WorkflowNode.HEIGHT_DEFAULT);

		WorkflowNode node = (WorkflowNode) object;
		NodeCreateCommand cmd = null;

		//simplify this part
		if (object.getClass() == WorkflowProcess.class)
			cmd = new ProcessCreateCommand();
		else if (object.getClass() == StandaloneArtifact.class) {
			cmd = new ArtifactCreateCommand();
		}else if (object.getClass() == WorkflowCondition.class) {
			cmd = new ConditionCreateCommand();
		}

		if (cmd == null)
			return null;

		cmd.setWorkflow(workflow);
		cmd.setWorkflowNode(node);
		cmd.setLayout(layout);

		return cmd;
	}

}
