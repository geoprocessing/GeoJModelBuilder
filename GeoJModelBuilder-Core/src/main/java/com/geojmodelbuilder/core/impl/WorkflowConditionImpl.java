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

import java.util.List;

import com.geojmodelbuilder.core.IBranchControl;
import com.geojmodelbuilder.core.ICondition;
import com.geojmodelbuilder.core.IExchange;
/**
 * @author Mingda Zhang
 *
 */
public class WorkflowConditionImpl extends AbstractProcessImpl<IExchange, IExchange> implements ICondition {

	private ControlFlowFalse falseFlow;
	private ControlFlowTrue trueFlow;
	private boolean satisfied;
	public WorkflowConditionImpl() {
		super();
	}

	public boolean execute() {
		return false;
	}

	/**
	 * Do nothing. There is only one boolean output for the condition: true or
	 * false.
	 */
	public List<IExchange> getOutputs() {
		return null;
	}

	public String getExpression() {
		return null;
	}

	public void setTrueFlow(ControlFlowTrue trueFlow) {
		this.trueFlow = trueFlow;
	}

	public IBranchControl getTrueFlow() {
		return this.trueFlow;
	}

	public void setFalseFlow(ControlFlowFalse falseFlow) {
		this.falseFlow = falseFlow;
	}

	public IBranchControl getFalseFlow() {
		return this.falseFlow;
	}

	public boolean canExecute() {
		return true;
	}

	public void setSatisfied(boolean satisfied) {
		this.satisfied = satisfied;
	}

	public boolean isSatisfied() {
		return this.satisfied;
	}
	
}
