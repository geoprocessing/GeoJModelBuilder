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
import net.opengis.wps.x100.WPSCapabilitiesType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.geojmodelbuilder.core.resource.ogc.wps.WPSProcess;
import com.geojmodelbuilder.core.resource.ogc.wps.WPService;

/**
 * OGC WPS 1.0.0
 * @author Mingda Zhang
 *
 */
public class WPSCacheThread extends Thread{

	private static String SUPPORTED_VERSION = "1.0.0";
	private String serviceURL;
	private boolean refresh;
	private Logger logger;

	public WPSCacheThread(String url) {
		this(url, true);
	}
	
	public WPSCacheThread(String url, boolean refresh) {
		this.serviceURL = url;
		this.refresh = refresh;
		this.logger = LoggerFactory.getLogger(WPSCacheThread.class);
	}
	
	
	@Override
	public void run() {
		String baseDir = Workspace.getInstance().getCacheDir();
		String folder = WPSCacheFactory.getFolderByUrl(serviceURL);
		String cacheDir = baseDir + File.separator + folder;
		File cacheFileDir = new File(cacheDir);
		if(cacheFileDir.exists()){
			if(!refresh)
				return;
		}else {
			cacheFileDir.mkdirs();
		}
		
		WPService wpsService = new WPService();
		wpsService.setUrl(this.serviceURL);
		wpsService.setVersion(SUPPORTED_VERSION);
		
		if(!wpsService.parseService()){
			logger.error("Failed to retrieve service: "+serviceURL);
			return;
		}
	
		//save the capabilities type
		WPSCapabilitiesType capabilitiesType = wpsService.getCapabilitiesType();
		if (capabilitiesType!=null) {
			try {
				capabilitiesType.save(new File(cacheDir+File.separator+"GetCapabilities.xml"));
			} catch (IOException e) {
				e.printStackTrace();
				logger.error("failed to save the capabilities type document");
			}
		}
		
		for(WPSProcess process:wpsService.getProcesses()){
			String fileName = process.getName()+".xml";
			String filePath = cacheDir+File.separator+fileName;
			ProcessDescriptionType description = process.getProcessDescriptionType();
			try {
				description.save(new File(filePath));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
}
