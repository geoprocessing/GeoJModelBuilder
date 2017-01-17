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
package com.geojmodelbuilder.ui.requests;

import org.eclipse.gef.requests.CreationFactory;

import com.geojmodelbuilder.ui.models.ProcessOutputArtifact;
import com.geojmodelbuilder.ui.models.StandaloneArtifact;
import com.geojmodelbuilder.ui.models.WorkflowCondition;
import com.geojmodelbuilder.ui.models.WorkflowNode;
import com.geojmodelbuilder.ui.models.WorkflowProcess;
import com.geojmodelbuilder.ui.models.links.DataFlow;
import com.geojmodelbuilder.ui.models.links.FalseThenFlow;
import com.geojmodelbuilder.ui.models.links.NodeLink;
import com.geojmodelbuilder.ui.models.links.TrueThenFlow;
/**
 * 
 * @author Mingda Zhang
 *
 */
public class AbstractElementCreationFactory implements CreationFactory {

	private Class<?> template;

	public AbstractElementCreationFactory(Class<?> template) {
		this.template = template;
	}

	@Override
	public Object getNewObject() {
//		System.out.println("ElementCreationFactory");
		WorkflowNode model = null;
		if (this.template == WorkflowCondition.class) {
			model = new WorkflowCondition();
			model.setName("Condition");
		} else if (this.template == WorkflowProcess.class) {
			model = new WorkflowProcess();
			//add a default output.
			ProcessOutputArtifact output = new ProcessOutputArtifact((WorkflowProcess)model);
			output.setName("output");
			((WorkflowProcess)model).addOutputArtifact(output);
			model.setName("Process");
		} else if (this.template == StandaloneArtifact.class) {
			model = new StandaloneArtifact();
			model.setName("Dataset");
		} else if (this.template == TrueThenFlow.class) {
			return new TrueThenFlow();
		} else if (this.template == FalseThenFlow.class) {
			return new FalseThenFlow();
		} else if (this.template == DataFlow.class) {
			return new DataFlow();
		}else if (this.template == NodeLink.class) {
			return new NodeLink();
		}

		return model;
	}

	@Override
	public Object getObjectType() {
		return template;
	}

}
