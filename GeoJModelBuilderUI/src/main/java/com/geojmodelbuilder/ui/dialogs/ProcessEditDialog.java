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
import org.eclipse.jface.viewers.TableLayout;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Monitor;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;

import com.geojmodelbuilder.core.template.impl.SpatialMetadata;
import com.geojmodelbuilder.ui.models.ProcessInputArtifact;
import com.geojmodelbuilder.ui.models.ProcessOutputArtifact;
import com.geojmodelbuilder.ui.models.WorkflowProcess;

/**
 * @author Mingda Zhang
 *
 */
public class ProcessEditDialog extends Dialog implements SelectionListener {

	private String procName;
	private Text textAbstract;
	private Composite container;
	private Label lblNewLabel;
	private Button btnAddInput;
	private Button btnDeleteInput;
	private Text textName;
	private ScrolledComposite scrolledCompositeInput;
	private WorkflowProcess process;
	private Table tableInput;
	private Label lblOutputs;
	private ScrolledComposite scrolledCompositeOutput;
	private Button btnAddOutput;
	private Button btnDeleteOutput;
	private Table tableOutput;

	/**
	 * @wbp.parser.constructor
	 */
	public ProcessEditDialog(Shell parentShell) {
		super(parentShell);
	}

	public ProcessEditDialog(Shell parentShell, WorkflowProcess process) {
		this(parentShell);
		this.process = process;
	}

	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText("Edit Process Dialog");
		newShell.setSize(350, 400);
	}

	@Override
	protected void initializeBounds() {
		super.initializeBounds();
		Shell shell = this.getShell();
		Monitor primary = shell.getMonitor();
		Rectangle bounds = primary.getBounds();
		Rectangle rect = shell.getBounds();
		int x = bounds.x + (bounds.width - rect.width) / 2;
		int y = bounds.y + (bounds.height - rect.height) / 2;
		shell.setLocation(x, y);
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		container = (Composite) super.createDialogArea(parent);
		GridLayout gridLayout = new GridLayout(2, false);
		gridLayout.marginRight = 5;
		gridLayout.marginLeft = 10;
		gridLayout.marginTop = 12;
		container.setLayout(gridLayout);

		Label lblUrl = new Label(container, SWT.RIGHT);
		lblUrl.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false,
				1, 1));
		lblUrl.setText("Name:");

		textName = new Text(container, SWT.BORDER);
		textName.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false,
				1, 1));

		Label lblName = new Label(container, SWT.RIGHT);
		lblName.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false,
				1, 1));
		lblName.setText("Abstract:");

		textAbstract = new Text(container, SWT.BORDER);
		textAbstract.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true,
				false, 1, 1));
		new Label(container, SWT.NONE);

		lblNewLabel = new Label(container, SWT.NONE);
		lblNewLabel.setText("Inputs:");

		btnAddInput = new Button(container, SWT.NONE);
		btnAddInput.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false,
				false, 1, 1));
		btnAddInput.setText("Add");
		btnAddInput.addSelectionListener(this);

		scrolledCompositeInput = new ScrolledComposite(container, SWT.BORDER
				| SWT.H_SCROLL | SWT.V_SCROLL);
		scrolledCompositeInput.setLayoutData(new GridData(SWT.FILL, SWT.FILL,
				false, false, 1, 3));
		scrolledCompositeInput.setExpandHorizontal(true);
		scrolledCompositeInput.setExpandVertical(true);

		tableInput = new Table(scrolledCompositeInput, SWT.BORDER
				| SWT.FULL_SELECTION);
		tableInput.setHeaderVisible(true);
		tableInput.setLinesVisible(true);
		scrolledCompositeInput.setContent(tableInput);
		scrolledCompositeInput.setMinSize(tableInput.computeSize(SWT.DEFAULT,
				SWT.DEFAULT));
		tableInput.setLayoutData(new TableLayout());

		String[] tableHeader = { "Name", "Description" };
		for (int i = 0; i < tableHeader.length; i++) {
			TableColumn tableColumn = new TableColumn(tableInput, SWT.NONE);
			tableColumn.setText(tableHeader[i]);
			tableColumn.setWidth(80);
			tableColumn.setMoveable(false);
		}

		btnDeleteInput = new Button(container, SWT.NONE);
		btnDeleteInput.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false,
				false, 1, 1));
		btnDeleteInput.setText("Delete");
		btnDeleteInput.addSelectionListener(this);
		new Label(container, SWT.NONE);
		new Label(container, SWT.NONE);

		lblOutputs = new Label(container, SWT.NONE);
		lblOutputs.setText("Outputs:");

		btnAddOutput = new Button(container, SWT.NONE);
		btnAddOutput.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false,
				false, 1, 1));
		btnAddOutput.setText("Add");
		btnAddOutput.addSelectionListener(this);

		scrolledCompositeOutput = new ScrolledComposite(container, SWT.BORDER
				| SWT.H_SCROLL | SWT.V_SCROLL);
		scrolledCompositeOutput.setLayoutData(new GridData(SWT.FILL, SWT.FILL,
				false, false, 1, 3));
		scrolledCompositeOutput.setExpandHorizontal(true);
		scrolledCompositeOutput.setExpandVertical(true);

		tableOutput = new Table(scrolledCompositeOutput, SWT.BORDER
				| SWT.FULL_SELECTION);
		tableOutput.setHeaderVisible(true);
		tableOutput.setLinesVisible(true);
		for (int i = 0; i < tableHeader.length; i++) {
			TableColumn tableColumn = new TableColumn(tableOutput, SWT.NONE);
			tableColumn.setText(tableHeader[i]);
			tableColumn.setWidth(80);
			tableColumn.setMoveable(false);
		}

		scrolledCompositeOutput.setContent(tableOutput);
		scrolledCompositeOutput.setMinSize(tableOutput.computeSize(SWT.DEFAULT,
				SWT.DEFAULT));

		btnDeleteOutput = new Button(container, SWT.NONE);
		btnDeleteOutput.setText("Delete");
		btnDeleteOutput.addSelectionListener(this);
		new Label(container, SWT.NONE);

		initTable();

		return container;
	}

	public void initTable() {
		if (this.process == null) {
			this.process = new WorkflowProcess();
			ProcessOutputArtifact output = new ProcessOutputArtifact(
					this.process);
			output.setName("output");
			SpatialMetadata spatialMetadata = new SpatialMetadata();
			spatialMetadata.setDescription("Default output");
			this.process.setName("Process");
			this.process.addOutputArtifact(output);
		}

		this.textName.setText(this.process.getName());
		String processDes = this.process.getDescription();
		processDes = processDes == null ? "" : processDes;
		this.textAbstract.setText(processDes);

		for (ProcessInputArtifact input : this.process.getInputs()) {
			String description = getDes(input.getSptialDescription());
			TableItem item = new TableItem(this.tableInput, SWT.NONE);
			item.setText(new String[] { input.getName(), description });
		}

		for (ProcessOutputArtifact output : this.process.getOutputs()) {
			String description = getDes(output.getSptialDescription());
			TableItem item = new TableItem(this.tableOutput, SWT.NONE);
			item.setText(new String[] { output.getName(), description });
		}
	}

	private String getDes(SpatialMetadata spatialMetadata) {
		if (spatialMetadata == null)
			return "";

		String description = spatialMetadata.getDescription();
		return description == null ? "" : description;
	}

	private void saveInput() {
		this.procName = this.textName.getText();
		this.process.setName(procName);

		String description = this.textAbstract.getText();
		this.process.setDescription(description);

		for (int i = 0; i < this.tableInput.getItemCount(); i++) {
			TableItem inputItem = this.tableInput.getItem(i);
			String inputName = inputItem.getText(0);
			if (this.process.getInput(inputName) != null)
				continue;

			ProcessInputArtifact inputArtifact = new ProcessInputArtifact(
					inputName);
			inputArtifact.setOwner(this.process);
			this.process.addInputArtifact(inputArtifact);

			String inputDes = inputItem.getText(1);
			if (inputDes == null || inputDes.equals(""))
				continue;

			SpatialMetadata spatialMetadata = new SpatialMetadata();
			spatialMetadata.setDescription(inputDes);
			inputArtifact.setSpatialDescription(spatialMetadata);
		}

		for (int i = 0; i < this.tableOutput.getItemCount(); i++) {
			TableItem outputItem = this.tableOutput.getItem(i);
			String outputName = outputItem.getText(0);

			if (this.process.getOutput(outputName) != null)
				continue;

			ProcessOutputArtifact outputArtifact = new ProcessOutputArtifact(
					this.process);
			outputArtifact.setName(outputName);
			this.process.addOutputArtifact(outputArtifact);
			outputArtifact.setOwner(this.process);

			String outputDes = outputItem.getText(1);
			if (outputDes == null || outputDes.equals(""))
				continue;

			SpatialMetadata spatialMetadata = new SpatialMetadata();
			spatialMetadata.setDescription(outputDes);
			outputArtifact.setSpatialDescription(spatialMetadata);
		}
	}

	@Override
	protected boolean isResizable() {
		return true;
	}

	@Override
	protected void okPressed() {
		this.procName = this.textName.getText();

		if (this.procName.equals("")) {
			MessageDialog.openWarning(this.getShell(), "Warning",
					"Name field is required.");
			return;
		}
		
		/* check the number of inputs and outputs
		int inputCount = this.tableInput.getItemCount();
		int outputCount = this.tableOutput.getItemCount();

		if (inputCount == 0 || outputCount == 0) {
			MessageDialog.openWarning(this.getShell(), "Warning",
					"There is at least one input and one output.");
			return;
		}
		*/
		
		saveInput();
		super.okPressed();
	}

	public WorkflowProcess getWorkflowProcess() {
		return this.process;
	}

	@Override
	public void widgetSelected(SelectionEvent e) {
		Object eventObj = e.getSource();
		if (eventObj == btnAddInput) {
			System.out.println("add");
			ParameterAddDialog addDialog = new ParameterAddDialog(
					this.getShell());
			if (addDialog.open() == Window.OK) {
				String name = addDialog.getName();
				String description = addDialog.getDescription();
				TableItem item = new TableItem(this.tableInput, SWT.NONE);
				item.setText(new String[] { name, description });
			}
		} else if (eventObj == btnDeleteInput) {
			System.out.println("delete");
			int index = this.tableInput.getSelectionIndex();
			if (index != -1)
				this.tableInput.remove(index);
		} else if (eventObj == btnAddOutput) {
			ParameterAddDialog addDialog = new ParameterAddDialog(
					this.getShell());
			if (addDialog.open() == Window.OK) {
				String name = addDialog.getName();
				String description = addDialog.getDescription();
				TableItem item = new TableItem(this.tableOutput, SWT.NONE);
				item.setText(new String[] { name, description });
			}
		} else if (eventObj == btnDeleteOutput) {
			int index = this.tableOutput.getSelectionIndex();
			if (index != -1)
				this.tableOutput.remove(index);
		}
	}

	@Override
	public void widgetDefaultSelected(SelectionEvent e) {
	}

	public static void main(String[] args) {
		ProcessEditDialog dialog = new ProcessEditDialog(null);
		dialog.open();
	}

}
