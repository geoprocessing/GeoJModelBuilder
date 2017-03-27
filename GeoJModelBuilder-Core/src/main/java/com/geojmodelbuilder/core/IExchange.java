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

/**
 * Exchangeable item
 * 
 * @author Mingda Zhang
 *
 */
public interface IExchange extends IIdentifiable{
	/**
	 * The name 
	 */
	String getName();
	
	/**
	 * The owner of this exchange
	 */
	IProcess getOwner();
}
