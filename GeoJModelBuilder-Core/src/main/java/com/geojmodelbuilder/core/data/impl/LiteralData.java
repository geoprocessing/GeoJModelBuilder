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

import com.geojmodelbuilder.core.data.ILiteralData;
/**
 * 
 * @author Mingda Zhang
 *
 */
public class LiteralData implements ILiteralData{

	private String dataType;
	/**
	 * The value is stored in String type.
	 */
	private String value;
	public void setType(String dataType){
		this.dataType = dataType;
	}
	public String getType() {
		return this.dataType;
	}

	public void setValue(Object value){
		this.value = String.valueOf(value);
	}
	
	public Object getValue() {
		return this.value;
	}

}
