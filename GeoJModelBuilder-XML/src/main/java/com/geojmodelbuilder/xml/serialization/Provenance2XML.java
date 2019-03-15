package com.geojmodelbuilder.xml.serialization;

import java.io.File;
import java.io.IOException;
import java.util.Date;

import javax.xml.namespace.QName;

import org.apache.xmlbeans.XmlAnySimpleType;
import org.apache.xmlbeans.XmlOptions;
import org.w3.ns.prov.Entity;
import org.w3.ns.prov.IDRef;

import cn.edu.whu.geos.wls.x10.DatasetType;
import cn.edu.whu.geos.wls.x10.Generation;
import cn.edu.whu.geos.wls.x10.ProcessBindingType;
import cn.edu.whu.geos.wls.x10.ProcessExecType;
import cn.edu.whu.geos.wls.x10.ResourceBindingType;
import cn.edu.whu.geos.wls.x10.Usage;
import cn.edu.whu.geos.wls.x10.Usage.AsVariable;
import cn.edu.whu.geos.wls.x10.WorkflowBindingType;
import cn.edu.whu.geos.wls.x10.WorkflowExecInfoDocument;
import cn.edu.whu.geos.wls.x10.WorkflowExecInfoType;
import cn.edu.whu.geos.wls.x10.WorkflowExecType;

import com.geojmodelbuilder.core.data.IData;
import com.geojmodelbuilder.core.instance.IInputParameter;
import com.geojmodelbuilder.core.instance.IOutputParameter;
import com.geojmodelbuilder.core.instance.IProcessInstance;
import com.geojmodelbuilder.core.provenance.IProcessProv;
import com.geojmodelbuilder.core.provenance.IWorkflowProv;
import com.geojmodelbuilder.core.template.IInputPort;
import com.geojmodelbuilder.core.template.IOutPutPort;
import com.geojmodelbuilder.core.template.IProcessTemplate;
import com.geojmodelbuilder.xml.util.UtilFactory;

public class Provenance2XML {
	private XmlOptions xmlOptions = new XmlOptions();
	private IWorkflowProv workflowProv;
	private WorkflowExecInfoDocument document;
	
	@SuppressWarnings("rawtypes")
	public Provenance2XML(IWorkflowProv workflowProv){
		this.workflowProv = workflowProv;
		this.xmlOptions = UtilFactory.XmlOptions();
	}
	
	public XmlOptions getXmlOptions(){
		return this.xmlOptions;
	}
	
	/**
	 * save to text
	 * @return
	 */
	public String xmlText(){
		buildDoc();
		return document.xmlText(xmlOptions);
	}
	
	private boolean processBinding(IProcessTemplate processTemplate,WorkflowBindingType workflowbinding,int order){
		
		ProcessBindingType processBindingType = workflowbinding.addNewProcessBinding();
		if(processTemplate.getInstances().size() <= order)
			return false;
		
		IProcessInstance processInstance = processTemplate.getInstances().get(order);
		processBindingType.setFrom(processTemplate.getID());
		processBindingType.setTo(processInstance.getID());
		
		for(IInputPort inputPort : processTemplate.getInputs()){
			if(inputPort.getInstances().size()<=order)
				return false;
			
			IInputParameter inputParameter = inputPort.getInstances().get(order);
			ResourceBindingType variableBindingType = processBindingType.addNewVariableBinding();
			variableBindingType.setFrom(inputPort.getName());
			variableBindingType.setTo(inputParameter.getName());
		}
		
		for(IOutPutPort outPutPort:processTemplate.getOutputs()){
			if(outPutPort.getInstances().size()<=order)
				return false;
			
			IOutputParameter outputParameter = outPutPort.getInstances().get(order);
			ResourceBindingType variableBindingType = processBindingType.addNewVariableBinding();
			variableBindingType.setFrom(outPutPort.getName());
			variableBindingType.setTo(outputParameter.getName());
		}
		
		return true;
	}
	
	private boolean buildDoc(){
		if(this.document !=null)
			return true;
		
		document = WorkflowExecInfoDocument.Factory.newInstance();
		WorkflowExecInfoType execInfoType = document.addNewWorkflowExecInfo();
		
		//set the id of this provenance
		Entity provMeta = execInfoType.addNewProvDoc();
		provMeta.setId(new QName(this.workflowProv.getID()));
		
		WorkflowExecType workflowExecType = execInfoType.addNewWorkflowExec();
		//start time
		Date startDate = this.workflowProv.getStartTime();
		if(startDate!=null)
				workflowExecType.setStartTime(UtilFactory.ToCalendar(startDate));
		
		//end time
		Date endDate = this.workflowProv.getEndTime();
		if(endDate!=null)
			workflowExecType.setEndTime(UtilFactory.ToCalendar(endDate));
		
		//whether success
		workflowExecType.setSuccessed(this.workflowProv.getStatus());
		
		for(IProcessProv processProv:this.workflowProv.getProcesses()){
			ProcessExecType processExecType = execInfoType.addNewProcessExec();
			Date startTime = processProv.getStartTime();
			if(startTime!=null)
				processExecType.setStartTime(UtilFactory.ToCalendar(startTime));
			
			Date endTime = processProv.getEndTime();
			if(endTime!=null)
				processExecType.setEndTime(UtilFactory.ToCalendar(endTime));
			
			processExecType.setSuccessed(processProv.getStatus());
			
			String processId = processProv.getID();
			processExecType.setId(new QName(processId));
			
			Usage usage = execInfoType.addNewUsed();
			IDRef activityId = usage.addNewActivity();
			activityId.setRef(new QName(processId));
			
			for(IInputParameter input:processProv.getInputs()){
				IData data = input.getData();
				if(data == null)
					continue;
				
				Object value = data.getValue();
				String inputName = input.getName();
				
				String dataid = processId+"#"+inputName;
				
				DatasetType datasetType = execInfoType.addNewDataset();
				XmlAnySimpleType simpleType = datasetType.addNewValue();
				datasetType.setId(new QName(dataid));
				
				simpleType.setStringValue(value.toString());
				IDRef entityRef = usage.addNewEntity();
				entityRef.setRef(new QName(dataid));
				AsVariable asVariable = usage.addNewAsVariable();
				asVariable.setDataID(dataid);
				asVariable.setParamID(inputName);
			}
			
			for(IOutputParameter output:processProv.getOutputs()){
				
				IData data = output.getData();
				if(data == null)
					continue;
				
				Generation generation = execInfoType.addNewWasGeneratedBy();
				
				Object value = data.getValue();
				String outputName = output.getName();
				
				String dataid = processId+"#"+outputName;
				
				DatasetType datasetType = execInfoType.addNewDataset();
				XmlAnySimpleType simpleType = datasetType.addNewValue();
				datasetType.setId(new QName(dataid));
				simpleType.setStringValue(value.toString());
				
				IDRef activityRef = generation.addNewActivity();
				activityRef.setRef(new QName(processId));
				
				IDRef entityRef = generation.addNewEntity();
				entityRef.setRef(new QName(dataid));
				Generation.AsVariable asVariable = generation.addNewAsVariable();
				asVariable.setDataID(dataid);
				asVariable.setParamID(outputName);
			}
		}
		
		
		return true;
	}
	
	/*
	 * save to file 
	 */
	public boolean save(String filePath)
	{
		if(!buildDoc())
			return false;
		
		try {
			document.save(new File(filePath),xmlOptions);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return true;
	}

}
