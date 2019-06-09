package com.geojmodelbuilder.xml.deserialization;

import java.io.File;

import net.opengis.wps.x20.GenericInputType;
import net.opengis.wps.x20.GenericOutputType;
import net.opengis.wps.x20.GenericProcessType;

import org.apache.xmlbeans.XmlException;

import cn.edu.whu.geos.xpso.x10.LinkType;
import cn.edu.whu.geos.xpso.x10.WorkflowTemplateDocument;
import cn.edu.whu.geos.xpso.x10.WorkflowTemplateType;

import com.geojmodelbuilder.core.impl.DataFlowImpl;
import com.geojmodelbuilder.core.template.IInputPort;
import com.geojmodelbuilder.core.template.IOutPutPort;
import com.geojmodelbuilder.core.template.IProcessTemplate;
import com.geojmodelbuilder.core.template.impl.InputPort;
import com.geojmodelbuilder.core.template.impl.OutputPort;
import com.geojmodelbuilder.core.template.impl.ProcessTemplate;
import com.geojmodelbuilder.core.template.impl.WorkflowTemplate;

public class XML2Template {

	private StringBuffer errInfo = new StringBuffer();

	public WorkflowTemplate parse(String xmlText) {
		WorkflowTemplateDocument empDoc = null;
		try {
			empDoc = WorkflowTemplateDocument.Factory.parse(xmlText);
		} catch (XmlException e) {
			e.printStackTrace();
			errInfo.append(e.getMessage());
			return null;
		}
		return parse(empDoc);
	}

	public WorkflowTemplate parse(File xmlFile) {
		WorkflowTemplateDocument empDoc = null;
		try {
			empDoc = WorkflowTemplateDocument.Factory.parse(xmlFile);
		} 
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			errInfo.append(e.getMessage());
			return null;
		}

		return parse(empDoc);
	}

	public WorkflowTemplate parse(WorkflowTemplateDocument empDoc) {
		WorkflowTemplate workflowTemplate = new WorkflowTemplate();
		
		WorkflowTemplateType wlstype = empDoc.getWorkflowTemplate();
		GenericProcessType[] processes = wlstype.getProcessTemplateArray();;

		// reading the title, abstract, metadata
		workflowTemplate.setID(wlstype.getIdentifier().getStringValue());
		if (wlstype.getAbstractArray().length != 0) {
			String des = wlstype.getAbstractArray(0).getStringValue();
			workflowTemplate.setDescription(des);
		}
		if (wlstype.getTitleArray().length != 0) {
			String title = wlstype.getTitleArray(0).getStringValue();
			workflowTemplate.setName(title);
		}

		// parse the Process, only support WPS for now.
		for (GenericProcessType processTemplateType : processes) {
			ProcessTemplate process = parseProcess(processTemplateType);
			workflowTemplate.addProcess(process);
		}

		// parse link
		LinkType[] links = wlstype.getLinkArray();
		for (LinkType link : links) {
			parseLink(link, workflowTemplate);
		}

		return workflowTemplate;
	}

	private void parseLink(LinkType linkType, WorkflowTemplate workflowTemplate) {
		String srcProcessId = linkType.getSourceProcess();
		String srcParamId = linkType.getSourceVariable();
		String tarProcessId = linkType.getTargetProcess();
		String tarParamId = linkType.getTargetVariable();

		IProcessTemplate srcProcess = searchById(workflowTemplate, srcProcessId);
		IOutPutPort srcInput = srcProcess.getOutput(srcParamId);

		IProcessTemplate tarProcess = searchById(workflowTemplate, tarProcessId);
		IInputPort tarInput = tarProcess.getInput(tarParamId);

		DataFlowImpl dataFlow = new DataFlowImpl(srcProcess, srcInput,
				tarProcess, tarInput);
		srcProcess.addLink(dataFlow);
		tarProcess.addLink(dataFlow);
	}

	private IProcessTemplate searchById(WorkflowTemplate workflowTemplate,
			String id) {
		for (IProcessTemplate process : workflowTemplate.getProcesses()) {
			if (process.getID().trim().equals(id.trim()))
				return process;
		}
		return null;
	}

	private ProcessTemplate parseProcess(GenericProcessType processTemplateType) {
		
		String identifier = processTemplateType.getIdentifier()
				.getStringValue();
		if(identifier!=null)
			identifier = identifier.trim();
		ProcessTemplate processTemplate = new ProcessTemplate();
		processTemplate.setID(identifier);
		
		if(processTemplateType.getTitleArray().length!=0){
			String title = processTemplateType.getTitleArray(0).getStringValue();
			processTemplate.setName(title);
		}
		GenericInputType[] inputs = processTemplateType.getInputArray();
		for (GenericInputType dataInputType : inputs) {
			parseInput(processTemplate, dataInputType);
		}

		GenericOutputType[] outputs = processTemplateType.getOutputArray();
		for (GenericOutputType outputType : outputs) {
			parseOutput(processTemplate, outputType);
		}
		return processTemplate;
	}

	/*
	 * parse the output
	 */
	private boolean parseOutput(ProcessTemplate process, GenericOutputType output) {
		String identifier = output.getIdentifier().getStringValue();
		IOutPutPort outputport = new OutputPort(process, identifier);
		process.addOutput(outputport);
		return true;
	}

	/*
	 * parse the input
	 */
	private boolean parseInput(ProcessTemplate process, GenericInputType inputType) {
		String identifier = inputType.getIdentifier().getStringValue();
        IInputPort inputport = new InputPort(process,identifier);
		process.addInput(inputport);
		return true;
	}

	// return the detailed information if error occurs
	public String getErrInfo() {
		return this.errInfo.toString();
	}
}
