/**
 * Copyright (C) 2013 - 2016 Wuhan University,
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

import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.ui.actions.WorkbenchPartAction;
import org.eclipse.jface.window.Window;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchWindow;

import com.geojmodelbuilder.ui.commands.ProcessCreateCommand;
import com.geojmodelbuilder.ui.dialogs.ProcessEditDialog;
import com.geojmodelbuilder.ui.editors.ModelEditor;
import com.geojmodelbuilder.ui.models.Workflow;
import com.geojmodelbuilder.ui.models.WorkflowNode;
import com.geojmodelbuilder.ui.models.WorkflowProcess;

/**
 * @author Mingda Zhang
 *
 */
public class ProcessAddAction extends WorkbenchPartAction{

	public static final String ID = "workflow.process.add";
	private IWorkbenchWindow window;
	private IEditorPart editorPart;
	public ProcessAddAction(IEditorPart editorPart){
		super(editorPart);
	}
	public ProcessAddAction(IWorkbenchWindow window){
		super(window.getActivePage().getActiveEditor());
		this.window = window;
		this.setText("Add Process");
		this.setId(ID);
	}
	
	@Override
	public void run() {
		super.run();
		
		ProcessEditDialog dialog = new ProcessEditDialog(window.getShell());
		if(dialog.open() == Window.OK){
			System.out.println("update");
			WorkflowProcess process = dialog.getWorkflowProcess();
			
			editorPart = window.getActivePage().getActiveEditor();
			setWorkbenchPart(editorPart);
			
			if(!(editorPart instanceof ModelEditor))
				return;

			ModelEditor modelEditor = (ModelEditor)editorPart;
			Workflow workflow = modelEditor.getWorkflow();
			ProcessCreateCommand command = new ProcessCreateCommand();
			command.setWorkflow(workflow);
			command.setWorkflowNode(process);
			Rectangle layout = new Rectangle(10, 10, WorkflowNode.WIDTH_DEFAULT,
					WorkflowNode.HEIGHT_DEFAULT);
			command.setLayout(layout);
			execute(command);
		}
	}

	@Override
	public String getId() {
		return ID;
	}

	@Override
	protected boolean calculateEnabled() {
		return true;
	}
}
