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
package com.geojmodelbuilder.ui.workspace;

import java.io.File;
import java.io.IOException;

import net.opengis.wps.x100.ProcessDescriptionType;

import org.apache.xmlbeans.XmlException;

/**
 * @author Mingda Zhang
 *
 */
public class WPSCacheFactory {
	
	private String baseDir;
	private static WPSCacheFactory instance;
	public static String GETCAPABILITEIS_NAME= "GetCapabilities";
	
	
	private WPSCacheFactory(){
	} 
	
	public static WPSCacheFactory getInstance(){
		if(instance == null)
			instance = new WPSCacheFactory();
		
		return instance;
	}
	
	public static String getFolderByUrl(String url) {
		StringBuffer buffer = new StringBuffer();
		buffer.append(url.replace("://", "_").replace(":", ".")
				.replace("/", "_"));
		return buffer.toString().trim();
	}
	
	@Deprecated
	public void setBaseDir(String baseDir){
		this.baseDir = baseDir;
	}
	
	public String getBaseDir(){
		if(this.baseDir == null)
			this.baseDir = Workspace.getInstance().getCacheDir();
		
		return this.baseDir;
	}
	
	public ProcessDescriptionType getProcessDescriptionType(String url,String processName){
		String folder = getFolderByUrl(url);
		String cacheDir = Workspace.getInstance().getCacheDir();
		String filePath = cacheDir+File.separator+folder+File.separator+processName+".xml";
		File file = new File(filePath);
		if (!file.exists()) 
			return null;
		
		try {
			return ProcessDescriptionType.Factory.parse(file);
		} catch (XmlException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
}
