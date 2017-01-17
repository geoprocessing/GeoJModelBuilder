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

import org.eclipse.gef.commands.Command;
import org.eclipse.gef.ui.actions.Clipboard;

import com.geojmodelbuilder.ui.models.ProcessOutputArtifact;
import com.geojmodelbuilder.ui.models.Workflow;
import com.geojmodelbuilder.ui.models.WorkflowNode;
/**
 * 
 * @author Mingda Zhang
 *
 */
public class NodePasteCommand extends Command {

	private Workflow workflow;

	@Override
	public void execute() {
		super.execute();
		for (WorkflowNode model : this.getSourceModels()) {
			this.workflow = model.getWorkflow();
			if (this.workflow == null || model instanceof ProcessOutputArtifact)
				continue;

			WorkflowNode cloneNode = null;
			try {
				cloneNode = (WorkflowNode) model.clone();
			} catch (CloneNotSupportedException e) {
				e.printStackTrace();
				continue;
			}
			
			this.workflow.addNode(cloneNode);
			/*if (model.getClass() == WorkflowProcess.class) {
				this.workflow.addProcess((WorkflowProcess) cloneNode);
			}else if (model.getClass() == WorkflowCondition.class) {
				this.workflow.addCondtion((WorkflowCondition)cloneNode);
			}else if (model.getClass() == StandaloneArtifact.class) {
				this.workflow.addArtifact((StandaloneArtifact) cloneNode);
			}*/
			
		}
	}

	@Override
	public boolean canUndo() {
		return false;
	}

	@Override
	public boolean canExecute() {
		return true;
		/*
		 * if(this.workflow == null) return false;
		 * 
		 * List<WorkflowNode> sourceModels = getSourceModels(); return
		 * sourceModels.size() > 0 ? true : false;
		 */
	}

	private List<WorkflowNode> getSourceModels() {
		List<WorkflowNode> sourceModels = new ArrayList<WorkflowNode>();

		Object obj = Clipboard.getDefault().getContents();
		if (!(obj instanceof List<?>))
			return sourceModels;

		List<?> objList = (List<?>) Clipboard.getDefault().getContents();
		for (Object object : objList) {
			if (object instanceof WorkflowNode)
				sourceModels.add((WorkflowNode) object);
		}

		return sourceModels;
	}
}
