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
package com.geojmodelbuilder.core.provenance.examples;

import java.util.List;

import org.apache.xmlbeans.XmlDateTime;
import org.apache.xmlbeans.impl.values.XmlDateTimeImpl;

import com.geojmodelbuilder.core.data.impl.ComplexData;
import com.geojmodelbuilder.core.instance.IOutputParameter;
import com.geojmodelbuilder.core.instance.IProcessInstance;
import com.geojmodelbuilder.core.instance.IWorkflowInstance;
import com.geojmodelbuilder.core.instance.examples.WaterExtractionInstance;
import com.geojmodelbuilder.core.provenance.IWorkflowProv;
import com.geojmodelbuilder.core.provenance.impl.ProcessProv;
import com.geojmodelbuilder.core.provenance.impl.WorkflowProv;

/**
 * @author Mingda Zhang
 *
 */
public class WaterExtractionProv {
	
	public IWorkflowProv getWorkflow(){
		IWorkflowInstance workflowInstance = new WaterExtractionInstance().getWorkflow();
		WorkflowProv workflowTrace = new WorkflowProv(workflowInstance);
		
		workflowTrace.setName("Workflow trace");
		
		String overallStart = "2017-03-02T14:57:36.180-05:00";
		String overallEnd = "2017-03-02T14:57:38.074-05:00";
		
		String[] starts = {"2017-03-02T14:57:36.180-05:00","2017-03-02T14:57:36.974-05:00","2017-03-02T14:57:37.548-05:00"};
		String[] ends = {"2017-03-02T14:57:36.974-05:00","2017-03-02T14:57:37.534-05:00","2017-03-02T14:57:38.074-05:00"};
		
		XmlDateTime dateTime = new XmlDateTimeImpl();
		dateTime.setStringValue(overallStart);
		workflowTrace.setStartTime(dateTime.getDateValue());
		
		dateTime.setStringValue(overallEnd);
		workflowTrace.setEndTime(dateTime.getDateValue());
		List<IProcessInstance> processInstances = workflowInstance.getProcesses();
		for(int i=0;i<processInstances.size();i++){
			IProcessInstance processInstance = processInstances.get(i);
			
			List<IOutputParameter> outputs = processInstance.getOutputs();
			for(IOutputParameter output:outputs){
				ComplexData complexData = new ComplexData();
				complexData.setMimeType("application/geotiff");
				complexData.setValue("http://geos.whu.edu.cn:8080/datas/temp/temp_"+i+".tif");
				output.setData(complexData);
			}
			
			ProcessProv processTrace = new ProcessProv(processInstance);
			
			dateTime.setStringValue(starts[i]);
			processTrace.setStartTime(dateTime.getDateValue());
			
			dateTime.setStringValue(ends[i]);
			processTrace.setEndTime(dateTime.getDateValue());
			
			workflowTrace.addProcess(processTrace);
		}
		return workflowTrace;
	}
}
