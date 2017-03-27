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
package com.geojmodelbuilder.core.template;

import java.util.List;

import com.geojmodelbuilder.core.IProcess;
import com.geojmodelbuilder.core.instance.IProcessInstance;


/**
 * @author Mingda Zhang
 *
 */
public interface IProcessTemplate extends IProcess{

	/**
	 * The executable processes hold by the abstract process
	 */
	List<? extends IProcessInstance> getInstances();
	List<? extends IInputPort> getInputs();
	List<? extends IOutPutPort> getOutputs();
	
	/**
	 * Get input port by name.
	 * @param name
	 */
	IInputPort getInput(String name);
	
	/**
	 * Get output port by name.
	 * @param name
	 */
	IOutPutPort getOutput(String name);
	
}
