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

import org.eclipse.draw2d.geometry.Rectangle;

import com.geojmodelbuilder.ui.models.ProcessOutputArtifact;
import com.geojmodelbuilder.ui.models.WorkflowNode;
import com.geojmodelbuilder.ui.models.WorkflowProcess;
import com.geojmodelbuilder.ui.models.links.NodeLink;
import com.geojmodelbuilder.ui.models.links.ProcessOutputLink;
/**
 * 
 * @author Mingda Zhang
 *
 */
public class ProcessCreateCommand extends NodeCreateCommand {

	@Override
	public void execute() {
		super.execute();
		
		//create a process with one default output.
		WorkflowProcess process = (WorkflowProcess)getWorkflowNode();
		Rectangle layout = process.getLayout();
		
		//set the layout of the output
		for(int i=0;i<process.getOutputCount();i++){
			ProcessOutputArtifact output = process.getOutputArtifacts().get(i);
			output.setLayout(new Rectangle(layout.x + WorkflowNode.WIDTH_DEFAULT*3/2,layout.y + i*WorkflowNode.HEIGHT_DEFAULT,WorkflowNode.WIDTH_DEFAULT,WorkflowNode.HEIGHT_DEFAULT));
			NodeLink nodeLink = new ProcessOutputLink(process,output);
			nodeLink.connect();
		}
		
		/*ProcessOutputArtifact output = new ProcessOutputArtifact(process);
		output.setName("output");
		output.setLayout(new Rectangle(layout.x + WorkflowNode.WIDTH_DEFAULT*3/2,layout.y,WorkflowNode.WIDTH_DEFAULT,WorkflowNode.HEIGHT_DEFAULT));
		process.addOutputArtifact(output);;
		
		NodeLink nodeLink = new ProcessOutputLink(process,output);
		nodeLink.connect();*/
		getWorkflow().addProcess(process);
	}

	@Override
	public void undo() {
		super.undo();
		getWorkflow().removeProcess((WorkflowProcess) getWorkflowNode());
	}
}
