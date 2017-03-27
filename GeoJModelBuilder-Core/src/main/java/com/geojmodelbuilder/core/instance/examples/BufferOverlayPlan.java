/**
 * Copyright (C) 2013 - 2016 Wuhan University,
 *                           Center for Geographic Analysis, Harvard University
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
 * @author Mingda Zhang
 *
 */
public class BufferOverlayPlan {

	private static IProcessInstance bufferProcess(){
		WPSProcess bufferProcess = new WPSProcess("GeoBufferProcess");
		bufferProcess.setWPSUrl("http://geos.whu.edu.cn:8080/wps10/WebProcessingService");
//		bufferProcess.parseProcessDescriptionType();
		
		IInputParameter inputWidth = bufferProcess.getInput("Width");
		LiteralData width = new LiteralData();
		width.setValue("15000");
		inputWidth.setData(width);
		
		IInputParameter fileInput = bufferProcess.getInput("InputData");
		ReferenceData referenceData = new ReferenceData();
		referenceData.setReference("http://202.114.118.181:8080/wps10/datas/longrive.zip");
		referenceData.setMimeType("application/x-zipped-shp");
		fileInput.setData(referenceData);
		
		IOutputParameter output = bufferProcess.getOutput("OutputData");
		ReferenceData outputRef = new ReferenceData();
		outputRef.setMimeType("application/x-zipped-shp");
		output.setData(outputRef);
		return bufferProcess;
	}
	
	private static IProcessInstance overlayProcess(){
		WPSProcess process = new WPSProcess("GeoOverlayProcess");
		process.setWPSUrl("http://geos.whu.edu.cn:8080/wps10/WebProcessingService");
//		process.parseProcessDescriptionType();
		
		IInputParameter inputOperator = process.getInput("Operator");
		LiteralData width = new LiteralData();
		width.setValue("and");
		inputOperator.setData(width);
		
		IInputParameter firstInput = process.getInput("FirstInputData");
		ReferenceData firstRefData = new ReferenceData();
		firstRefData.setReference("http://202.114.118.181:8080/wps10/datas/sanxia.zip");
		firstRefData.setMimeType("application/x-zipped-shp");
		firstInput.setData(firstRefData);
		
		IInputParameter secondInput =process.getInput("SecondInputData");
		ReferenceData secondRefData = new ReferenceData();
		secondRefData.setMimeType("application/x-zipped-shp");
		secondInput.setData(secondRefData);
		
		IOutputParameter output = process.getOutput("OutputData");
		ReferenceData outputRef = new ReferenceData();
		outputRef.setMimeType("application/x-zipped-shp");
		output.setData(outputRef);
		return process;
	}
	public IWorkflowInstance getWorkflow() {
		WorkflowInstance workflowExec = new WorkflowInstance();
		IProcessInstance bufferInstance = bufferProcess();
		IProcessInstance overlayPorcessExec = overlayProcess();
		DataFlowImpl dataflow = new DataFlowImpl(bufferInstance, bufferInstance.getOutput("OutputData"), overlayPorcessExec, overlayPorcessExec.getInput("SecondInputData"));
		bufferInstance.addLink(dataflow);
		overlayPorcessExec.addLink(dataflow);
		
		workflowExec.addProcess(bufferInstance);
		workflowExec.addProcess(overlayPorcessExec);
		workflowExec.setID("BufferOverlayWorkflowInstance");
		return workflowExec;
	}
}
