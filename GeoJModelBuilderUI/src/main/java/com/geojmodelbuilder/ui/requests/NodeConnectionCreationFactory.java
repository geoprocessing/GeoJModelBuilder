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
package com.geojmodelbuilder.ui.requests;

import org.eclipse.gef.requests.CreationFactory;

import com.geojmodelbuilder.ui.models.links.NodeLink;
/**
 * 
 * @author Mingda Zhang
 *
 */
public class NodeConnectionCreationFactory implements CreationFactory {

	@Override
	public Object getNewObject() {
		return new NodeLink();
	}

	@Override
	public Object getObjectType() {
		return NodeLink.class;
	}

}
