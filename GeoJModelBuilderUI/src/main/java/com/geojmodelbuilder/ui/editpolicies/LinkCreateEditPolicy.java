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

import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.GraphicalNodeEditPolicy;
import org.eclipse.gef.requests.CreateConnectionRequest;
import org.eclipse.gef.requests.ReconnectRequest;

import com.geojmodelbuilder.ui.commands.CommandFactory;
import com.geojmodelbuilder.ui.commands.LinkCreateCommand;
import com.geojmodelbuilder.ui.models.WorkflowNode;
import com.geojmodelbuilder.ui.models.links.NodeLink;
/**
 * 
 * @author Mingda Zhang
 *
 */
public class LinkCreateEditPolicy extends GraphicalNodeEditPolicy {

	
	@Override
	protected Command getConnectionCompleteCommand(
			CreateConnectionRequest request) {
		
		LinkCreateCommand linkCreateCommand = (LinkCreateCommand)request.getStartCommand();
		WorkflowNode targetNode = (WorkflowNode)getHost().getModel();
		linkCreateCommand.setTargetNode(targetNode);
		return linkCreateCommand;
	}

	/**
	 * different operations based on the link type.
	 * There are two types:
	 * DataFlow, start node: WorkflowArtifact or ProcessOutputArtifact; end node: WorkflowProcess
	 * ControlFlow, start node: WorkflowCondition; end node: WorkflowProcess.
	 */
	@Override
	protected Command getConnectionCreateCommand(CreateConnectionRequest request) {
		
		WorkflowNode sourceNode = (WorkflowNode)getHost().getModel();
		NodeLink nodeLink = (NodeLink)request.getNewObject();
		LinkCreateCommand command = CommandFactory.getLinkCreateCommand(nodeLink);
		
		command.setSourceNode(sourceNode);
		command.setLink(nodeLink);
		request.setStartCommand(command);
		
		return command;
	}

	@Override
	protected Command getReconnectTargetCommand(ReconnectRequest request) {
		return null;
	}

	@Override
	protected Command getReconnectSourceCommand(ReconnectRequest request) {
		return null;
	}

	/*
	@Override
	protected Command getReconnectTargetCommand(ReconnectRequest request) {
		NodeLink link = (NodeLink)request.getConnectionEditPart().getModel();
		LinkReconnectCommand cmd = new LinkReconnectCommand(link);
		cmd.setNewTargetNode((WorkflowNode)getHost().getModel());
		return cmd;
	}

	@Override
	protected Command getReconnectSourceCommand(ReconnectRequest request) {
		NodeLink link = (NodeLink)request.getConnectionEditPart().getModel();
		LinkReconnectCommand cmd = new LinkReconnectCommand(link);
		cmd.setNewSourceNode((WorkflowNode)getHost().getModel());
		return cmd;
	}
	*/

}
