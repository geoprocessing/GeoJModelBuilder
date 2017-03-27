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

import java.util.List;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import com.geojmodelbuilder.core.template.impl.SpatialMetadata;
import com.geojmodelbuilder.ui.properties.IOProperties;

/**
 * @author Mingda Zhang
 *
 */
public class SpatialMetadataDialog extends Dialog {
	private Text textAbstract;
	private Text textKeywords;
	private Combo comboMimeType;
	private SpatialMetadata spatialMetadata;

	public SpatialMetadataDialog(Shell parentShell, SpatialMetadata spatialMetadata) {
		super(parentShell);
		this.spatialMetadata = spatialMetadata;
	}

	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText("Spatial metadata");
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		Composite container = (Composite) super.createDialogArea(parent);
		GridLayout layout = new GridLayout(2, false);
		layout.marginRight = 5;
		layout.marginLeft = 10;
		layout.marginTop = 12;
		container.setLayout(layout);
		
		Label lblAbstract = new Label(container, SWT.NONE);
		lblAbstract.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblAbstract.setText("Abstract:");
		
		textAbstract = new Text(container, SWT.BORDER);
		textAbstract.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		if(this.spatialMetadata!=null && hasValue(this.spatialMetadata.getDescription()))
			textAbstract.setText(this.spatialMetadata.getDescription());
		
		Label lblKeywords = new Label(container, SWT.NONE);
		lblKeywords.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblKeywords.setText("KeyWords:");
		
		textKeywords = new Text(container, SWT.BORDER);
		textKeywords.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		if(this.spatialMetadata!=null && hasValue(this.spatialMetadata.getKeyWords()))
			textKeywords.setText(this.spatialMetadata.getKeyWords());
		
		Label lblMimeType = new Label(container, SWT.NONE);
		lblMimeType.setText("MimeType:");
		lblMimeType.setToolTipText("This is a must when invoking OGC WPS.");
		
		initializeMimeType(container);
		
		return container;
	}
	
	
	private boolean hasValue(String str){
		if(str != null && !str.trim().equals(""))
			return true;
		
		return false;
	}
	
	private void initializeMimeType(Composite container){
		comboMimeType = new Combo(container, SWT.NONE);
		comboMimeType.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		List<String> mimeTypes = IOProperties.getInstance().getMimeTypes();
		for(String mimeType:mimeTypes){
			comboMimeType.add(mimeType);
		}
		
		if(this.spatialMetadata!=null && hasValue(this.spatialMetadata.getMimeType()))
			comboMimeType.setText(this.spatialMetadata.getMimeType());
		
	}
	
	
	@Override
	protected boolean isResizable() {
		return true;
	}
	
	@Override
	protected void okPressed() {
		if(spatialMetadata!=null){
			spatialMetadata.setMimeType(this.comboMimeType.getText());
			spatialMetadata.setDescription(this.textAbstract.getText());
			spatialMetadata.setKeyWords(this.textKeywords.getText());
		}
		super.okPressed();
	}
	
	public static void main(String[] args){
		SpatialMetadataDialog dialog = new SpatialMetadataDialog(null, new SpatialMetadata());
		dialog.open();
	}
}
