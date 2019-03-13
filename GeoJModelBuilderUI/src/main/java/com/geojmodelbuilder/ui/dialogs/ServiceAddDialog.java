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
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Monitor;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import com.geojmodelbuilder.core.resource.ogc.wps.WPService;

/**
 * @author Mingda Zhang
 *
 */
public class ServiceAddDialog extends Dialog {

	private String version;
	private String url;
	private String name;
	private Combo comboType;
	private Combo comboVersion; 
	private Text textName;
	private WPService wpService;
	private Combo comboUrl;
	public ServiceAddDialog(Shell parentShell) {
		super(parentShell);
	}

	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText("Add Service Dialog");
		newShell.setSize(500, 400);
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
         lblUrl.setText("URL:");
         
         createComboUrl(container);
         
         Label lblType = new Label(container, SWT.RIGHT);
         lblType.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
         lblType.setText("Type:");
         
         createComboType(container);
         
         Label lblVersion = new Label(container, SWT.RIGHT);
         lblVersion.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
         lblVersion.setText("Version:");
         
         createComboVersion(container);
         
         Label lblName = new Label(container, SWT.RIGHT);
         lblName.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
         lblName.setText("Name:");
         
         textName = new Text(container, SWT.BORDER);
         textName.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
         
		return container;
	}
	
	private void createComboUrl(Composite container){
		comboUrl = new Combo(container, SWT.NONE);
        comboUrl.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
        comboUrl.add("http://geos.whu.edu.cn:8080/wps10/WebProcessingService");
	}
	private void createComboType(Composite container){
		comboType = new Combo(container, SWT.READ_ONLY);
        comboType.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		comboType.add("WPS");
		comboType.select(0);
	}
	
	private void createComboVersion(Composite container){
		 comboVersion = new Combo(container, SWT.READ_ONLY);
         comboVersion.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1)); 
         comboVersion.add("1.0.0");
         comboVersion.select(0);
	}
	
	private void saveInput(){
		this.name = this.textName.getText();
		this.version = this.comboVersion.getText();
		this.url = this.comboUrl.getText();
		
		wpService = new WPService();
		wpService.setName(this.name);
		wpService.setUrl(url);
		wpService.setVersion(this.version);
	}
	
	@Override
	protected boolean isResizable() {
		return true;
	}
	
	@Override
	protected void okPressed() {
		saveInput();
		if(this.url.equals("") || this.name.equals("")){
			MessageDialog.openWarning(this.getShell(), "Warning","Url and Name fields are must.");
			return;
		}
		super.okPressed();
	}
	
	public WPService getWPS(){
		return wpService;
	}
	public static void main(String[] args){
		ServiceAddDialog dialog = new ServiceAddDialog(null);
		dialog.open();
	}
}
