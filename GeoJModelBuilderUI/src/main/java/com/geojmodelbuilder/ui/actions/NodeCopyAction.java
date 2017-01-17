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
package com.geojmodelbuilder.ui.actions;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.gef.commands.Command;
import org.eclipse.gef.ui.actions.SelectionAction;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.ActionFactory;

import com.geojmodelbuilder.ui.commands.NodeCopyCommand;
import com.geojmodelbuilder.ui.editparts.WorkflowNodeEditPart;
import com.geojmodelbuilder.ui.models.WorkflowNode;
/**
 * 
 * @author Mingda Zhang
 *
 */
public class NodeCopyAction extends SelectionAction {

	public NodeCopyAction(IWorkbenchPart part) {
		super(part);
		setLazyEnablementCalculation(false);
	}

	@Override
	protected void init() {
		super.init();

		setText("Copy");
		setId(ActionFactory.COPY.getId());

		ISharedImages sharedImages = PlatformUI.getWorkbench()
				.getSharedImages();
		setHoverImageDescriptor(sharedImages
				.getImageDescriptor(ISharedImages.IMG_TOOL_COPY));
		setImageDescriptor(sharedImages
				.getImageDescriptor(ISharedImages.IMG_TOOL_COPY));
		setDisabledImageDescriptor(sharedImages
				.getImageDescriptor(ISharedImages.IMG_TOOL_COPY_DISABLED));

		setEnabled(false);
	}

	private Command createCopyCommand() {
		List<WorkflowNode> selectedNodes = getSelectedModels();
		if (selectedNodes.size() == 0)
			return null;

		NodeCopyCommand copyCmd = new NodeCopyCommand();
		for (WorkflowNode model : selectedNodes) {
			copyCmd.addModel(model);
		}

		return copyCmd;
	}

	@Override
	public void run() {
		super.run();
		
		Command cmd = createCopyCommand();
		if (cmd != null && cmd.canExecute())
			execute(cmd);
	}

	@Override
	protected boolean calculateEnabled() {
		List<WorkflowNode> selectedModels = getSelectedModels();
		return selectedModels.size() > 0 ? true : false;
	}

	private List<WorkflowNode> getSelectedModels() {
		List<WorkflowNode> models = new ArrayList<WorkflowNode>();

		List<Object> objects = getSelectedObjects();
		for (Object obj : objects) {
			if (obj instanceof WorkflowNodeEditPart){
				WorkflowNodeEditPart part = (WorkflowNodeEditPart)obj;
				models.add((WorkflowNode)part.getModel());
			}
		}

		return models;
	}
}
