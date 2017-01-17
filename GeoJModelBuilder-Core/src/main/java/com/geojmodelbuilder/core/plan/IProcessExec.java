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
package com.geojmodelbuilder.core.plan;

import java.util.List;

import com.geojmodelbuilder.core.IProcess;

/**
 * Executable process.
 * 
 * @author MingdaZhang
 *
 */
public interface IProcessExec extends IProcess{
	
	/**
	 * messages if error occurred.
	 */
	String getErrInfo();
	
	/**
	 * Get input parameter by name.
	 * @param name
	 */
	IInputParameter getInput(String name);
	
//	void addInput(IInputParameter input);
	
	/**
	 * Get output parameter by name.
	 * @param name
	 */
	IOutputParameter getOuput(String name);
	
//	void addOutput(IOutputParameter output);
	
	/**
	 * Perform execution
	 */
	boolean execute();
	
	/**
	 * Whether the process is ready to execute
	 */
	
	boolean canExecute();
	
	public List<IOutputParameter> getOutputs();
}
