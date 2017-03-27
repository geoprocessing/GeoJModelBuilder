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
package com.gejmodelbuilder.core.wps;

import com.geojmodelbuilder.core.data.IData;
import com.geojmodelbuilder.core.data.impl.LiteralData;
import com.geojmodelbuilder.core.data.impl.ReferenceData;
import com.geojmodelbuilder.core.instance.IInputParameter;
import com.geojmodelbuilder.core.instance.IOutputParameter;
import com.geojmodelbuilder.core.resource.ogc.wps.WPSProcess;
/**
 * 
 * @author Mingda Zhang
 *
 */
public class WPSProcessExecution_ReferenceOutput {
	public static void main(String[] args){
		WPSProcess bufferProcess = new WPSProcess("GeoBufferProcess");
		bufferProcess.setWPSUrl("http://geos.whu.edu.cn:8080/wps10/WebProcessingService");
//		bufferProcess.parseProcessDescriptionType();
		
		IInputParameter inputWidth = bufferProcess.getInput("Width");
		LiteralData width = new LiteralData();
		width.setValue("150000");
		inputWidth.setData(width);
		
		IInputParameter fileInput = bufferProcess.getInput("InputData");
		ReferenceData referenceData = new ReferenceData();
		referenceData.setReference("http://202.114.118.181:8080/wps10/datas/longrive.zip");
		referenceData.setMimeType("application/x-zipped-shp");
		fileInput.setData(referenceData);
		
		boolean executed = bufferProcess.canExecute();
		if(!executed){
			String errInfo = bufferProcess.getErrInfo();
			System.out.println(errInfo);
			return;
		}
		
		executed = 	bufferProcess.execute();
		if(!executed){
			String errInfo = bufferProcess.getErrInfo();
			System.out.println(errInfo);
			return;
		}
		IOutputParameter output = bufferProcess.getOutput("OutputData");
		IData data = output.getData();
		System.out.println("output:"+data.getValue());
	}
}
