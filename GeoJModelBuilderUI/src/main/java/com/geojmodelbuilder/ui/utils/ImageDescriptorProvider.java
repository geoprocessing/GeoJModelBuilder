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
package com.geojmodelbuilder.ui.utils;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.jface.resource.ImageDescriptor;

import com.geojmodelbuilder.ui.Activator;

/**
 * @author Mingda Zhang
 *
 */
public class ImageDescriptorProvider {
	private Map<String, ImageDescriptor> imageDescriptorMap;
	private static ImageDescriptorProvider instance;
	public static String IMG_LAUNCH_RUN = "image_descriptor_launch_run";
	public static String IMG_RESOURCE_TREE_PARENT = "image_descriptor_resource_tree_parent";
	public static String IMG_RESOURCE_TREE_CHILD = "image_descriptor_resource_tree_child";
	private Map<String, String> iconMap;
	
	private ImageDescriptorProvider(){
		imageDescriptorMap = new HashMap<String, ImageDescriptor>();
		iconMap = new HashMap<String,String>();
		iconMap.put(IMG_LAUNCH_RUN, "icons/launch_run.gif");
		iconMap.put(IMG_RESOURCE_TREE_PARENT, "icons/folder.png");
		iconMap.put(IMG_RESOURCE_TREE_CHILD, "icons/process.png");
	}

	public static ImageDescriptorProvider getInstance(){
		if(instance == null)
			instance = new ImageDescriptorProvider();
		
		return instance;
	}
	
	public ImageDescriptor getImageDescriptor(String ID){
		if(!iconMap.containsKey(ID))
			return null;
		
		ImageDescriptor descriptor = imageDescriptorMap.get(ID);
		if(descriptor!=null)
			return descriptor;
		
		String icon = iconMap.get(ID);
		URL url = Activator.getDefault().getBundle().getEntry(icon);       
		if(url == null)
			return null;
		
		descriptor = ImageDescriptor.createFromURL(url);
		imageDescriptorMap.put(ID, descriptor);
		return descriptor;
	}
}
