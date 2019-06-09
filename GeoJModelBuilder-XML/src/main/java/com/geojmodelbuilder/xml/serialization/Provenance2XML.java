package com.geojmodelbuilder.xml.serialization;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.UUID;

import javax.xml.namespace.QName;

import org.apache.xmlbeans.XmlAnySimpleType;
import org.apache.xmlbeans.XmlOptions;
import org.w3.ns.prov.Association;
import org.w3.ns.prov.Derivation;
import org.w3.ns.prov.Generation;
import org.w3.ns.prov.IDRef;
import org.w3.ns.prov.Plan;
import org.w3.ns.prov.Usage;

import cn.edu.whu.geos.xpso.x10.DatasetType;
import cn.edu.whu.geos.xpso.x10.ProcessExecType;
import cn.edu.whu.geos.xpso.x10.WorkflowExecInfoDocument;
import cn.edu.whu.geos.xpso.x10.WorkflowExecInfoType;
import cn.edu.whu.geos.xpso.x10.WorkflowExecType;
import cn.edu.whu.geos.xpso.x10.WorkflowExecutionDocument;

import com.geojmodelbuilder.core.IProcess;
import com.geojmodelbuilder.core.data.IData;
import com.geojmodelbuilder.core.instance.IInputParameter;
import com.geojmodelbuilder.core.instance.IOutputParameter;
import com.geojmodelbuilder.core.instance.IWorkflowInstance;
import com.geojmodelbuilder.core.provenance.IProcessProv;
import com.geojmodelbuilder.core.provenance.IWorkflowProv;
import com.geojmodelbuilder.xml.util.UtilFactory;

public class Provenance2XML {
	private XmlOptions xmlOptions = new XmlOptions();
	private IWorkflowProv workflowProv;
	private WorkflowExecInfoDocument document;
	private IWorkflowInstance workflowInst;
	
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
	
	private boolean buildDoc(){
		if(this.document !=null)
			return true;
		
		document = WorkflowExecInfoDocument.Factory.newInstance();
		WorkflowExecInfoType execInfoType = document.addNewWorkflowExecInfo();
		
		String workflowProvId = this.workflowProv.getID();
		//set the id of this provenance
		execInfoType.setId(new QName(workflowProvId));
		
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
		
		String workflowExecId = UUID.randomUUID().toString();
		workflowExecType.setId(new QName(workflowExecId));
		
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
			
			if(!processProv.getStatus()){
				String errInfo = processProv.getErrInfo();
				processExecType.addNewLabel().setStringValue(errInfo);
			}
			
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
		
		

		Derivation derivation = execInfoType.addNewWasDerivedFrom();
		derivation.addNewActivity().setRef(new QName(workflowExecId));
		derivation.addNewGeneratedEntity().setRef(new QName(workflowProvId));
		
		//record the workflow plan.
		if(this.workflowInst!=null){
		
			
			Instance2XML instance2xml = new Instance2XML(this.workflowInst);
			WorkflowExecutionDocument workflowInstDoc = instance2xml.getWorkflowInstanceDocument();
			instance2xml.getWorkflowInstanceDocument();
			
			// need to be modified
			execInfoType.setWorkflowInstance(null);
//			execInfoType.setworkf
			
			String workflowInstId = this.workflowInst.getID();
			derivation.addNewUsedEntity().setRef(new QName(workflowInstId));
			//the workflow plan
						Plan workflowPlan = execInfoType.addNewPlan();
			workflowPlan.setId(new QName(workflowInstId));
			XmlAnySimpleType workflowLoc = workflowPlan.addNewLocation();
			workflowLoc.setStringValue(workflowInstId);

			Association accordance = execInfoType.addNewWasAssociatedWith();
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
				
				Association procAccordance = execInfoType.addNewWasAssociatedWith();
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
