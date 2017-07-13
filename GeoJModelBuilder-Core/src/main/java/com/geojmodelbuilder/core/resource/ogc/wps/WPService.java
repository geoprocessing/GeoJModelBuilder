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
package com.geojmodelbuilder.core.resource.ogc.wps;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import net.opengis.wps.x100.CapabilitiesDocument;
import net.opengis.wps.x100.ProcessDescriptionType;
import net.opengis.wps.x100.WPSCapabilitiesType;

import org.n52.wps.client.WPSClientException;
import org.n52.wps.client.WPSClientSession;

/**
 * @author Mingda Zhang
 *
 */
public class WPService {
	private List<WPSProcess> processes;
	private String name;
	private String version;
	private String url;
	private WPSCapabilitiesType capabilitiesType;
	
	public WPService(){
		processes = new ArrayList<WPSProcess>();
	}

	public List<WPSProcess> getProcesses() {
		return processes;
	}
	
	/**
	 * Set processes directly without parsing.
	 * Maybe be set by the local caches.
	 */
	public void setProcesses(List<WPSProcess> processes){
		this.processes = processes;
	}
	
	
	public WPSCapabilitiesType getCapabilitiesType(){
		return this.capabilitiesType;
	}
	
	public boolean reparseService(){
		WPSClientSession clientSession = WPSClientSession.getInstance();
		clientSession.disconnect(url);
		return parseService();
	}
	/**
	 * Parse using the service address.
	 * Retrieve the processes hold by this service.
	 */
	public boolean parseService(){
		WPSClientSession clientSession = WPSClientSession.getInstance();
		try {
			clientSession.connect(url);
		} catch (WPSClientException e) {
			e.printStackTrace();
			return false;
		}
		
		
		//get the capabilities
		CapabilitiesDocument capabilitiesDoc = clientSession.getWPSCaps(url);
		if(capabilitiesDoc != null)
			this.capabilitiesType = capabilitiesDoc.getCapabilities();
		
		try {
			ProcessDescriptionType[] processDescriptionTypes = clientSession.getAllProcessDescriptions(url);
			for(ProcessDescriptionType type:processDescriptionTypes){
				WPSProcess process = new WPSProcess(type.getIdentifier().getStringValue());
				process.setProcessDescriptionType(type);
				process.setWPSUrl(url);
				this.processes.add(process);
			}
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		
		return true;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
	
	public WPSProcess getProcess(String name){
		for(WPSProcess process:this.processes){
			if(process.getName().equals(name))
				return process;
		}
		return null;
	}
}
