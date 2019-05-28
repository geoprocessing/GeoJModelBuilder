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
package com.geojmodelbuilder.core.instance.impl;

import com.geojmodelbuilder.core.INamespaceDefault;
import com.geojmodelbuilder.core.data.IData;
import com.geojmodelbuilder.core.instance.IParameter;
import com.geojmodelbuilder.core.instance.IProcessInstance;
import com.geojmodelbuilder.core.utils.ValidateUtil;

/**
 * @author Mingda Zhang
 *
 */
public abstract class Parameter implements IParameter {
	private String name;
	private IData value;
	private IProcessInstance owner;
	private String id;
	private String namespace;
	private String description;
	
	public Parameter(IProcessInstance owner){
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
	
	public IProcessInstance getOwner() {
		return this.owner;
	}
	
	public void setID(String id){
		this.id = id;
	}
	
	public String getID() {
		if(ValidateUtil.isStrEmpty(this.id)){
			String suffix = ValidateUtil.isStrEmpty(this.name)?"artifact":this.name;
			this.id = getOwner().getID() + "_" + suffix;
		}
		
		return this.id;
	}
	
	public void setNamespace(String namespace){
		this.namespace = namespace;
	}
	
	public String getNamespace() {
		if(ValidateUtil.isStrEmpty(this.namespace))
			this.namespace = INamespaceDefault.INSTANCE_ARTIFACT;
		
		return this.namespace;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
}
