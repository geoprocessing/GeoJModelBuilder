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
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Monitor;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

/**
 * @author Mingda Zhang
 *
 */
public class ParameterAddDialog extends Dialog {
	private Text textName;
	private Text textDescription;
	private String strName,strDescription;
	private Composite container_1;

	public ParameterAddDialog(Shell parentShell) {
		super(parentShell);
	}

	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText("Add Parameter");
		newShell.setSize(300, 200);
	}
	
	@Override
	protected void initializeBounds() {
		super.initializeBounds();
		Shell shell = this.getShell(); 
		Monitor primary = shell.getMonitor(); 
		Rectangle bounds = primary.getBounds (); 
		Rectangle rect = shell.getBounds (); 
		int x = bounds.x + (bounds.width - rect.width) / 2; 
		int y = bounds.y + (bounds.height - rect.height) / 2; 
		shell.setLocation (x, y); 
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		container_1 = (Composite) super.createDialogArea(parent);
		GridLayout gl_container_1 = new GridLayout(2, false);
		gl_container_1.marginRight = 5;
		gl_container_1.marginLeft = 10;
		gl_container_1.marginTop = 12;
		container_1.setLayout(gl_container_1);
		
		Label lblName = new Label(container_1, SWT.NONE);
		lblName.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblName.setText("Name:");
		
		textName = new Text(container_1, SWT.BORDER);
		textName.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblDescription = new Label(container_1, SWT.NONE);
		lblDescription.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblDescription.setText("Description:");
		
		textDescription = new Text(container_1, SWT.BORDER);
		textDescription.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		initializeMimeType(container_1);
		
		return container_1;
	}
	
	
	private void initializeMimeType(Composite container){
	}
	
	@Override
	protected boolean isResizable() {
		return true;
	}
	
	@Override
	protected void okPressed() {
		this.strName = this.textName.getText();
		this.strDescription = this.textDescription.getText();
		if(this.strName.equals("")){
			MessageDialog.openWarning(this.getShell(), "Warning","Name fields is required.");
			return;
		}
		super.okPressed();
	}
	
	public String getName(){
		return this.strName;
	}
	
	public String getDescription(){
		return this.strDescription;
	}
	
	public static void main(String[] args){
		ParameterAddDialog dialog = new ParameterAddDialog(null);
		dialog.open();
	}
}
