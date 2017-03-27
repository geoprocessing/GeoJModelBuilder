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
public class WPSNode implements ITreeNode{
	private String name;
	private List<ProcessNode> processes;
	private GeoprocessingNode parent;
	private String url;
	
	public WPSNode(String name,GeoprocessingNode parent){
		this.name = name;
		processes = new ArrayList<ProcessNode>();
		this.parent = parent;
	}
	
	public void addProcess(ProcessNode process){
		this.processes.add(process);
	}
	
	public GeoprocessingNode getParent(){
		return this.parent;
	}
	public String getName(){
		return this.name;
	}
	
	public List<ProcessNode> getProcesses(){
		return this.processes;
	}
	
	public void setUrl(String url){
		this.url = url;
	}
	
	public String getUrl(){
		return this.url;
	}

	@Override
	public boolean hasChildren() {
		return processes.size()>0;
	}

	@Override
	public Object[] getChildren() {
		return processes.toArray();
	}
}
