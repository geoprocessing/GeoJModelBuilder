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

import java.util.Map;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;

import com.geojmodelbuilder.core.instance.IParameter;
import com.geojmodelbuilder.core.resource.ogc.wps.WPSProcess;
import com.geojmodelbuilder.core.template.IPort;
import com.geojmodelbuilder.ui.models.WorkflowProcess;
import com.geojmodelbuilder.ui.utils.UIUtils;

/**
 * @author Mingda Zhang
 *
 */
public class InstanceDetailDialog extends Dialog{
	private Table table;
	private WPSProcess wpsProcess;
	private WorkflowProcess workflowProcess;
	private Text textName;
	private Text textServiceType;
	private Text textServiceAddress;
	public InstanceDetailDialog(Shell parentShell,WPSProcess wpsProcess,WorkflowProcess workflowProcess) {
		super(parentShell);
		this.wpsProcess = wpsProcess;
		this.workflowProcess = workflowProcess;
	}

	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		String title = "Detail info of  ";
		title += this.wpsProcess == null?"instance":this.wpsProcess.getName();
		newShell.setText(title);
		newShell.setSize(300, 300);
	}
	
	@Override
	protected void initializeBounds() {
		super.initializeBounds();
		
		UIUtils.getInstance().centerDialog(this.getShell());
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
         lblUrl.setText("Process Name:");
         
         textName = new Text(container, SWT.BORDER | SWT.READ_ONLY);
         textName.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
         
         Label lblProcessInstance = new Label(container, SWT.NONE);
         lblProcessInstance.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
         lblProcessInstance.setText("Service Type:");
         
         textServiceType = new Text(container, SWT.BORDER | SWT.READ_ONLY);
         textServiceType.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
         
         Label lblTemplateVariable = new Label(container, SWT.NONE);
         lblTemplateVariable.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
         lblTemplateVariable.setText("Service Address:");
         
         textServiceAddress = new Text(container, SWT.BORDER | SWT.READ_ONLY);
         textServiceAddress.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
  
         new Label(container, SWT.NONE);
         
         Label lblNewLabel = new Label(container, SWT.NONE);
         lblNewLabel.setText("Parameter Mapping");
         
         ScrolledComposite scrolledComposite = new ScrolledComposite(container, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
         scrolledComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL,
				false, false, 2, 3));
         scrolledComposite.setExpandHorizontal(true);
         scrolledComposite.setExpandVertical(true);
         
         table = new Table(scrolledComposite, SWT.BORDER | SWT.FULL_SELECTION);
         table.setHeaderVisible(true);
         table.setLinesVisible(true);
         
        String[] tableHeader = { "Instance Var", "Template Var" };
 		for (int i = 0; i < tableHeader.length; i++) {
 			TableColumn tableColumn = new TableColumn(table, SWT.NONE);
 			tableColumn.setText(tableHeader[i]);
 			tableColumn.setWidth(100);
 			tableColumn.setMoveable(false);
 		}

         scrolledComposite.setContent(table);
         scrolledComposite.setMinSize(table.computeSize(SWT.DEFAULT, SWT.DEFAULT));
         
         initComp();
         
		return container;
	}
	
	private void initComp(){
		this.textName.setText(this.wpsProcess.getName());
		this.textServiceType.setText("OGC WPS");
		this.textServiceAddress.setText(this.wpsProcess.getWPSUrl());
		
		Map<IPort, IParameter> paramMap = this.workflowProcess.getProcessExecMap(wpsProcess);
		for(IPort templateVar:paramMap.keySet()){
			IParameter instanceVar = paramMap.get(templateVar);
			TableItem item = new TableItem(this.table, SWT.NONE);
			item.setText(new String[] { instanceVar.getName(), templateVar.getName()});
		}
	}
	
	@Override
	protected boolean isResizable() {
		return true;
	}
	
}
