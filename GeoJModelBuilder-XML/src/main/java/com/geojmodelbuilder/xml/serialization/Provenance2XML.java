package com.geojmodelbuilder.xml.serialization;

import java.io.File;
import java.io.IOException;
import java.util.Date;

import javax.xml.namespace.QName;

import org.apache.xmlbeans.XmlAnySimpleType;
import org.apache.xmlbeans.XmlOptions;
import org.apache.xmlbeans.impl.values.XmlAnyTypeImpl;
import org.w3.ns.prov.Entity;
import org.w3.ns.prov.IDRef;
import org.w3.ns.prov.Plan;

import cn.edu.whu.geos.wls.x10.Accordance;
import cn.edu.whu.geos.wls.x10.DatasetType;
import cn.edu.whu.geos.wls.x10.Generation;
import cn.edu.whu.geos.wls.x10.ProcessBindingType;
import cn.edu.whu.geos.wls.x10.ProcessExecType;
import cn.edu.whu.geos.wls.x10.ResourceBindingType;
import cn.edu.whu.geos.wls.x10.Usage;
import cn.edu.whu.geos.wls.x10.WorkflowBindingType;
import cn.edu.whu.geos.wls.x10.WorkflowExecInfoDocument;
import cn.edu.whu.geos.wls.x10.WorkflowExecInfoType;
import cn.edu.whu.geos.wls.x10.WorkflowExecType;
import cn.edu.whu.geos.wls.x10.WorkflowInstanceDocument;

import com.geojmodelbuilder.core.IProcess;
import com.geojmodelbuilder.core.data.IData;
import com.geojmodelbuilder.core.instance.IInputParameter;
import com.geojmodelbuilder.core.instance.IOutputParameter;
import com.geojmodelbuilder.core.instance.IProcessInstance;
import com.geojmodelbuilder.core.instance.IWorkflowInstance;
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
	private IWorkflowInstance workflowInst;
	
	@SuppressWarnings("rawtypes")
	public Provenance2XML(IWorkflowProv workflowProv, IWorkflowInstance workflowInst){
		this.workflowProv = workflowProv;
		this.workflowInst = workflowInst;
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
			
			
			
			for(IInputParameter input:processProv.getInputs()){
				IData data = input.getData();
				if(data == null)
					continue;
				
				Usage usage = execInfoType.addNewUsed();
				IDRef activityId = usage.addNewActivity();
				activityId.setRef(new QName(processId));
				
				Object value = data.getValue();
				String inputName = input.getName();
				
				String dataid = processId+"#"+inputName;
				
				DatasetType datasetType = execInfoType.addNewDataset();
				XmlAnySimpleType simpleType = datasetType.addNewValue();
				datasetType.setId(new QName(dataid));
				
				simpleType.setStringValue(value.toString());
				IDRef entityRef = usage.addNewEntity();
				entityRef.setRef(new QName(dataid));
				usage.addNewRole().setStringValue(inputName);
				/*
				AsVariable asVariable = usage.addNewAsVariable();
				asVariable.setDataID(dataid);
				asVariable.setParamID(inputName);
				*/
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
				generation.addNewRole().setStringValue(outputName);
				/*
				Generation.AsVariable asVariable = generation.addNewAsVariable();
				asVariable.setDataID(dataid);
				asVariable.setParamID(outputName);
				*/
			}
		}
		
		//record the workflow plan.
		if(this.workflowInst!=null){
			Instance2XML instance2xml = new Instance2XML(this.workflowInst);
			WorkflowInstanceDocument workflowInstDoc = instance2xml.getWorkflowInstanceDocument();
			execInfoType.setWorkflowInstance(workflowInstDoc.getWorkflowInstance());
			
			String workflowInstId = this.workflowInst.getID();
			String workflowProvId = this.workflowProv.getID();

			//the workflow plan
			
			Plan workflowPlan = execInfoType.addNewPlan();
			workflowPlan.setId(new QName(workflowInstId));
			XmlAnySimpleType workflowLoc = workflowPlan.addNewLocation();
			workflowLoc.setStringValue(workflowInstId);

			Accordance accordance = execInfoType.addNewAccordingTo();
			accordance.addNewActivity().setRef(new QName(workflowInstId));
			accordance.addNewPlan().setRef(new QName(workflowProvId));
			
			
			for(IProcessProv processProv : this.workflowProv.getProcesses()){
				IProcess processInst = processProv.getProcess();
				String processProvId = processProv.getID();
				String processInstId = processInst.getID();
				
				Plan processPlan = execInfoType.addNewPlan();
				processPlan.setId(new QName(processInstId));
				XmlAnySimpleType processLoc = processPlan.addNewLocation();
				processLoc.setStringValue(workflowInstId+"/"+processInstId);
				
				Accordance procAccordance = execInfoType.addNewAccordingTo();
				procAccordance.addNewActivity().setRef(new QName(processProvId));
				procAccordance.addNewPlan().setRef(new QName(processInstId));
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
