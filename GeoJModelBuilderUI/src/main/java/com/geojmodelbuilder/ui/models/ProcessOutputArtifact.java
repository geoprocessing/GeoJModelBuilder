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
package com.geojmodelbuilder.ui.models;

import com.geojmodelbuilder.core.recipe.IOutPutPort;

/**
 * 
 * @author Mingda Zhang
 *
 */
public class ProcessOutputArtifact extends WorkflowArtifact implements IOutPutPort{
	
	private WorkflowProcess parent;
	public ProcessOutputArtifact(){}
	public ProcessOutputArtifact(WorkflowProcess parent) {
		super();
		this.parent = parent;
	}
	
	public WorkflowProcess getProcess(){
		return this.parent;
	}
	
	public void setProcess(WorkflowProcess process){
		this.parent = process;
	}
	
	//hide this constructor
	protected ProcessOutputArtifact(Workflow workflow){
		super(workflow);
	}
	
	@Override
	public void removeSelf() {
		parent.getWorkflow().removeProcessOutput(parent, this);
	}
	
	@Override
	public void reloadSelf() {
		parent.getWorkflow().addProcessOutput(parent,this);
	}
	
	@Override
	public Object clone() throws CloneNotSupportedException {
		ProcessOutputArtifact output = (ProcessOutputArtifact) super.clone();
		output.setProcess(null);
		return output;
	}
	
}
