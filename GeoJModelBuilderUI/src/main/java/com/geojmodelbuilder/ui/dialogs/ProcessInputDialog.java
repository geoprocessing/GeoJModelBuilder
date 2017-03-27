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
import java.util.Map;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import com.geojmodelbuilder.core.template.impl.SpatialMetadata;
import com.geojmodelbuilder.core.utils.ValidateUtil;
import com.geojmodelbuilder.ui.models.ProcessInputArtifact;
import com.geojmodelbuilder.ui.models.ProcessOutputArtifact;
import com.geojmodelbuilder.ui.models.WorkflowArtifact;
import com.geojmodelbuilder.ui.models.WorkflowCondition;
import com.geojmodelbuilder.ui.models.WorkflowProcess;
import com.geojmodelbuilder.ui.models.links.DataFlow;

/**
 * 
 * @author Mingda Zhang
 *
 */
public class ProcessInputDialog extends Dialog implements ModifyListener {

	private WorkflowProcess process;
	private boolean changed = false;
	private Map<ProcessInputArtifact, String> updateMap;
	private Map<ProcessInputArtifact, String> oldValueMap;
	private Map<Text, ProcessInputArtifact> textInputMap;

	public ProcessInputDialog(Shell parentShell, WorkflowProcess process) {
		super(parentShell);
		this.process = process;
		updateMap = new HashMap<ProcessInputArtifact, String>();
		oldValueMap = new HashMap<ProcessInputArtifact, String>();
		textInputMap = new HashMap<Text, ProcessInputArtifact>();

		// test module dependencies
		WorkflowCondition condition = new WorkflowCondition();
		condition.setName("name");
		System.out.println(condition.getName());

	}

	private void createTextComposite(Composite container,
			WorkflowArtifact artifact) {

		Label label = new Label(container, SWT.NONE);
		label.setText(artifact.getName() + ": ");
		Text text = new Text(container, SWT.BORDER);
		text.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		Button btn = new Button(container, SWT.ARROW | SWT.RIGHT);
		Shell shell = this.getShell();
		btn.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				SpatialMetadata metadata = artifact.getSptialDescription();
				if(metadata == null)
					artifact.setSpatialDescription(new SpatialMetadata());
				
				SpatialMetadataDialog dialog = new SpatialMetadataDialog(shell,
						artifact.getSptialDescription());
				dialog.open();
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
			}
		});

		String value = "";
		value = artifact.getValue();
		value = value == null ? "" : value;
		text.setText(value);

		if (artifact instanceof ProcessInputArtifact) {
			ProcessInputArtifact inputArtifact = (ProcessInputArtifact) artifact;
			textInputMap.put(text, inputArtifact);
			text.addModifyListener(this);

			DataFlow dataFlow = process.getInLinkBoundWithPort(inputArtifact);
			if (dataFlow != null) {
				WorkflowProcess sourceProcess = dataFlow.getSourceProcess();
				if (sourceProcess != null)
					value = sourceProcess.getName() + "#";
				else {
					value = "";
				}
				WorkflowArtifact sourceArtifact = dataFlow.getSourceArtifact();
				if (sourceArtifact != null)
					value = value + sourceArtifact.getName();

				value = "{" + value + "}";
				text.setText(value);
				text.setEditable(false);
			}

		} else if (artifact instanceof ProcessOutputArtifact) {
			text.setEditable(false);
			
			if (process.getColor() == ColorConstants.lightGreen) {
				String result = ((ProcessOutputArtifact)artifact).getExecutedResult();
				if(!ValidateUtil.isStrEmpty(result))
					text.setText(result);
			}
			
		}

	}

	@Override
	protected Control createDialogArea(Composite parent) {
		Composite container = (Composite) super.createDialogArea(parent);
		GridLayout layout = new GridLayout(3, false);
		layout.marginRight = 5;
		layout.marginLeft = 10;
		layout.marginTop = 12;
		container.setLayout(layout);

		if (process.getInputArtifacts().size() == 0) {
			Label label = new Label(container, SWT.NONE);
			label.setText("No input parameters.");
			label.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false,
					3, 1));
		}

		for (ProcessInputArtifact inputArtifact : process.getInputArtifacts()) {
			createTextComposite(container, inputArtifact);
		}

		for (ProcessOutputArtifact outputArtifact : process
				.getOutputArtifacts()) {
			createTextComposite(container, outputArtifact);
		}

		return container;
	}

	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText(this.process.getName());
	}

	@Override
	protected Point getInitialSize() {
		return new Point(400, 300);
	}

	@Override
	protected boolean isResizable() {
		return true;
	}

	@Override
	protected void okPressed() {
		saveValue();
		super.okPressed();
	}

	protected void saveValue() {
		for (ProcessInputArtifact inputArtifact : this.updateMap.keySet()) {
			String newValue = this.updateMap.get(inputArtifact);
			inputArtifact.setValue(newValue);
		}
		if (this.updateMap.size() > 0)
			setChanged(true);
	}

	protected void setChanged(boolean changed) {
		this.changed = changed;
	}

	public boolean isChanged() {
		return changed;
	}

	@Override
	public void modifyText(ModifyEvent e) {
		Text textWidget = (Text) e.getSource();
		String newValue = textWidget.getText();

		ProcessInputArtifact inputArtifact = textInputMap.get(textWidget);
		if (inputArtifact != null) {
			updateMap.put(inputArtifact, newValue);
			oldValueMap.put(inputArtifact, inputArtifact.getValue());
		}
	}
}
