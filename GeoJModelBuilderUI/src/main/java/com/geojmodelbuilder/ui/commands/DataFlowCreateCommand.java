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
package com.geojmodelbuilder.ui.commands;

import java.util.List;

import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

import com.geojmodelbuilder.ui.dialogs.TargetArtifactSelectorDialog;
import com.geojmodelbuilder.ui.models.ProcessInputArtifact;
import com.geojmodelbuilder.ui.models.ProcessOutputArtifact;
import com.geojmodelbuilder.ui.models.WorkflowArtifact;
import com.geojmodelbuilder.ui.models.WorkflowProcess;
import com.geojmodelbuilder.ui.models.links.DataFlow;
/**
 * 
 * @author Mingda Zhang
 *
 */
public class DataFlowCreateCommand extends LinkCreateCommand {

	@Override
	public boolean canExecute() {
		if (!super.canExecute())
			return false;

		// This command is to create the data flow
		if (!(getLink() instanceof DataFlow)) {
			return false;
		}

		// The source must be an artifact
		if (!(getSourceNode() instanceof WorkflowArtifact)) {
			return false;
		}

		// The target should be a process or an event
		if (!(getTargetNode() instanceof WorkflowProcess))
			return false;

		return true;
	}

	@Override
	public void execute() {
		ProcessInputArtifact targetArtifact = getTargetArtifact();
		if (targetArtifact == null)
			return;

		DataFlow dataFlow = (DataFlow) getLink();
		
		WorkflowArtifact sourceArtifact = (WorkflowArtifact)getSourceNode();
		if (sourceArtifact instanceof ProcessOutputArtifact) {
			dataFlow.setSourceProcess(((ProcessOutputArtifact)sourceArtifact).getProcess());
		}
		
		dataFlow.setSourceArtifact(sourceArtifact);
		dataFlow.setTargetProcess((WorkflowProcess) getTargetNode());
		dataFlow.setTargetArtifact(targetArtifact);
		dataFlow.connect();
	}

	private ProcessInputArtifact getTargetArtifact() {
		WorkflowProcess process = (WorkflowProcess) getTargetNode();
		ProcessInputArtifact inputArtifact = null;
		List<ProcessInputArtifact> inputPorts = process.getInputArtifacts();

		IWorkbenchWindow window = PlatformUI.getWorkbench()
				.getActiveWorkbenchWindow();
		// no available inport, whether to create a input artifact for the
		// process
		if (inputPorts == null || inputPorts.size() == 0) {
			MessageBox dialog = new MessageBox(window.getShell(),
					SWT.ICON_QUESTION | SWT.OK | SWT.CANCEL);
			dialog.setText("No available input ports");
			dialog.setMessage("There is no available input ports. \nWould you want to add a new one to the target process?");

			// open dialog and await user selection
			int returnCode = dialog.open();
			if (returnCode == SWT.CANCEL)
				return null;

			inputArtifact = new ProcessInputArtifact(
					(WorkflowArtifact) getSourceNode());
			process.addInputArtifact(inputArtifact);

		} else {
			TargetArtifactSelectorDialog selectDialog = new TargetArtifactSelectorDialog(
					window.getShell(), inputPorts);
			if (selectDialog.open() == Window.OK)
				inputArtifact = selectDialog.getTargetInputPort();

			if (process.isInputPortBound(inputArtifact)) {
				MessageBox dialog = new MessageBox(window.getShell(),
						SWT.ICON_QUESTION | SWT.YES | SWT.NO);
				dialog.setText(inputArtifact.getName() + " is already used");
				dialog.setMessage("The input port is already bound to an artifact.\nWould you like to add a new one to the process?");

				// open dialog and await user operation
				if (dialog.open() == SWT.NO)
					return null;

				inputArtifact = new ProcessInputArtifact(
						(WorkflowArtifact) getSourceNode());
				process.addInputArtifact(inputArtifact);
			}
		}

		return inputArtifact;
	}
}
