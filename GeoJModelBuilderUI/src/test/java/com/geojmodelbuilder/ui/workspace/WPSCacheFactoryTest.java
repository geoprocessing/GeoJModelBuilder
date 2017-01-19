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

import java.io.IOException;

import net.opengis.wps.x100.ProcessDescriptionType;

import org.apache.xmlbeans.XmlException;

import com.geojmodelbuilder.ui.workspace.Workspace;

/**
 * @author Mingda Zhang
 *
 */
public class WPSCacheFactoryTest {

	public static void main(String[] args) throws XmlException, IOException {
		String url = "http://geos.whu.edu.cn:8080/wps10/WebProcessingService";
		Workspace.getInstance().setWorkspace("C:/Users/MingdaZhang/Desktop/workspace");

		ProcessDescriptionType descriptionType = WPSCacheFactory.getInstance().getProcessDescriptionType(url, "GeoBufferProcess");
		System.out.println(descriptionType);
	}

}
