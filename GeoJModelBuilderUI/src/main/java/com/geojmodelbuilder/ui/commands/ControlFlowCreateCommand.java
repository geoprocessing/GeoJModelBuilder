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

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.window.Window;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

import com.geojmodelbuilder.ui.dialogs.TargetArtifactSelectorDialog;
import com.geojmodelbuilder.ui.models.ProcessInputArtifact;
import com.geojmodelbuilder.ui.models.WorkflowArtifact;
import com.geojmodelbuilder.ui.models.WorkflowCondition;
import com.geojmodelbuilder.ui.models.WorkflowProcess;
import com.geojmodelbuilder.ui.models.links.ControlFlow;
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

		/*// The target must not be a conditon or artifact
		if (getTargetNode() instanceof WorkflowCondition
				|| getTargetNode() instanceof WorkflowArtifact)
			return false;*/

		//The target must be a workflow process, including WorkflowCondition
		if(!(getTargetNode() instanceof WorkflowProcess))
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
	
	@Override
	public void execute() {
		WorkflowCondition sourceCondition = (WorkflowCondition)getSourceNode();
		WorkflowProcess targetProcess = (WorkflowProcess)getTargetNode();
		
		//if there is no data item to bind, just create the control flow
		if (sourceCondition.getInputArtifacts().size()==0||targetProcess.getInputArtifacts().size()==0) {
			super.execute();
			return;
		}
		
		List<WorkflowArtifact> sourceArtifacts = new ArrayList<WorkflowArtifact>();
		sourceArtifacts.addAll(sourceCondition.getInputArtifacts());
		IWorkbenchWindow window = PlatformUI.getWorkbench()
				.getActiveWorkbenchWindow();
		TargetArtifactSelectorDialog selectDialog = new TargetArtifactSelectorDialog(
				window.getShell(), sourceArtifacts,targetProcess.getInputArtifacts());
		
		if(selectDialog.open() != Window.OK)
			return;
		
		WorkflowArtifact targetArtifact = selectDialog.getTargetInputPort();
		WorkflowArtifact sourceArtifact = selectDialog.getSourceArtifact();
		if(targetArtifact == null || sourceArtifact == null){
			super.execute();
			return;
		}
		
		ControlFlow controlFlow = (ControlFlow)getLink();
		controlFlow.setSourceProcess(sourceCondition);
		controlFlow.setSourceArtifact(sourceArtifact);
		controlFlow.setTargetProcess((WorkflowProcess) getTargetNode());
		controlFlow.setTargetArtifact((ProcessInputArtifact)targetArtifact);
		getLink().connect();
	}
}
