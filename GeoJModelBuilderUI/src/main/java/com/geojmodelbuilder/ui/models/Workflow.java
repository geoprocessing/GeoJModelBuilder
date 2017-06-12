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

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.List;

import com.geojmodelbuilder.core.INamespaceDefault;
import com.geojmodelbuilder.core.IProcess;
import com.geojmodelbuilder.core.instance.IProcessInstance;
import com.geojmodelbuilder.core.instance.IWorkflowInstance;
import com.geojmodelbuilder.core.provenance.IWorkflowProv;
import com.geojmodelbuilder.core.template.IProcessTemplate;
import com.geojmodelbuilder.core.template.IWorkflowTemplate;
import com.geojmodelbuilder.core.utils.ValidateUtil;
import com.geojmodelbuilder.ui.models.links.DataFlow;
/**
 * 
 * @author Mingda Zhang
 *
 */
public class Workflow extends AbstractWorkflowElement implements IWorkflowElement,IWorkflowTemplate {
	private String name;
	/**
	 * independent artifacts
	 */
	private List<StandaloneArtifact> artifactList;

	/**
	 * process include output artifacts
	 */
	private List<WorkflowProcess> processList;
	private List<WorkflowCondition> conditionList;
	private PropertyChangeSupport listeners;
	public static final String CHILD_REMOVE = "child_remove";
	public static final String CHILD_ADD = "child_add";
	private List<DataFlow> dataFlows;

	private String description;
	
	private List<IWorkflowInstance> workflowInstances;
	private List<IWorkflowProv> workflowProvs;
	
	public Workflow() {
		artifactList = new ArrayList<StandaloneArtifact>();
		processList = new ArrayList<WorkflowProcess>();
		conditionList = new ArrayList<WorkflowCondition>();
		listeners = new PropertyChangeSupport(this);
		dataFlows = new ArrayList<DataFlow>();
		workflowProvs = new ArrayList<IWorkflowProv>();
		workflowInstances = new ArrayList<IWorkflowInstance>();
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return this.name;
	}

	public void addArtifact(StandaloneArtifact data) {
		this.artifactList.add(data);
		this.listeners.firePropertyChange(CHILD_ADD, null, data);
	}

	public List<StandaloneArtifact> getArtifacts() {
		return this.artifactList;
	}

	public void removeArtifact(StandaloneArtifact artifact) {
		if (this.artifactList.contains(artifact)) {
			this.artifactList.remove(artifact);
			this.listeners.firePropertyChange(CHILD_REMOVE, artifact, null);
		}
	}

	public void addProcess(WorkflowProcess process) {
		if (!this.processList.contains(process)) {
			this.processList.add(process);
			this.listeners.firePropertyChange(CHILD_ADD, null, process);
		}
	}

	public List<WorkflowProcess> getAllProcess() {
		return this.processList;
	}

	public void removeProcess(WorkflowProcess process) {
		if (this.processList.contains(process)) {
			this.processList.remove(process);
			this.listeners.firePropertyChange(CHILD_REMOVE, process, null);
		}
	}

	public void removeProcessOutput(WorkflowProcess process, ProcessOutputArtifact output){
		//If there is only one output in the process, then the entire process will be removed.
		process.removeOutputArtifact(output);
		this.listeners.firePropertyChange(CHILD_REMOVE, process, null);
	}
	
	public void addCondtion(WorkflowCondition condition) {
		this.conditionList.add(condition);
		this.listeners.firePropertyChange(CHILD_ADD, null, condition);
	}

	public List<WorkflowCondition> getConditions() {
		return this.conditionList;
	}

	public void removeCondition(WorkflowCondition condition) {
		if (this.conditionList.contains(condition)) {
			this.conditionList.remove(condition);
			this.listeners.firePropertyChange(CHILD_REMOVE, condition, null);
		}
	}

	public void addDataFlow(DataFlow link) {
		this.dataFlows.add(link);
	}

	public void removeDataFlow(DataFlow link) {
		this.dataFlows.remove(link);
	}

	public List<DataFlow> getDataFlows() {
		return this.dataFlows;
	}

	
	public void addNode(WorkflowNode node) {
		Class targetClass = node.getClass();
		if (targetClass == WorkflowProcess.class) {
			addProcess((WorkflowProcess) node);
		}else if (targetClass == WorkflowCondition.class) {
			addCondtion((WorkflowCondition) node);
		}else if (targetClass == StandaloneArtifact.class) {
			addArtifact((StandaloneArtifact) node);
		}
	}

	public void removeNode(WorkflowNode node) {
		Class targetClass = node.getClass();
		if (targetClass == WorkflowProcess.class) {
			removeProcess((WorkflowProcess) node);
		}else if (targetClass == WorkflowCondition.class) {
			removeCondition((WorkflowCondition) node);
		}else if (targetClass == StandaloneArtifact.class) {
			removeArtifact((StandaloneArtifact) node);
		}
		
	}

	public int getProcessCount() {
		return this.processList.size();
	}

	public void addListener(PropertyChangeListener listener) {
		this.listeners.addPropertyChangeListener(listener);
	}

	public void removeListener(PropertyChangeListener listener) {
		this.listeners.removePropertyChangeListener(listener);
	}

	public boolean isContainDataFlow(WorkflowNode sourceNode,
			WorkflowNode targetNode) {
		for (DataFlow link : this.dataFlows) {
			if (link.getSourceNode() == sourceNode
					&& link.getTargetNode() == targetNode)
				return true;
		}
		return false;
	}

	public void addProcessOutput(WorkflowProcess parent,
			ProcessOutputArtifact processOutputArtifact) {
		parent.addOutputArtifact(processOutputArtifact);
		this.listeners.firePropertyChange(CHILD_ADD, null, processOutputArtifact);
	}

	public WorkflowProcess getProcessByBinding(IProcess processExec){
		for(WorkflowProcess process:this.getAllProcess()){
			List<? extends IProcessInstance> candidates = process.getInstances();
			if(candidates == null)
				continue;
			
			if(candidates.contains(processExec))
				return process;
		}
		return null;
	}
	@Override
	public List<IProcessTemplate> getProcesses() {
		List<IProcessTemplate> processes = new ArrayList<IProcessTemplate>();
		processes.addAll(getAllProcess());
		processes.addAll(getConditions());
		return processes;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	@Override
	public String getDescription() {
		return this.description;
	}
	
	@Override
	public String getNamespace() {
		if(ValidateUtil.isStrEmpty(this.namespace))
			this.namespace = INamespaceDefault.TEMPLATE_WORKFLOW;
		
		return this.namespace;
	}

	public void addInstance(IWorkflowInstance instance){
		if(!this.workflowInstances.contains(instance))
			this.workflowInstances.add(instance);
	}
	
	@Override
	public List<IWorkflowInstance> getInstances() {
		return this.workflowInstances;
	}
	
	public void addWorkflowProv(IWorkflowProv workflowProv){
		if(!this.workflowProvs.contains(workflowProv))
			this.workflowProvs.add(workflowProv);
	}
	
	public List<IWorkflowProv> getWorkflowProvs(){
		return this.workflowProvs;
	}
}
