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

import com.geojmodelbuilder.core.IProcess;

/**
 * The engine is based on event mechanism.
 * 
 * @author Mingda Zhang
 *
 */
public interface IProcessEvent {
	/**
	 * The source that generated the event.
	 */
	IProcess getSource();
	
	/**
	 * The type of the event.
	 */
	EventType getType();
	
	/**
     * Enumeration for event types.
     * Statuses of execution: Executing, Failed, Succeeded
     * Commands from the monitor: stop
     */
	public enum EventType{
		
		/**
		 * The process is executing. 
		 * The progress of execution may be obtained.
		 */
		Executing,
		
		/**
		 * The execution is failed. 
		 */
		Failed,
		
		/**
		 * The execution is successful.
		 */
		Succeeded,
		
		/**
		 * The workflow is stopped.
		 */
		Stopped,
		
		/**
		 * The process is ready to be executed.
		 */
		Ready,
		
		/**
		 * One process is executed in a workflow.
		 */
		StepPerformed
	}
}
