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
import com.geojmodelbuilder.core.instance.IInputParameter;
import com.geojmodelbuilder.core.instance.IOutputParameter;
import com.geojmodelbuilder.core.resource.ogc.wps.WPSProcess;
/**
 * 
 * @author Mingda Zhang
 *
 */
public class WPSProcessExecution_LiteralOutput {
	public static void main(String[] args){
		WPSProcess lengthProcess = new WPSProcess("CGAlgorithmsService-length");
		lengthProcess.setWPSUrl("http://geos.whu.edu.cn:8080/wps10/WebProcessingService");
//		lengthProcess.parseProcessDescriptionType();
		
		IInputParameter pointsParameter = lengthProcess.getInput("points");
		LiteralData width = new LiteralData();
		width.setValue("100,0,200,100,300,200");
		pointsParameter.setData(width);
		
		boolean executed = lengthProcess.canExecute();
		if(!executed){
			String errInfo = lengthProcess.getErrInfo();
			System.out.println(errInfo);
			return;
		}
		
		executed = 	lengthProcess.execute();
		if(!executed){
			String errInfo = lengthProcess.getErrInfo();
			System.out.println(errInfo);
			return;
		}
		IOutputParameter output = lengthProcess.getOutput("OutputData");
		IData data = output.getData();
		System.out.println("output:"+data.getValue());
	}
}
