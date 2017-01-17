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
package com.geojmodelbuilder.ui.commands;

import com.geojmodelbuilder.ui.models.WorkflowCondition;
/**
 * 
 * @author Mingda Zhang
 *
 */
public class ConditionCreateCommand extends NodeCreateCommand {
	@Override
	public void execute() {
		super.execute();
		getWorkflow().addCondtion((WorkflowCondition) getWorkflowNode());
	}

	@Override
	public void undo() {
		super.undo();
		getWorkflow().removeNode(getWorkflowNode());
	}
}
