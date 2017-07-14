/**
 * Copyright (C) 2013 - 2016 Wuhan University,
 *                           Center for Geographic Analysis, Harvard University
 * 
 * This program is free software; you can redistribute and/or modify it under 
 * the terms of the GNU General Public License version 2 as published by the 
 * Free Software Foundation.
 * 
 * This program is distributed WITHOUT ANY WARRANTY; even without the implied
 * WARRANTY OF MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 */
package com.geojmodelbuilder.core.utils;

import java.util.HashMap;
import java.util.Map;


/**
 * @author Mingda Zhang
 * This should read from properties file.
 */
public class SuffixMimeType {
	private static SuffixMimeType instance;
	private Map<String, String> suffixMap;
	private SuffixMimeType(){
		suffixMap = new HashMap<String, String>();
		suffixMap.put("tif", "application/geotiff");
		suffixMap.put("tiff", "application/geotiff");
	}
	
	public static SuffixMimeType getInstance(){
		if(instance == null)
			instance = new SuffixMimeType();
		
		return instance;
	}
	
	public String getMimeType(String suffix){
		return suffixMap.get(suffix);
	}
	
	public String getSuffix(String url){
		if(url == null || !url.contains("."))
			return null;
		
		return url.substring(url.lastIndexOf(".") + 1);  
	}
	
	public static void main(String[] args){
		SuffixMimeType suffixMimeType = SuffixMimeType.getInstance();
		System.out.println(suffixMimeType.getSuffix("http://localhost:8080/data/data.tif"));
		System.out.println(suffixMimeType.getMimeType("tif"));
	}
}
