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

import com.geojmodelbuilder.core.ILink;
import com.geojmodelbuilder.core.plan.IProcessExec;
import com.geojmodelbuilder.core.recipe.IInputPort;
import com.geojmodelbuilder.core.recipe.IOutPutPort;
import com.geojmodelbuilder.core.recipe.IProcessRecipe;

/**
 * @author Mingda Zhang
 *
 */
public class ProcessRecipe implements IProcessRecipe{

	private List<IProcessExec> procExecs = null;
	private List<IInputPort> inputs ;
	private List<IOutPutPort> outputs;
	private String id;
	private String name;
	private String description;
	private List<ILink> links;
	
	public ProcessRecipe(){
		super();
		this.procExecs = new ArrayList<IProcessExec>();
		this.inputs = new ArrayList<IInputPort>();
		this.outputs = new ArrayList<IOutPutPort>();
		this.links = new ArrayList<ILink>();
	}
	
	
	
	public void addExecCandidate(IProcessExec procExec){
		if(!this.procExecs.contains(procExec))
			this.procExecs.add(procExec);
	}
	
	public void removeExecCandidate(IProcessExec procExec){
		if(this.procExecs.contains(procExec))
			this.procExecs.remove(procExec);
	}
	
	public List<IProcessExec> getExecCandidates() {
		return this.procExecs;
	}
	public List<IInputPort> getInputs() {
		return this.inputs;
	}
	public List<IOutPutPort> getOutputs() {
		return this.outputs;
	}

	/**
	 * Do nothing
	 */
	public boolean execute() {
		return false;
	}

	public void setID(String id){
		this.id = id;
	}

	public String getID() {
		return this.id;
	}

	public void setDescription(String description){
		this.description = description;
	}

	public String getDescription() {
		return this.description;
	}

	public void setName(String name){
		this.name = name;
	}
	public String getName() {
		return this.name;
	}

	public boolean canExecute() {
		return false;
	}
	
	public List<ILink> getLinks() {
		return this.links;
	}

	public void removeLink(ILink link){
		if(this.links.contains(link))
			this.links.remove(link);
	}

	public void addLink(ILink link) {
		if(!this.links.contains(link))
			this.links.add(link);
	}
}
