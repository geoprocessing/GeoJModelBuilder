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
package com.geojmodelbuilder.ui.dialogs;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Monitor;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Button;
import org.hamcrest.core.IsInstanceOf;

import com.geojmodelbuilder.core.utils.ValidateUtil;
import com.geojmodelbuilder.ui.utils.UIUtils;
import com.geojmodelbuilder.ui.workspace.Workspace;

/**
 * @author Mingda Zhang
 *
 */
public class WorkflowSaveDialog extends Dialog implements SelectionListener{

	private Text textPath;
	private Button btnBrowse;
	private String path;
	private  Button btnTemplate,btnInstance,btnExecution;
	private boolean template = false,instance = false, execution = false;
	
	public WorkflowSaveDialog(Shell parentShell) {
		super(parentShell);
	}

	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText("Save Workflow");
		newShell.setSize(400, 200);
	}
	
	@Override
	protected void initializeBounds() {
		super.initializeBounds();
		UIUtils.getInstance().centerDialog(this.getShell());
	}
	@Override
	protected Control createDialogArea(Composite parent) {
		 Composite container = (Composite) super.createDialogArea(parent);
         GridLayout layout = new GridLayout(5, false);
         layout.marginRight = 5;
         layout.marginLeft = 10;
         layout.marginTop = 12;
         container.setLayout(layout);
         
         Label lblUrl = new Label(container, SWT.RIGHT);
         lblUrl.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
         lblUrl.setText("Workflow:");
         
         btnTemplate = new Button(container, SWT.CHECK);
         btnTemplate.setText("Template");
         
         btnInstance = new Button(container, SWT.CHECK);
         btnInstance.setText("Instance");
         
         btnExecution = new Button(container, SWT.CHECK);
         btnExecution.setText("Execution");
         new Label(container, SWT.NONE);
         
         Label lblNewLabel = new Label(container, SWT.NONE);
         lblNewLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
         lblNewLabel.setText("Path:");
         
         textPath = new Text(container, SWT.BORDER);
         textPath.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 3, 1));
         
         btnBrowse = new Button(container, SWT.NONE);
         btnBrowse.setText("Browse");
         btnBrowse.addSelectionListener(this);
         
		return container;
	}
	
	private void saveInput(){
		this.path = this.textPath.getText();
		template = btnTemplate.getSelection();
		instance = btnInstance.getSelection();
		execution = btnExecution.getSelection();
	}
	
	public String getPath(){
		return this.path;
	}
	
	public boolean isTemplate(){
		return this.template;
	}
	
	public boolean isInstanceOf(){
		return this.instance;
	}
	
	public boolean isExecution(){
		return this.execution;
	}
	
	@Override
	protected boolean isResizable() {
		return true;
	}
	
	@Override
	protected void okPressed() {
		saveInput();
		
		if(ValidateUtil.isStrEmpty(this.path)){
			MessageDialog.openWarning(this.getShell(), "Warning",
					"Path is required.");
			return;
		}
			
		if(execution || template || execution){
			save();
			super.okPressed();
		}else {
			MessageDialog.openWarning(this.getShell(), "Warning",
					"Check at least one aspect of workflow.");
			return;
		}
	}
	
	
	private boolean save(){
		
		return true;
	}
	
	public static void main(String[] args){
		WorkflowSaveDialog dialog = new WorkflowSaveDialog(null);
		dialog.open();
	}

	@Override
	public void widgetSelected(SelectionEvent e) {
		Object eventObj = e.getSource();
		if(eventObj == btnBrowse){
			FileDialog dialog = new FileDialog(this.getShell(), SWT.SAVE);
			dialog.setFilterExtensions(new String [] {"*.rdf"});
			
			String defaultPath = Workspace.getInstance().getModelDir();
			dialog.setFilterPath(defaultPath);
			
			String filePath = dialog.open();
			
			if(ValidateUtil.isStrEmpty(filePath))
				return;
			
			if (!filePath.endsWith(".rdf")) {
				filePath += ".rdf";
			}
			
			this.textPath.setText(filePath);
		}
	}

	@Override
	public void widgetDefaultSelected(SelectionEvent e) {}
}
