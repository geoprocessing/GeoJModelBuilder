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

import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.XYLayoutEditPolicy;
import org.eclipse.gef.requests.CreateRequest;

import com.geojmodelbuilder.ui.editparts.WorkflowNodeEditPart;
import com.geojmodelbuilder.ui.commands.NodeXYLayoutChangeCommand;
import com.geojmodelbuilder.ui.models.WorkflowNode;
/**
 * 
 * @author Mingda Zhang
 *
 */
public class NodeXYLayoutChangeEditPolicy extends XYLayoutEditPolicy {

	@Override
	protected Command getCreateCommand(CreateRequest request) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected Command createChangeConstraintCommand(EditPart child,
			Object constraint) {
		Command command = null;
		
		if (child instanceof WorkflowNodeEditPart) {
			command = new NodeXYLayoutChangeCommand();
			NodeXYLayoutChangeCommand nodeLayoutCmd = (NodeXYLayoutChangeCommand)command;
			nodeLayoutCmd.setModel((WorkflowNode)child.getModel());
			nodeLayoutCmd.setXYLayout((Rectangle)constraint);
		}
		
		return command;
	}
	
}
