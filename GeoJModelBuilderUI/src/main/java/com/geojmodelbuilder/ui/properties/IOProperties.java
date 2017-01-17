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
package com.geojmodelbuilder.ui.properties;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import com.geojmodelbuilder.ui.Activator;

/**
 * @author Mingda Zhang
 *
 */
public class IOProperties {
	private Properties properties;
	private static IOProperties instance;
	private List<String> mimeTypes;
	
	private IOProperties(){
		properties = new Properties();
		URL url = Activator.getDefault().getBundle().getEntry("properties/io.properties");  
		try {
			properties.load(url.openStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static IOProperties getInstance(){
		if(instance == null)
			instance = new IOProperties();
		
		return instance;
	}
	
	public List<String> getMimeTypes(){
		if(mimeTypes !=null)
			return mimeTypes;
		
		mimeTypes = new ArrayList<String>();
		for(Object obj:this.properties.keySet()){
			mimeTypes.add(obj.toString());
		}
		
		return mimeTypes;
	}
}
