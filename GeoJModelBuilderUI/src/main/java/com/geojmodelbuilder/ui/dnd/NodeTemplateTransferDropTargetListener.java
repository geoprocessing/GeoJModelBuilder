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
package com.geojmodelbuilder.ui.dnd;

import org.eclipse.gef.EditPartViewer;
import org.eclipse.gef.dnd.TemplateTransferDropTargetListener;
import org.eclipse.gef.requests.CreationFactory;

import com.geojmodelbuilder.core.resource.ogc.wps.WPSProcess;
import com.geojmodelbuilder.ui.requests.AbstractElementCreationFactory;
import com.geojmodelbuilder.ui.requests.WPSProcessCreationFactory;
/**
 * 
 * @author Mingda Zhang
 *
 */
public class NodeTemplateTransferDropTargetListener extends
		TemplateTransferDropTargetListener {

	public NodeTemplateTransferDropTargetListener(EditPartViewer viewer) {
		super(viewer);
	}

	@Override
	protected CreationFactory getFactory(Object template) {
		if (template instanceof WPSProcess) {
			return new WPSProcessCreationFactory((WPSProcess)template);
		}else {
			return new AbstractElementCreationFactory((Class<?>) template);
		}
	}
}
