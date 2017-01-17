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

import com.geojmodelbuilder.ui.workspace.Workspace;
import com.geojmodelbuilder.ui.workspace.cache.WPSCacheThread;

/**
 * @author Mingda Zhang
 *
 */
public class WPSCacheThreadTest {
	public static void main(String[] args){
		Workspace.getInstance().setWorkspace("C:/Users/MingdaZhang/Desktop/workspace0236");
		WPSCacheThread cacheThread = new WPSCacheThread("http://geos.whu.edu.cn:8080/wps10/WebProcessingService");
		cacheThread.start();
	}
}
