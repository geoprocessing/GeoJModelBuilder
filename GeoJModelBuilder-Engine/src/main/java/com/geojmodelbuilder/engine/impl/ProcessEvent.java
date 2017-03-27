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
import com.geojmodelbuilder.engine.IProcessEvent;

/**
 * @author Mingda Zhang
 *
 */
public class ProcessEvent implements IProcessEvent {
	private EventType eventType;
	private IProcess process;
	
	public ProcessEvent(EventType eventType, IProcess process){
		this.eventType = eventType;
		this.process = process;
	}
	
	public IProcess getSource() {
		return this.process;
	}

	public EventType getType() {
		return this.eventType;
	}

}
