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

import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

/**
 * @author Mingda Zhang
 *
 */
public class WorkspaceLauncherDialog extends TitleAreaDialog implements SelectionListener{
	private Combo comboPath;
	private String path;
	public WorkspaceLauncherDialog(Shell parentShell) {
		super(parentShell);
	}

	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText("Workspace Launcher");
	}
	
	@Override
	protected Control createDialogArea(Composite parent) {
		setTitle("Select a workspace");
		setMessage("GeoJModelBuilder stores your projects in a folder called a workspace.\nChoose a workspace folder to use for this session.");
	
		Composite area = (Composite) super.createDialogArea(parent);
		Composite container = new Composite(area, SWT.NONE);
		GridData gd_container = new GridData(SWT.FILL, SWT.FILL, true, true);
		gd_container.widthHint = 443;
		gd_container.heightHint = 68;
		container.setLayoutData(gd_container);
		GridLayout layout = new GridLayout(3, false);
		container.setLayout(layout);

		Label lbteWorkspace = new Label(container, SWT.NONE);
		lbteWorkspace.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lbteWorkspace.setText("Workspace:");
		
		//add history input, suppose to modify.
		comboPath = new Combo(container, SWT.NONE);
		comboPath.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		// temporarily 
		comboPath.add("C:/workspace");
		comboPath.select(0);
		
		Button btnBrowse = new Button(container, SWT.NONE);
		btnBrowse.addSelectionListener(this);
		btnBrowse.setText("Browse...");
		return area;
	}

	@Override
	protected boolean isResizable() {
		return true;
	}

	// save content of the Combo fields because they get disposed
	// as soon as the Dialog closes
	private void saveInput() {
		this.path = this.comboPath.getText();
	}

	@Override
	protected void okPressed() {
		saveInput();
		super.okPressed();
	}

	
	public String getSelectedPath(){
		return this.path;
	}

	public static void main(String[] args) {
		WorkspaceLauncherDialog dialog = new WorkspaceLauncherDialog(null);
		if (dialog.open() == Window.OK) {
			System.out.println(dialog.getSelectedPath());
		}
	}

	@Override
	public void widgetSelected(SelectionEvent e) {
		DirectoryDialog dialog = new DirectoryDialog(this.getShell());
		String path = dialog.open();
		if(path!=null && !path.equals("")){
			this.comboPath.add(path, 0);
			this.comboPath.select(0);
		}
	}

	@Override
	public void widgetDefaultSelected(SelectionEvent e) {}

}
