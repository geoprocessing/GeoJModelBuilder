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

import com.geojmodelbuilder.ui.models.WorkflowArtifact;
import com.geojmodelbuilder.ui.models.WorkflowCondition;
import com.geojmodelbuilder.ui.models.links.FalseThenFlow;
import com.geojmodelbuilder.ui.models.links.TrueThenFlow;
/**
 * 
 * @author Mingda Zhang
 *
 */
public class ControlFlowCreateCommand extends LinkCreateCommand {
	@Override
	public boolean canExecute() {
		if (!super.canExecute())
			return false;

		// The source must be a condition
		if (!(getSourceNode() instanceof WorkflowCondition))
			return false;

		// The target must not be a conditon or artifact
		if (getTargetNode() instanceof WorkflowCondition
				|| getTargetNode() instanceof WorkflowArtifact)
			return false;

		WorkflowCondition condition = (WorkflowCondition)getSourceNode();
		
		//There is only one FalseThenFlow
		if (condition.getFalseFlow() != null && getLink() instanceof FalseThenFlow) {
			return false;
		}
		
		//There is only one TrueThenFlow
		if (condition.getTrueFlow() != null && getLink() instanceof TrueThenFlow) {
			return false;
		}
		
		
		return true;
	}
}
