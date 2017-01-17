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

import com.geojmodelbuilder.core.recipe.IInputPort;

/**
 * 
 * @author Mingda Zhang
 *
 */
public class ProcessInputArtifact extends WorkflowArtifact implements IInputPort {
	public ProcessInputArtifact(){
		super();
	}
	public ProcessInputArtifact(WorkflowArtifact artifact){
		super();
		this.setName(artifact.getName());
		this.setColor(artifact.getColor());
		this.setOwner(artifact.getOwner());
		this.setValue(artifact.getValue());
	}
	
	public ProcessInputArtifact(String name){
		super(name);
	}

}
