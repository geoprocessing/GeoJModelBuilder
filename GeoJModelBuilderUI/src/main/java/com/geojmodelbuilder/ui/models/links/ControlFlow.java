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

import com.geojmodelbuilder.core.IBranchControl;
import com.geojmodelbuilder.ui.models.WorkflowArtifact;
import com.geojmodelbuilder.ui.models.WorkflowCondition;
import com.geojmodelbuilder.ui.models.WorkflowProcess;
/**
 * 
 * @author Mingda Zhang
 *
 */
public class ControlFlow extends DataFlow implements IBranchControl{
	
	public WorkflowCondition getCondition() {
		return (WorkflowCondition)getSourceNode();
	}

	public WorkflowCondition getSourceProcess(){
		return (WorkflowCondition)getSourceNode();
	}

	@Override
	public void setSourceProcess(WorkflowProcess sourceProcess) {
		this.sourceProcess = sourceProcess;
		setSourceNode(sourceProcess);
	}
	
	@Override
	public void setSourceArtifact(WorkflowArtifact sourceArtifact) {
		this.sourceArtifact = sourceArtifact;
	}
	
	@Override
	public boolean isTrue() {
		return false;
	}

	@Override
	public String getID() {
		return null;
	}

	@Override
	public String getNamespace() {
		return null;
	}

}
