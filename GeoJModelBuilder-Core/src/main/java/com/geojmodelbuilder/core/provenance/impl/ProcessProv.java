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
package com.geojmodelbuilder.core.provenance.impl;

import java.util.Date;

import com.geojmodelbuilder.core.INamespaceDefault;
import com.geojmodelbuilder.core.IProcess;
import com.geojmodelbuilder.core.instance.impl.ProcessInstance;
import com.geojmodelbuilder.core.provenance.IProcessProv;
import com.geojmodelbuilder.core.utils.IDGenerator;
import com.geojmodelbuilder.core.utils.ValidateUtil;
/**
 * 
 * @author Mingda Zhang
 *
 */
public class ProcessProv extends ProcessInstance implements IProcessProv {
	private Date startTime;
	private Date endTime;
	private boolean status;
	private IProcess process;
	
	public ProcessProv(IProcess process){
		super();
		this.process = process;
		this.setID(process.getName()+"Trace"+IDGenerator.dateID());
		
	}
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getStartTime() {
		return this.startTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public Date getEndTime() {
		return this.endTime;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	public boolean getStatus() {
		return this.status;
	}
	
	public IProcess getProcess(){
		return this.process;
	}
	
	@Override
	public String getName() {
		return this.process.getName();
	}
	

	@Override
	public String getID() {
		if(ValidateUtil.isStrEmpty(this.id))
			this.id = "activity_" + IDGenerator.uuid();
		
		return this.id;
	}
	
	@Override
	public String getNamespace() {
		if(ValidateUtil.isStrEmpty(this.namespace))
			this.namespace = INamespaceDefault.EXECUTION_PROCESS;
		
		return this.namespace;
	}
}
