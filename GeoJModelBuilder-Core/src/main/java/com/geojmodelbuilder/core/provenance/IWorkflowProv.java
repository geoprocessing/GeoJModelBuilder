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
package com.geojmodelbuilder.core.provenance;

import java.util.Date;

import com.geojmodelbuilder.core.IWorkflow;
import com.geojmodelbuilder.core.instance.IWorkflowInstance;
/**
 * 
 * @author Mingda Zhang
 *
 */
public interface IWorkflowProv extends IWorkflow<IProcessProv>{
	Date getStartTime();
	Date getEndTime();
	boolean getStatus();
	IWorkflowInstance getWorkflow();
}
