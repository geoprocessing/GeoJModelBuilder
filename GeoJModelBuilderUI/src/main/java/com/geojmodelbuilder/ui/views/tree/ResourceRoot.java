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
package com.geojmodelbuilder.ui.views.tree;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Mingda Zhang
 *
 */
public class ResourceRoot implements ITreeNode{

	private String name;
	private List<GeoprocessingNode> geoprocessings;
	public ResourceRoot(String name) {
		this.name = name;
		geoprocessings = new ArrayList<GeoprocessingNode>();
	}
	
	public void addGeoprocessing(GeoprocessingNode node){
		this.geoprocessings.add(node);
		node.setParent(this);
	}
	@Override
	public String getName() {
		return this.name;
	}
	@Override
	public boolean hasChildren() {
		return geoprocessings.size()>0;
	}
	@Override
	public Object[] getChildren() {
		return geoprocessings.toArray();
	}
	@Override
	public Object getParent() {
		return null;
	}
}
