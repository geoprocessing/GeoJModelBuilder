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

import com.geojmodelbuilder.core.plan.IInputParameter;
import com.geojmodelbuilder.core.plan.IOutputParameter;
import com.geojmodelbuilder.core.resource.ogc.wps.WPSProcess;
import com.geojmodelbuilder.ui.models.ProcessInputArtifact;
import com.geojmodelbuilder.ui.models.ProcessOutputArtifact;
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
		//add the process
		for(IInputParameter input:wpsProcess.getInputs()){
			process.addInputArtifact(new ProcessInputArtifact(input.getName()));
		}
		
		for(IOutputParameter output:wpsProcess.getOutputs()){
			ProcessOutputArtifact outputArtifact = new ProcessOutputArtifact(process);
			outputArtifact.setName(output.getName());
			process.addOutputArtifact(outputArtifact);
		}
		
		return process;
	}

	@Override
	public Object getObjectType() {
		return wpsProcess.getClass();
	}

}
