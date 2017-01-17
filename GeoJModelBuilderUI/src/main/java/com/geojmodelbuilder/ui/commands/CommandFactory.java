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
package com.geojmodelbuilder.ui.commands;

import com.geojmodelbuilder.ui.models.links.ControlFlow;
import com.geojmodelbuilder.ui.models.links.DataFlow;
import com.geojmodelbuilder.ui.models.links.NodeLink;
/**
 * 
 * @author Mingda Zhang
 *
 */
public class CommandFactory {
	public static LinkCreateCommand getLinkCreateCommand(NodeLink link) {
		
		if(link instanceof DataFlow)
			return new DataFlowCreateCommand();
		
		if(link instanceof ControlFlow)
			return new ControlFlowCreateCommand();
		
		return new LinkCreateCommand();
		
	}
}