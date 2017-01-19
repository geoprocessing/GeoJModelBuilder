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

import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.geojmodelbuilder.ui.dialogs.WorkflowExecutionMonitorDialog;
import com.geojmodelbuilder.ui.editors.ModelEditor;
import com.geojmodelbuilder.ui.models.Workflow;
import com.geojmodelbuilder.ui.utils.ImageDescriptorProvider;

/**
 * @author Mingda Zhang
 *
 */
public class WorkflowExecuteAction extends Action {
	private IWorkbenchWindow window;
	public static String ID = "action.workflow.run";
	private Logger logger;
	public WorkflowExecuteAction(IWorkbenchWindow window) {
		this.window = window;
		this.setText("Run");
		ImageDescriptor descriptor = ImageDescriptorProvider.getInstance()
				.getImageDescriptor(ImageDescriptorProvider.IMG_LAUNCH_RUN);
		if (descriptor != null)
			setImageDescriptor(descriptor);
		
		logger = LoggerFactory.getLogger(WorkflowExecuteAction.class);
	}

	@Override
	public String getId() {
		return WorkflowExecuteAction.ID;
	}
	
	@Override
	public void run() {
		super.run();
		IEditorPart editorPart = window.getActivePage().getActiveEditor();
		
		if(editorPart == null){
			MessageDialog.openWarning(window.getShell(), "Warning", "Please open the choose the workflow to execute.");
			return;
		}
		
		if(!(editorPart instanceof ModelEditor)){
			logger.error("The editor is supposed to ");
			return;
		}
		
		ModelEditor modelEditor = (ModelEditor)editorPart;
		Workflow workflow = modelEditor.getWorkflow();
		
		if(workflow.getProcessCount() == 0){
			MessageDialog.openInformation(window.getShell(), "Warning", "There is no process in the workflow.");
			return;
		}
		
		WorkflowExecutionMonitorDialog executionMonitor = new WorkflowExecutionMonitorDialog(window.getShell(), workflow);
		executionMonitor.open();
		/*Recipe2Plan recipe2Plan = new Recipe2Plan(workflow);
		recipe2Plan.transfer();
		WorkflowImpl workflowImpl = recipe2Plan.getExecutableWorkflow();*/
		
		System.out.println(workflow.getProcessCount());
		
	}
}
