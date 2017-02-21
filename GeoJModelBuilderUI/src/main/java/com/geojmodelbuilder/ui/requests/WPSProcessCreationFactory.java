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

import java.util.HashMap;
import java.util.Map;

import org.eclipse.gef.requests.CreationFactory;

import com.geojmodelbuilder.core.plan.IInputParameter;
import com.geojmodelbuilder.core.plan.IOutputParameter;
import com.geojmodelbuilder.core.plan.IParameter;
import com.geojmodelbuilder.core.recipe.IPort;
import com.geojmodelbuilder.core.resource.ogc.wps.WPSProcess;
import com.geojmodelbuilder.ui.models.ProcessInputArtifact;
import com.geojmodelbuilder.ui.models.ProcessOutputArtifact;
import com.geojmodelbuilder.ui.models.WorkflowArtifact;
import com.geojmodelbuilder.ui.models.WorkflowProcess;
/**
 * 
 * @author Mingda Zhang
 *
 */
public class WPSProcessCreationFactory implements CreationFactory {
	private WPSProcess wpsProcess;
	public WPSProcessCreationFactory(WPSProcess wpsProcess) {
		this.wpsProcess = wpsProcess;
	}
	@Override
	public Object getNewObject() {
		WorkflowProcess process = new WorkflowProcess();
		process.setName(wpsProcess.getName());
		
		Map<WorkflowArtifact, IParameter> port2Parameter = new HashMap<WorkflowArtifact, IParameter>();
		//add the process
		for(IInputParameter input:wpsProcess.getInputs()){
			ProcessInputArtifact inputArtifact = new ProcessInputArtifact(input.getName());
			process.addInputArtifact(inputArtifact);
			port2Parameter.put(inputArtifact, input);
		}
		
		for(IOutputParameter output:wpsProcess.getOutputs()){
			ProcessOutputArtifact outputArtifact = new ProcessOutputArtifact(process);
			outputArtifact.setName(output.getName());
			port2Parameter.put(outputArtifact, output);
			process.addOutputArtifact(outputArtifact);
		}
		
		process.addExectableProcess(wpsProcess);
		process.addProcessMap(wpsProcess, port2Parameter);
		
		return process;
	}

	@Override
	public Object getObjectType() {
		return wpsProcess.getClass();
	}

}
