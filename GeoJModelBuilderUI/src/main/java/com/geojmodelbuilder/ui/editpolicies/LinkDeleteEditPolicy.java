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
import org.eclipse.gef.editpolicies.ConnectionEditPolicy;
import org.eclipse.gef.requests.GroupRequest;

import com.geojmodelbuilder.ui.commands.LinkDeleteCommand;
import com.geojmodelbuilder.ui.commands.NodeDeleteCommand;
import com.geojmodelbuilder.ui.models.WorkflowProcess;
import com.geojmodelbuilder.ui.models.links.NodeLink;
import com.geojmodelbuilder.ui.models.links.ProcessOutputLink;
/**
 * 
 * @author Mingda Zhang
 *
 */
public class LinkDeleteEditPolicy extends ConnectionEditPolicy {

	@Override
	protected Command getDeleteCommand(GroupRequest request) {
		Object obj = getHost().getModel();
		if (!(obj instanceof NodeLink)) {
			return null;
		}
		
		//If the link is a process output link, the output artifact or the associated process will be deleted at the same time.
		if (obj instanceof ProcessOutputLink) {
			ProcessOutputLink outputLink = (ProcessOutputLink)obj;
			WorkflowProcess workflowProcess = (WorkflowProcess)outputLink.getSourceNode();
			
			NodeDeleteCommand command = new NodeDeleteCommand();
			if (workflowProcess.getOutputCount() == 1) {
				command.setWorkflowNode(workflowProcess);
			}else {
				command.setWorkflowNode(outputLink.getTargetNode());
			}
			return command;
		}
		
		LinkDeleteCommand cmd = new LinkDeleteCommand();
		cmd.setLink((NodeLink) obj);
		return cmd;
	}
}
