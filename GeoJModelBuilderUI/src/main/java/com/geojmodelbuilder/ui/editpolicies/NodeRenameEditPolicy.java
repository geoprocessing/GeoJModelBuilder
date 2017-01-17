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

import java.util.Map;

import org.eclipse.gef.Request;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.AbstractEditPolicy;

import com.geojmodelbuilder.ui.actions.NodeRenameAction;
import com.geojmodelbuilder.ui.commands.NodeRenameCommand;
import com.geojmodelbuilder.ui.models.WorkflowNode;
/**
 * 
 * @author Mingda Zhang
 *
 */
public class NodeRenameEditPolicy extends AbstractEditPolicy {

	@Override
	public Command getCommand(Request request) {
		// TODO Auto-generated method stub
		
		if(!request.getType().equals(NodeRenameAction.REQUEST_NAME))
			return null;
		
		Map<String, String> reqData = request.getExtendedData();
		String newName = reqData.get(NodeRenameAction.REQEST_DATA_KEY);
		
		NodeRenameCommand cmd = new NodeRenameCommand();
		cmd.setNewName(newName);
		
		WorkflowNode workflowNode = (WorkflowNode)getHost().getModel();
		cmd.setModel(workflowNode);
		
		return cmd;
	}

}
