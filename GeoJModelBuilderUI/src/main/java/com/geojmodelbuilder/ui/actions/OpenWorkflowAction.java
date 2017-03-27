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

import org.eclipse.jface.action.Action;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.geojmodelbuilder.semantic.deserialization.RDF2Instance;
import org.geojmodelbuilder.semantic.deserialization.RDF2Template;
import org.geojmodelbuilder.semantic.ont.WorkflowOntModel;

import com.geojmodelbuilder.core.instance.IWorkflowInstance;
import com.geojmodelbuilder.core.template.IWorkflowTemplate;
import com.geojmodelbuilder.core.utils.ValidateUtil;
import com.geojmodelbuilder.ui.editors.ModelEditor;
import com.geojmodelbuilder.ui.models.ModelFactory;
import com.geojmodelbuilder.ui.models.Workflow;

/**
 * @author Mingda Zhang
 *
 */
public class OpenWorkflowAction extends Action{

	public static final String ID = "custom.file.open";
	private IWorkbenchWindow window;
	public OpenWorkflowAction(IWorkbenchWindow window){
		this.window = window;
		this.setText("Open");
		this.setId(ID);
	}
	
	@Override
	public void run() {
		super.run();
		
		FileDialog dialog = new FileDialog(window.getShell(), SWT.OPEN);
		dialog.setFilterExtensions(new String [] {"*.rdf"});
		dialog.setFilterPath("c:/");
		String filePath = dialog.open();
		
		if(ValidateUtil.isStrEmpty(filePath))
			return;
		
		IEditorPart editorPart = window.getActivePage().getActiveEditor();
		if(!(editorPart instanceof ModelEditor))
			return;
		
		boolean flagTemplate = WorkflowOntModel.getInstance().existWorkflowTemplate(filePath);
		boolean flagInstance= WorkflowOntModel.getInstance().existWorkflowInstance(filePath);
		
		Workflow workflow = null;
		if(flagTemplate){
			RDF2Template rdf2Template = new RDF2Template(filePath);
			IWorkflowTemplate workflowTemplate = rdf2Template.parse();
			workflow = ModelFactory.getInstance().createWorkflow(workflowTemplate);
		}
		
		if(!flagTemplate && flagInstance){
			RDF2Instance rdf2Instance = new RDF2Instance();
			IWorkflowInstance workflowInstance = rdf2Instance.parse(filePath);
			workflow = ModelFactory.getInstance().createWorkflow(workflowInstance);
		}
		
		if (workflow == null) {
			MessageBox mb = new MessageBox(window.getShell(), SWT.OK);
			mb.setMessage("There is no workflow templates or instances.");
			mb.setText("Warning");
			mb.open();
			return;
		}
		
		ModelEditor modelEditor = (ModelEditor)editorPart;
		modelEditor.setWorkflow(workflow);
		
	}

}
