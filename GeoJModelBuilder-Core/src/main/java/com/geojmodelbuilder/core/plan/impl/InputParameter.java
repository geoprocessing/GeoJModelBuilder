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

import com.geojmodelbuilder.core.plan.IInputParameter;
import com.geojmodelbuilder.core.plan.IProcessExec;

/**
 * @author Mingda Zhang
 *
 */
public class InputParameter extends Parameter implements IInputParameter {
	public InputParameter(IProcessExec owner){
		super(owner);
//		owner.addInput(this);
	}
}
