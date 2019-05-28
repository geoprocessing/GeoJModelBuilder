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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

import com.geojmodelbuilder.ui.models.ProcessInputArtifact;
/**
 * 
 * @author Mingda Zhang
 *
 */
public class TargetArtifactSelectorDialog extends TitleAreaDialog {

	private List<ProcessInputArtifact> inputPorts;
	private ProcessInputArtifact targetInputPort;
	private Combo comboInputPort;
	private Map<Integer, ProcessInputArtifact> mapInputPort = new HashMap<Integer, ProcessInputArtifact>();

	public TargetArtifactSelectorDialog(Shell parentShell,List<ProcessInputArtifact> inputPorts) {
		super(parentShell);
		this.inputPorts = inputPorts;
	}

	@Override
	public void create() {
		super.create();
		setTitle("Dataflow construction");
		setMessage("Please choose an available input port.",
				IMessageProvider.INFORMATION);
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		Composite area = (Composite) super.createDialogArea(parent);
		Composite container = new Composite(area, SWT.NONE);
		container.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		GridLayout layout = new GridLayout(2, false);
		container.setLayout(layout);

		createCombo(container);
		return area;
	}

	private void createCombo(Composite container){
		Label lblInputPort = new Label(container, SWT.NONE);
		lblInputPort.setText("Input Port");
		
		GridData dataInputPort = new GridData();
		dataInputPort.grabExcessHorizontalSpace = true;
		dataInputPort.horizontalAlignment = GridData.FILL;
		comboInputPort = new Combo(container, SWT.BORDER);
		comboInputPort.setLayoutData(dataInputPort);
		int i = 0;
		for(ProcessInputArtifact inputPort:this.inputPorts){
			comboInputPort.add(inputPort.getName(), i);
			mapInputPort.put(i, inputPort);
			i++;
		}
	}
	
	//reference
	/*private void createFirstName(Composite container) {
		Label lbtFirstName = new Label(container, SWT.NONE);
		lbtFirstName.setText("First Name");

		GridData dataFirstName = new GridData();
		dataFirstName.grabExcessHorizontalSpace = true;
		dataFirstName.horizontalAlignment = GridData.FILL;

		txtFirstName = new Text(container, SWT.BORDER);
		txtFirstName.setLayoutData(dataFirstName);
	}*/

	@Override
	protected Point getInitialSize() {
		return new Point(500, 350);
	}

	@Override
	protected Point getInitialLocation(Point initialSize) {
		// TODO Auto-generated method stub
		return super.getInitialLocation(initialSize);
	}
	@Override
	protected boolean isResizable() {
		return true;
	}

	// save content of the combo fields because they get disposed
	// as soon as the Dialog closes
	private void saveSelection() {
		int index = comboInputPort.getSelectionIndex();
		this.targetInputPort =  mapInputPort.get(index);
	}

	@Override
	protected void okPressed() {
		saveSelection();
		
		super.okPressed();
	}

	public ProcessInputArtifact getTargetInputPort(){
		return this.targetInputPort;
	}
}
