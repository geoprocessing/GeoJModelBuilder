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
import org.eclipse.gef.editpolicies.ComponentEditPolicy;
import org.eclipse.gef.requests.GroupRequest;

import com.geojmodelbuilder.ui.commands.NodeDeleteCommand;
import com.geojmodelbuilder.ui.models.ProcessOutputArtifact;
import com.geojmodelbuilder.ui.models.WorkflowNode;
import com.geojmodelbuilder.ui.models.WorkflowProcess;
/**
 * 
 * @author Mingda Zhang
 *
 */
public class NodeDeleteEditPolicy extends ComponentEditPolicy {

	@Override
	protected Command createDeleteCommand(GroupRequest deleteRequest) {
		NodeDeleteCommand command = new NodeDeleteCommand();
		WorkflowNode workflowNode = (WorkflowNode)getHost().getModel();
		
		//If there is only one output in the process, the entire process will be deleted when delete the output.
		if (workflowNode instanceof ProcessOutputArtifact) {
			WorkflowProcess process = ((ProcessOutputArtifact)workflowNode).getProcess();
			if (process!=null && process.getOutputCount() ==1) {
				workflowNode = process;
			}
		}
		command.setWorkflowNode(workflowNode);
		return command;
	}
}
