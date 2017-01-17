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
package com.geojmodelbuilder.engine;


/**
 * @author Mingda Zhang
 *
 */
public interface IPublisher {
	
	/**
	 * Adds a listener to specific event
	 * @param listener 
	 * @param eventType 
	 */
	void subscribe(IListener listener,IEvent.EventType eventType);
	
	/**
	 * Remove the listener
	 * @param listener
	 * @param eventType
	 */
	void unSubscribe(IListener listener,IEvent.EventType eventType);
	
	/**
	 * Specific kind of event happened, notify all the listeners.
	 */
	void sendEvent(IEvent event);
}
