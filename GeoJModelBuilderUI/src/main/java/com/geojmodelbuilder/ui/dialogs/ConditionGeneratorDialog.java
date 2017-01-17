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
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import com.geojmodelbuilder.ui.models.ProcessInputArtifact;
import com.geojmodelbuilder.ui.models.WorkflowCondition;
/**
 * 
 * @author Mingda Zhang
 *
 */
public class ConditionGeneratorDialog extends Dialog {

	private Text textExpr = null;
	private List listInputs;
	private WorkflowCondition condition;

	/**
	 * Create the dialog.
	 * 
	 * @param parentShell
	 */
	public ConditionGeneratorDialog(Shell parentShell,
			WorkflowCondition condition) {
		super(parentShell);
		this.condition = condition;
	}

	/**
	 * Create contents of the dialog.
	 * 
	 * @param parent
	 */
	@Override
	protected Control createDialogArea(Composite parent) {
		Composite container = (Composite) super.createDialogArea(parent);
		GridLayout gridLayout = new GridLayout(8, false);
		container.setLayout(gridLayout);

		listInputs = new List(container, SWT.BORDER);
		listInputs.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false,
				4, 4));

		listInputs.addMouseListener(new MouseListener() {

			@Override
			public void mouseUp(MouseEvent e) {
			}

			@Override
			public void mouseDown(MouseEvent e) {
			}

			@Override
			public void mouseDoubleClick(MouseEvent e) {
				if (listInputs.getItemCount() == 0)
					return;
				String selectItem = listInputs.getSelection()[0];
				textExpr.setText(selectItem);
			}
		});

		if (this.condition != null) {
			for (ProcessInputArtifact artifact : this.condition
					.getInputArtifacts()) {
				this.listInputs.add(artifact.getName());
			}
		}

		String[] strBtnNames = { "+", "-", "*", "/", "==", "!=", "<", ">",
				">=", "<=", "&&", "||" };
		for (String str : strBtnNames) {
			Button btn = new Button(container, SWT.NONE);
			GridData gridData = new GridData();
			gridData.widthHint = 40;
			gridData.heightHint = 30;
			btn.setLayoutData(gridData);
			btn.setText(str);
			btn.addSelectionListener(new SelectionListener() {

				@Override
				public void widgetSelected(SelectionEvent e) {
					Button btn = (Button) e.getSource();
					String text = btn.getText();
					System.out.println(text);
					String expr = textExpr.getText();
					expr += text;
					textExpr.setText(expr);
				}

				@Override
				public void widgetDefaultSelected(SelectionEvent e) {
				}
			});
		}

		textExpr = new Text(container, SWT.BORDER);
		GridData gridData = new GridData(SWT.FILL, SWT.FILL, false, false);
		gridData.heightHint = 30;
		gridData.horizontalSpan = 8;
		textExpr.setLayoutData(gridData);

		return container;
	}

	/**
	 * Create contents of the button bar.
	 * 
	 * @param parent
	 */
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL,
				true);
		createButton(parent, IDialogConstants.CANCEL_ID,
				IDialogConstants.CANCEL_LABEL, false);
	}

	/**
	 * Return the initial size of the dialog.
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(350, 240);
	}

	@Override
	protected boolean isResizable() {
		return true;
	}

	public static void main(String[] args) {
		WorkflowCondition condition = new WorkflowCondition();
		condition.addInputArtifact(new ProcessInputArtifact("input1"));
		condition.addInputArtifact(new ProcessInputArtifact("input2"));
		ConditionGeneratorDialog dialog = new ConditionGeneratorDialog(null,
				condition);
		dialog.open();
	}

	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText("Condition Expression");
	}

	public String getExpression() {
		return "";
	}
}
