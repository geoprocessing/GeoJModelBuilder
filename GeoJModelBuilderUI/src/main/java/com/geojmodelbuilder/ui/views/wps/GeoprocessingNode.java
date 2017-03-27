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
package com.geojmodelbuilder.ui.views.wps;

import java.util.ArrayList;
import java.util.List;

import com.geojmodelbuilder.ui.views.tree.ITreeNode;

/**
 * @author Mingda Zhang
 *
 */
public class GeoprocessingNode implements ITreeNode{
	private String name;
	private List<WPSNode> wpsList;
	private WPSResourceRoot parent;
	public GeoprocessingNode(String name,WPSResourceRoot parent){
		this.name = name;
		this.parent = parent;
		wpsList = new ArrayList<WPSNode>();
	}
		
	public void addWPS(WPSNode wps){
		wpsList.add(wps);
	}
	
	public void setName(String name){
		this.name = name;
	}
	
	public String getName(){
		return this.name;
	}
	
	public List<WPSNode> getWPSs(){
		return this.wpsList;
	}

	public void removeChild(WPSNode node){
		if(this.wpsList.contains(node))
			this.wpsList.remove(node);
	}
	@Override
	public boolean hasChildren() {
		return wpsList.size()>0;
	}

	@Override
	public Object[] getChildren() {
		return wpsList.toArray();
	}

	@Override
	public Object getParent() {
		return parent;
	}
	
	public void setParent(WPSResourceRoot root){
		this.parent = root;
	}
}
