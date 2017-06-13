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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.draw2d.ColorConstants;

import com.geojmodelbuilder.core.IDataFlow;
import com.geojmodelbuilder.core.ILink;
import com.geojmodelbuilder.core.INamespaceDefault;
import com.geojmodelbuilder.core.instance.IParameter;
import com.geojmodelbuilder.core.instance.IProcessInstance;
import com.geojmodelbuilder.core.template.IPort;
import com.geojmodelbuilder.core.template.IProcessTemplate;
import com.geojmodelbuilder.core.utils.ValidateUtil;
import com.geojmodelbuilder.ui.models.links.DataFlow;
import com.geojmodelbuilder.ui.models.links.NodeLink;
import com.geojmodelbuilder.ui.models.links.ProcessOutputLink;
/**
 * 
 * @author Mingda Zhang
 *
 */
public class WorkflowProcess extends WorkflowNode implements IProcessTemplate{
	private List<ProcessInputArtifact> inputList;
	private List<ProcessOutputArtifact> outputList;
	private String description;
	private List<ILink> coreLinks;
	private List<IProcessInstance> processExecs;
	
	private IProcessInstance activeInstance;
	
	/**
	 * Mapping the value of the port to the concrete parameter.
	 */
	private Map<IProcessInstance, Map<IPort, IParameter>> processMap;
	
	public WorkflowProcess() {
		super();
		setColor(ColorConstants.lightBlue);
		inputList = new ArrayList<ProcessInputArtifact>();
		outputList = new ArrayList<ProcessOutputArtifact>();
		coreLinks = new ArrayList<ILink>();
		processExecs = new ArrayList<IProcessInstance>();
		processMap = new HashMap<IProcessInstance, Map<IPort,IParameter>>();
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
		if(!this.coreLinks.contains(link)){
			this.coreLinks.add(link);
		}
		
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

	@Override
	public List<ILink> getLinks() {
		return this.coreLinks;
	}

	
	public void addExectableProcess(IProcessInstance processExec){
		if(!this.processExecs.contains(processExec))
			this.processExecs.add(processExec);
	}
	
	public Map<IPort, IParameter> getProcessExecMap(IProcessInstance process){
		return this.processMap.get(process);
	}
	
	public List<IParameter> getPortInstances(IPort port){
		List<IParameter> parameters = new ArrayList<IParameter>();
		for(Map<IPort, IParameter> portParamMap : processMap.values()){
			IParameter parameter = portParamMap.get(port);
			if (parameter != null) {
				parameters.add(parameter);
			}
		}
		
		return parameters;
	}
	
	public void addProcessMap(IProcessInstance processExec,Map<IPort, IParameter> port2Parameter){
		this.processMap.put(processExec, port2Parameter);
	}
	
	@Override
	public List<ProcessInputArtifact> getInputs() {
		return this.inputList;
	}

	@Override
	public List<ProcessOutputArtifact> getOutputs() {
		return this.outputList;
	}
	
	@Override
	public void removeLink(ILink link) {
		if(this.coreLinks.contains(link))
			this.coreLinks.remove(link);
	}

	@Override
	public List<IProcessInstance> getInstances() {
		return this.processExecs;
	}

	public IProcessInstance getInstance(String name){
		for(IProcessInstance instance:this.processExecs){
			if(instance.getName().equals(name))
				return instance;
		}
		
		return null;
	}
	
	@Override
	public ProcessInputArtifact getInput(String name) {
		for(ProcessInputArtifact artifact:this.inputList){
			if(artifact.getName().equals(name))
				return artifact;
		}
		
		return null;
	}

	@Override
	public ProcessOutputArtifact getOutput(String name) {
		for(ProcessOutputArtifact artifact:this.outputList){
			if(artifact.getName().equals(name))
				return artifact;
		}
		
		return null;
	}
	
	public IProcessInstance getActiveInstance(){
		if(this.activeInstance == null){
			return this.processExecs.size()==0 ? null:this.processExecs.get(0);
		}
		
		return this.activeInstance;
	}
	
	public boolean setActiveInstance(IProcessInstance activeInstance) {
		if(this.processExecs.contains(activeInstance)){
			this.activeInstance = activeInstance;
			return true;
		}
		
		return false;
	}
	
	public boolean setActiveInstance(String name){
		for(IProcessInstance instance:this.processExecs){
			if(instance.getName().equals(name)){
				this.activeInstance = instance;
				return true;
			}
		}
		
		return false;
	}
	public WorkflowArtifact getArtifact(String name){
		ProcessInputArtifact input = getInput(name);
		if(input !=null)
			return input;
		
		return getOutput(name);
	}
	
	@Override
	public String getNamespace() {
		if(ValidateUtil.isStrEmpty(this.namespace))
			this.namespace = INamespaceDefault.TEMPLATE_PROCESS;
		
		return this.namespace;
	}
}
