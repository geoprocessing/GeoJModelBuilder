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

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

import com.geojmodelbuilder.core.IProcess;
import com.geojmodelbuilder.core.instance.IWorkflowInstance;
import com.geojmodelbuilder.core.provenance.IProcessProv;
import com.geojmodelbuilder.core.provenance.IWorkflowProv;
import com.geojmodelbuilder.engine.IListener;
import com.geojmodelbuilder.engine.IProcessEvent;
import com.geojmodelbuilder.engine.IProcessEvent.EventType;
import com.geojmodelbuilder.engine.IRecorder;
import com.geojmodelbuilder.engine.impl.WorkflowEngine;
import com.geojmodelbuilder.ui.models.Workflow;
import com.geojmodelbuilder.ui.models.WorkflowProcess;
import com.geojmodelbuilder.ui.run.UIModles2Instance;

/**
 * @author Mingda Zhang
 *
 */
public class WorkflowExecutionMonitorDialog extends Dialog implements IListener, IRecorder{
	private Workflow workflow;
	private Text text;
	private Button okButton;
	private IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
	private WorkflowEngine workflowEngine = null;
	
	public WorkflowExecutionMonitorDialog(Shell parentShell, Workflow workflow){
		super(parentShell);
		setShellStyle(SWT.CLOSE);
		this.workflow = workflow;
//		setBlockOnOpen(false);
//		setShellStyle(SWT.CLOSE | SWT.TITLE |SWT.MODELESS |SWT.ON_TOP);
	}

	
	@Override
	protected Control createDialogArea(Composite parent) {
		Composite container = (Composite) super.createDialogArea(parent);
		container.setLayout(new FillLayout(SWT.HORIZONTAL));
		
		ScrolledComposite scrolledComposite = new ScrolledComposite(container, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		scrolledComposite.setExpandHorizontal(true);
		scrolledComposite.setExpandVertical(true);
		
		text = new Text(scrolledComposite, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL | SWT.CANCEL | SWT.MULTI);
		scrolledComposite.setContent(text);
		scrolledComposite.setMinSize(text.computeSize(SWT.DEFAULT, SWT.DEFAULT));

		return container;
	}
	
	
	@Override
	protected void initializeBounds() {
		super.initializeBounds();
		okButton = getButton(IDialogConstants.OK_ID);
		if(okButton!=null)
			okButton.setText("Run");
	}
	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText("Workflow Execution Monitor");
	}
	
	
	@Override
	protected Point getInitialSize() {
		return new Point(400, 300);
	}
	
	@Override
	public void appendMsg(String msg) {
		Display.getDefault().syncExec(new Runnable() {
		    public void run() {
		    	text.append(msg+"\n");
		    }
		});
		
	}

	@Override
	public String getRecord() {
		return this.text.getText();
	}

	@Override
	protected void okPressed() {
		if(this.workflow == null){
			this.appendMsg("There is no target workflow.");
			return;
		}

		setOKButtonEnabled(false);
		
		UIModles2Instance recipe2Plan = new UIModles2Instance(this.workflow);
		if(!recipe2Plan.transfer()){
			this.appendMsg("Failed to generate the executable workflow");
			this.appendMsg(recipe2Plan.getErrInfo());
			setOKButtonEnabled(true);
			return;
		}
		
		IWorkflowInstance workflow = recipe2Plan.getExecutableWorkflow();
		workflowEngine = new WorkflowEngine(workflow, this);
		workflowEngine.subscribe(this, EventType.StepPerformed);
		workflowEngine.subscribe(this, EventType.Stopped);
		workflowEngine.execute();
	}
	
	private void setOKButtonEnabled(boolean enabled){
		Display.getDefault().syncExec(new Runnable() {
		    public void run() {
		    	okButton.setEnabled(enabled);
		    }
		});
	}
	@Override
	public synchronized void onEvent(IProcessEvent event) {
		EventType eventType = event.getType();
		IProcess source = event.getSource();
		if(eventType == EventType.Stopped){
			this.appendMsg("engine stopped.");
			IWorkflowProv workflowProv = workflowEngine.getWorkflowTrace();
			if(workflowProv != null)
				workflow.addWorkflowProv(workflowProv);
			
			setOKButtonEnabled(true);
		}else if (eventType == EventType.StepPerformed) {
			this.appendMsg(source.getName() + "executed.");
			if(source instanceof IProcessProv){
				IProcessProv processProv = (IProcessProv)source;
				IProcess processExec = processProv.getProcess();
				WorkflowProcess workflowProcess = this.workflow.getProcessByBinding(processExec);
				
				Display.getDefault().syncExec(new Runnable() {
				    public void run() {
				    	if(processProv.getStatus()){
							workflowProcess.setColor(ColorConstants.lightGreen);
						}else {
							workflowProcess.setColor(ColorConstants.red);
						}
				    }
				});
			}
		}
	}
	
	protected boolean isResizable() {
		return true;
	}
	
	public static void main(String[] args){
		Workflow workflow = new Workflow();
		WorkflowExecutionMonitorDialog dialog = new WorkflowExecutionMonitorDialog(null, workflow);
		dialog.open();
	}

}
