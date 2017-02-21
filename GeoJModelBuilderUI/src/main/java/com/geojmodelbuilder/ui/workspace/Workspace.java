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

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

import net.opengis.wps.x100.ProcessBriefType;
import net.opengis.wps.x100.ProcessDescriptionType;
import net.opengis.wps.x100.ProcessOfferingsDocument.ProcessOfferings;
import net.opengis.wps.x100.WPSCapabilitiesType;

import org.apache.xmlbeans.XmlException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.geojmodelbuilder.ui.Activator;
import com.geojmodelbuilder.ui.utils.FileTools;

/**
 * @author Mingda Zhang
 *
 */
public class Workspace {
	private String serviceConfigName = "resourceConfig.xml";
	private static Workspace instance;
	private String workspace;
	private Logger logger;
	private String default_wps = "http://geos.whu.edu.cn:8080/wps10/WebProcessingService";
	
	private Workspace(){
		logger = LoggerFactory.getLogger(Workspace.class);
	}
	
	public static Workspace getInstance(){
		if(instance == null){
			instance = new Workspace();
		}
		return instance;
	}
	
	public void setWorkspace(String path){
		this.workspace = path;
	}
	
	public String getWorkspace(){
		return this.workspace;
	}
	
	public String getServiceConfig(){
		return getWorkspace()+File.separator + "configs" + File.separator + serviceConfigName;
	}
	
	public String getCacheDir(){
		return getWorkspace() + File.separator + "caches";
	}
	
	public String getConfigDir(){
		return getWorkspace() + File.separator + "configs";
	}
	
	public String getModelDir(){
		return getWorkspace() + File.separator + "models";
	}
	
	
	public boolean validate(){
		if(this.workspace == null || this.workspace.equals(""))
			return false;
		
		File cacheDir = new File(getCacheDir());
		File modelDir = new File(getModelDir());
		File configDir = new File(getConfigDir());
		
		if (!modelDir.exists()) {
			modelDir.mkdirs();
		}
		
		if(!configDir.exists()){
			configDir.mkdirs();
		}
		
		if(!cacheDir.exists()){
			cacheDir.mkdirs();
			initializeDefaultWPS();
		}
		
		return true;
	}
	
	/**
	 * Initialize the default workspace.
	 * 1) add the default WPS: http://geos.whu.edu.cn:8080/wps10/WebProcessingService to cache directory
	 * 2) add the default WPS to configuration
	 */
	private void initializeDefaultWPS(){
		if(this.workspace == null || this.workspace.equals(""))
			return;
		
		String wsBase = getCacheDir()+ File.separator + WPSCacheFactory.getFolderByUrl(default_wps) + File.separator;
		File wsBaseDir = new File(wsBase);
		if(!wsBaseDir.exists())
			wsBaseDir.mkdirs();
		
		URL capUrl = Activator.getDefault().getBundle().getEntry("resources/default_wps/GetCapabilities.xml"); 
		try {
			WPSCapabilitiesType capabilitiesType = WPSCapabilitiesType.Factory.parse(capUrl.openStream());
			capabilitiesType.save(new File(wsBase+"GetCapabilities.xml"));
			
			ProcessOfferings processOfferings = capabilitiesType.getProcessOfferings();
			ProcessBriefType[] processBriefTypes = processOfferings.getProcessArray();
			for(ProcessBriefType briefType:processBriefTypes){
				String processFile = briefType.getIdentifier().getStringValue().trim()+".xml";
				URL processUrl = Activator.getDefault().getBundle().getEntry("resources/default_wps/"+processFile); 
				if(processUrl == null){
					System.err.println(processFile);
					continue;
				}
				ProcessDescriptionType processDescriptionType = ProcessDescriptionType.Factory.parse(processUrl.openStream());
				processDescriptionType.save(new File(wsBase+processFile));
			}
		} catch (XmlException | IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
	/*	
		
		URL url = Activator.getDefault().getBundle().getEntry("resources/default_wps/geowps.txt");  
		try {
			InputStreamReader inputStreamReader = new InputStreamReader(url.openStream());
			BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
			StringBuffer stringBuffer = new StringBuffer();
			String row;
			while ((row = bufferedReader.readLine()) != null) {
				stringBuffer.append(row);
			}
			String[] algorithms = stringBuffer.toString().split(",");
			try {
				bufferedReader.close();
				inputStreamReader.close();
			} catch (Exception e) {
				logger.error("Error occured when close the inputstream");
			}
			
			String processFile = null;
			for(String algorithm:algorithms){
				processFile = algorithm.trim()+".xml";
				System.out.println(processFile);
				URL processUrl = Activator.getDefault().getBundle().getEntry("resources/default_wps/"+processFile); 
				ProcessDescriptionType processDescriptionType = ProcessDescriptionType.Factory.parse(processUrl.openStream());
				processDescriptionType.save(new File(wsBase+processFile));
			}
			
		} catch (IOException | XmlException e) {
			e.printStackTrace();
		}*/
		
		String config = getServiceConfig();
		URL defaultConfigUrl = Activator.getDefault().getBundle().getEntry("resources/resourceConfig.xml");  
		FileTools.getInstance().copyFile(defaultConfigUrl, config);
	}
}
