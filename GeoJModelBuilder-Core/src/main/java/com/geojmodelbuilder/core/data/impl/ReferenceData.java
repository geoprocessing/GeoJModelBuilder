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

import com.geojmodelbuilder.core.data.IReferenceData;
/**
 * 
 * @author Mingda Zhang
 *
 */
public class ReferenceData extends ComplexData implements IReferenceData{
	private String reference;
	
	public void setReference(String reference){
		this.reference = reference;
	} 

	@Override
	public void setValue(Object value) {
		setReference((String)value);
	}
	
	public String getReference() {
		return this.reference;
	}
	
	@Override
	public Object getValue() {
		return this.reference;
	}
	
}
