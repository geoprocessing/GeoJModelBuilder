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
package com.geojmodelbuilder.core.plan.impl;

import java.util.ArrayList;
import java.util.List;

import com.geojmodelbuilder.core.ILink;
import com.geojmodelbuilder.core.data.IData;
import com.geojmodelbuilder.core.plan.IInputParameter;
import com.geojmodelbuilder.core.plan.IOutputParameter;
import com.geojmodelbuilder.core.plan.IParameter;
import com.geojmodelbuilder.core.plan.IProcessExec;

/**
 * @author Mingda Zhang
 *
 */
public class ProcessExec implements IProcessExec {

	private List<IInputParameter> inputs;
	private List<IOutputParameter> outputs;
	private String id;
	private String name;
	private String description;
	private List<ILink> links;
	private StringBuffer strBufErr;

	public ProcessExec() {
		this.inputs = new ArrayList<IInputParameter>();
		this.outputs = new ArrayList<IOutputParameter>();
		this.links = new ArrayList<ILink>();
		this.strBufErr = new StringBuffer();
	}

	public List<IInputParameter> getInputs() {
		return this.inputs;
	}

	public boolean execute() {
		return false;
	}

	/**
	 * The value of every parameter is assigned.
	 */
	public boolean canExecute() {
		for(IParameter parameter:getInputs()){
			IData data = parameter.getData();
			Object value = data.getValue();
			if(value == null || value.toString().equals(""))
				return false;
		}
		
		return true;
	}

	public List<IOutputParameter> getOutputs() {
		return this.outputs;
	}

	public void setID(String id) {
		this.id = id;
	}

	public String getID() {
		return id;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getDescription() {
		return description;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return this.name;
	}

	public List<ILink> getLinks() {
		return this.links;
	}

	protected StringBuffer appendErr(String errInfo) {
		this.strBufErr.append(errInfo + "\n");
		return this.strBufErr;
	}

	public String getErrInfo() {
		return this.strBufErr.toString();
	}

	public IInputParameter getInput(String name) {
		for(IInputParameter input:this.inputs){
			if(input.getName().equals(name))
				return input;
		}
		return null;
	}

	public IOutputParameter getOuput(String name) {
		for(IOutputParameter output:this.outputs){
			if(output.getName().equals(name))
				return output;
		}
		return null;
	}

	protected void addInput(IInputParameter input) {
		if(!this.inputs.contains(input))
			this.inputs.add(input);
	}

	protected void addOutput(IOutputParameter output) {
		if(!this.outputs.contains(output))
			this.outputs.add(output);
	}

	public void addLink(ILink link) {
		if(!this.links.contains(link))
			this.links.add(link);
	}
	
	public void removeLink(ILink link){
		if(this.links.contains(link))
			this.links.remove(link);
	}
}
