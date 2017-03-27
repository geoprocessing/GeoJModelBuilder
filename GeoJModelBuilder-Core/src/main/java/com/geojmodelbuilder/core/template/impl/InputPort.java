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
package com.geojmodelbuilder.core.template.impl;

import com.geojmodelbuilder.core.instance.IInputParameter;
import com.geojmodelbuilder.core.template.IInputPort;
import com.geojmodelbuilder.core.template.IProcessTemplate;

/**
 * @author Mingda Zhang
 *
 */
public class InputPort extends Port<IInputParameter> implements IInputPort {
	public InputPort(IProcessTemplate owner){
		super(owner);
	}
	
	public InputPort(IProcessTemplate owner,String name){
		super(owner,name);
	}
}
