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

import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartFactory;

import com.geojmodelbuilder.ui.models.Workflow;
import com.geojmodelbuilder.ui.models.WorkflowCondition;
import com.geojmodelbuilder.ui.models.WorkflowProcess;
import com.geojmodelbuilder.ui.models.WorkflowArtifact;
import com.geojmodelbuilder.ui.models.links.FalseThenFlow;
import com.geojmodelbuilder.ui.models.links.NodeLink;
import com.geojmodelbuilder.ui.models.links.TrueThenFlow;
import com.geojmodelbuilder.ui.editparts.WorkflowArtifactEditPart;
import com.geojmodelbuilder.ui.editparts.links.FalseThenFlowEditPart;
import com.geojmodelbuilder.ui.editparts.links.LinkEditPart;
import com.geojmodelbuilder.ui.editparts.links.TrueThenFlowEditPart;
/**
 * 
 * @author Mingda Zhang
 *
 */
public class WorkflowEditPartFactory implements EditPartFactory {

	@Override
	public EditPart createEditPart(EditPart context, Object model) {
		EditPart editPart = null;
		
		//The WorkflowCondition should be before the WorkflowProcess
		if (model instanceof Workflow) {
			editPart = new WorkflowEditPart();
		} else if (model instanceof WorkflowCondition) {
			editPart = new WorkflowConditionEditPart();
		}else if (model instanceof WorkflowProcess) {
			editPart = new WorkflowProcessEditPart();
		} else if (model instanceof WorkflowArtifact) {
			editPart = new WorkflowArtifactEditPart();
		}else if (model instanceof TrueThenFlow) {
			editPart = new TrueThenFlowEditPart();
		}else if (model instanceof FalseThenFlow) {
			editPart = new FalseThenFlowEditPart();
		}else if (model instanceof NodeLink) {
			editPart = new LinkEditPart();
		}

		if (editPart != null)
			editPart.setModel(model);

		return editPart;
	}

}
