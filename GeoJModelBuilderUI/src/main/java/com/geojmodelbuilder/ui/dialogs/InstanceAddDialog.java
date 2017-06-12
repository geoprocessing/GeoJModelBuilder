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

import net.opengis.wps.x100.ProcessDescriptionType;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Monitor;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

import com.geojmodelbuilder.core.instance.IInputParameter;
import com.geojmodelbuilder.core.instance.IOutputParameter;
import com.geojmodelbuilder.core.instance.IParameter;
import com.geojmodelbuilder.core.resource.ogc.wps.WPSProcess;
import com.geojmodelbuilder.core.template.IPort;
import com.geojmodelbuilder.ui.models.ProcessInputArtifact;
import com.geojmodelbuilder.ui.models.ProcessOutputArtifact;
import com.geojmodelbuilder.ui.models.WorkflowProcess;
import com.geojmodelbuilder.ui.views.wps.GeoprocessingNode;
import com.geojmodelbuilder.ui.views.wps.ProcessNode;
import com.geojmodelbuilder.ui.views.wps.WPSNode;
import com.geojmodelbuilder.ui.workspace.WPSCacheFactory;
import com.geojmodelbuilder.ui.workspace.WorkspaceReader;

/**
 * @author Mingda Zhang
 *
 */
public class InstanceAddDialog extends Dialog implements SelectionListener{
	private Table table;
	private WorkflowProcess process;
	private GeoprocessingNode wpsResources;
	private Combo comboProcessSet,comboProcInstance,comboInstVar,comboTempVar;
	private Button btnBind, btnDelete;
	private WPSProcess wpsProcess;
	private Map<IPort, IParameter> parameterMap;
	public InstanceAddDialog(Shell parentShell,WorkflowProcess process) {
		super(parentShell);
		this.process = process;
	}

	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		String title = "Bind instance to ";
		title += this.process == null?"template":this.process.getName();
		newShell.setText(title);
		newShell.setSize(400, 350);
	}
	
	@Override
	protected void initializeBounds() {
		super.initializeBounds();
		
		Shell shell = this.getShell(); 
		Monitor primary = shell.getMonitor(); 
		Rectangle bounds = primary.getBounds (); 
		Rectangle rect = shell.getBounds (); 
		int x = bounds.x + (bounds.width - rect.width) / 2; 
		int y = bounds.y + (bounds.height - rect.height) / 2; 
		shell.setLocation (x, y); 
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
         lblUrl.setText("Process Set:");
         
         comboProcessSet = new Combo(container, SWT.READ_ONLY);
         comboProcessSet.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
         comboProcessSet.addSelectionListener(this);
         
         Label lblProcessInstance = new Label(container, SWT.NONE);
         lblProcessInstance.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
         lblProcessInstance.setText("Process Instance:");
         
         comboProcInstance = new Combo(container, SWT.READ_ONLY);
         comboProcInstance.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
         comboProcInstance.addSelectionListener(this);
         
         Label lblTemplateVariable = new Label(container, SWT.NONE);
         lblTemplateVariable.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
         lblTemplateVariable.setText("Instance Variable:");
         
         comboInstVar = new Combo(container, SWT.READ_ONLY);
         comboInstVar.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
         
         Label lblInstanceVariable = new Label(container, SWT.NONE);
         lblInstanceVariable.setText("Template Variable:");
         lblInstanceVariable.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
         
         comboTempVar = new Combo(container, SWT.READ_ONLY);
         comboTempVar.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
         new Label(container, SWT.NONE);
         
         ScrolledComposite scrolledComposite = new ScrolledComposite(container, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
         scrolledComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL,
				false, false, 1, 4));
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
         
         btnBind = new Button(container, SWT.NONE);
         btnBind.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
         btnBind.setText("Bind");
         btnBind.addSelectionListener(this);
         
         btnDelete = new Button(container, SWT.NONE);
         btnDelete.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
         btnDelete.setText("Delete");
         btnDelete.addSelectionListener(this);
         new Label(container, SWT.NONE);
         
         initComp();
         
		return container;
	}
	
	private void initComp(){
		wpsResources = WorkspaceReader.getInstance().getWPSResouces();
		if(wpsResources == null || wpsResources.getWPSs().size()==0){
			MessageDialog.openWarning(this.getShell(), "Warning",
					"There is no instance available.");
			return;
		}
		
		if(this.process == null){
			MessageDialog.openWarning(this.getShell(), "Warning",
					"The template is null.");
			return;
		}
		
		for(WPSNode wpsNode:wpsResources.getWPSs()){
			this.comboProcessSet.add(wpsNode.getName());
		}
		
		/*if(this.comboProcessSet.getItemCount() > 0){
			this.comboProcessSet.select(0);
		}*/
		
		for(ProcessInputArtifact input: this.process.getInputArtifacts()){
			this.comboTempVar.add(input.getName());
		}
		
		for(ProcessOutputArtifact output:this.process.getOutputArtifacts()){
			this.comboTempVar.add(output.getName());
		}
		
		if(this.comboTempVar.getItemCount() > 0)
			this.comboTempVar.select(0);
	}
	
	private boolean saveInput(){
		if(wpsProcess == null)
			return false;
		
		parameterMap = new HashMap<IPort, IParameter>();
		
		for (int i = 0; i < this.table.getItemCount(); i++) {
			TableItem inputItem = this.table.getItem(i);
			
			String insVar = inputItem.getText(0);
			String temVar = inputItem.getText(1);
			
			IPort temPort = this.process.getArtifact(temVar);
			IParameter instParameter = this.wpsProcess.getInput(insVar);
			instParameter = instParameter == null?this.wpsProcess.getOutput(insVar):instParameter;
			
			if(temPort == null || instParameter == null)
				continue;
			
			parameterMap.put(temPort, instParameter);
		}
		
		return true;

	}
	
	public WPSProcess getInstance(){
		return this.wpsProcess;
	}
	
	public Map<IPort, IParameter> getParameterMap(){
		return this.parameterMap;
	}
	
	@Override
	protected boolean isResizable() {
		return true;
	}
	
	@Override
	protected void okPressed() {
		saveInput();
		super.okPressed();
	}
	
	public static void main(String[] args){
		InstanceAddDialog dialog = new InstanceAddDialog(null,new WorkflowProcess());
		dialog.open();
	}

	@Override
	public void widgetSelected(SelectionEvent e) {
		Object eventObj = e.getSource();
		if(eventObj == this.comboProcessSet){
			String processSet = this.comboProcessSet.getText();
			if(processSet.equals(""))
				return;
			
			WPSNode wps = this.wpsResources.getWPSByName(processSet);
			this.comboProcInstance.removeAll();
			for(ProcessNode processNode:wps.getProcesses()){
				this.comboProcInstance.add(processNode.getName());
			}
			
			/*if(this.comboProcInstance.getItemCount() > 0)
				this.comboProcInstance.select(0);*/
			
		}else if (eventObj == this.comboProcInstance) {
			String processSet = this.comboProcessSet.getText();
			WPSNode wps = this.wpsResources.getWPSByName(processSet);
			String processInst = this.comboProcInstance.getText();
			if(wps == null)
				return;
			
			ProcessNode processNode = wps.getProcessByName(processInst);
			if(processNode == null)
				return;
			
			wpsProcess = new WPSProcess(processInst);
			wpsProcess.setWPSUrl(wps.getUrl());
			
			//using cache if exists.
			ProcessDescriptionType descriptionType = WPSCacheFactory.getInstance().getProcessDescriptionType(wps.getUrl(), processNode.getName());
			if(descriptionType!=null)
				wpsProcess.setProcessDescriptionType(descriptionType);
			
			this.comboInstVar.removeAll();
			for(IInputParameter input:wpsProcess.getInputs()){
				this.comboInstVar.add(input.getName());
			}
			
			for(IOutputParameter output:wpsProcess.getOutputs()){
				this.comboInstVar.add(output.getName());
			}
			
			if(this.comboInstVar.getItemCount() > 0)
				this.comboInstVar.select(0);
		}else if (eventObj == btnBind) {
			String templateVar = this.comboTempVar.getText();
			String instanceVar = this.comboInstVar.getText();
			
			TableItem item = new TableItem(this.table, SWT.NONE);
			item.setText(new String[] { instanceVar, templateVar});
			
		}else if (eventObj == btnDelete) {
			int index = this.table.getSelectionIndex();
			if(index != -1)
				this.table.remove(index);
		}
	}

	@Override
	public void widgetDefaultSelected(SelectionEvent e) {}
}
