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

import com.geojmodelbuilder.core.IProcess;
import com.geojmodelbuilder.engine.IEvent;

/**
 * @author Mingda Zhang
 *
 */
public class Event implements IEvent {
	private EventType eventType;
	private IProcess process;
	
	public Event(EventType eventType, IProcess processExec){
		this.eventType = eventType;
		this.process = processExec;
	}
	
	public IProcess getSource() {
		return this.process;
	}

	public EventType getType() {
		return this.eventType;
	}

}
