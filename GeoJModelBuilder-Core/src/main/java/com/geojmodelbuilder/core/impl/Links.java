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
package com.geojmodelbuilder.core.impl;

import com.geojmodelbuilder.core.ILink;
import com.geojmodelbuilder.core.IProcess;

import java.util.ArrayList;

/**
 * @author Mingda Zhang
 *
 */
public class Links extends ArrayList<ILink>{
	private static final long serialVersionUID = 1L;
	
	public Links getLinksWithSource(IProcess source){
		Links sourceLinks = new Links();
		for(ILink link:this){
			if(link.getSourceProcess() == source)
				sourceLinks.add(link);
		}
		return sourceLinks;
	}
	
	public Links getLinksWithTarget(IProcess target){
		Links targetLinks = new Links();
		for(ILink link:this){
			if(link.getTargetProcess() == target)
				targetLinks.add(link);
		}
		return targetLinks;
	}
}
