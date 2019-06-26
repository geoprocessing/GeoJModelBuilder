/**
 * Copyright (C) 2013 - 2016 Wuhan University,
 * 
 * This program is free software; you can redistribute and/or modify it under 
 * the terms of the GNU General Public License version 2 as published by the 
 * Free Software Foundation.
 * 
 * This program is distributed WITHOUT ANY WARRANTY; even without the implied
 * WARRANTY OF MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 */
package com.geojmodelbuilder.core.impl;

import java.util.ArrayList;
import java.util.List;

import com.geojmodelbuilder.core.IExchange;
import com.geojmodelbuilder.core.ILink;
import com.geojmodelbuilder.core.IProcess;
import com.geojmodelbuilder.core.utils.IDGenerator;
import com.geojmodelbuilder.core.utils.ValidateUtil;

/**
 * @author Mingda Zhang
 *
 */
public abstract class AbstractProcessImpl<I extends IExchange,O extends IExchange> implements IProcess{
	protected String id,name,namespace,description,title;
	private List<I> inputs;
	private List<O> outputs;
	private List<ILink> links;
	protected String generatedAt;
	
	public AbstractProcessImpl(){
		inputs = new ArrayList<I>();
		outputs = new ArrayList<O>();
		links = new ArrayList<ILink>();
		this.generatedAt = IDGenerator.dateID();
	}
	
	public List<ILink> getLinks() {
		return this.links;
	}

	public void addInput(I input){
		if(!this.inputs.contains(input))
			this.inputs.add(input);
	}
	
	public List<I> getInputs() {
		return this.inputs;
	}

	public boolean execute() {
		return false;
	}

	public boolean canExecute() {
		return false;
	}

	public void addOutput(O output){
		if(!this.outputs.contains(output))
			this.outputs.add(output);
	}
	
	public List<O> getOutputs() {
		return this.outputs;
	}

	public void addLink(ILink link) {
		if(!this.links.contains(link))
			this.links.add(link);
	}

	public void removeLink(ILink link) {
		if(this.links.contains(link))
			this.links.remove(link);
	}
	
	public void setID(String id){
		this.id = id;
	}
	
	public String getID() {
		if (ValidateUtil.isStrEmpty(this.id)) {
			this.id = "process"+this.generatedAt;
		}
		return this.id;
	}

	public void setNamespace(String namespace){
		this.namespace = namespace;
	}
	
	public String getNamespace() {
		return this.namespace;
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
	
	public I getInput(String name) {
		for(I input:this.inputs){
			if(input.getName().equals(name))
				return input;
		}
		
		return null;
	}
	
	public O getOutput(String name){
		for(O output:this.outputs){
			if(output.getName().equals(name))
				return output;
		}
		
		return null;
	}
	
	public String getErrInfo(){
		return "";
	}
}
