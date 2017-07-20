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
import com.geojmodelbuilder.ui.models.WorkflowArtifact;
/**
 * 
 * @author Mingda Zhang
 *
 */
public class TargetArtifactSelectorDialog extends TitleAreaDialog {

	private List<ProcessInputArtifact> inputPorts;
	private ProcessInputArtifact targetInputPort;
	private WorkflowArtifact sourceArtifact;
	private List<WorkflowArtifact> sourceArtifacts;
	private Combo comboTargetItem,comboSourceItem;
	private Map<Integer, ProcessInputArtifact> mapInputPort = new HashMap<Integer, ProcessInputArtifact>();
	private Map<Integer, WorkflowArtifact> mapSourceArtifact = new HashMap<Integer, WorkflowArtifact>();
	private Composite container_1;

	public TargetArtifactSelectorDialog(Shell parentShell,List<WorkflowArtifact> sourceArtifacts, List<ProcessInputArtifact> inputPorts) {
		super(parentShell);
		this.inputPorts = inputPorts;
		this.sourceArtifacts = sourceArtifacts;
	}

	@Override
	public void create() {
		super.create();
		setTitle("Dataflow construction");
		setMessage("Please choose available source item and target item.",
				IMessageProvider.INFORMATION);
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		Composite area = (Composite) super.createDialogArea(parent);
		container_1 = new Composite(area, SWT.NONE);
		container_1.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		GridLayout gl_container_1 = new GridLayout(2, false);
		container_1.setLayout(gl_container_1);

		createCombo(container_1);
		return area;
	}

	private void createCombo(Composite container){
		Label lblSourceItem = new Label(container_1, SWT.NONE);
		lblSourceItem.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblSourceItem.setText("Source Item:");
		
		comboSourceItem = new Combo(container_1, SWT.NONE);
		comboSourceItem.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		int i = 0;
		for(WorkflowArtifact sourceArtifact:this.sourceArtifacts){
			comboSourceItem.add(sourceArtifact.getName(),i);
			mapSourceArtifact.put(i, sourceArtifact);
			i++;
		}
		if(i>0)
			comboSourceItem.select(0);
		
		Label lblTargetItem = new Label(container, SWT.NONE);
		lblTargetItem.setText("Target Item:");
		
		GridData gd_comboTargetItem = new GridData();
		gd_comboTargetItem.grabExcessHorizontalSpace = true;
		gd_comboTargetItem.horizontalAlignment = GridData.FILL;
		comboTargetItem = new Combo(container, SWT.BORDER);
		comboTargetItem.setLayoutData(gd_comboTargetItem);
		i = 0;
		for(ProcessInputArtifact inputPort:this.inputPorts){
			comboTargetItem.add(inputPort.getName(), i);
			mapInputPort.put(i, inputPort);
			i++;
		}
		if(i>0)
			comboTargetItem.select(0);
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
		return new Point(450, 236);
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
		int index = comboTargetItem.getSelectionIndex();
		this.targetInputPort =  mapInputPort.get(index);
		
		index = comboSourceItem.getSelectionIndex();
		this.sourceArtifact = mapSourceArtifact.get(index);
	}

	@Override
	protected void okPressed() {
		saveSelection();
		
		super.okPressed();
	}

	public ProcessInputArtifact getTargetInputPort(){
		return this.targetInputPort;
	}
	
	public WorkflowArtifact getSourceArtifact(){
		return this.sourceArtifact;
	}
}
