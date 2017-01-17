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
package com.geojmodelbuilder.ui.editors;

import org.eclipse.gef.EditPartViewer;
import org.eclipse.gef.Request;
import org.eclipse.gef.dnd.AbstractTransferDropTargetListener;
import org.eclipse.gef.requests.CreateRequest;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;

import com.geojmodelbuilder.ui.models.WorkflowProcess;
import com.geojmodelbuilder.ui.requests.AbstractElementCreationFactory;

/**
 * @author Mingda Zhang
 *
 */
public class ProcessDropListener extends AbstractTransferDropTargetListener {

	public ProcessDropListener(EditPartViewer viewer) {
		super(viewer,TextTransfer.getInstance());
		// TODO Auto-generated constructor stub
	}
	
	public ProcessDropListener(EditPartViewer viewer,
			Transfer xfer) {
		super(viewer, xfer);
	}
	@Override
	protected Request createTargetRequest() {
		// TODO Auto-generated method stub
		System.out.println("target request");
		CreateRequest request = new CreateRequest();
		request.setFactory(new AbstractElementCreationFactory(String.class));
		return request;
	}
	
	@Override
	public void drop(DropTargetEvent event) {
		// TODO Auto-generated method stub
		System.out.println("dropped.");
		super.drop(event);
	}
	@Override
	protected void handleDrop() {
		// TODO Auto-generated method stub
		System.out.println("handle drop");
		getCurrentEvent().detail = DND.DROP_COPY;
		super.handleDrop();
	}
	
	
	@Override
	protected void updateTargetRequest() {
		// TODO Auto-generated method stub
		 ((CreateRequest)getTargetRequest()).setLocation(getDropLocation());
	}

}
