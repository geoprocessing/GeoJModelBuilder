package com.geojmodelbuilder.xml.deserialization;

import java.io.File;

import net.opengis.ows.x20.CodeType;
import net.opengis.ows.x20.LanguageStringType;
import net.opengis.ows.x20.MetadataType;
import net.opengis.wps.x20.DataDescriptionType;
import net.opengis.wps.x20.FormatDocument.Format;
import net.opengis.wps.x20.InputDescriptionType;
import net.opengis.wps.x20.LiteralDataType;
import net.opengis.wps.x20.OutputDescriptionType;

import org.apache.xmlbeans.XmlDocumentProperties;
import org.apache.xmlbeans.XmlException;

import cn.edu.whu.geos.xpso.x10.ExecEnvDocument;
import cn.edu.whu.geos.xpso.x10.ExecEnvType;
import cn.edu.whu.geos.xpso.x10.LinkType;
import cn.edu.whu.geos.xpso.x10.ProcessInstanceType;
import cn.edu.whu.geos.xpso.x10.WPSEnvDocument;
import cn.edu.whu.geos.xpso.x10.WPSEnvDocument.WPSEnv;
import cn.edu.whu.geos.xpso.x10.WorkflowInstanceDocument;
import cn.edu.whu.geos.xpso.x10.WorkflowInstanceType;

import com.geojmodelbuilder.core.desc.IInputParameterDesc;
import com.geojmodelbuilder.core.desc.IOutputParameterDesc;
import com.geojmodelbuilder.core.desc.IProcessDesc;
import com.geojmodelbuilder.core.desc.WPSDataFormat;
import com.geojmodelbuilder.core.desc.WPSDataType;
import com.geojmodelbuilder.core.desc.impl.InputParameterDesc;
import com.geojmodelbuilder.core.desc.impl.Metadata;
import com.geojmodelbuilder.core.desc.impl.OutputParameterDesc;
import com.geojmodelbuilder.core.desc.impl.ProcessDesc;
import com.geojmodelbuilder.core.desc.impl.WorkflowDesc;
import com.geojmodelbuilder.core.impl.DataFlowImpl;

public class XML2Description {

	private StringBuffer errInfo = new StringBuffer();

	public WorkflowDesc parse(String xmlText) {
		WorkflowInstanceDocument empDoc = null;
		try {
			empDoc = WorkflowInstanceDocument.Factory.parse(xmlText);
		} catch (XmlException e) {
			e.printStackTrace();
			errInfo.append(e.getMessage());
			return null;
		}
		return parse(empDoc);
	}

	public WorkflowDesc parse(File xmlFile) {
		WorkflowInstanceDocument empDoc = null;
		try {
			empDoc = WorkflowInstanceDocument.Factory.parse(xmlFile);
		} 
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			errInfo.append(e.getMessage());
			return null;
		}

		return parse(empDoc);
	}

	public WorkflowDesc parse(WorkflowInstanceDocument empDoc) {
		WorkflowDesc workflowInstance = new WorkflowDesc();

		WorkflowInstanceType wlstype = empDoc.getWorkflowInstance();
		ProcessInstanceType[] processes = wlstype.getProcessInstanceArray();
		
		workflowInstance.setID(wlstype.getId());
		// reading the title, abstract, metadata
		
		workflowInstance.setName(wlstype.getIdentifier().getStringValue());
		if (wlstype.getAbstractArray().length != 0) {
			String des = wlstype.getAbstractArray(0).getStringValue();
			workflowInstance.setDescription(des);
		}
		if (wlstype.getTitleArray().length != 0) {
			String title = wlstype.getTitleArray(0).getStringValue();
			workflowInstance.setTitle(title);
		}

		// parse the Process, only support WPS for now.
		for (ProcessInstanceType processInstanceType : processes) {
			//ExecType.Enum execType = processInstanceType.getExecType();

			ExecEnvType execType = processInstanceType.getExecEnv();
			// only support WPS
			String execTypeStr = execType.getExecType();
			if(execTypeStr==null || execTypeStr.equals(""))
				continue;
			if (!execTypeStr.equalsIgnoreCase("OGC_WPS"))
				continue;

			ProcessDesc process = parseWPSProcess(processInstanceType);
			workflowInstance.addProcess(process);
		}

		// parse link
		LinkType[] links = wlstype.getLinkArray();
		for (LinkType link : links) {
			parseLink(link, workflowInstance);
		}

		return workflowInstance;
	}

	private void parseLink(LinkType linkType, WorkflowDesc workflowInstance) {
		String srcProcessId = linkType.getSourceProcess();
		String srcParamId = linkType.getSourceVariable();
		String tarProcessId = linkType.getTargetProcess();
		String tarParamId = linkType.getTargetVariable();

		IProcessDesc srcProcess = searchById(workflowInstance, srcProcessId);
		IOutputParameterDesc srcInput = srcProcess.getOutput(srcParamId);

		IProcessDesc tarProcess = searchById(workflowInstance, tarProcessId);
		IInputParameterDesc tarInput = tarProcess.getInput(tarParamId);

		DataFlowImpl dataFlow = new DataFlowImpl(srcProcess, srcInput,
				tarProcess, tarInput);
		srcProcess.addLink(dataFlow);
		tarProcess.addLink(dataFlow);
	}

	private IProcessDesc searchById(WorkflowDesc workflowInstance,
			String id) {
		for (IProcessDesc process : workflowInstance.getProcesses()) {
			if (process.getID().trim().equals(id.trim()))
				return process;
		}
		return null;
	}

	private ProcessDesc parseWPSProcess(ProcessInstanceType processInstanceType) {
		// processInstanceType.getExecType();
		ExecEnvType envtype = processInstanceType.getExecEnv();
		ExecEnvDocument execDoc = ExecEnvDocument.Factory.newInstance();
		execDoc.setExecEnv(envtype);

		WPSEnvDocument envDoc = null;

		try {
			// System.out.println(execDoc.xmlText());
			String xmlString = execDoc.xmlText();
			xmlString = xmlString.replaceAll("ExecEnv", "WPSEnv");
			envDoc = WPSEnvDocument.Factory.parse(xmlString);

		} catch (XmlException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			return null;
		}
		WPSEnv wpsenv = envDoc.getWPSEnv();
		//String name = processInstanceType.getIdentifier().getStringValue();
		// System.out.println("name is "+ name);
		String identifier = processInstanceType.getIdentifier()
				.getStringValue();
		if(identifier!=null)
			identifier = identifier.trim();

		String id = processInstanceType.getId();
		ProcessDesc  processDesc = new ProcessDesc();
		processDesc.setID(id);
		processDesc.setName(identifier);
		// the WPS address
		// String url = wpsEnv.getURL();
		com.geojmodelbuilder.core.impl.WPSEnv wpsEnvClass = new com.geojmodelbuilder.core.impl.WPSEnv();
		wpsEnvClass.setExecType(wpsenv.getExecType());
		wpsEnvClass.setId(wpsenv.getId());
		wpsEnvClass.setMode(wpsenv.getMode().toString());
		wpsEnvClass.setUrl(wpsenv.getURL());
		wpsEnvClass.setVersion(wpsenv.getVersion());
		processDesc.setExecEnv(wpsEnvClass);
		
		LanguageStringType[] abstractTypes = processInstanceType.getAbstractArray();
		if(abstractTypes!=null){
			String description="";
			for(LanguageStringType abstractType : abstractTypes){
				description+=abstractType.getStringValue()+" ";
			}
			processDesc.setDescription(description.trim());
		}
		
		LanguageStringType[] titleTypes = processInstanceType.getTitleArray();
		if(titleTypes!=null){
			String title = "";
			for(LanguageStringType titleType : titleTypes){
				title += titleType.getStringValue()+" ";
			}
			processDesc.setTitle(title.trim());
		}
		
		MetadataType[] metadataTypes= processInstanceType.getMetadataArray();
	    for(MetadataType metaType : metadataTypes){
	    	Metadata metadata = new Metadata();
	    	metadata.setHref(metaType.getHref());
	    	metadata.setRole(metaType.getRole());
	    	processDesc.addMeta(metadata);
	    }

		InputDescriptionType[] inputs = processInstanceType.getInputArray();
		// System.out.println("the count of inputs is " + inputs.length);
		for (InputDescriptionType dataInputType : inputs) {
			parseInput(processDesc, dataInputType);
		}

		OutputDescriptionType[] outputs = processInstanceType.getOutputArray();
		for (OutputDescriptionType outputType : outputs) {
			parseOutput(processDesc, outputType);
		}
		// System.out.println(envtype);
		return processDesc;
	}

	/*
	 * parse the output
	 */
	private boolean parseOutput(ProcessDesc process, OutputDescriptionType output) {
		IOutputParameterDesc outputParam = new OutputParameterDesc();
		
		CodeType idType = output.getIdentifier();
		if(idType!=null)
			outputParam.setName(idType.getStringValue());
		
		LanguageStringType[] abstractTypes = output.getAbstractArray();
		if(abstractTypes!=null && abstractTypes.length >0){
			String description = "";
			for(LanguageStringType abstractType:abstractTypes){
				description += abstractType.getStringValue()+" ";
			}
			outputParam.setDescription(description.trim());
		}
		
		LanguageStringType[] titleTypes = output.getTitleArray();
		if(titleTypes!=null&&titleTypes.length>0){
			String title ="";
			for(LanguageStringType titleType : titleTypes){
				title += titleType.getStringValue()+" ";
			}
			outputParam.setTitle(title.trim());
		}
		
		MetadataType[] metaTypes = output.getMetadataArray();
		if(metaTypes!=null & metaTypes.length>0){
			for(MetadataType metaType:metaTypes){
				Metadata meta = new Metadata();
				meta.setHref(metaType.getHref());
				meta.setRole(metaType.getRole());
				outputParam.addMeta(meta);
			}
		}
		
		DataDescriptionType dataType = output.getDataDescription();
		if(dataType instanceof LiteralDataType)
		{
			outputParam.setDataType(WPSDataType.LiteralData);
		}else {
			outputParam.setDataType(WPSDataType.ComplexData);
		}
		
		Format[] formats = dataType.getFormatArray();
		if(formats!=null & formats.length>0){
			
			for(Format format:formats){
				WPSDataFormat wpsFormat = new WPSDataFormat();
				wpsFormat.setEncoding(format.getEncoding());
				wpsFormat.setMimeType(format.getMimeType());
				wpsFormat.setDefaultF(format.getDefault());
				outputParam.addFormat(wpsFormat);
			}
		}
		
		process.addOutput(outputParam);
		return true;
	}

	/*
	 * parse the input
	 */
	private boolean parseInput(ProcessDesc process, InputDescriptionType inputType) {
		IInputParameterDesc inputParam = new InputParameterDesc();
		
		CodeType idType = inputType.getIdentifier();
		if(idType!=null)
			inputParam.setName(idType.getStringValue());
		
		LanguageStringType[] abstractTypes = inputType.getAbstractArray();
		if(abstractTypes!=null && abstractTypes.length >0){
			String description = "";
			for(LanguageStringType abstractType:abstractTypes){
				description += abstractType.getStringValue()+" ";
			}
			inputParam.setDescription(description.trim());
		}
		
		LanguageStringType[] titleTypes = inputType.getTitleArray();
		if(titleTypes!=null&&titleTypes.length>0){
			String title ="";
			for(LanguageStringType titleType : titleTypes){
				title += titleType.getStringValue()+" ";
			}
			inputParam.setTitle(title.trim());
		}
		
		MetadataType[] metaTypes = inputType.getMetadataArray();
		if(metaTypes!=null & metaTypes.length>0){
			for(MetadataType metaType:metaTypes){
				Metadata meta = new Metadata();
				meta.setHref(metaType.getHref());
				meta.setRole(metaType.getRole());
				inputParam.addMeta(meta);
			}
		}
		
		DataDescriptionType dataType = inputType.getDataDescription();
		if(dataType instanceof LiteralDataType){
			inputParam.setDataType(WPSDataType.LiteralData);
		}else {
			inputParam.setDataType(WPSDataType.ComplexData);
		}
		
		Format[] formats = dataType.getFormatArray();
		if(formats!=null & formats.length>0){
			
			for(Format format:formats){
				WPSDataFormat wpsFormat = new WPSDataFormat();
				wpsFormat.setEncoding(format.getEncoding());
				wpsFormat.setMimeType(format.getMimeType());
				wpsFormat.setDefaultF(format.getDefault());
				inputParam.addFormat(wpsFormat);
			}
		}
		
		process.addInput(inputParam);
		return true;
	}

	// return the detailed information if error occurs
	public String getErrInfo() {
		return this.errInfo.toString();
	}
}
