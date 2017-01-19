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
package com.geojmodelbuilder.core.data.impl;

import com.geojmodelbuilder.core.data.IComplexData;
/**
 * 
 * @author Mingda Zhang
 *
 */
public class ComplexData implements IComplexData{

	private String mimeType;
	private Object value;
	private String schema;
	private String encoding;
	
	public void setMimeType(String mimeType){
		this.mimeType = mimeType;
	}
	
	public String getMimeType() {
		return this.mimeType;
	}
	
	public String getType() {
		return this.mimeType;
	}

	public void setValue(Object value){
		this.value = value;
	}
	
	public Object getValue() {
		return this.value;
	}

	public void setSchema(String schema){
		this.schema = schema;
	}
	
	public String getSchema() {
		return this.schema;
	}

	public void setEncoding(String encoding){
		this.encoding = encoding;
	}
	
	public String getEncoding() {
		return this.encoding;
	}

	public void setType(String type) {
		setMimeType(type);
	}
}
