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
import java.net.URL;

import org.apache.xmlbeans.XmlException;

import net.opengis.wps.x100.ProcessDescriptionType;

import com.geojmodelbuilder.ui.Activator;

/**
 * @author Mingda Zhang
 *
 */
public class DefaultWPSInitializeTest {

	public static void main(String[] args) {
		Workspace.getInstance().setWorkspace("C:/workspace");
		
		URL url = Activator.getDefault().getBundle().getEntry("resources/default_wps");  
//		URL url = Activator.getDefault().getBundle().getEntry("resources/default_wps/GeoBufferProcess.xml");  
		
		
		try {
			System.out.println(url.toString());
			ProcessDescriptionType descriptionType = ProcessDescriptionType.Factory.parse(url.openStream());
			System.out.println(descriptionType);
		} catch (XmlException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
