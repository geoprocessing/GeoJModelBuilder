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
package com.geojmodelbuilder.ui.actions;

import org.eclipse.jface.action.Action;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.ui.IWorkbenchWindow;
/**
 * 
 * @author Mingda Zhang
 *
 */
public class DemoAction extends Action{
	private IWorkbenchWindow window;
	public DemoAction(IWorkbenchWindow window){
		this.window = window;
		this.setText("Demo");
		
		/*ImageDescriptor imgDes = WorkbenchImages.getImageDescriptor(
				IWorkbenchGraphicConstants.IMG_ETOOL_HELP_CONTENTS);
		this.setImageDescriptor(imgDes);*/
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		super.run();
		MessageBox mb = new MessageBox(window.getShell(), SWT.OK);
		mb.setMessage("Hello world!");
		mb.setText("Demo");
		mb.open();
	}
}
