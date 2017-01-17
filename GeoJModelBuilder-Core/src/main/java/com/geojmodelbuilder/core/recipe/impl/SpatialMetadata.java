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
package com.geojmodelbuilder.core.recipe.impl;


/**
 * @author Mingda Zhang
 * Suppose to be updated.
 */
public class SpatialMetadata  {
	
	private String mimeType;
	private String keyWords;
	private String description;
	
	
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setKeyWords(String keyWords) {
		this.keyWords = keyWords;
	}

	public String getTemporalExtent() {
		return null;
	}

	public String getSpatialExtent() {
		return null;
	}

	public void setMimeType(String mimeType){
		this.mimeType = mimeType;
	}
	
	public String getMimeType() {
		return this.mimeType;
	}

	public String getKeyWords() {
		return this.keyWords;
	}

}
