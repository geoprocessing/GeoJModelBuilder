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

import com.geojmodelbuilder.core.IProcess;
import com.geojmodelbuilder.core.IWorkflow;
import com.geojmodelbuilder.ui.models.links.DataFlow;
/**
 * 
 * @author Mingda Zhang
 *
 */
public class Workflow implements IWorkflowElement,IWorkflow {
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

	public Workflow() {
		artifactList = new ArrayList<StandaloneArtifact>();
		processList = new ArrayList<WorkflowProcess>();
		conditionList = new ArrayList<WorkflowCondition>();
		listeners = new PropertyChangeSupport(this);
		dataFlows = new ArrayList<DataFlow>();
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

	public List<WorkflowProcess> getProcessRecipe() {
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

	@Override
	public List<IProcess> getProcesses() {
		List<IProcess> processes = new ArrayList<IProcess>();
		processes.addAll(getProcessRecipe());
		processes.addAll(getConditions());
		return processes;
	}
}
