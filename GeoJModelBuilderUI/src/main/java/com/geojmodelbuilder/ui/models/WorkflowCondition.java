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

import org.eclipse.draw2d.ColorConstants;

import com.geojmodelbuilder.core.ICondition;
import com.geojmodelbuilder.ui.models.links.ControlFlow;
import com.geojmodelbuilder.ui.models.links.FalseThenFlow;
import com.geojmodelbuilder.ui.models.links.TrueThenFlow;
/**
 * 
 * @author Mingda Zhang
 *
 */
public class WorkflowCondition extends WorkflowProcess implements ICondition {

	private TrueThenFlow trueThenFlow;
	private FalseThenFlow falseThenFlow;
	
	/*conditional expression*/
	private String expression;
	
	public WorkflowCondition() {
		super();
		setColor(ColorConstants.lightGray);
	}

	public WorkflowCondition(Workflow parent) {
		this();
		setWorkflow(parent);
	}

	public TrueThenFlow getTrueFlow() {
		return trueThenFlow;
	}

	public void setTrueThenFlow(TrueThenFlow flow) {
		this.trueThenFlow = flow;
	}

	public FalseThenFlow getFalseFlow() {
		return falseThenFlow;
	}

	public void setFalseThenFlow(FalseThenFlow flow) {
		this.falseThenFlow = flow;
	}

	
	public void addControlFlow(ControlFlow flow) {
		if (flow instanceof TrueThenFlow) {
			this.trueThenFlow = (TrueThenFlow) flow;
		} else if (flow instanceof FalseThenFlow) {
			this.falseThenFlow = (FalseThenFlow) flow;
		}
	}
	
	/**
	 * determine the expression is true or false.
	 * @return
	 */
	public boolean execute(){
		if(this.expression==null || this.expression.equals(""))
			return true;
		
		return true;
	}
	
	public String getExpression() {
		return expression;
	}

	public void setExpression(String expression) {
		this.expression = expression;
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		WorkflowCondition condition = (WorkflowCondition) super.clone();
		condition.setFalseThenFlow(null);
		condition.setTrueThenFlow(null);
		return condition;
	}

	@Override
	public boolean isSatisfied() {
		// TODO Auto-generated method stub
		return false;
	}
}
