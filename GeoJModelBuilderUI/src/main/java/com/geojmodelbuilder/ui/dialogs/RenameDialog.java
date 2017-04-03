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
public class RenameDialog extends Dialog {

	private String newName;
	private String oldName;
	private Text textName;
	public RenameDialog(Shell parentShell,String oldName) {
		super(parentShell);
		this.oldName = oldName;
	}

	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText("Rename Dialog");
		newShell.setSize(400, 150);
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
		 Composite container = (Composite) super.createDialogArea(parent);
         GridLayout layout = new GridLayout(2, false);
         layout.marginRight = 5;
         layout.marginLeft = 10;
         layout.marginTop = 12;
         container.setLayout(layout);
         
         Label lblUrl = new Label(container, SWT.RIGHT);
         lblUrl.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
         lblUrl.setText("New Name:");
         
         textName = new Text(container, SWT.BORDER);
         textName.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
         if(this.oldName != null)
        	 textName.setText(this.oldName);
         
		return container;
	}
	
	
	private void saveInput(){
		this.newName = this.textName.getText();
	}
	
	public String getNewName(){
		return this.newName;
	}
	
	@Override
	protected boolean isResizable() {
		return true;
	}
	
	@Override
	protected void okPressed() {
		saveInput();
		super.okPressed();
	}
	
	public static void main(String[] args){
		RenameDialog dialog = new RenameDialog(null,"old name");
		dialog.open();
	}
}
