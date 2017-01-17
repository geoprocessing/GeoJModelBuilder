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
package com.geojmodelbuilder.engine.impl;

import java.util.ArrayList;

import com.geojmodelbuilder.engine.IEvent;
import com.geojmodelbuilder.engine.IListener;

/**
 * @author Mingda Zhang
 *
 */
public class Listeners extends ArrayList<IListener>{
	private static final long serialVersionUID = 1L;
	
	public void onEvent(IEvent event){
		for(IListener listener:this){
			listener.onEvent(event);
		}
	}
}
