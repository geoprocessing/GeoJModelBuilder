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
package com.geojmodelbuilder.ui.views;

import net.opengis.wps.x100.ProcessDescriptionType;

import org.eclipse.swt.dnd.DragSourceEvent;
import org.eclipse.swt.dnd.DragSourceListener;
import org.eclipse.swt.widgets.Tree;

import com.geojmodelbuilder.core.resource.ogc.wps.WPSProcess;
import com.geojmodelbuilder.ui.views.tree.ProcessNode;
import com.geojmodelbuilder.ui.views.tree.WPSNode;
import com.geojmodelbuilder.ui.workspace.cache.WPSCacheFactory;
/**
 * @author Mingda Zhang
 *
 */
public class ProcessDragListener implements DragSourceListener {
	private Tree tree;
//	private WPSProcessTreeNode processNode;
	private WPSProcess wpsProcess;
	public ProcessDragListener(Tree tree) {
		this.tree = tree;
	}
	
	@Override
	public void dragStart(DragSourceEvent event) {
		Object obj = this.tree.getSelection()[0].getData();
		if(obj instanceof ProcessNode){
			ProcessNode processNode = (ProcessNode)obj;
			WPSNode wpsNode = processNode.getParent();
			wpsProcess = new WPSProcess(processNode.getName());
			wpsProcess.setWPSUrl(wpsNode.getUrl());
			
			//using cache if exists.
			ProcessDescriptionType descriptionType = WPSCacheFactory.getInstance().getProcessDescriptionType(wpsNode.getUrl(), processNode.getName());
			if(descriptionType!=null)
				wpsProcess.setProcessDescriptionType(descriptionType);
			
			event.doit = true;
		}else{
			//do not drag
			event.doit = false;
		}
	}

	@Override
	public void dragSetData(DragSourceEvent event) {
		event.data = this.wpsProcess;
	}

	@Override
	public void dragFinished(DragSourceEvent event) {
	}

}
