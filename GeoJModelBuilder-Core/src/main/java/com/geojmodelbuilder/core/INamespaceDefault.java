/**
 * Copyright (C) 2013 - 2016 Wuhan University,
 *                           Center for Geographic Analysis, Harvard University
 * 
 * This program is free software; you can redistribute and/or modify it under 
 * the terms of the GNU General Public License version 2 as published by the 
 * Free Software Foundation.
 * 
 * This program is distributed WITHOUT ANY WARRANTY; even without the implied
 * WARRANTY OF MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 */
package com.geojmodelbuilder.core;

/**
 * @author Mingda Zhang
 *
 */
public interface INamespaceDefault {
	String BASE = "http://www.gis.harvard.edu/opso/";
	
	String INOUTPORT = BASE + "inoutport/";
	String CONDITION= BASE + "condition/";
	
	String TEMPLATE_WORKFLOW = BASE + "template/workflow/";
	String TEMPLATE_PROCESS= BASE + "template/process/";
	String TEMPLATE_ARTIFACT= BASE + "template/artifact/";
	
	String INSTANCE_WORKFLOW= BASE + "instance/workflow/";
	String INSTANCE_PROCESS= BASE + "instance/process/";
	String INSTANCE_ARTIFACT= BASE + "instance/artifact/";
	
	String EXECUTION_WORKFLOW= BASE + "execution/workflow/";
	String EXECUTION_PROCESS= BASE + "execution/process/";
	String EXECUTION_ARTIFACT= BASE + "execution/artifact/";
}
