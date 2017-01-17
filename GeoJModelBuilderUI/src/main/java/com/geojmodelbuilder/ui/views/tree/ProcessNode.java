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

/**
 * @author Mingda Zhang
 *
 */
public class ProcessNode implements ITreeNode{
	private String name;
	private WPSNode parent;
	public ProcessNode(String name, WPSNode parent){
		this.name = name;
		this.parent = parent;
	}
	
	public String getName(){
		return this.name;
	}
	
	public WPSNode getParent(){
		return this.parent;
	}

	@Override
	public boolean hasChildren() {
		return false;
	}

	@Override
	public Object[] getChildren() {
		return null;
	}
}
