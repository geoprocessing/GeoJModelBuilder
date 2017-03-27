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

import com.geojmodelbuilder.core.data.impl.LiteralData;
import com.geojmodelbuilder.core.data.impl.ReferenceData;
import com.geojmodelbuilder.core.impl.DataFlowImpl;
import com.geojmodelbuilder.core.instance.IInputParameter;
import com.geojmodelbuilder.core.instance.IOutputParameter;
import com.geojmodelbuilder.core.instance.IProcessInstance;
import com.geojmodelbuilder.core.instance.IWorkflowInstance;
import com.geojmodelbuilder.core.instance.impl.InputParameter;
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
public class FloodAnalysisTemplate {
	
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
		
		IProcessTemplate mapcalcProcess2 = mapcalcProcess("http://geos.whu.edu.cn:8080/datas/MOD09A1_20100603_band1.tif", "http://geos.whu.edu.cn:8080/datas/MOD09A1_20100603_band2.tif");
		IProcessInstance mapcalcExec2 = mapcalcProcess2.getInstances().get(0);
		
		IProcessTemplate binaryProcess1 = binaryProcess();
		IProcessInstance binaryExec1 = binaryProcess1.getInstances().get(0);
		
		IProcessTemplate binaryProcess2 = binaryProcess();
		IProcessInstance binaryExec2 = binaryProcess2.getInstances().get(0);
		
		IProcessTemplate colorsProcess1 = colorsProcess();
		IProcessInstance colorsExec1 = colorsProcess1.getInstances().get(0);
		
		IProcessTemplate colorsProcess2 = colorsProcess();
		IProcessInstance colorsExec2 = colorsProcess2.getInstances().get(0);
		
		IProcessTemplate blendPorcess = blendProcess();
		IProcessInstance blendExec = blendPorcess.getInstances().get(0);
		
		IProcessTemplate colorsProcess3 = colorsProcess();
		IProcessInstance colorsExec3 = colorsProcess3.getInstances().get(0);
		
		//workflow
		workflowTemplate = new WorkflowTemplate();
		workflowInstance = new WorkflowInstance();
		
		workflowTemplate.addProcess(mapcalcProcess1);
		workflowTemplate.addProcess(mapcalcProcess2);
		workflowTemplate.addProcess(binaryProcess1);
		workflowTemplate.addProcess(binaryProcess2);
		workflowTemplate.addProcess(colorsProcess1);
		workflowTemplate.addProcess(colorsProcess2);
		workflowTemplate.addProcess(blendPorcess);
		workflowTemplate.addProcess(colorsProcess3);
		
		workflowInstance.addProcess(mapcalcExec1);
		workflowInstance.addProcess(mapcalcExec2);
		workflowInstance.addProcess(binaryExec1);
		workflowInstance.addProcess(binaryExec2);
		workflowInstance.addProcess(colorsExec1);
		workflowInstance.addProcess(colorsExec2);
		workflowInstance.addProcess(blendExec);
		workflowInstance.addProcess(colorsExec3);
		
		//data flows between processes
		DataFlowImpl mapcalcBinaryFlow1 = new DataFlowImpl(mapcalcProcess1, mapcalcProcess1.getOutput("output"), binaryProcess1, binaryProcess1.getInput("image"));
		mapcalcProcess1.addLink(mapcalcBinaryFlow1);
		binaryProcess1.addLink(mapcalcBinaryFlow1);
		
		DataFlowImpl mapcalcBinaryFlow_Exec1 = new DataFlowImpl(mapcalcExec1, mapcalcExec1.getOutput("OutputData"), binaryExec1, binaryExec1.getInput("InputData"));
		mapcalcExec1.addLink(mapcalcBinaryFlow_Exec1);
		binaryExec1.addLink(mapcalcBinaryFlow_Exec1);
		
		DataFlowImpl mapcalcBinaryFlow2 = new DataFlowImpl(mapcalcProcess2, mapcalcProcess2.getOutput("output"), binaryProcess2, binaryProcess2.getInput("image"));
		mapcalcProcess2.addLink(mapcalcBinaryFlow2);
		binaryProcess2.addLink(mapcalcBinaryFlow2);
		
		DataFlowImpl mapcalcBinaryFlow_Exec2 = new DataFlowImpl(mapcalcExec2, mapcalcExec2.getOutput("OutputData"), binaryExec2, binaryExec2.getInput("InputData"));
		mapcalcExec2.addLink(mapcalcBinaryFlow_Exec2);
		binaryExec2.addLink(mapcalcBinaryFlow_Exec2);
		
		DataFlowImpl binaryColorLink1 = new DataFlowImpl(binaryProcess1, binaryProcess1.getOutput("output"), colorsProcess1, colorsProcess1.getInput("image"));
		binaryProcess1.addLink(binaryColorLink1);
		colorsProcess1.addLink(binaryColorLink1);
		
		DataFlowImpl binaryColorLink_Exec1 = new DataFlowImpl(binaryExec1, binaryExec1.getOutput("OutputData"), colorsExec1, colorsExec1.getInput("InputData"));
		binaryExec1.addLink(binaryColorLink_Exec1);
		colorsExec1.addLink(binaryColorLink_Exec1);
		
		DataFlowImpl binaryColorLink2 = new DataFlowImpl(binaryProcess2, binaryProcess2.getOutput("output"), colorsProcess2, colorsProcess2.getInput("image"));
		binaryProcess2.addLink(binaryColorLink2);
		colorsProcess2.addLink(binaryColorLink2);
		
		DataFlowImpl binaryColorLink_Exec2 = new DataFlowImpl(binaryExec2, binaryExec2.getOutput("OutputData"), colorsExec2, colorsExec2.getInput("InputData"));
		binaryExec2.addLink(binaryColorLink_Exec2);
		colorsExec2.addLink(binaryColorLink_Exec2);
		
		DataFlowImpl colorBlendFlow1 = new DataFlowImpl(colorsProcess1, colorsProcess1.getOutput("output"), blendPorcess, blendPorcess.getInput("image1"));
		colorsProcess1.addLink(colorBlendFlow1);
		blendPorcess.addLink(colorBlendFlow1);
		
		DataFlowImpl colorBlendFlow_Exec1 = new DataFlowImpl(colorsExec1, colorsExec1.getOutput("OutputData"), blendExec, blendExec.getInput("FirstInputData"));
		colorsExec1.addLink(colorBlendFlow_Exec1);
		blendExec.addLink(colorBlendFlow_Exec1);
		
		DataFlowImpl colorBlendFlow2 = new DataFlowImpl(colorsProcess2, colorsProcess2.getOutput("output"), blendPorcess, blendPorcess.getInput("image2"));
		colorsProcess2.addLink(colorBlendFlow2);
		blendPorcess.addLink(colorBlendFlow2);
		
		DataFlowImpl colorBlendFlow_Exe2 = new DataFlowImpl(colorsExec2, colorsExec2.getOutput("OutputData"), blendExec, blendExec.getInput("SecondInputData"));
		colorsExec2.addLink(colorBlendFlow_Exe2);
		blendExec.addLink(colorBlendFlow_Exe2);
		
		DataFlowImpl blendColorFlow = new DataFlowImpl(blendPorcess, blendPorcess.getOutput("output"), colorsProcess3, colorsProcess3.getInput("image"));
		blendPorcess.addLink(blendColorFlow);
		colorsProcess3.addLink(blendColorFlow);
		
		DataFlowImpl blendColorFlow_exec = new DataFlowImpl(blendExec, blendExec.getOutput("OutputData"), colorsExec3, colorsExec3.getInput("InputData"));
		blendExec.addLink(blendColorFlow_exec);
		colorsExec3.addLink(blendColorFlow_exec);
		
		
		workflowTemplate.setName("FloodAnalysisWorkflowTemplate");
		workflowTemplate.setID("FloodAnalysisWorkflowTemplate");
		workflowInstance.setID("FloodAnalysisWorkflowInstance");
		workflowInstance.setName("FloodAnalysisWorkflowInstance");
		workflowTemplate.addInstance(workflowInstance);
		
	
	}
	private  IProcessTemplate colorsProcess(){
		
		//color process instance
		WPSProcess colorsProcess = new WPSProcess("RasterColorsProcess");
		colorsProcess.setWPSUrl("http://geos.whu.edu.cn:8080/wps10/WebProcessingService");
		
		IInputParameter inputColor = colorsProcess.getInput("Color");
		LiteralData width = new LiteralData();
		width.setValue("random");
		inputColor.setData(width);
		
		IInputParameter fileInput = colorsProcess.getInput("InputData");
		ReferenceData referenceData = new ReferenceData();
		referenceData.setMimeType("application/geotiff");
		fileInput.setData(referenceData);
		
		IOutputParameter output = colorsProcess.getOutput("OutputData");
		ReferenceData outputRef = new ReferenceData();
		outputRef.setMimeType("application/geotiff");
		output.setData(outputRef);
		
		ProcessTemplate processTemplate = new ProcessTemplate("Color");
		processTemplate.setDescription("rendering images");
		processTemplate.addExecCandidate(colorsProcess);
		
		InputPort firstInputPort = new InputPort(processTemplate,"image");
		SpatialMetadata firstPortMetadata = new  SpatialMetadata();
		firstPortMetadata.setDescription("image");
		firstPortMetadata.setMimeType("application/geotiff");
		firstInputPort.setSpatialDescription(firstPortMetadata);
		firstInputPort.addParameter(fileInput);
		processTemplate.addInput(firstInputPort);
		
		
		InputPort secondInputPort = new InputPort(processTemplate,"color");
		SpatialMetadata secondPortMetadata = new  SpatialMetadata();
		firstPortMetadata.setDescription("color");
		secondInputPort.setSpatialDescription(secondPortMetadata);
		secondInputPort.addParameter(inputColor);
		processTemplate.addInput(secondInputPort);
		
		OutputPort outputPort = new OutputPort(processTemplate,"output");
		SpatialMetadata outPortMetadata = new  SpatialMetadata();
		outPortMetadata.setDescription("image with color");
		outPortMetadata.setMimeType("application/geotiff");
		outputPort.setSpatialDescription(outPortMetadata);
		outputPort.addParameter(output);
		processTemplate.addOutput(outputPort);
		
		return processTemplate;
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
	
	private  IProcessTemplate blendProcess(){
		
		//blending process instance
		WPSProcess process = new WPSProcess("RasterBlendProcess");
		process.setWPSUrl("http://geos.whu.edu.cn:8080/wps10/WebProcessingService");
		
		IInputParameter inputPercent = process.getInput("Percent");
		LiteralData percent = new LiteralData();
		percent.setValue("50");
		inputPercent.setData(percent);
		
		IInputParameter firstInput = process.getInput("FirstInputData");
		ReferenceData firstRefData = new ReferenceData();
		firstRefData.setMimeType("application/geotiff");
		firstInput.setData(firstRefData);
		
		InputParameter secondInput = new InputParameter(process);
		secondInput.setName("SecondInputData");
		ReferenceData secondRefData = new ReferenceData();
		secondRefData.setMimeType("application/geotiff");
		secondInput.setData(secondRefData);
		
		IOutputParameter output = process.getOutput("OutputData");
		ReferenceData outputRef = new ReferenceData();
		outputRef.setMimeType("application/geotiff");
		output.setData(outputRef);


		ProcessTemplate processTemplate = new ProcessTemplate("Blend");
		processTemplate.setDescription("images blending");
		processTemplate.addExecCandidate(process);
		
		InputPort firstInputPort = new InputPort(processTemplate,"image1");
		SpatialMetadata firstPortMetadata = new  SpatialMetadata();
		firstPortMetadata.setDescription("the first image");
		firstPortMetadata.setMimeType("application/geotiff");
		firstInputPort.setSpatialDescription(firstPortMetadata);
		firstInputPort.addParameter(firstInput);
		processTemplate.addInput(firstInputPort);
		
		
		InputPort secondInputPort = new InputPort(processTemplate,"image2");
		SpatialMetadata secondPortMetadata = new  SpatialMetadata();
		firstPortMetadata.setDescription("the second image");
		secondInputPort.setSpatialDescription(secondPortMetadata);
		secondInputPort.addParameter(secondInput);
		processTemplate.addInput(secondInputPort);
		
		InputPort percentInputPort = new InputPort(processTemplate,"percent");
		percentInputPort.addParameter(inputPercent);
		processTemplate.addInput(percentInputPort);
		
		OutputPort outputPort = new OutputPort(processTemplate,"output");
		SpatialMetadata outPortMetadata = new  SpatialMetadata();
		outPortMetadata.setDescription("blending images");
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
