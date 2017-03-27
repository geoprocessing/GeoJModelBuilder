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
import com.geojmodelbuilder.core.impl.AbstractProcessImpl;
import com.geojmodelbuilder.core.instance.IInputParameter;
import com.geojmodelbuilder.core.instance.IOutputParameter;
import com.geojmodelbuilder.core.instance.IParameter;
import com.geojmodelbuilder.core.instance.IProcessInstance;
import com.geojmodelbuilder.core.utils.ValidateUtil;

/**
 * @author Mingda Zhang
 *
 */
public class ProcessInstance extends AbstractProcessImpl<IInputParameter, IOutputParameter> implements IProcessInstance {

	private StringBuffer strBufErr;

	public ProcessInstance() {
		super();
		this.strBufErr = new StringBuffer();
	}


	/**
	 * The value of every parameter is assigned.
	 */
	public boolean canExecute() {
		for(IParameter parameter:getInputs()){
			IData data = parameter.getData();
			Object value = data.getValue();
			if(value == null || value.toString().equals(""))
				return false;
		}
		
		return true;
	}

	protected StringBuffer appendErr(String errInfo) {
		this.strBufErr.append(errInfo + "\n");
		return this.strBufErr;
	}

	public String getErrInfo() {
		return this.strBufErr.toString();
	}


	@Override
	public String getID() {
		if(ValidateUtil.isStrEmpty(this.id)){
			String prefix = ValidateUtil.isStrEmpty(this.name) ? "Process":this.name;
			this.id = prefix + this.generatedAt;
		}
		
		return this.id;
	}
	
	@Override
	public String getNamespace() {
		if(ValidateUtil.isStrEmpty(this.namespace))
			this.namespace = INamespaceDefault.INSTANCE_PROCESS;
		
		return this.namespace;
	}

}
