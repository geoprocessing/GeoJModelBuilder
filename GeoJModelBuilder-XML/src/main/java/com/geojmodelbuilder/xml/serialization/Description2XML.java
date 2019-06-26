package com.geojmodelbuilder.xml.serialization;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.opengis.ows.x20.CodeType;
import net.opengis.ows.x20.LanguageStringType;
import net.opengis.ows.x20.MetadataType;
import net.opengis.wps.x20.ComplexDataDocument;
import net.opengis.wps.x20.ComplexDataType;
import net.opengis.wps.x20.DataDescriptionType;
import net.opengis.wps.x20.FormatDocument.Format;
import net.opengis.wps.x20.LiteralDataDocument;
import net.opengis.wps.x20.LiteralDataType;
import net.opengis.wps.x20.OutputDescriptionType;

import org.apache.xmlbeans.XmlOptions;

import cn.edu.whu.geos.xpso.x10.ExtendedLinkType;
import cn.edu.whu.geos.xpso.x10.ProcessInstanceDocument;
import cn.edu.whu.geos.xpso.x10.ProcessInstanceType;
import cn.edu.whu.geos.xpso.x10.WPSEnvDocument;
import cn.edu.whu.geos.xpso.x10.WPSEnvDocument.WPSEnv;
import cn.edu.whu.geos.xpso.x10.WPSEnvDocument.WPSEnv.Mode;
import cn.edu.whu.geos.xpso.x10.WorkflowInstanceDocument;
import cn.edu.whu.geos.xpso.x10.WorkflowInstanceType;

import com.geojmodelbuilder.core.IDataFlow;
import com.geojmodelbuilder.core.IExchange;
import com.geojmodelbuilder.core.ILink;
import com.geojmodelbuilder.core.IProcess;
import com.geojmodelbuilder.core.desc.IInputParameterDesc;
import com.geojmodelbuilder.core.desc.IOutputParameterDesc;
import com.geojmodelbuilder.core.desc.IProcessDesc;
import com.geojmodelbuilder.core.desc.IWorkflowDesc;
import com.geojmodelbuilder.core.desc.WPSDataFormat;
import com.geojmodelbuilder.core.desc.WPSDataType;
import com.geojmodelbuilder.core.desc.impl.Metadata;
import com.geojmodelbuilder.core.impl.ExecEnv;
import com.geojmodelbuilder.core.utils.IDGenerator;
import com.geojmodelbuilder.core.utils.ValidateUtil;
import com.geojmodelbuilder.xml.util.UtilFactory;

public class Description2XML {
	
	private IWorkflowDesc workflowDesc;
	private XmlOptions xmlOptions = new XmlOptions();
	
	//save the WPS execution environments, key is GetCapabilities url
	private Map<String, WPSEnvDocument> envDocMap = new HashMap<String, WPSEnvDocument>();
	private WorkflowInstanceDocument document;
	private Map<String, ProcessInstanceDocument> processDocMap = new HashMap<String, ProcessInstanceDocument>();
	
	public Description2XML(IWorkflowDesc workflowDesc){
		this.workflowDesc = workflowDesc;
		xmlOptions.setUseDefaultNamespace();
		xmlOptions.setSaveSuggestedPrefixes(UtilFactory.NSMap());
		xmlOptions.setSavePrettyPrint();
		xmlOptions.setSaveAggressiveNamespaces();
	}
	
	private void addProcess(WorkflowInstanceType workflowInstanceType,IProcessDesc processDesc){
		ProcessInstanceType processType = workflowInstanceType.addNewProcessInstance();

		String processid = processDesc.getID();
		String processName = processDesc.getName();
		
		CodeType processIdType =  processType.addNewIdentifier();
		if(ValidateUtil.isStrEmpty(processid))
			processid = IDGenerator.uuid();
			
		processIdType.setStringValue(processName);
		//processType.setIdentifier(wpsProcess.getName());
		//rocessType.set
		//ProcessExecutionDocument execDoc = ProcessExecutionDocument.Factory.newInstance();
		processType.setId(processid);
		ExecEnv execEnv = processDesc.getExecEnv();
		if(execEnv instanceof com.geojmodelbuilder.core.impl.WPSEnv){
			com.geojmodelbuilder.core.impl.WPSEnv wpsEnv = (com.geojmodelbuilder.core.impl.WPSEnv)execEnv;
			WPSEnvDocument wpsEnvDoc = this.getEnv(wpsEnv.getUrl());
			processType.setExecEnv(wpsEnvDoc.getWPSEnv());
		}
        //wpsEnvDoc.getWPSEnv().setExecType("OGC_WPS");
        
        for(IInputParameterDesc inputParam:processDesc.getInputs()){
        	this.addInput(processType, inputParam);
        }
        
        for(IOutputParameterDesc outputParam:processDesc.getOutputs()){
        	this.addOutput(processType, outputParam);
        }
        
		ProcessInstanceDocument processDoc = ProcessInstanceDocument.Factory.newInstance();
		processDoc.setProcessInstance(processType);
		this.processDocMap.put(processid, processDoc);
	}
	
	private void addOutput(ProcessInstanceType processType, IOutputParameterDesc outputParam)
	{
		OutputDescriptionType output = processType.addNewOutput();
		String description = outputParam.getDescription();
		if(!ValidateUtil.isStrEmpty(description))
		{
			LanguageStringType descriptionType = output.addNewAbstract();
			descriptionType.setStringValue(description);
		}
		
		String name = outputParam.getName();
		if(!ValidateUtil.isStrEmpty(name)){
			CodeType nameType = output.addNewIdentifier();
			nameType.setStringValue(name);
		}
		
		String title = outputParam.getTitle();
		if(!ValidateUtil.isStrEmpty(title)){
			LanguageStringType titleType = output.addNewTitle();
			titleType.setStringValue(title);
		}
		
		List<Metadata> metadatas = outputParam.getMetas();
		if(metadatas!=null && metadatas.size()!=0){
			for (Metadata metadata:metadatas) {
				MetadataType metaType = output.addNewMetadata();
				metaType.setRole(metadata.getRole());
				metaType.setHref(metadata.getHref());
			}
		}
		
		WPSDataType dataType=outputParam.getDataType();
		
		//DataDescriptionType dataDescType = output.getDataDescription();
		
		/*LiteralDataDocument literalDataDocument = LiteralDataDocument.Factory.newInstance();
		LiteralDataType literalDataType = literalDataDocument.addNewLiteralData();*/
		
		DataDescriptionType dataDescType = null;
		if(dataType == WPSDataType.ComplexData){
			ComplexDataDocument complexDataDocument = ComplexDataDocument.Factory.newInstance();
			dataDescType = complexDataDocument.addNewComplexData();
		}else {
			LiteralDataDocument literalDataDocument = LiteralDataDocument.Factory.newInstance();
			dataDescType = literalDataDocument.addNewLiteralData();
		}
		
		List<WPSDataFormat> formats = outputParam.getFormats();
		for(WPSDataFormat format:formats){
			Format formatType = dataDescType.addNewFormat();
			formatType.setMimeType(format.getMimeType());
			formatType.setEncoding(format.getEncoding());
			if(format.isDefaultF())
				formatType.setDefault(true);;
		}
		output.setDataDescription(dataDescType);
	}
	private void addInput(ProcessInstanceType processType, IInputParameterDesc inputParam){
		//ComplexData
        
		OutputDescriptionType inputType = processType.addNewOutput();
		String description = inputParam.getDescription();
		if(!ValidateUtil.isStrEmpty(description))
		{
			LanguageStringType descriptionType = inputType.addNewAbstract();
			descriptionType.setStringValue(description);
		}
		
		String name = inputParam.getName();
		if(!ValidateUtil.isStrEmpty(name)){
			CodeType nameType = inputType.addNewIdentifier();
			nameType.setStringValue(name);
		}
		
		String title = inputParam.getTitle();
		if(!ValidateUtil.isStrEmpty(title)){
			LanguageStringType titleType = inputType.addNewTitle();
			titleType.setStringValue(title);
		}
		
		List<Metadata> metadatas = inputParam.getMetas();
		if(metadatas!=null && metadatas.size()!=0){
			for (Metadata metadata:metadatas) {
				MetadataType metaType = inputType.addNewMetadata();
				metaType.setRole(metadata.getRole());
				metaType.setHref(metadata.getHref());
			}
		}
		
		WPSDataType dataType=inputParam.getDataType();
		
		//DataDescriptionType dataDescType = inputType.addNewDataDescription();
		
		
		
		//DataDescriptionType dataDescType = null;
		if(dataType == WPSDataType.ComplexData){
			ComplexDataDocument complexDataDocument = ComplexDataDocument.Factory.newInstance();
			ComplexDataType dataDescType = complexDataDocument.addNewComplexData();
			inputType.setDataDescription(dataDescType);
//			dataDescType = complexDataDocument.getDataDescription();
		}else {
			LiteralDataDocument literalDataDocument = LiteralDataDocument.Factory.newInstance();
//			literalDataDocument.addNewDataDescription();
			LiteralDataType dataDescType = literalDataDocument.addNewLiteralData();
			inputType.setDataDescription(dataDescType);
		}
		
		DataDescriptionType dataDescType = inputType.getDataDescription();
		List<WPSDataFormat> formats = inputParam.getFormats();
		for(WPSDataFormat format:formats){
			Format formatType = dataDescType.addNewFormat();
			formatType.setMimeType(format.getMimeType());
			formatType.setEncoding(format.getEncoding());
			if(format.isDefaultF())
				formatType.setDefault(true);;
		}
		
	}
	
	public ProcessInstanceDocument getProcessDoc(String processId){
		buildDoc();
		return this.processDocMap.get(processId);
	}
	
	
	public XmlOptions getXmlOptions(){
		return this.xmlOptions;
	}
	
	private void addLink(ILink link, WorkflowInstanceType workflowInstance){
		if(!(link instanceof IDataFlow))
			return;
		
		ExtendedLinkType linkType = workflowInstance.addNewLink();
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
	
	public WorkflowInstanceDocument getWorkflowInstanceDocument(){
		buildDoc();
		return this.document;
	}
	
	private boolean buildDoc(){
		if(this.document !=null)
			return true;
		
		document = WorkflowInstanceDocument.Factory.newInstance();
		WorkflowInstanceType workflowInstanceType = document.addNewWorkflowInstance();
		
		//set the title
		String title = this.workflowDesc.getTitle();
		if(title != null){
			LanguageStringType titleType = workflowInstanceType.insertNewTitle(0);
			titleType.setStringValue(title);
		}
		
		//set the abstract
		String description = this.workflowDesc.getDescription();
		if(description!=null){
			LanguageStringType abstractType = workflowInstanceType.insertNewAbstract(0);
			abstractType.setStringValue(description);
		}
		
		//set the identifier
		String id = this.workflowDesc.getID();
		if(id!= null){
			workflowInstanceType.setId(id);
		}
		
		String name = this.workflowDesc.getName();
		if(!ValidateUtil.isStrEmpty(name)){
			CodeType identifier = workflowInstanceType.addNewIdentifier();
			identifier.setStringValue(name);
		}
		
		for(IProcessDesc processIns : this.workflowDesc.getProcesses()){
			this.addProcess(workflowInstanceType, processIns);
		}
		
		for(IProcessDesc process:this.workflowDesc.getProcesses()){
			List<ILink> links = process.getLinks();
			for(ILink link:links){
				if(link.getSourceProcess() == process){
					addLink(link, workflowInstanceType);
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
	
	
	//only consider the WPS 1.0 version
	private WPSEnvDocument getEnv(String url){
		
		if(this.envDocMap.containsKey(url))
			return this.envDocMap.get(url);
		
		WPSEnvDocument wpsEnvDoc = WPSEnvDocument.Factory.newInstance();
        WPSEnv wpsenv = wpsEnvDoc.addNewWPSEnv();
        
        //id is reserved for late use
        wpsenv.setId("");
        wpsenv.setVersion("1.0.0");
        wpsenv.setMode(Mode.Enum.forString("sync"));
        wpsenv.setURL(url);
        wpsenv.setExecType("OGC_WPS");
        
        this.envDocMap.put(url, wpsEnvDoc);
        
        return  wpsEnvDoc;
	}
}
