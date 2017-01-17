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
package com.geojmodelbuilder.core.impl;

import com.geojmodelbuilder.core.IBranchControl;
import com.geojmodelbuilder.core.ICondition;
import com.geojmodelbuilder.core.IProcess;
/**
 * @author Mingda Zhang
 *
 */
public abstract class BranchingControl implements IBranchControl {

	private ICondition condition;
	private IProcess process;
	
	public void setCondition(ICondition condition){
		this.condition = condition;
	}
	public ICondition getCondition() {
		return this.condition;
	}

	public void setTarget(IProcess process){
		this.process = process;
	}

	public ICondition getSourceProcess() {
		return this.condition;
	}
	
	public IProcess getTargetProcess() {
		return this.process;
	}
	

}
