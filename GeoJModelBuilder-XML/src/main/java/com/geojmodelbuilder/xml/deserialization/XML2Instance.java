package com.geojmodelbuilder.xml.deserialization;

import java.io.File;

import net.opengis.wps.x20.DataDocument.Data;
import net.opengis.wps.x20.DataInputType;
import net.opengis.wps.x20.OutputDefinitionType;
import net.opengis.wps.x20.ReferenceType;

import org.apache.xmlbeans.XmlCursor;
import org.apache.xmlbeans.XmlException;

import cn.edu.whu.geos.xpso.x10.ExecEnvDocument;
import cn.edu.whu.geos.xpso.x10.ExecEnvType;
import cn.edu.whu.geos.xpso.x10.LinkType;
import cn.edu.whu.geos.xpso.x10.ProcessExecutionType;
import cn.edu.whu.geos.xpso.x10.WPSEnvDocument;
import cn.edu.whu.geos.xpso.x10.WPSEnvDocument.WPSEnv;
import cn.edu.whu.geos.xpso.x10.WorkflowExecutionDocument;
import cn.edu.whu.geos.xpso.x10.WorkflowExecutionType;

import com.geojmodelbuilder.core.data.IData;
import com.geojmodelbuilder.core.data.impl.ComplexData;
import com.geojmodelbuilder.core.data.impl.LiteralData;
import com.geojmodelbuilder.core.impl.DataFlowImpl;
import com.geojmodelbuilder.core.instance.IInputParameter;
import com.geojmodelbuilder.core.instance.IOutputParameter;
import com.geojmodelbuilder.core.instance.IProcessInstance;
import com.geojmodelbuilder.core.instance.impl.InputParameter;
import com.geojmodelbuilder.core.instance.impl.OutputParameter;
import com.geojmodelbuilder.core.instance.impl.WorkflowInstance;
import com.geojmodelbuilder.core.resource.ogc.wps.WPSProcess;

public class XML2Instance {

	private StringBuffer errInfo = new StringBuffer();

	public WorkflowInstance parse(String xmlText) {
		WorkflowExecutionDocument empDoc = null;
		try {
			empDoc = WorkflowExecutionDocument.Factory.parse(xmlText);
		} catch (XmlException e) {
			e.printStackTrace();
			errInfo.append(e.getMessage());
			return null;
		}
		return parse(empDoc);
	}

	public WorkflowInstance parse(File xmlFile) {
		WorkflowExecutionDocument empDoc = null;
		try {
			empDoc = WorkflowExecutionDocument.Factory.parse(xmlFile);
		} 
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			errInfo.append(e.getMessage());
			return null;
		}

		return parse(empDoc);
	}

	public WorkflowInstance parse(WorkflowExecutionDocument empDoc) {
		WorkflowInstance workflowInstance = new WorkflowInstance();

		WorkflowExecutionType wlstype = empDoc.getWorkflowExecution();
		ProcessExecutionType[] processes = wlstype.getProcessExecutionArray();

		// reading the title, abstract, metadata
		workflowInstance.setID(wlstype.getIdentifier().getStringValue());
		if (wlstype.getAbstractArray().length != 0) {
			String des = wlstype.getAbstractArray(0).getStringValue();
			workflowInstance.setDescription(des);
		}
		if (wlstype.getTitleArray().length != 0) {
			String title = wlstype.getTitleArray(0).getStringValue();
			workflowInstance.setName(title);
		}

		// parse the Process, only support WPS for now.
		for (ProcessExecutionType processInstanceType : processes) {
			//ExecType.Enum execType = processInstanceType.getExecType();

			ExecEnvType execType = processInstanceType.getExecEnv();
			// only support WPS
			String execTypeStr = execType.getExecType();
			if(execTypeStr==null || execTypeStr.equals(""))
				continue;
			if (!execTypeStr.equalsIgnoreCase("OGC_WPS"))
				continue;

			WPSProcess process = parseWPSProcess(processInstanceType);
			workflowInstance.addProcess(process);
		}

		// parse link
		LinkType[] links = wlstype.getLinkArray();
		for (LinkType link : links) {
			parseLink(link, workflowInstance);
		}

		return workflowInstance;
	}

	private void parseLink(LinkType linkType, WorkflowInstance workflowInstance) {
		String srcProcessId = linkType.getSourceProcess();
		String srcParamId = linkType.getSourceVariable();
		String tarProcessId = linkType.getTargetProcess();
		String tarParamId = linkType.getTargetVariable();

		IProcessInstance srcProcess = searchById(workflowInstance, srcProcessId);
		IOutputParameter srcInput = srcProcess.getOutput(srcParamId);

		IProcessInstance tarProcess = searchById(workflowInstance, tarProcessId);
		IInputParameter tarInput = tarProcess.getInput(tarParamId);

		DataFlowImpl dataFlow = new DataFlowImpl(srcProcess, srcInput,
				tarProcess, tarInput);
		srcProcess.addLink(dataFlow);
		tarProcess.addLink(dataFlow);
	}

	private IProcessInstance searchById(WorkflowInstance workflowInstance,
			String id) {
		for (IProcessInstance process : workflowInstance.getProcesses()) {
			if (process.getID().trim().equals(id.trim()))
				return process;
		}
		return null;
	}

	private WPSProcess parseWPSProcess(ProcessExecutionType processInstanceType) {
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
		WPSProcess wpsProcess = new WPSProcess(identifier);
		wpsProcess.setID(id);
		// the WPS address
		// String url = wpsEnv.getURL();
		wpsProcess.setWPSUrl(wpsenv.getURL().trim());

		DataInputType[] inputs = processInstanceType.getInputArray();
		// System.out.println("the count of inputs is " + inputs.length);
		for (DataInputType dataInputType : inputs) {
			parseInput(wpsProcess, dataInputType);
		}

		OutputDefinitionType[] outputs = processInstanceType.getOutputArray();
		for (OutputDefinitionType outputType : outputs) {
			parseOutput(wpsProcess, outputType);
		}
		// System.out.println(envtype);
		return wpsProcess;
	}

	/*
	 * parse the output
	 */
	private boolean parseOutput(WPSProcess process, OutputDefinitionType output) {
		String transmission = output.getTransmission().toString();
		IOutputParameter outputParam = new OutputParameter(process);
		outputParam.setName(output.getId());
		IData data = null;
		if (transmission.equalsIgnoreCase("value")) {
			data = new LiteralData();
		} else {
			data = new ComplexData();
			String mimeType = output.getMimeType();
			if (mimeType != null)
				data.setType(mimeType);
		}

		outputParam.setData(data);
		process.addOutput(outputParam);

		return true;
	}

	/*
	 * parse the input
	 */
	private boolean parseInput(WPSProcess process, DataInputType inputType) {
		ReferenceType refType = inputType.getReference();
		IData data = null;

		IInputParameter inputParam = new InputParameter(process);

		// ComplexData
		if (refType != null) {
			data = new ComplexData();
			data.setValue(refType.getHref());
			data.setType(refType.getMimeType());
		} else {
			Data literalData = inputType.getData();

			data = new LiteralData();
			// String xmlValue = literalData.xmlText();
			XmlCursor xmlCursor = literalData.newCursor();
			String value = xmlCursor.getTextValue();
			data.setValue(value);
		}

		inputParam.setData(data);
		inputParam.setName(inputType.getId());

		process.addInput(inputParam);
		return true;
	}

	// return the detailed information if error occurs
	public String getErrInfo() {
		return this.errInfo.toString();
	}
}
