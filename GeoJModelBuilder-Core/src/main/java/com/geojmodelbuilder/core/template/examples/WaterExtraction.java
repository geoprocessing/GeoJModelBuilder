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
package com.geojmodelbuilder.core.template.examples;

import com.geojmodelbuilder.core.data.impl.ReferenceData;
import com.geojmodelbuilder.core.impl.DataFlowImpl;
import com.geojmodelbuilder.core.instance.IInputParameter;
import com.geojmodelbuilder.core.instance.IOutputParameter;
import com.geojmodelbuilder.core.instance.IProcessInstance;
import com.geojmodelbuilder.core.instance.IWorkflowInstance;
import com.geojmodelbuilder.core.instance.impl.WorkflowInstance;
import com.geojmodelbuilder.core.resource.ogc.wps.WPSProcess;
import com.geojmodelbuilder.core.template.IProcessTemplate;
import com.geojmodelbuilder.core.template.IWorkflowTemplate;
import com.geojmodelbuilder.core.template.impl.InputPort;
import com.geojmodelbuilder.core.template.impl.OutputPort;
import com.geojmodelbuilder.core.template.impl.ProcessTemplate;
import com.geojmodelbuilder.core.template.impl.SpatialMetadata;
import com.geojmodelbuilder.core.template.impl.WorkflowTemplate;
/**
 * 
 * @author Mingda Zhang
 *
 */
public class WaterExtraction {
	
	private WorkflowTemplate workflowTemplate;
	private WorkflowInstance workflowInstance;

	private IProcessTemplate mapcalcProcess(String input1,String input2){
		
		//one candidate
		WPSProcess mapcalcProcess = new WPSProcess("RasterMapcalcProcess");
		mapcalcProcess.setWPSUrl("http://geos.whu.edu.cn:8080/wps10/WebProcessingService");
		
		IInputParameter firstInput = mapcalcProcess.getInput("FirstInputData");
		ReferenceData referenceData = new ReferenceData();
		referenceData.setReference(input1);
		referenceData.setMimeType("application/geotiff");
		firstInput.setData(referenceData);
		
		IInputParameter secondInput = mapcalcProcess.getInput("SecondInputData");
		ReferenceData referenceData2 = new ReferenceData();
		referenceData2.setReference(input2);
		referenceData2.setMimeType("application/geotiff");
		secondInput.setData(referenceData2);
		
		IOutputParameter output = mapcalcProcess.getOutput("OutputData");
		ReferenceData outputRef = new ReferenceData();
		outputRef.setMimeType("application/geotiff");
		output.setData(outputRef);
		
		//template with a candidate
		ProcessTemplate processTemplate = new ProcessTemplate("NDVICalculation");
		processTemplate.setDescription("Normalized Difference Vegetation Index (NDVI) calculation");
		processTemplate.addExecCandidate(mapcalcProcess);
		
		InputPort firstInputPort = new InputPort(processTemplate,"Image_Band1");
		SpatialMetadata firstPortMetadata = new  SpatialMetadata();
		firstPortMetadata.setDescription("band1");
		firstPortMetadata.setMimeType("application/geotiff");
		firstInputPort.setSpatialDescription(firstPortMetadata);
		firstInputPort.addParameter(firstInput);
		processTemplate.addInput(firstInputPort);
		
		
		InputPort secondInputPort = new InputPort(processTemplate,"Image_Band2");
		SpatialMetadata secondPortMetadata = new  SpatialMetadata();
		secondPortMetadata.setDescription("band2");
		secondPortMetadata.setMimeType("application/geotiff");
		secondInputPort.setSpatialDescription(secondPortMetadata);
		secondInputPort.addParameter(secondInput);
		processTemplate.addInput(secondInputPort);
		
		OutputPort outputPort = new OutputPort(processTemplate,"output");
		SpatialMetadata outPortMetadata = new  SpatialMetadata();
		outPortMetadata.setDescription("water");
		outPortMetadata.setMimeType("application/geotiff");
		outputPort.setSpatialDescription(outPortMetadata);
		outputPort.addParameter(output);
		processTemplate.addOutput(outputPort);
		
		return processTemplate;
	}
	
	private void construct(){
		//There are eight processes.
		IProcessTemplate mapcalcProcess1 = mapcalcProcess("http://geos.whu.edu.cn:8080/datas/MOD09A1_20100619_band1.tif", "http://geos.whu.edu.cn:8080/datas/MOD09A1_20100619_band2.tif");
		IProcessInstance mapcalcExec1 = mapcalcProcess1.getInstances().get(0);
		
		
		IProcessTemplate binaryProcess1 = binaryProcess();
		IProcessInstance binaryExec1 = binaryProcess1.getInstances().get(0);
		
		//workflow
		workflowTemplate = new WorkflowTemplate();
		workflowInstance = new WorkflowInstance();
		
		workflowTemplate.addProcess(mapcalcProcess1);
		workflowTemplate.addProcess(binaryProcess1);
		
		workflowInstance.addProcess(mapcalcExec1);
		workflowInstance.addProcess(binaryExec1);
		
		//data flows between processes
		DataFlowImpl mapcalcBinaryFlow1 = new DataFlowImpl(mapcalcProcess1, mapcalcProcess1.getOutput("output"), binaryProcess1, binaryProcess1.getInput("image"));
		mapcalcProcess1.addLink(mapcalcBinaryFlow1);
		binaryProcess1.addLink(mapcalcBinaryFlow1);
		
		DataFlowImpl mapcalcBinaryFlow_Exec1 = new DataFlowImpl(mapcalcExec1, mapcalcExec1.getOutput("OutputData"), binaryExec1, binaryExec1.getInput("InputData"));
		mapcalcExec1.addLink(mapcalcBinaryFlow_Exec1);
		binaryExec1.addLink(mapcalcBinaryFlow_Exec1);
		
		workflowTemplate.setName("WaterExtractionWorkflowTemplate");
		workflowTemplate.setID("WaterExtractionWorkflowTemplate");
		workflowInstance.setID("WaterExtractionWorkflowInstance");
		workflowInstance.setName("WaterExtractionWorkflowInstance");
		workflowTemplate.addInstance(workflowInstance);
	
	}
	
	private  IProcessTemplate binaryProcess(){
		
		//binary process instance
		WPSProcess binaryProcess = new WPSProcess("RasterBinaryProcess");
		binaryProcess.setWPSUrl("http://geos.whu.edu.cn:8080/wps10/WebProcessingService");
		
		IInputParameter firstInput = binaryProcess.getInput("InputData");
		ReferenceData referenceData = new ReferenceData();
		referenceData.setMimeType("application/geotiff");
		firstInput.setData(referenceData);
		
		IOutputParameter output = binaryProcess.getOutput("OutputData");
		ReferenceData outputRef = new ReferenceData();
		outputRef.setMimeType("application/geotiff");
		output.setData(outputRef);
		
		
		ProcessTemplate processTemplate = new ProcessTemplate("Binary");
		processTemplate.setDescription("binary process");
		processTemplate.addExecCandidate(binaryProcess);
		
		InputPort firstInputPort = new InputPort(processTemplate,"image");
		SpatialMetadata firstPortMetadata = new  SpatialMetadata();
		firstPortMetadata.setDescription("image");
		firstPortMetadata.setMimeType("application/geotiff");
		firstInputPort.setSpatialDescription(firstPortMetadata);
		firstInputPort.addParameter(firstInput);
		processTemplate.addInput(firstInputPort);
		
		
		OutputPort outputPort = new OutputPort(processTemplate,"output");
		SpatialMetadata outPortMetadata = new  SpatialMetadata();
		outPortMetadata.setDescription("binaried image");
		outPortMetadata.setMimeType("application/geotiff");
		outputPort.setSpatialDescription(outPortMetadata);
		outputPort.addParameter(output);
		processTemplate.addOutput(outputPort);
		
		return processTemplate;
	}
	
	
	public IWorkflowTemplate getTemplateWorkflow(){
		if(this.workflowTemplate == null)
			construct();
		
		return this.workflowTemplate;
	}
	
	public IWorkflowInstance getInstanceWorkflow(){
		if(this.workflowInstance == null)
			construct();
		
		return this.workflowInstance;
	}

}
