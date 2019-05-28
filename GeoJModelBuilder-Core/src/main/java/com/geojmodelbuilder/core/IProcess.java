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
package com.geojmodelbuilder.core;

import java.util.List;

/**
 * @author Mingda Zhang
 *
 */
public interface IProcess extends IIdentifiable{
	
	List<ILink> getLinks();
	/**
	 * Inputs
	 */
	List<? extends IExchange> getInputs();
	
	/**
	 * Perform execution
	 */
	boolean execute();
	
	/**
	 * Whether the process is ready to execute
	 */
	
	boolean canExecute();
	
	/**
	 * Outputs
	 */
	List<? extends IExchange> getOutputs();
	
	/**
	 * Identification string
	 */
	String getID();
	
	/**
	 * Descriptive information
	 */
	String getDescription();
	
	/**
	 * Object name
	 */
	String getName();
	
	/**
	 * Get input by name.
	 */
	IExchange getInput(String name);
	
	/**
	 * Get output by name.
	 */
	IExchange getOutput(String name);
	
	void addLink(ILink link);
	
	void removeLink(ILink link);
	
	String getErrInfo();
}
