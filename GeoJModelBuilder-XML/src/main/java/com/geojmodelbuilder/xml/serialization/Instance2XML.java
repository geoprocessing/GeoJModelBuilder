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

import cn.edu.whu.geos.xpso.x10.FunctionLinkType;
import cn.edu.whu.geos.xpso.x10.ProcessExecutionDocument;
import cn.edu.whu.geos.xpso.x10.ProcessExecutionType;
import cn.edu.whu.geos.xpso.x10.WPSEnvDocument;
import cn.edu.whu.geos.xpso.x10.WPSEnvDocument.WPSEnv;
import cn.edu.whu.geos.xpso.x10.WPSEnvDocument.WPSEnv.Mode;
import cn.edu.whu.geos.xpso.x10.WorkflowExecutionDocument;
import cn.edu.whu.geos.xpso.x10.WorkflowExecutionType;

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
import com.geojmodelbuilder.xml.util.UtilFactory;

public class Instance2XML {
	
	private IWorkflowInstance workflowInstance;
	private XmlOptions xmlOptions = new XmlOptions();
	
	//save the WPS execution environments, key is GetCapabilities url
	private Map<String, WPSEnvDocument> envDocMap = new HashMap<String, WPSEnvDocument>();
	private WorkflowExecutionDocument document;
	private Map<String, ProcessExecutionDocument> processDocMap = new HashMap<String, ProcessExecutionDocument>();
	
	public Instance2XML(IWorkflowInstance workflowInstance){
		this.workflowInstance = workflowInstance;
		xmlOptions.setUseDefaultNamespace();
		/*Map nsMap = new HashMap(); 
		nsMap.put("http://geos.whu.edu.cn/xpso/1.0","xpso");
		nsMap.put("http://www.opengis.net/wps/2.0","wps");
		nsMap.put( "http://www.opengis.net/ows/2.0","ows");
		nsMap.put("http://www.w3.org/1999/xlink","xlink");*/
		xmlOptions.setSaveSuggestedPrefixes(UtilFactory.NSMap());
		xmlOptions.setSavePrettyPrint();
		xmlOptions.setSaveAggressiveNamespaces();
	}
	
	private void addProcess(WorkflowExecutionType workflowInstanceType,WPSProcess wpsProcess){
		ProcessExecutionType processType = workflowInstanceType.addNewProcessExecution();

		String processid = wpsProcess.getID();
		String processName = wpsProcess.getName();
		
		CodeType processIdType =  processType.addNewIdentifier();
		if(ValidateUtil.isStrEmpty(processid))
			processid = IDGenerator.uuid();
			
		processIdType.setStringValue(processName);
		//processType.setIdentifier(wpsProcess.getName());
		//rocessType.set
		//ProcessExecutionDocument execDoc = ProcessExecutionDocument.Factory.newInstance();
		processType.setId(processid);
		
		WPSEnvDocument wpsEnvDoc = this.getEnv(wpsProcess.getWPSUrl());
        processType.setExecEnv(wpsEnvDoc.getWPSEnv());
        //wpsEnvDoc.getWPSEnv().setExecType("OGC_WPS");
        
        for(IInputParameter inputParam:wpsProcess.getInputs()){
        	this.addInput(processType, inputParam);
        }
        
        for(IOutputParameter outputParam:wpsProcess.getOutputs()){
        	this.addOutput(processType, outputParam);
        }
        
		ProcessExecutionDocument processDoc = ProcessExecutionDocument.Factory.newInstance();
		processDoc.setProcessExecution(processType);
		this.processDocMap.put(processid, processDoc);
	}
	
	private void addOutput(ProcessExecutionType processType, IOutputParameter outputParam)
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
	private void addInput(ProcessExecutionType processType, IInputParameter inputParam){
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
	
	public ProcessExecutionDocument getProcessDoc(String processId){
		buildDoc();
		return this.processDocMap.get(processId);
	}
	
	
	public XmlOptions getXmlOptions(){
		return this.xmlOptions;
	}
	
	private void addLink(ILink link, WorkflowExecutionType workflowInstance){
		if(!(link instanceof IDataFlow))
			return;
		
		FunctionLinkType linkType = workflowInstance.addNewLink();
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
	
	public WorkflowExecutionDocument getWorkflowInstanceDocument(){
		buildDoc();
		return this.document;
	}
	
	private boolean buildDoc(){
		if(this.document !=null)
			return true;
		
		document = WorkflowExecutionDocument.Factory.newInstance();
		WorkflowExecutionType workflowInstanceType = document.addNewWorkflowExecution();
		
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
        wpsenv.setExecType("OGC_WPS");
        
        this.envDocMap.put(url, wpsEnvDoc);
        
        return  wpsEnvDoc;
	}
}
