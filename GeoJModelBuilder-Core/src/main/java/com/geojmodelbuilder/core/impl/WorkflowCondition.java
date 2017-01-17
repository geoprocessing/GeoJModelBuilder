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

import java.util.ArrayList;
import java.util.List;

import com.geojmodelbuilder.core.IBranchControl;
import com.geojmodelbuilder.core.ICondition;
import com.geojmodelbuilder.core.IExchange;
import com.geojmodelbuilder.core.ILink;
/**
 * @author Mingda Zhang
 *
 */
public class WorkflowCondition implements ICondition {

	private ControlFlowFalse falseFlow;
	private ControlFlowTrue trueFlow;
	private List<IExchange> inputExchanges;
	private String id;
	private String name;
	private String description;
	private boolean satisfied;
	private List<ILink> links;
	public WorkflowCondition() {
		inputExchanges = new ArrayList<IExchange>();
		links = new ArrayList<ILink>();
	}

	public List<IExchange> getInputs() {
		return this.inputExchanges;
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

	public void setID(String id) {
		this.id = id;
	}

	public String getID() {
		return this.id;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getDescription() {
		return description;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return this.name;
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
	
	public void addLink(ILink link){
		this.links.add(link);
	}
	
	public void removeLink(ILink link){
		if(this.links.contains(link))
			this.links.remove(link);
	}
	
	public List<ILink> getLinks() {
		return this.links;
	}
}
