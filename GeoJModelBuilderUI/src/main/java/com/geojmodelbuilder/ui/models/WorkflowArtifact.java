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

import com.geojmodelbuilder.core.INamespaceDefault;
import com.geojmodelbuilder.core.data.IData;
import com.geojmodelbuilder.core.instance.IParameter;
import com.geojmodelbuilder.core.template.IPort;
import com.geojmodelbuilder.core.template.impl.SpatialMetadata;
import com.geojmodelbuilder.core.utils.ValidateUtil;

/**
 * This is a superclass of all kinds of artifact, including the input and output
 * of a process, and an independent artifact.
 * 
 * @author Mingda Zhang
 *
 */
public class WorkflowArtifact extends WorkflowNode implements IWorkflowElement,IPort {
	private String value;
	private WorkflowProcess owner;
	private SpatialMetadata spatialMetadata;
	public WorkflowArtifact() {
		super();
		spatialMetadata = new SpatialMetadata();
		setColor(ColorConstants.lightGray);
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
	
	public void setOwner(WorkflowProcess owner){
		this.owner = owner;
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

	@Override
	public WorkflowProcess getOwner() {
		return this.owner;
	}

	
	@Override
	public List<? extends IParameter> getInstances() {
		//do nothing
		if(this.owner == null)
			return null;
		
		return this.owner.getPortInstances(this);
	}

	
	@Override
	public String getNamespace() {
		if(ValidateUtil.isStrEmpty(this.namespace))
			this.namespace = INamespaceDefault.TEMPLATE_ARTIFACT;
		
		return this.namespace;
	}
}
