package com.geojmodelbuilder.xml.serialization;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.opengis.ows.x20.CodeType;
import net.opengis.ows.x20.LanguageStringType;
import net.opengis.wps.x20.DataDocument.Data;
import net.opengis.wps.x20.DataInputType;
import net.opengis.wps.x20.DataTransmissionModeType;
import net.opengis.wps.x20.OutputDefinitionType;
import net.opengis.wps.x20.ReferenceType;

import org.apache.xmlbeans.XmlCursor;
import org.apache.xmlbeans.XmlOptions;

import cn.edu.whu.geos.wls.x10.ExtendedLinkType;
import cn.edu.whu.geos.wls.x10.ProcessInstanceType;
import cn.edu.whu.geos.wls.x10.ProcessInstanceType.ExecType;
import cn.edu.whu.geos.wls.x10.WPSEnvDocument;
import cn.edu.whu.geos.wls.x10.WPSEnvDocument.WPSEnv;
import cn.edu.whu.geos.wls.x10.WPSEnvDocument.WPSEnv.Mode;
import cn.edu.whu.geos.wls.x10.WorkflowInstanceDocument;
import cn.edu.whu.geos.wls.x10.WorkflowInstanceType;

import com.geojmodelbuilder.core.IDataFlow;
import com.geojmodelbuilder.core.IExchange;
import com.geojmodelbuilder.core.ILink;
import com.geojmodelbuilder.core.IProcess;
import com.geojmodelbuilder.core.data.IComplexData;
import com.geojmodelbuilder.core.data.IData;
import com.geojmodelbuilder.core.data.ILiteralData;
import com.geojmodelbuilder.core.instance.IInputParameter;
import com.geojmodelbuilder.core.instance.IOutputParameter;
import com.geojmodelbuilder.core.instance.IProcessInstance;
import com.geojmodelbuilder.core.instance.IWorkflowInstance;
import com.geojmodelbuilder.core.resource.ogc.wps.WPSProcess;
import com.geojmodelbuilder.core.utils.IDGenerator;
import com.geojmodelbuilder.core.utils.ValidateUtil;

public class Instance2XML {
	
	private IWorkflowInstance workflowInstance;
	private XmlOptions xmlOptions = new XmlOptions();
	
	//save the WPS execution environments, key is GetCapabilities url
	private Map<String, WPSEnvDocument> envDocMap = new HashMap<String, WPSEnvDocument>();
	private WorkflowInstanceDocument document;
	
	@SuppressWarnings("rawtypes")
	public Instance2XML(IWorkflowInstance workflowInstance){
		this.workflowInstance = workflowInstance;
		xmlOptions.setUseDefaultNamespace();
		Map nsMap = new HashMap(); 
		nsMap.put("http://geos.whu.edu.cn/wls/1.0","wls");
		nsMap.put("http://www.opengis.net/wps/2.0","wps");
		nsMap.put( "http://www.opengis.net/ows/2.0","ows");
		nsMap.put("http://www.w3.org/1999/xlink","xlink");
		xmlOptions.setSaveSuggestedPrefixes(nsMap);
		xmlOptions.setSavePrettyPrint();
		xmlOptions.setSaveAggressiveNamespaces();
	}
	
	private void addProcess(WorkflowInstanceType workflowInstanceType,WPSProcess wpsProcess){
		ProcessInstanceType processType = workflowInstanceType.addNewProcessInstance();
		processType.setExecType(ExecType.Enum.forString("OGC_WPS"));
		
		String processid = wpsProcess.getID();
		CodeType processIdType =  processType.addNewIdentifier();
		if(ValidateUtil.isStrEmpty(processid))
			processid = IDGenerator.uuid();
			
		processIdType.setStringValue(processid);
		processType.setName(wpsProcess.getName());
		
		WPSEnvDocument wpsEnvDoc = this.getEnv(wpsProcess.getWPSUrl());
        processType.setExecEnv(wpsEnvDoc.getWPSEnv());
        
        for(IInputParameter inputParam:wpsProcess.getInputs()){
        	this.addInput(processType, inputParam);
        }
        
        for(IOutputParameter outputParam:wpsProcess.getOutputs()){
        	this.addOutput(processType, outputParam);
        }
	}
	
	private void addOutput(ProcessInstanceType processType, IOutputParameter outputParam)
	{
		OutputDefinitionType output = processType.addNewOutput();
        output.setId(outputParam.getName());
        
        IData data = outputParam.getData();
        
        //only have two value{reference, value}
        String trass = "reference";
        if(data instanceof ILiteralData)
        	trass = "value";
        output.setTransmission(DataTransmissionModeType.Enum.forString(trass));
	}
	private void addInput(ProcessInstanceType processType, IInputParameter inputParam){
		//ComplexData
        DataInputType input = processType.addNewInput();
        input.setId(inputParam.getName());
        
        IData data = inputParam.getData();
        if(data instanceof IComplexData){
        	ReferenceType refType = input.addNewReference();
        	if(!ValidateUtil.isStrEmpty(data.getType()))
        		refType.setMimeType(data.getType());
        	
        	Object value = data.getValue();
        	if(value!=null)
        		refType.setHref(value.toString());
        }else {
            Data dataType = input.addNewData();
            XmlCursor xmlCursor = dataType.newCursor();
            xmlCursor.setTextValue(data.getValue().toString());
		}
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
	
	private boolean buildDoc(){
		if(this.document !=null)
			return true;
		
		document = WorkflowInstanceDocument.Factory.newInstance();
		WorkflowInstanceType workflowInstanceType = document.addNewWorkflowInstance();
		
		//set the title
		String title = this.workflowInstance.getName();
		if(title != null){
			LanguageStringType titleType = workflowInstanceType.insertNewTitle(0);
			titleType.setStringValue(title);
		}
		
		//set the abstract
		String description = this.workflowInstance.getDescription();
		if(description!=null){
			LanguageStringType abstractType = workflowInstanceType.insertNewAbstract(0);
			abstractType.setStringValue(description);
		}
		
		//set the identifier
		String id = this.workflowInstance.getID();
		if(id!= null){
			CodeType identifier = workflowInstanceType.addNewIdentifier();
			identifier.setStringValue(id);
		}
		
		for(IProcessInstance processIns : this.workflowInstance.getProcesses()){
			//only support WPS for now
			if(!(processIns instanceof WPSProcess))
				continue;
			
			WPSProcess wpsProcess = (WPSProcess)processIns;
			this.addProcess(workflowInstanceType, wpsProcess);
		}
		
		for(IProcessInstance process:this.workflowInstance.getProcesses()){
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
        
        this.envDocMap.put(url, wpsEnvDoc);
        
        return  wpsEnvDoc;
	}
}
