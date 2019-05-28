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

import com.geojmodelbuilder.core.impl.DataFlowImpl;
import com.geojmodelbuilder.core.template.IProcessTemplate;
import com.geojmodelbuilder.core.template.impl.InputPort;
import com.geojmodelbuilder.core.template.impl.OutputPort;
import com.geojmodelbuilder.core.template.impl.ProcessTemplate;
import com.geojmodelbuilder.core.template.impl.WorkflowTemplate;
/**
 * 
 * @author Mingda Zhang
 *
 */
public class WaterExtractionToVector {
	
	private static WorkflowTemplate WflowTemplate;

	public static WorkflowTemplate Template(){
		if(WflowTemplate !=null)
			return WflowTemplate;
		
		WflowTemplate = new WorkflowTemplate();
		IProcessTemplate ndwiProc = NDWIProcess();
		IProcessTemplate clipProc = ImageClip();
		IProcessTemplate preProc = PreProcessing();
		IProcessTemplate binaryProc = BinaryProcess();
		IProcessTemplate r2vProc = RasterToVector();
		
		WflowTemplate.addProcess(ndwiProc);
		WflowTemplate.addProcess(clipProc);
		WflowTemplate.addProcess(preProc);
		WflowTemplate.addProcess(binaryProc);
		WflowTemplate.addProcess(r2vProc);
		
		DataFlowImpl pre_clip = new DataFlowImpl(preProc, preProc.getOutput("Ouput"), clipProc, clipProc.getInput("Image"));
		preProc.addLink(pre_clip);
		clipProc.addLink(pre_clip);
		
		DataFlowImpl clip_ndwi = new DataFlowImpl(clipProc, clipProc.getOutput("ClippedImage"), ndwiProc, ndwiProc.getInput("Image"));
		clipProc.addLink(clip_ndwi);
		ndwiProc.addLink(clip_ndwi);
		
		DataFlowImpl ndwi_binary = new DataFlowImpl(ndwiProc, ndwiProc.getOutput("NDWIOutput"), binaryProc, binaryProc.getInput("Image"));
		ndwiProc.addLink(ndwi_binary);
		binaryProc.addLink(ndwi_binary);
		
		DataFlowImpl binary_r2v = new DataFlowImpl(binaryProc, binaryProc.getOutput("BinariedOutput"), r2vProc, r2vProc.getInput("RasterInput"));
		binaryProc.addLink(binary_r2v);
		r2vProc.addLink(binary_r2v);
		
		return WflowTemplate;
	}
	
	
	private static IProcessTemplate NDWIProcess(){
		ProcessTemplate processTemplate = new ProcessTemplate("NDWICalculation");
		processTemplate.setDescription("Normalized Difference Water Index (NDWI) calculation");
		
		InputPort inputPort = new InputPort(processTemplate,"Image");
		processTemplate.addInput(inputPort);
		
		InputPort firstInputPort = new InputPort(processTemplate,"Green_Order");
		processTemplate.addInput(firstInputPort);
		
		InputPort secondInputPort = new InputPort(processTemplate,"NIR_Order");
		processTemplate.addInput(secondInputPort);
		
		OutputPort outputPort = new OutputPort(processTemplate,"NDWIOutput");
		processTemplate.addOutput(outputPort);
		
		return processTemplate;
	}
	
	
	private static IProcessTemplate BinaryProcess(){
		ProcessTemplate processTemplate = new ProcessTemplate("BinaryProcess");
		processTemplate.setDescription("Binarization");
		
		InputPort inputPort = new InputPort(processTemplate,"Image");
		processTemplate.addInput(inputPort);
		
		InputPort threshold = new InputPort(processTemplate,"Threshold");
		processTemplate.addInput(threshold);
		
		OutputPort outputPort = new OutputPort(processTemplate,"BinariedOutput");
		processTemplate.addOutput(outputPort);
		
		return processTemplate;
	}
	
	private static IProcessTemplate RasterToVector(){
		ProcessTemplate processTemplate = new ProcessTemplate("Raster2Vector");
		processTemplate.setDescription("Transfer the raster data to vector");
		
		InputPort inputPort = new InputPort(processTemplate,"RasterInput");
		processTemplate.addInput(inputPort);
		
		OutputPort outputPort = new OutputPort(processTemplate,"VectorOutput");
		processTemplate.addOutput(outputPort);
		
		return processTemplate;
	}
	
	private static IProcessTemplate ImageClip(){
		ProcessTemplate processTemplate = new ProcessTemplate("ImageClip");
		processTemplate.setDescription("Image clipping");
		
		InputPort firstInputPort = new InputPort(processTemplate,"Image");
		processTemplate.addInput(firstInputPort);
		
		InputPort secondInputPort = new InputPort(processTemplate,"AOI");
		processTemplate.addInput(secondInputPort);
		
		OutputPort outputPort = new OutputPort(processTemplate,"ClippedImage");
		processTemplate.addOutput(outputPort);
		
		return processTemplate;
	}
	
	private static IProcessTemplate PreProcessing(){
		ProcessTemplate processTemplate = new ProcessTemplate("Preprocessing");
		processTemplate.setDescription("Image preprocessing");
		
		InputPort firstInputPort = new InputPort(processTemplate,"OriginImage");
		processTemplate.addInput(firstInputPort);
		
		OutputPort outputPort = new OutputPort(processTemplate,"Ouput");
		processTemplate.addOutput(outputPort);
		
		return processTemplate;
	}
}
