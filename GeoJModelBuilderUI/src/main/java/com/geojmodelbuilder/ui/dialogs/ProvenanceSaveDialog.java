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

import java.text.SimpleDateFormat;
import java.util.List;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Monitor;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

import com.geojmodelbuilder.core.provenance.IWorkflowProv;
import com.geojmodelbuilder.ui.models.Workflow;

/**
 * @author Mingda Zhang
 *
 */
public class ProvenanceSaveDialog extends Dialog {

	private Workflow workflow;
	private Table table;
	private int[] selectIndices;
	private int index = -1;

	public ProvenanceSaveDialog(Shell parentShell, Workflow workflow) {
		super(parentShell);
		this.workflow = workflow;
	}

	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText("Save Provenance");
		newShell.setSize(400, 260);
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
		Composite container = (Composite) super.createDialogArea(parent);
		GridLayout layout = new GridLayout(1, false);
		layout.marginRight = 5;
		layout.marginLeft = 10;
		layout.marginTop = 12;
		container.setLayout(layout);

		Label lblUrl = new Label(container, SWT.RIGHT);
		lblUrl.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false,
				1, 1));
		lblUrl.setText("Choose execution to save");

		ScrolledComposite scrolledComposite = new ScrolledComposite(container,
				SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		scrolledComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false,
				false, 1, 3));
		scrolledComposite.setExpandHorizontal(true);
		scrolledComposite.setExpandVertical(true);

		table = new Table(scrolledComposite, SWT.BORDER | SWT.FULL_SELECTION);

		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		scrolledComposite.setContent(table);
		scrolledComposite.setMinSize(table
				.computeSize(SWT.DEFAULT, SWT.DEFAULT));

		String[] tableHeader = {"Start Time", "End Time", "Status" };
		for (int i = 0; i < tableHeader.length; i++) {
			TableColumn tableColumn = new TableColumn(table, SWT.NONE);
			tableColumn.setText(tableHeader[i]);
			tableColumn.setWidth(120);
			tableColumn.setMoveable(false);
		}

		initComp();
		return container;
	}

	private void initComp() {
		List<IWorkflowProv> provs = workflow.getWorkflowProvs();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd'T'HH:mm:ss.SSSZ");
		for (IWorkflowProv prov : provs) {
			String startTime = dateFormat.format(prov.getStartTime());
			String endTime = dateFormat.format(prov.getEndTime());
			String status = prov.getStatus() ? "true" : "false";

			TableItem item = new TableItem(this.table, SWT.NONE);
			item.setText(new String[] {startTime, endTime, status });
		}
	}

	@Override
	protected boolean isResizable() {
		return true;
	}

	public int getIndex() {
		return this.index;
	}

	public int[] getSelectIndices() {
		return selectIndices;
	}

	@Override
	protected void okPressed() {
		// selectIndices = this.table.getSelectionIndices();
		index = this.table.getSelectionIndex();

		if (index == -1) {
			MessageDialog.openInformation(this.getShell(), "Message",
					"Please select at least one. ");
			return;
		}
		super.okPressed();
	}

	public static void main(String[] args) {
		ProvenanceSaveDialog dialog = new ProvenanceSaveDialog(null,
				new Workflow());
		dialog.open();
	}
}
