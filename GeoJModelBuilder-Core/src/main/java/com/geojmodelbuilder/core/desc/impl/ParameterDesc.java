package com.geojmodelbuilder.core.desc.impl;

import java.util.ArrayList;
import java.util.List;

import com.geojmodelbuilder.core.IProcess;
import com.geojmodelbuilder.core.desc.IParameterDesc;
import com.geojmodelbuilder.core.desc.WPSDataFormat;
import com.geojmodelbuilder.core.desc.WPSDataType;

public class ParameterDesc implements IParameterDesc{
	
	private String name,id,namespace,description,title;
	private WPSDataType dataTyp;
	private List<Metadata> metadatas = new ArrayList<Metadata>();
	private List<WPSDataFormat> formats = new ArrayList<WPSDataFormat>();
	
	public String getName() {
		return name;
	}

	public IProcess getOwner() {
		return null;
	}

	public String getID() {
		return id;
	}

	public String getNamespace() {
		return namespace;
	}

	public void setName(String name) {
		this.name = name;
	}

	public WPSDataType getDataType() {
		return this.dataTyp;
	}

	public void setDataType(WPSDataType dataType) {
		this.dataTyp = dataType;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getDescription() {
		return this.description;
	}

	public void setTitle(String title) {
		
	}

	public String getTitle() {
		return this.title;
	}

	public void addMeta(Metadata metadata) {
		if(!this.metadatas.contains(metadata))
			this.metadatas.add(metadata);
	}

	public List<Metadata> getMetas() {
		return this.metadatas;
	}
	
	public void addFormat(WPSDataFormat format){
		this.formats.add(format);
	}
	
	public List<WPSDataFormat> getFormats() {
		return this.formats;
	}
}
