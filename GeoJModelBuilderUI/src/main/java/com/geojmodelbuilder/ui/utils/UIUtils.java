package com.geojmodelbuilder.ui.utils;

import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Monitor;
import org.eclipse.swt.widgets.Shell;

public class UIUtils {
	private static UIUtils instance;
	private UIUtils(){}
	
	public static UIUtils getInstance(){
		if(instance == null)
			instance = new UIUtils();
		
		return instance;
	}
	
	public void centerDialog(Shell shell){
		Monitor primary = shell.getMonitor(); 
		Rectangle bounds = primary.getBounds (); 
		Rectangle rect = shell.getBounds (); 
		int x = bounds.x + (bounds.width - rect.width) / 2; 
		int y = bounds.y + (bounds.height - rect.height) / 2; 
		shell.setLocation (x, y); 
	}
}
