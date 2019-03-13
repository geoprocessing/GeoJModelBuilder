package com.geojmodelbuilder.xml.serialization;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.opengis.ows.x20.CodeType;
import net.opengis.ows.x20.LanguageStringType;
import net.opengis.wps.x20.GenericInputType;
import net.opengis.wps.x20.GenericOutputType;
import net.opengis.wps.x20.GenericProcessDocument;
import net.opengis.wps.x20.GenericProcessType;

import org.apache.xmlbeans.XmlOptions;

import cn.edu.whu.geos.wls.x10.LinkType;
import cn.edu.whu.geos.wls.x10.WorkflowTemplateDocument;
import cn.edu.whu.geos.wls.x10.WorkflowTemplateType;

import com.geojmodelbuilder.core.IDataFlow;
import com.geojmodelbuilder.core.IExchange;
import com.geojmodelbuilder.core.ILink;
import com.geojmodelbuilder.core.IProcess;
import com.geojmodelbuilder.core.template.IInputPort;
import com.geojmodelbuilder.core.template.IOutPutPort;
import com.geojmodelbuilder.core.template.IProcessTemplate;
import com.geojmodelbuilder.core.template.IWorkflowTemplate;
import com.geojmodelbuilder.core.utils.IDGenerator;
import com.geojmodelbuilder.core.utils.ValidateUtil;
import com.geojmodelbuilder.xml.util.NSFactory;

public class Template2XML {

	
	private IWorkflowTemplate workflowTemplate;
	private XmlOptions xmlOptions = new XmlOptions();
	
	private WorkflowTemplateDocument document;
	private Map<String, GenericProcessDocument> processDocMap = new HashMap<String, GenericProcessDocument>();
	
	@SuppressWarnings("rawtypes")
	public Template2XML(IWorkflowTemplate workflowTemplate){
		this.workflowTemplate = workflowTemplate;
		xmlOptions.setUseDefaultNamespace();
		xmlOptions.setSaveSuggestedPrefixes(NSFactory.NSMap());
		xmlOptions.setSavePrettyPrint();
		xmlOptions.setSaveAggressiveNamespaces();
	}
	
	private void addProcess(WorkflowTemplateType workflowTemplateType,IProcessTemplate processTemplate){
		GenericProcessType processType = workflowTemplateType.addNewProcessTemplate();

		String processid = processTemplate.getID();
		CodeType processIdType =  processType.addNewIdentifier();
		if(ValidateUtil.isStrEmpty(processid))
			processid = IDGenerator.uuid();
			
		processIdType.setStringValue(processid);
		
		LanguageStringType titleType = processType.addNewTitle();
		titleType.setStringValue(processTemplate.getName());
		
		
        for(IInputPort inputParam:processTemplate.getInputs()){
        	this.addInput(processType, inputParam);
        }
        
        for(IOutPutPort outputParam:processTemplate.getOutputs()){
        	this.addOutput(processType, outputParam);
        }
         
        GenericProcessDocument processDoc = GenericProcessDocument.Factory.newInstance();
        processDoc.setGenericProcess(processType);
		this.processDocMap.put(processid, processDoc);
	}
	
	private void addOutput(GenericProcessType processType, IOutPutPort outputParam)
	{
		GenericOutputType output = processType.addNewOutput();
		CodeType idType = output.addNewIdentifier();
		idType.setStringValue(outputParam.getName());
	}
	private void addInput(GenericProcessType processType, IInputPort inputParam){
        GenericInputType input = processType.addNewInput();
        CodeType idType = input.addNewIdentifier();
        idType.setStringValue(inputParam.getName());
	}
	
	public GenericProcessDocument getProcessDoc(String processId){
		buildDoc();
		return this.processDocMap.get(processId);
	}
	
	
	public XmlOptions getXmlOptions(){
		return this.xmlOptions;
	}
	
	private void addLink(ILink link, WorkflowTemplateType workflowInstance){
		if(!(link instanceof IDataFlow))
			return;
		
		LinkType linkType = workflowInstance.addNewLink();
		IProcess srcProcess = link.getSourceProcess();
		IProcess tarProcess = link.getTargetProcess();
		IDataFlow dataflow = (IDataFlow)link;
		
		IExchange srcExchange = dataflow.getSourceExchange();
		IExchange tarExchange = dataflow.getTargetExchange();
		linkType.setSourceProcess(srcProcess.getID());
		linkType.setTargetProcess(tarProcess.getID());
		linkType.setSourceVariable(srcExchange.getName());
		linkType.setTargetVariable(tarExchange.getName());
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
		
		document = WorkflowTemplateDocument.Factory.newInstance();
		WorkflowTemplateType workflowTemplateType = document.addNewWorkflowTemplate();
		
		//set the title
		String title = this.workflowTemplate.getName();
		if(title != null){
			LanguageStringType titleType = workflowTemplateType.insertNewTitle(0);
			titleType.setStringValue(title);
		}
		
		//set the abstract
		String description = this.workflowTemplate.getDescription();
		if(description!=null){
			LanguageStringType abstractType = workflowTemplateType.insertNewAbstract(0);
			abstractType.setStringValue(description);
		}
		
		//set the identifier
		String id = this.workflowTemplate.getID();
		if(id!= null){
			CodeType identifier = workflowTemplateType.addNewIdentifier();
			identifier.setStringValue(id);
		}
		
		for(IProcessTemplate processTemplate : this.workflowTemplate.getProcesses()){
			
			this.addProcess(workflowTemplateType, processTemplate);
		}
		
		for(IProcessTemplate process:this.workflowTemplate.getProcesses()){
			List<ILink> links = process.getLinks();
			for(ILink link:links){
				if(link.getSourceProcess() == process){
					addLink(link, workflowTemplateType);
				}
			}
		}
		
		return true;
	}
	
	/*
	 * save to file 
	 */
	public boolean save(String filePath)
	{
		buildDoc();
		
		try {
			document.save(new File(filePath),xmlOptions);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return true;
	}

}
