package com.geojmodelbuilder.xml.serialization;

import java.io.File;
import java.io.IOException;

import org.apache.xmlbeans.XmlOptions;

import cn.edu.whu.geos.wls.x10.ProcessBindingType;
import cn.edu.whu.geos.wls.x10.ResourceBindingType;
import cn.edu.whu.geos.wls.x10.WorkflowBindingDocument;
import cn.edu.whu.geos.wls.x10.WorkflowBindingType;

import com.geojmodelbuilder.core.instance.IInputParameter;
import com.geojmodelbuilder.core.instance.IOutputParameter;
import com.geojmodelbuilder.core.instance.IProcessInstance;
import com.geojmodelbuilder.core.instance.IWorkflowInstance;
import com.geojmodelbuilder.core.template.IInputPort;
import com.geojmodelbuilder.core.template.IOutPutPort;
import com.geojmodelbuilder.core.template.IProcessTemplate;
import com.geojmodelbuilder.core.template.IWorkflowTemplate;
import com.geojmodelbuilder.xml.util.UtilFactory;

public class Binding2XML {
	private XmlOptions xmlOptions = new XmlOptions();
	private IWorkflowTemplate workflowTemplate;
	private WorkflowBindingDocument document;
	
	@SuppressWarnings("rawtypes")
	public Binding2XML(IWorkflowTemplate workflowTemplate){
		this.workflowTemplate = workflowTemplate;
		xmlOptions.setUseDefaultNamespace();
		xmlOptions.setSaveSuggestedPrefixes(UtilFactory.NSMap());
		xmlOptions.setSavePrettyPrint();
		xmlOptions.setSaveAggressiveNamespaces();
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
		
		document = WorkflowBindingDocument.Factory.newInstance();
		WorkflowBindingType workflowBindingType = document.addNewWorkflowBinding();
		
		for(int i =0;i<workflowTemplate.getInstances().size();i++){
			
			IWorkflowInstance workflowInstance = workflowTemplate.getInstances().get(i);
			
			workflowBindingType.setFrom(workflowTemplate.getID());
			workflowBindingType.setTo(workflowInstance.getID());
			
			for(IProcessTemplate processTemplate:this.workflowTemplate.getProcesses()){
				if(!processBinding(processTemplate, workflowBindingType, i))
					return false;
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
