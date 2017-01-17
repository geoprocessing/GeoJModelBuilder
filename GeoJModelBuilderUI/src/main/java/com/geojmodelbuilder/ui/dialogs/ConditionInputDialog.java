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

import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import com.geojmodelbuilder.ui.models.WorkflowCondition;
/**
 * 
 * @author Mingda Zhang
 *
 */
public class ConditionInputDialog extends ProcessInputDialog implements SelectionListener{
	private WorkflowCondition workflowCondition;
	
	public ConditionInputDialog(Shell parentShell,WorkflowCondition condition){
		super(parentShell, condition);
		this.workflowCondition = condition;
	}
	
	@Override
	protected Control createDialogArea(Composite parent) {
		Composite container = (Composite) super.createDialogArea(parent);
		
		Label label = new Label(container, SWT.NONE);
   	 	label.setText("Expression:");
   	 	
		Composite conditionContainer =  new Composite(container, SWT.NONE);
		conditionContainer.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 2, 1));
		conditionContainer.setLayout(new GridLayout(2, false));
		
		Text text = new Text(conditionContainer, SWT.BORDER);
   		text.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false,
                1, 1));
   		text.setEditable(false);
   		
   		Button btnEidt = new Button(conditionContainer, SWT.NONE);
   		btnEidt.setText("Edit");
   		btnEidt.addSelectionListener(this);
		return container;
	}
	
	
	@Override
	protected void saveValue() {
		super.saveValue();
		//Expression
		workflowCondition.setExpression("");
	}

	@Override
	public void widgetSelected(SelectionEvent e) {
		// TODO Auto-generated method stub
		ConditionGeneratorDialog dialog = new ConditionGeneratorDialog(this.getShell(), this.workflowCondition);
		if(dialog.open() == Window.OK){
			System.out.println("OK pressed.");
		}
	}

	@Override
	public void widgetDefaultSelected(SelectionEvent e) {
		// TODO Auto-generated method stub
		
	}
}
