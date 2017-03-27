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
import com.geojmodelbuilder.ui.models.WorkflowCondition;
import com.geojmodelbuilder.ui.models.WorkflowProcess;
/**
 * 
 * @author Mingda Zhang
 *
 */
public class ControlFlow extends NodeLink implements IBranchControl{

	@Override
	public void connect() {
		super.connect();
		getTargetProcess().addLink(this);
	}
	
	@Override
	public void disconnect() {
		super.disconnect();
		getTargetProcess().removeLink(this);
	}
	
	public WorkflowCondition getCondition() {
		return (WorkflowCondition)getSourceNode();
	}

	public WorkflowProcess getTargetProcess() {
		return (WorkflowProcess)getTargetNode();
	}
	
	public WorkflowCondition getSourceProcess(){
		return (WorkflowCondition)getSourceNode();
	}

	@Override
	public boolean isTrue() {
		return false;
	}

	@Override
	public String getID() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getNamespace() {
		// TODO Auto-generated method stub
		return null;
	}

}
