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
package com.geojmodelbuilder.core.instance;

import com.geojmodelbuilder.core.IExchange;
import com.geojmodelbuilder.core.data.IData;

/**
 * @author Mingda Zhang
 *
 */
public interface IParameter extends IExchange{
	
	void setName(String name);
	
	/**
	 * The data hold by the parameter.
	 */
	IData getData();
	
	void setData(IData data);
	
	/**
	 * The owner that parameter belongs to 
	 */
	IProcessInstance getOwner();
}
