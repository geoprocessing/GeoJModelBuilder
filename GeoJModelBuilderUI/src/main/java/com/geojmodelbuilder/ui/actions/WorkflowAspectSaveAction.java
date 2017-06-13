/**
 * Copyright (C) 2013 - 2016 Wuhan University,
 *                           Center for Geographic Analysis, Harvard University
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
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchWindow;

import com.geojmodelbuilder.core.utils.ValidateUtil;
import com.geojmodelbuilder.ui.editors.ModelEditor;
import com.geojmodelbuilder.ui.models.Workflow;
import com.geojmodelbuilder.ui.models.WorkflowAspect;
import com.geojmodelbuilder.ui.workspace.Workspace;

/**
 * @author Mingda Zhang
 * 
 * Save specific aspect of workflow in current Model editor.
 * 
 */
public abstract class WorkflowAspectSaveAction extends Action{

	public static final String ID = "workflow.aspect.save";
	protected IWorkbenchWindow window;
	protected String filePath; 
	protected Workflow workflow;
	private WorkflowAspect aspect;
	
	public WorkflowAspectSaveAction(IWorkbenchWindow window,WorkflowAspect aspect){
		this.window = window;
		this.setId(ID);
		this.setText("Save "+aspect.toString());
		this.aspect = aspect;
	}
	
	abstract boolean saveAspect();
	
	@Override
	public void run() {
		super.run();
		
		IEditorPart editorPart = window.getActivePage().getActiveEditor();
		if(!(editorPart instanceof ModelEditor))
			return;
		
		workflow = ((ModelEditor)editorPart).getWorkflow();
		
		if (workflow == null || workflow.getProcesses().size()==0) {
			MessageDialog.openWarning(window.getShell(), "Warning",
					"There is no process.");
			return;
		}
		
		FileDialog dialog = new FileDialog(window.getShell(), SWT.SAVE);
		dialog.setFilterExtensions(new String [] {"*.rdf"});
		String defaultPath = Workspace.getInstance().getModelDir();
		dialog.setFilterPath(defaultPath);
		filePath = dialog.open();
		
		if(ValidateUtil.isStrEmpty(filePath))
			return;
		
		
		if (!filePath.endsWith(".rdf")) {
			filePath += ".rdf";
		}
		
		boolean flag = saveAspect();
		
		if(flag){
			MessageDialog.openInformation(window.getShell(),"Message", "Saved "+this.aspect.toString()+" successfully.");
		}else {
			MessageDialog.openError(window.getShell(), "Message", "Failed to save "+this.aspect.toString());
		}
		
	}
}
