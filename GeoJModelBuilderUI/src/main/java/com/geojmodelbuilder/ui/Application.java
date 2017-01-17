package com.geojmodelbuilder.ui;

import org.eclipse.equinox.app.IApplication;
import org.eclipse.equinox.app.IApplicationContext;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PlatformUI;

import com.geojmodelbuilder.ui.dialogs.WarningDialog;
import com.geojmodelbuilder.ui.dialogs.WorkspaceLauncherDialog;
import com.geojmodelbuilder.ui.workspace.Workspace;

/**
 * This class controls all aspects of the application's execution
 */
public class Application implements IApplication {

	public Object start(IApplicationContext context) throws Exception {
		Display display = PlatformUI.createDisplay();
		
		/*Location instanceLoc = Platform.getInstallLocation();
		System.out.println(instanceLoc.getURL());*/
		
		//Set the workspace directory
		WorkspaceLauncherDialog launcherDialog = new WorkspaceLauncherDialog(null);
		if (launcherDialog.open() == Window.OK) {
			String path = launcherDialog.getSelectedPath();
			if(path == null || path.equals("")){
				WarningDialog dialog = new WarningDialog(null, "There is on workspace selected.");
				dialog.open();
				return EXIT_OK;
			}
			
			Workspace.getInstance().setWorkspace(path);
			Workspace.getInstance().validate();
			
		}else {
			return EXIT_OK;
		}
	    
		try {
			int returnCode = PlatformUI.createAndRunWorkbench(display, new ApplicationWorkbenchAdvisor());
			if (returnCode == PlatformUI.RETURN_RESTART)
				return IApplication.EXIT_RESTART;
			else
				return IApplication.EXIT_OK;
		} finally {
			display.dispose();
		}
		
	}

	public void stop() {
		if (!PlatformUI.isWorkbenchRunning())
			return;
		final IWorkbench workbench = PlatformUI.getWorkbench();
		final Display display = workbench.getDisplay();
		display.syncExec(new Runnable() {
			public void run() {
				if (!display.isDisposed())
					workbench.close();
			}
		});
	}
	
}
