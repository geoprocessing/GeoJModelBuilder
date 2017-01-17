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
package com.geojmodelbuilder.core.recipe.impl;

import java.util.ArrayList;
import java.util.List;

import com.geojmodelbuilder.core.IProcess;
import com.geojmodelbuilder.core.data.IData;
import com.geojmodelbuilder.core.recipe.IPort;

/**
 * @author Mingda Zhang
 *
 */
public class Port implements IPort {

	private List<IData> dataList;
	private IProcess owner;
	private SpatialMetadata spatialMetadata;
	
	public Port() {
		dataList = new ArrayList<IData>();
		spatialMetadata = new SpatialMetadata();
	}

	public void addDataCandidate(IData data) {
		if (!this.dataList.contains(data))
			this.dataList.add(data);

	}

	public void removeDataCandidate(IData data) {
		if (this.dataList.contains(data))
			this.dataList.remove(data);
	}

	public List<IData> getDataCandidates() {
		return this.dataList;
	}

	public void setOwer(IProcess owner){
		this.owner = owner;
	}
	
	public IProcess getOwner() {
		return this.owner;
	}

	public void setSpatialDescription(SpatialMetadata spatialMetadata){
		this.spatialMetadata = spatialMetadata;
	}
	
	public SpatialMetadata getSptialDescription() {
		return this.spatialMetadata;
	}

}
