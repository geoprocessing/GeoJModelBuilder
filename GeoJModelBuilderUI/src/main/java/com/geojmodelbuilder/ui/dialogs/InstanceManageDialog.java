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
import org.eclipse.jface.window.Window;
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

import com.geojmodelbuilder.core.instance.IProcessInstance;
import com.geojmodelbuilder.core.resource.ogc.wps.WPSProcess;
import com.geojmodelbuilder.ui.models.WorkflowProcess;

/**
 * @author Mingda Zhang
 *
 */
public class InstanceManageDialog extends Dialog {

	private final static int ADD_ID = 101;
	private final static int Detail_ID = 102;
	private WorkflowProcess process;
	private Combo comboInstance;
	private IProcessInstance originInstance;

	public InstanceManageDialog(Shell parentShell, WorkflowProcess process) {
		super(parentShell);
		this.process = process;
	}

	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText("Process Instance Manage Dialog");
		newShell.setSize(420, 150);
	}

	@Override
	protected void initializeBounds() {

		Composite comp = (Composite) getButtonBar();
		super.createButton(comp, ADD_ID, "Add", true);
		super.createButton(comp, Detail_ID, "Detail", true);

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
		GridLayout layout = new GridLayout(2, false);
		layout.marginRight = 5;
		layout.marginLeft = 10;
		layout.marginTop = 12;
		container.setLayout(layout);

		Label lblUrl = new Label(container, SWT.RIGHT);
		lblUrl.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false,
				1, 1));
		lblUrl.setText("Process Instance:");

		comboInstance = new Combo(container, SWT.READ_ONLY);
		comboInstance.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true,
				false, 1, 1));

		initComp();
		return container;
	}

	private void initComp() {
		IProcessInstance activeInstance = this.process.getActiveInstance();
		if (activeInstance == null)
			return;

		originInstance = activeInstance;
		updateInstanceChoices(originInstance);
	}

	private void updateInstanceChoices(IProcessInstance select) {
		this.comboInstance.removeAll();
		this.comboInstance.add(select.getName());
		this.comboInstance.select(0);
		List<IProcessInstance> instances = this.process.getInstances();
		for (IProcessInstance inst : instances) {
			if (inst == select)
				continue;
			this.comboInstance.add(inst.getName());
		}
	}

	@Override
	protected boolean isResizable() {
		return true;
	}

	@Override
	protected void buttonPressed(int buttonId) {
		if (buttonId == ADD_ID) {
			System.out.println("add instance");

			InstanceAddDialog dialog = new InstanceAddDialog(this.getShell(),
					process);
			if (dialog.open() == Window.OK) {
				System.out.println("update");
				WPSProcess wpsInstance = dialog.getInstance();
				this.process.addExectableProcess(wpsInstance);
				this.process.addProcessMap(wpsInstance,
						dialog.getParameterMap());
				this.updateInstanceChoices(wpsInstance);
			}

		} else if (buttonId == Detail_ID) {
			System.out.println("instance detail");
			String instanceStr = this.comboInstance.getText();
			IProcessInstance instance = process.getInstance(instanceStr);
			if (!(instance instanceof WPSProcess))
				return;
			WPSProcess wpsProcess = (WPSProcess) instance;
			InstanceDetailDialog dialog = new InstanceDetailDialog(
					this.getShell(), wpsProcess, process);
			dialog.open();
		}
		super.buttonPressed(buttonId);
	}

	@Override
	protected void okPressed() {
		String newActive = this.comboInstance.getText();
		if (!newActive.equals(originInstance)) {
			this.process.setActiveInstance(newActive);
		}
		super.okPressed();
	}

	public static void main(String[] args) {
		InstanceManageDialog dialog = new InstanceManageDialog(null,
				new WorkflowProcess());
		dialog.open();
	}
}
