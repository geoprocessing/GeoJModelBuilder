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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.geojmodelbuilder.core.instance.IOutputParameter;
import com.geojmodelbuilder.core.instance.IParameter;
import com.geojmodelbuilder.core.instance.IProcessInstance;
import com.geojmodelbuilder.core.template.IOutPutPort;
import com.geojmodelbuilder.core.template.IPort;


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
		setOwner(parent);
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
	
	@Override
	public List<IOutputParameter> getInstances() {

		List<? extends IParameter> instances =  super.getInstances();
		if(instances == null || instances.size() == 0)
			return null;
		
		List<IOutputParameter> outputParameters = new ArrayList<IOutputParameter>();
		for(IParameter parameter:instances){
			if(parameter instanceof IOutputParameter)
				outputParameters.add((IOutputParameter)parameter);
		}
		
		return outputParameters;
	}
	
	public String getExecutedResult(){
		String result = null;
		if (this.parent == null) 
			return result;

		List<IProcessInstance> instances = this.parent.getInstances();
		if(instances == null || instances.size() == 0)
			return result;
		
		Map<IPort, IParameter> portParamMap = this.parent.getProcessExecMap(instances.get(0));
		if(portParamMap == null)
			return result;
		
		IParameter iParameter = portParamMap.get(this);
		if(iParameter == null || iParameter.getData() == null)
			return result;
		
		Object obj = iParameter.getData().getValue();
		if(obj == null)
			return result;
		
		return obj.toString();
	}
}
