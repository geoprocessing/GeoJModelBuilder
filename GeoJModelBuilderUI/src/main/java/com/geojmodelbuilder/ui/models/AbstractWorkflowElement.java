/**
 * Copyright (C) 2013 - 2016 Wuhan University,
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

import java.util.UUID;

import com.geojmodelbuilder.core.IIdentifiable;
import com.geojmodelbuilder.core.INamespaceDefault;
import com.geojmodelbuilder.core.utils.ValidateUtil;

/**
 * @author Mingda Zhang
 *
 */
public abstract class AbstractWorkflowElement implements IWorkflowElement,IIdentifiable{

	protected String id,namespace;

	public AbstractWorkflowElement(){
		setId(UUID.randomUUID().toString());
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
		if(ValidateUtil.isStrEmpty(this.namespace))
			this.namespace = INamespaceDefault.BASE;
		
		return this.namespace;
	}

}
