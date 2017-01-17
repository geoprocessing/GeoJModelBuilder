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
package com.geojmodelbuilder.core.recipe;

import java.util.List;

import com.geojmodelbuilder.core.IExchange;
import com.geojmodelbuilder.core.IProcess;
import com.geojmodelbuilder.core.data.IData;
import com.geojmodelbuilder.core.recipe.impl.SpatialMetadata;

/**
 * @author Mingda Zhang
 *
 */
public interface IPort extends IExchange{

	/**
	 * The data candidates hold by this port.
	 */
	List<IData> getDataCandidates();

	/**
	 * The routes starting or ending with the port. There may be several data
	 * routes starting with output ports. But there is only one ending with input port.
	 */
//	List<IDataFlow> getRoutes();
	
	/**
	 * The owner of the port.
	 * @return
	 */
	IProcess getOwner();
	
	/**
	 * spatial-temporal constraints of the port.
	 */
	SpatialMetadata getSptialDescription();
}
