/**
 * Copyright (C) 2013 - 2016 Wuhan University
 * 
 * This program is free software; you can redistribute and/or modify it under 
 * the terms of the GNU General Public License version 2 as published by the 
 * Free Software Foundation.
 * 
 * This program is distributed WITHOUT ANY WARRANTY; even without the implied
 * WARRANTY OF MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 */
package com.geojmodelbuilder.core.resource.ogc.wps;

import java.io.IOException;
import java.util.List;

import net.opengis.ows.x11.ExceptionReportDocument;
import net.opengis.wps.x100.DataType;
import net.opengis.wps.x100.ExecuteDocument;
import net.opengis.wps.x100.ExecuteResponseDocument;
import net.opengis.wps.x100.ExecuteResponseDocument.ExecuteResponse;
import net.opengis.wps.x100.ExecuteResponseDocument.ExecuteResponse.ProcessOutputs;
import net.opengis.wps.x100.InputDescriptionType;
import net.opengis.wps.x100.LiteralDataType;
import net.opengis.wps.x100.OutputDataType;
import net.opengis.wps.x100.OutputDescriptionType;
import net.opengis.wps.x100.OutputReferenceType;
import net.opengis.wps.x100.ProcessDescriptionType;
import net.opengis.wps.x100.ProcessDescriptionType.DataInputs;
import net.opengis.wps.x100.StatusType;

import org.n52.wps.client.ExecuteRequestBuilder;
import org.n52.wps.client.WPSClientException;
import org.n52.wps.client.WPSClientSession;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.geojmodelbuilder.core.data.IComplexData;
import com.geojmodelbuilder.core.data.IData;
import com.geojmodelbuilder.core.data.ILiteralData;
import com.geojmodelbuilder.core.data.impl.ComplexData;
import com.geojmodelbuilder.core.data.impl.LiteralData;
import com.geojmodelbuilder.core.data.impl.ReferenceData;
import com.geojmodelbuilder.core.plan.IInputParameter;
import com.geojmodelbuilder.core.plan.IOutputParameter;
import com.geojmodelbuilder.core.plan.IParameter;
import com.geojmodelbuilder.core.plan.impl.InputParameter;
import com.geojmodelbuilder.core.plan.impl.OutputParameter;
import com.geojmodelbuilder.core.plan.impl.ProcessExec;

/**
 * 
 * @author Mingda Zhang
 *
 */
public class WPSProcess extends ProcessExec {
	/**
	 * The GetCapabilities request URL.
	 */
	private String wpsUrl;
	private ProcessDescriptionType processDescription;

	public void setProcessDescriptionType(ProcessDescriptionType processDescription) {
		this.processDescription = processDescription;
		parseProcessDescriptionType();
	}

	public WPSProcess(String name) {
		super();
		setName(name);
		setID(name);
	}

	
	private boolean isLiteralNode(Node node) {
		NodeList nodeList = node.getChildNodes();
		for (int i = 0; i < nodeList.getLength(); i++) {
			Node childNode = nodeList.item(i);
			if (childNode == null || childNode.getLocalName() == null) 
				continue;
			
			if (childNode.getLocalName().contains("Literal"))
				return true;
		}
		return false;
	}

	/**
	 * Retrieve the input and output from process description in a lazy way.
	 */
	private boolean parseProcessDescriptionType() {
		if (this.processDescription == null) {
			if (getProcessDescriptionType()==null)
				return false;
		}

		getInputs().clear();
		getOutputs().clear();
		
		DataInputs dataInputs = this.processDescription.getDataInputs();
		InputDescriptionType[] inputDescriptionTypes = dataInputs
				.getInputArray();
		for (InputDescriptionType input : inputDescriptionTypes) {
			InputParameter inputParameter = new InputParameter(this);
			inputParameter.setName(input.getIdentifier().getStringValue().trim());
			Node node = input.getDomNode();
			IData dataType;
			if (isLiteralNode(node)) {
				dataType = new LiteralData();
			} else {
				dataType = new ComplexData();
			}
			inputParameter.setData(dataType);
			this.addInput(inputParameter);
		}

		OutputDescriptionType[] outputDescriptionTypes = this.processDescription
				.getProcessOutputs().getOutputArray();
		for (OutputDescriptionType output : outputDescriptionTypes) {
			OutputParameter outputParameter = new OutputParameter(this);
			outputParameter.setName(output.getIdentifier().getStringValue());
			Node node = output.getDomNode();
			IData dataType;
			if (isLiteralNode(node)) {
				dataType = new LiteralData();
			} else {
				dataType = new ComplexData();
			}
			outputParameter.setData(dataType);
			this.addOutput(outputParameter);
		}
		return true;
	}

	/**
	 * Obtain the process description.
	 */
	public ProcessDescriptionType getProcessDescriptionType() {
		if (this.processDescription != null)
			return this.processDescription;

		WPSClientSession wpsClient = WPSClientSession.getInstance();
		try {
			this.processDescription = wpsClient.getProcessDescription(wpsUrl,
					getID());
		} catch (IOException e) {
			e.printStackTrace();
			appendErr(e.getMessage());
		}

		return this.processDescription;
	}

	public void setWPSUrl(String url) {
		this.wpsUrl = url;
	}

	@Override
	public boolean canExecute() {
		if (this.wpsUrl == null || this.wpsUrl.equals("")) {
			appendErr("Service url is missing.");
			return false;
		}

		if (getID() == null || getID().equals("")) {
			appendErr("Process identifier is missing.");
			return false;
		}

		List<IInputParameter> inputs = getInputs();
		for(IInputParameter input:inputs){
			if(input.getData() == null || input.getData().getValue() == null){
				appendErr("There is no value for the parameter named "+input.getName()+" in "+getName());
				return false;
			}
				
		}
		if (this.processDescription == null) {
			getProcessDescriptionType();
		}

		return this.processDescription != null?true:false;
	}

	@Override
	public boolean execute() {
		WPSClientSession wpsClient = WPSClientSession.getInstance();

		if (this.processDescription == null) {
			getProcessDescriptionType();
		}

		if (this.processDescription == null) {
			appendErr("Fail to get the description");
			return false;
		}

		// build the Execute request
		ExecuteDocument executeReq = buildRequest();
		if (executeReq == null) {
			appendErr("Fail to build the Execute request.");
			return false;
		}

		// send request
		Object response = null;
		try {
			response = wpsClient.execute(wpsUrl, executeReq);
		} catch (WPSClientException e) {
			e.printStackTrace();
			appendErr(e.getMessage());
			return false;
		}

		// parse the response
		if (response instanceof ExecuteResponseDocument) {
			return parseResponse((ExecuteResponseDocument) response);
		} else if (response instanceof ExceptionReportDocument) {
			appendErr("Exception occurred.");
		}

		return false;
	}

	private ExecuteDocument buildRequest() {

		ExecuteRequestBuilder executeBuilder = new ExecuteRequestBuilder(
				processDescription);
		for (IParameter parameter : getInputs()) {
			IData data = parameter.getData();
			String identifier = parameter.getName();

			// Only literal data and complex data are considered, complex data
			// are treated as reference
			if (data instanceof ILiteralData) {
				executeBuilder.addLiteralData(identifier,
						String.valueOf(data.getValue()));
			} else {
				IComplexData complexDataData = ((IComplexData) data);
				executeBuilder.addComplexDataReference(identifier,
						complexDataData.getValue().toString(),
						complexDataData.getSchema(),
						complexDataData.getEncoding(),
						complexDataData.getMimeType());
			}
		}

		for (IParameter parameter : getOutputs()) {
			String identifier = parameter.getName();
			IData data = parameter.getData();
			if (data instanceof IComplexData) {
				executeBuilder.setResponseDocument(identifier, null, "UTF-8",
						data.getType());
				executeBuilder.setAsReference(identifier, true);
			} else if (data instanceof ILiteralData) {
				executeBuilder.setResponseDocument(identifier, null, "UTF-8",
						null);
			}
		}

		if (!executeBuilder.isExecuteValid()) {
			appendErr("Created execute request is NOT valid.");
			return null;
		}

		return executeBuilder.getExecute();
	}

	private boolean parseResponse(ExecuteResponseDocument responseDoc) {

		ExecuteResponse response = responseDoc.getExecuteResponse();
		StatusType statusType = response.getStatus();

		if (statusType.isSetProcessFailed()) {
			appendErr("Fail to execute this process");
			appendErr(responseDoc.xmlText());
			return false;
		}

		ProcessOutputs outputs = response.getProcessOutputs();
		if (outputs == null || outputs.sizeOfOutputArray() == 0) {
			appendErr("There is no output in the response.");
			return false;
		}

		// BBoxbounding is temporarily ignored
		for (int i = 0; i < outputs.sizeOfOutputArray(); i++) {
			OutputDataType outputType = outputs.getOutputArray(i);
			String identifer = outputType.getIdentifier().getStringValue();
			IOutputParameter outputParameter = getOuput(identifer);
			OutputReferenceType referenceType = outputType.getReference();
			if (referenceType != null) {
				ReferenceData referenceData = new ReferenceData();
				referenceData.setMimeType(referenceType.getMimeType());
				referenceData.setReference(referenceType.getHref());
				referenceData.setEncoding(referenceType.getEncoding());
				referenceData.setSchema(referenceType.getSchema());
				outputParameter.setData(referenceData);
			} else {
				// Complex data is ignored.
				DataType dataType = outputType.getData();
				if (dataType != null) {
					// handle literal output
					if (dataType.isSetLiteralData()) {
						LiteralDataType literalDataType = dataType
								.getLiteralData();
						LiteralData literalData = new LiteralData();
						literalData.setValue(literalDataType.getStringValue());
						outputParameter.setData(literalData);
					}
				}
			}
		}

		return true;
	}

	@Override
	public IInputParameter getInput(String name) {
		if (this.processDescription == null)
			parseProcessDescriptionType();

		return super.getInput(name);
	}

	@Override
	public List<IInputParameter> getInputs() {
		if (this.processDescription == null)
			parseProcessDescriptionType();

		return super.getInputs();
	}

	@Override
	public IOutputParameter getOuput(String name) {
		if (this.processDescription == null)
			parseProcessDescriptionType();

		return super.getOuput(name);
	}

	@Override
	public List<IOutputParameter> getOutputs() {
		if (this.processDescription == null)
			parseProcessDescriptionType();

		return super.getOutputs();
	}
}
