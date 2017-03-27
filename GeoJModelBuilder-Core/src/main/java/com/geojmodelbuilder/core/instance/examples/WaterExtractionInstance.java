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
package com.geojmodelbuilder.core.instance.examples;

import com.geojmodelbuilder.core.data.impl.LiteralData;
import com.geojmodelbuilder.core.data.impl.ReferenceData;
import com.geojmodelbuilder.core.impl.DataFlowImpl;
import com.geojmodelbuilder.core.instance.IInputParameter;
import com.geojmodelbuilder.core.instance.IOutputParameter;
import com.geojmodelbuilder.core.instance.IProcessInstance;
import com.geojmodelbuilder.core.instance.IWorkflowInstance;
import com.geojmodelbuilder.core.instance.impl.WorkflowInstance;
import com.geojmodelbuilder.core.resource.ogc.wps.WPSProcess;
/**
 * 
 * @author Mingda Zhang
 *
 */
public class WaterExtractionInstance {

	private IProcessInstance mapcalcProcess(String input1,String input2){
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
		return mapcalcProcess;
	}
	
	private  IProcessInstance colorsProcess(){
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
		return colorsProcess;
	}
	
	private  IProcessInstance binaryProcess(){
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
		return binaryProcess;
	}
	
	
	public IWorkflowInstance getWorkflow(){
		//There are eight processes.
		IProcessInstance mapcalcProcess1 = mapcalcProcess("http://geos.whu.edu.cn:8080/datas/MOD09A1_20100619_band1.tif", "http://geos.whu.edu.cn:8080/datas/MOD09A1_20100619_band2.tif");
		
		IProcessInstance binaryProcess1 = binaryProcess();
		
		IProcessInstance colorsProcess1 = colorsProcess();
		
		
		//workflow
		WorkflowInstance workflowExec = new WorkflowInstance();
		workflowExec.addProcess(mapcalcProcess1);
		workflowExec.addProcess(binaryProcess1);
		workflowExec.addProcess(colorsProcess1);
		
		//data flows between processes
		DataFlowImpl mapcalcBinaryFlow1 = new DataFlowImpl(mapcalcProcess1, mapcalcProcess1.getOutput("OutputData"), binaryProcess1, binaryProcess1.getInput("InputData"));
		mapcalcProcess1.addLink(mapcalcBinaryFlow1);
		binaryProcess1.addLink(mapcalcBinaryFlow1);
		
		
		DataFlowImpl binaryColorLink1 = new DataFlowImpl(binaryProcess1, binaryProcess1.getOutput("OutputData"), colorsProcess1, colorsProcess1.getInput("InputData"));
		binaryProcess1.addLink(binaryColorLink1);
		colorsProcess1.addLink(binaryColorLink1);
		
		workflowExec.setID("WaterExtractionWorkflowInstance");
		return workflowExec;
	} 

}
