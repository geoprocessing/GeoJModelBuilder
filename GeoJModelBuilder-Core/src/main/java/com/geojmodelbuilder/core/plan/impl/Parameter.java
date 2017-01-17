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
package com.geojmodelbuilder.core.plan.impl;

import com.geojmodelbuilder.core.data.IData;
import com.geojmodelbuilder.core.plan.IParameter;
import com.geojmodelbuilder.core.plan.IProcessExec;

/**
 * @author Mingda Zhang
 *
 */
public abstract class Parameter implements IParameter {
	private String name;
	private IData value;
	private IProcessExec owner;
	public Parameter(IProcessExec owner){
		this.owner = owner;
	}
	public void setName(String name){
		this.name = name;
	}
	
	public String getName() {
		return this.name;
	}
	
	public void setData(IData data){
		this.value = data;
	}
	public IData getData() {
		return this.value;
	}
	
	public IProcessExec getOwner() {
		return this.owner;
	}

}
