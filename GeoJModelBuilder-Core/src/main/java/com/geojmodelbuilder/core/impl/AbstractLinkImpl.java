/**
 * Copyright (C) 2013 - 2016 Wuhan University,
 *                           Center for Geographic Analysis, Harvard University
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

import com.geojmodelbuilder.core.ILink;
import com.geojmodelbuilder.core.INamespaceDefault;
import com.geojmodelbuilder.core.IProcess;
import com.geojmodelbuilder.core.utils.IDGenerator;
import com.geojmodelbuilder.core.utils.ValidateUtil;

/**
 * @author Mingda Zhang
 *
 */
public abstract class AbstractLinkImpl<ISource extends IProcess,ITarget extends IProcess> 
						implements ILink{

	private ISource processSource;
	private ITarget processTarget;
	private String id,namespace;
	private String generatedAt = IDGenerator.dateID();
	
	public void setID(String id){
		this.id = id;
	}
	
	public String getID() {
		if(ValidateUtil.isStrEmpty(this.id))
			this.id = "Link" + generatedAt;
		
		return this.id;
	}
	
	public void setNamespace(String namespace){
		this.namespace = namespace;
	}

	public String getNamespace() {
		if(ValidateUtil.isStrEmpty(this.namespace))
			this.namespace = INamespaceDefault.INOUTPORT;
		
		return this.namespace;
	}

	public void setSourceProcess(ISource sourceProcess){
		this.processSource = sourceProcess;
	}
	
	public ISource getSourceProcess() {
		return this.processSource;
	}

	public void setTargetProcess(ITarget targetProcess){
		this.processTarget = targetProcess;
	}
	
	public ITarget getTargetProcess() {
		return this.processTarget;
	}
	
	
}
