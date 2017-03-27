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
package com.geojmodelbuilder.ui.models.links;

import com.geojmodelbuilder.core.IDataFlow;
import com.geojmodelbuilder.core.IExchange;
import com.geojmodelbuilder.core.INamespaceDefault;
import com.geojmodelbuilder.ui.models.ProcessInputArtifact;
import com.geojmodelbuilder.ui.models.ProcessOutputArtifact;
import com.geojmodelbuilder.ui.models.Workflow;
import com.geojmodelbuilder.ui.models.WorkflowArtifact;
import com.geojmodelbuilder.ui.models.WorkflowProcess;

/**
 * link between the artifact which may be the output of one process, and the
 * input of another process.
 * In the core module, there is no standalone input port. So, if the source artifact is a standalone artifact,
 * this data flow will not be added to IProcess. 
 * 
 * @author Mingda Zhang
 *
 */
public class DataFlow extends NodeLink implements IDataFlow{
	/** this may be null if artifact is independent. */
	private WorkflowProcess sourceProcess;
	private WorkflowProcess targetProcess;
	private WorkflowArtifact sourceArtifact;
	private ProcessInputArtifact targetArtifact;
	private Workflow parent;
	
	private String id,namespace;
	
	public WorkflowProcess getSourceProcess() {
		return sourceProcess;
	}

	public void setSourceProcess(WorkflowProcess sourceProcess) {
		this.sourceProcess = sourceProcess;
	}

	public WorkflowProcess getTargetProcess() {
		return targetProcess;
	}

	public void setTargetProcess(WorkflowProcess targetProcess) {
		this.targetProcess = targetProcess;
		setTargetNode(targetProcess);
	}

	public WorkflowArtifact getSourceArtifact() {
		return sourceArtifact;
	}

	public Workflow getWorkflow(){
		return this.parent != null? this.parent:getSourceNode().getWorkflow();
	}
	
	public void setSourceArtifact(WorkflowArtifact sourceArtifact) {
		this.sourceArtifact = sourceArtifact;
		setSourceNode(sourceArtifact);
	}

	public ProcessInputArtifact getTargetArtifact() {
		return targetArtifact;
	}

	public void setTargetArtifact(ProcessInputArtifact targetArtifact) {
		this.targetArtifact = targetArtifact;
	}
	
	/**
	 * Determine whether the Dataflow is a 'real' IDataflow.
	 */
	private boolean isValidCoreIDataflow(){
		return this.sourceProcess != null && this.sourceArtifact instanceof ProcessOutputArtifact;
	}
	
	@Override
	public void connect() {
		super.connect();
		parent = getSourceProcess().getWorkflow();
		if (parent!=null) {
			this.parent.addDataFlow(this);
		}
		
		//if this is a valid data flow in core module, it should be added to associated process.
		if(isValidCoreIDataflow()){
			this.sourceProcess.addLink(this);
			this.targetProcess.addLink(this);
		}
	}
	
	@Override
	public void disconnect() {
		super.disconnect();
		
		if(this.parent == null)
			this.parent = getSourceNode().getWorkflow();
		
		if (this.parent!=null) {
			this.parent.removeDataFlow(this);
		}
		
		if(isValidCoreIDataflow()){
			this.sourceProcess.removeLink(this);
			this.targetProcess.removeLink(this);
		}
	}

	@Override
	public IExchange getSourceExchange() {
		/*if(this.sourceArtifact instanceof ProcessInputArtifact)
			return (ProcessInputArtifact)this.sourceArtifact;*/
		
		return this.sourceArtifact;
	}

	@Override
	public IExchange getTargetExchange() {
		return this.targetArtifact;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	@Override
	public String getID() {
		return this.id;
	}

	public void setNamespace(String namespace) {
		this.namespace = namespace;
	}
	
	@Override
	public String getNamespace() {
		if(this.namespace == null){
			this.namespace = INamespaceDefault.INOUTPORT;
		}
		return this.namespace;
	}
}
