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
package com.geojmodelbuilder.engine;

import com.geojmodelbuilder.core.data.impl.LiteralData;
import com.geojmodelbuilder.core.data.impl.ReferenceData;
import com.geojmodelbuilder.core.impl.DataFlowImpl;
import com.geojmodelbuilder.core.impl.WorkflowImpl;
import com.geojmodelbuilder.core.plan.IInputParameter;
import com.geojmodelbuilder.core.plan.IOutputParameter;
import com.geojmodelbuilder.core.plan.IProcessExec;
import com.geojmodelbuilder.core.plan.impl.InputParameter;
import com.geojmodelbuilder.core.resource.ogc.wps.WPSProcess;
import com.geojmodelbuilder.engine.impl.RecorderImpl;
import com.geojmodelbuilder.engine.impl.WorkflowEngine;
import com.geojmodelbuilder.engine.impl.WorkflowExecutor;
/**
 * 
 * @author Mingda Zhang
 *
 */
public class WorkflowExectuion_WaterExtraction {

	private static IProcessExec mapcalcProcess(String input1,String input2){
		WPSProcess mapcalcProcess = new WPSProcess("RasterMapcalcProcess");
		mapcalcProcess.setWPSUrl("http://geos.whu.edu.cn:8080/wps10/WebProcessingService");
//		mapcalcProcess.parseProcessDescriptionType();
		
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
		
		IOutputParameter output = mapcalcProcess.getOuput("OutputData");
		ReferenceData outputRef = new ReferenceData();
		outputRef.setMimeType("application/geotiff");
		output.setData(outputRef);
		return mapcalcProcess;
	}
	
	private static IProcessExec colorsProcess(){
		WPSProcess colorsProcess = new WPSProcess("RasterColorsProcess");
		colorsProcess.setWPSUrl("http://geos.whu.edu.cn:8080/wps10/WebProcessingService");
//		colorsProcess.parseProcessDescriptionType();
		
		IInputParameter inputColor = colorsProcess.getInput("Color");
		LiteralData width = new LiteralData();
		width.setValue("random");
		inputColor.setData(width);
		
		IInputParameter fileInput = colorsProcess.getInput("InputData");
		ReferenceData referenceData = new ReferenceData();
		referenceData.setMimeType("application/geotiff");
		fileInput.setData(referenceData);
		
		IOutputParameter output = colorsProcess.getOuput("OutputData");
		ReferenceData outputRef = new ReferenceData();
		outputRef.setMimeType("application/geotiff");
		output.setData(outputRef);
		return colorsProcess;
	}
	
	private static IProcessExec binaryProcess(){
		WPSProcess binaryProcess = new WPSProcess("RasterBinaryProcess");
		binaryProcess.setWPSUrl("http://geos.whu.edu.cn:8080/wps10/WebProcessingService");
//		binaryProcess.parseProcessDescriptionType();
		
		IInputParameter firstInput = binaryProcess.getInput("InputData");
		ReferenceData referenceData = new ReferenceData();
		referenceData.setMimeType("application/geotiff");
		firstInput.setData(referenceData);
		
		IOutputParameter output = binaryProcess.getOuput("OutputData");
		ReferenceData outputRef = new ReferenceData();
		outputRef.setMimeType("application/geotiff");
		output.setData(outputRef);
		return binaryProcess;
	}
	
	private static IProcessExec blendProcess(){
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
		
		IOutputParameter output = process.getOuput("OutputData");
		ReferenceData outputRef = new ReferenceData();
		outputRef.setMimeType("application/geotiff");
		output.setData(outputRef);
		return process;
	}
	public static void main(String[] args) {
		
		//There are eight processes.
		IProcessExec mapcalcProcess1 = mapcalcProcess("http://geos.whu.edu.cn:8080/datas/MOD09A1_20100619_band1.tif", "http://geos.whu.edu.cn:8080/datas/MOD09A1_20100619_band2.tif");
		IProcessExec mapcalcProcess2 = mapcalcProcess("http://geos.whu.edu.cn:8080/datas/MOD09A1_20100603_band1.tif", "http://geos.whu.edu.cn:8080/datas/MOD09A1_20100603_band2.tif");
		
		IProcessExec binaryProcess1 = binaryProcess();
		IProcessExec binaryProcess2 = binaryProcess();
		
		IProcessExec colorsProcess1 = colorsProcess();
		IProcessExec colorsProcess2 = colorsProcess();
		
		IProcessExec blendPorcess = blendProcess();
		
		IProcessExec colorsProcess3 = colorsProcess();
		
		//workflow
		WorkflowImpl workflowExec = new WorkflowImpl();
		workflowExec.addProcess(mapcalcProcess1);
		workflowExec.addProcess(mapcalcProcess2);
		workflowExec.addProcess(binaryProcess1);
		workflowExec.addProcess(binaryProcess2);
		workflowExec.addProcess(colorsProcess1);
		workflowExec.addProcess(colorsProcess2);
		workflowExec.addProcess(blendPorcess);
		workflowExec.addProcess(colorsProcess3);
		
		//data flows between processes
		DataFlowImpl mapcalcBinaryFlow1 = new DataFlowImpl(mapcalcProcess1, mapcalcProcess1.getOuput("OutputData"), binaryProcess1, binaryProcess1.getInput("InputData"));
		mapcalcProcess1.addLink(mapcalcBinaryFlow1);
		binaryProcess1.addLink(mapcalcBinaryFlow1);
		
		DataFlowImpl mapcalcBinaryFlow2 = new DataFlowImpl(mapcalcProcess2, mapcalcProcess2.getOuput("OutputData"), binaryProcess2, binaryProcess2.getInput("InputData"));
		mapcalcProcess2.addLink(mapcalcBinaryFlow2);
		binaryProcess2.addLink(mapcalcBinaryFlow2);
		
		DataFlowImpl binaryColorLink1 = new DataFlowImpl(binaryProcess1, binaryProcess1.getOuput("OutputData"), colorsProcess1, colorsProcess1.getInput("InputData"));
		binaryProcess1.addLink(binaryColorLink1);
		colorsProcess1.addLink(binaryColorLink1);
		
		DataFlowImpl binaryColorLink2 = new DataFlowImpl(binaryProcess2, binaryProcess2.getOuput("OutputData"), colorsProcess2, colorsProcess2.getInput("InputData"));
		binaryProcess2.addLink(binaryColorLink2);
		colorsProcess2.addLink(binaryColorLink2);
		
		DataFlowImpl colorBlendFlow1 = new DataFlowImpl(colorsProcess1, colorsProcess1.getOuput("OutputData"), blendPorcess, blendPorcess.getInput("FirstInputData"));
		colorsProcess1.addLink(colorBlendFlow1);
		blendPorcess.addLink(colorBlendFlow1);
		
		DataFlowImpl colorBlendFlow2 = new DataFlowImpl(colorsProcess2, colorsProcess2.getOuput("OutputData"), blendPorcess, blendPorcess.getInput("SecondInputData"));
		colorsProcess2.addLink(colorBlendFlow2);
		blendPorcess.addLink(colorBlendFlow2);
		
		DataFlowImpl blendColorFlow = new DataFlowImpl(blendPorcess, blendPorcess.getOuput("OutputData"), colorsProcess3, colorsProcess3.getInput("InputData"));
		blendPorcess.addLink(blendColorFlow);
		colorsProcess3.addLink(blendColorFlow);
		
		//workflow execution
		/*WorkflowEngine engine = new WorkflowEngine(workflowExec,new RecorderImpl());
		engine.execute();*/
		
		//Using the executor
		WorkflowExecutor executor = new WorkflowExecutor(workflowExec);
		executor.run();
	}

}
