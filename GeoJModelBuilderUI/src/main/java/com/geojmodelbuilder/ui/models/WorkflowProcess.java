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

import org.eclipse.draw2d.ColorConstants;

import com.geojmodelbuilder.core.ILink;
import com.geojmodelbuilder.core.plan.IProcessExec;
import com.geojmodelbuilder.core.recipe.IInputPort;
import com.geojmodelbuilder.core.recipe.IOutPutPort;
import com.geojmodelbuilder.core.recipe.IProcessRecipe;
import com.geojmodelbuilder.ui.models.links.DataFlow;
import com.geojmodelbuilder.ui.models.links.NodeLink;
import com.geojmodelbuilder.ui.models.links.ProcessOutputLink;
/**
 * 
 * @author Mingda Zhang
 *
 */
public class WorkflowProcess extends WorkflowNode implements IProcessRecipe{
	private List<ProcessInputArtifact> inputList;
	private List<ProcessOutputArtifact> outputList;
	private String id;
	private String description;
	private List<ILink> coreLinks;
	private List<IProcessExec> processExecs;
	
	public WorkflowProcess() {
		super();
		setColor(ColorConstants.lightBlue);
		inputList = new ArrayList<ProcessInputArtifact>();
		outputList = new ArrayList<ProcessOutputArtifact>();
		coreLinks = new ArrayList<ILink>();
		processExecs = new ArrayList<IProcessExec>();
	}

	public WorkflowProcess(Workflow parent) {
		this();
		setWorkflow(parent);
		parent.addProcess(this);
	}

	public int getInputCount() {
		return inputList.size();
	}

	public int getOutputCount() {
		return outputList.size();
	}

	public void addInputArtifact(ProcessInputArtifact input) {
		if (!this.inputList.contains(input))
			this.inputList.add(input);
	}

	public void removeInputArtifact(ProcessInputArtifact input) {
		if (this.inputList.contains(input))
			this.inputList.remove(input);
	}

	public List<ProcessInputArtifact> getInputArtifacts() {
		return this.inputList;
	}

	public void setInputArtifacts(List<ProcessInputArtifact> inputs){
		this.inputList = inputs;
	}
	public void addOutputArtifact(ProcessOutputArtifact output) {
		if (!this.outputList.contains(output)){
			this.outputList.add(output);
			// make sure that the output has the parent node
			output.setProcess(this);
		}
	}

	public void removeOutputArtifact(ProcessOutputArtifact output) {
		if (this.outputList.contains(output))
			this.outputList.remove(output);
	}

	public List<ProcessOutputArtifact> getOutputArtifacts() {
		return this.outputList;
	}

	public void setOutputArtifacts(List<ProcessOutputArtifact> outputs){
		this.outputList = outputs;
	}
	
	@Deprecated
	public List<ProcessInputArtifact> getAvailableInPorts() {
		List<ProcessInputArtifact> artifacts = new ArrayList<ProcessInputArtifact>();
		boolean flag = true;
		for (ProcessInputArtifact artifact : this.inputList) {
			flag = true;
			for (NodeLink link : this.getInLinks()) {
				if (!(link instanceof DataFlow))
					continue;

				DataFlow dataFlow = (DataFlow) link;
				if (dataFlow.getTargetArtifact() == artifact) {
					flag = false;
					break;
				}
			}

			if (flag)
				artifacts.add(artifact);
		}
		return artifacts;
	}

	public boolean isInputPortBound(ProcessInputArtifact inputArtifact) {

		for (NodeLink link : this.getInLinks()) {
			if (!(link instanceof DataFlow))
				continue;

			DataFlow dataFlow = (DataFlow) link;
			if (dataFlow.getTargetArtifact() == inputArtifact) {
				return true;
			}
		}

		return false;
	}
	
	public DataFlow getInLinkBoundWithPort(ProcessInputArtifact inputArtifact){
		for (NodeLink link : this.getInLinks()) {
			if (!(link instanceof DataFlow))
				continue;

			DataFlow dataFlow = (DataFlow) link;
			if (dataFlow.getTargetArtifact() == inputArtifact) {
				return dataFlow;
			}
		}
		return null;
	}

	public void removeInputPortBound(ProcessInputArtifact inputArtifact) {
		for (NodeLink link : this.getInLinks()) {
			if (link.getTargetNode() == inputArtifact)
				link.disconnect();
		}
	}

	
	@Override
	public Object clone() throws CloneNotSupportedException {

		WorkflowProcess process = (WorkflowProcess)super.clone();
		process.setInputArtifacts(new ArrayList<ProcessInputArtifact>());
		process.setOutputArtifacts(new ArrayList<ProcessOutputArtifact>());;
		
		for (ProcessInputArtifact input : this.inputList) {
			process.addInputArtifact((ProcessInputArtifact) input.clone());
		}
		for (ProcessOutputArtifact output : this.outputList) {
			ProcessOutputArtifact outputClone = (ProcessOutputArtifact)output.clone();
			process.addOutputArtifact(outputClone);
			ProcessOutputLink link = new ProcessOutputLink(process, outputClone);
			link.connect();
		}
		return process;
	}

	
	
	@Override
	public void addLink(ILink link) {
		if(!this.coreLinks.contains(link))
			this.coreLinks.add(link);
	}

	@Override
	public boolean canExecute() {
		return false;
	}

	@Override
	public boolean execute() {
		return false;
	}

	
	public void setDescription(String description) {
		this.description = description;
	}
	
	@Override
	public String getDescription() {
		return this.description;
	}

	public void setId(String id){
		this.id = id;
	}
	
	@Override
	public String getID() {
		return this.id;
	}

	@Override
	public List<ILink> getLinks() {
		return this.coreLinks;
	}

	
	public void addExectableProcess(IProcessExec processExec){
		if(!this.processExecs.contains(processExec))
			this.processExecs.add(processExec);
	}
	
	@Override
	public List<IProcessExec> getExecCandidates() {
		return this.processExecs;
	}

	@Override
	public List<? extends IInputPort> getInputs() {
		return this.inputList;
	}

	@Override
	public List<? extends IOutPutPort> getOutputs() {
		return this.outputList;
	}
	
	@Override
	public void removeLink(ILink link) {
		if(this.coreLinks.contains(link))
			this.coreLinks.remove(link);
	}
}