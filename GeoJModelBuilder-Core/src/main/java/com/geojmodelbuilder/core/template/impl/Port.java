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
package com.geojmodelbuilder.core.template.impl;

import java.util.ArrayList;
import java.util.List;

import com.geojmodelbuilder.core.INamespaceDefault;
import com.geojmodelbuilder.core.data.IData;
import com.geojmodelbuilder.core.instance.IParameter;
import com.geojmodelbuilder.core.template.IPort;
import com.geojmodelbuilder.core.template.IProcessTemplate;
import com.geojmodelbuilder.core.utils.ValidateUtil;

/**
 * @author Mingda Zhang
 *
 */
public class Port<P extends IParameter> implements IPort  {

	private List<IData> dataList;
	private IProcessTemplate owner;
	private SpatialMetadata spatialMetadata;
	private String name;
	private String id,namespace;
	private List<P> paramList;
	
	public Port(IProcessTemplate owner) {
		dataList = new ArrayList<IData>();
		spatialMetadata = new SpatialMetadata();
		paramList = new ArrayList<P>();
		this.owner = owner;
	}

	public Port(IProcessTemplate owner,String name){
		this(owner);
		this.name = name;
	}
	public void addDataCandidate(IData data) {
		if (!this.dataList.contains(data))
			this.dataList.add(data);

	}

	public void removeDataCandidate(IData data) {
		if (this.dataList.contains(data))
			this.dataList.remove(data);
	}

	public List<IData> getDataCandidates() {
		return this.dataList;
	}

	public void setOwer(IProcessTemplate owner){
		this.owner = owner;
	}
	
	public IProcessTemplate getOwner() {
		return this.owner;
	}

	public void setSpatialDescription(SpatialMetadata spatialMetadata){
		this.spatialMetadata = spatialMetadata;
	}
	
	public SpatialMetadata getSptialDescription() {
		return this.spatialMetadata;
	}

	public void setName(String name){
		this.name = name;
	}
	
	public String getName() {
		return this.name;
	}
	
	public void setNamespace(String namespace){
		this.namespace = namespace;
	}
	
	public String getNamespace() {
		if(ValidateUtil.isStrEmpty(this.namespace))
			this.namespace = INamespaceDefault.TEMPLATE_ARTIFACT;
		
		return this.namespace;
	}
	
	public void setID(String id){
		this.id = id;
	}
	public String getID() {
		if(ValidateUtil.isStrEmpty(this.id))
			this.id = getOwner().getID() + "_" + getName();
		
		return this.id;
	}

	public void addParameter(P param){
		if(!this.paramList.contains(param))
			this.paramList.add(param);
	}
	
	public List<P> getInstances() {
		return this.paramList;
	}
}
