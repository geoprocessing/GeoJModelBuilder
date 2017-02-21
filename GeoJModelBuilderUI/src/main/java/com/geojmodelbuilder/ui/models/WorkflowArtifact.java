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
package com.geojmodelbuilder.ui.models;

import java.util.List;

import org.eclipse.draw2d.ColorConstants;

import com.geojmodelbuilder.core.IProcess;
import com.geojmodelbuilder.core.data.IData;
import com.geojmodelbuilder.core.recipe.IPort;
import com.geojmodelbuilder.core.recipe.impl.SpatialMetadata;

/**
 * This is a superclass of all kinds of artifact, including the input and output
 * of a process, and an independent artifact.
 * 
 * @author Mingda Zhang
 *
 */
public class WorkflowArtifact extends WorkflowNode implements IWorkflowElement,IPort {
	private String value;
	private IProcess owner;
	private SpatialMetadata spatialMetadata;

	public WorkflowArtifact() {
		super();
		spatialMetadata = new SpatialMetadata();
		setColor(ColorConstants.lightGreen);
	}

	public WorkflowArtifact(String name){
		super(name);
	}
	
	public WorkflowArtifact(Workflow parent) {
		this();
		setWorkflow(parent);
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getValue() {
		return this.value;
	}
	
	public void setOwner(IProcess owner){
		this.owner = owner;
	}
	
	public IProcess getOwner() {
		return this.owner;
	}

	public SpatialMetadata getSptialDescription(){
		return this.spatialMetadata;
	}
	
	public void setSpatialDescription(SpatialMetadata metadata){
		this.spatialMetadata = metadata;
	}
	
	@Override
	public List<IData> getDataCandidates() {
		return null;
	}
}
