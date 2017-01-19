package com.geojmodelbuilder.ui;

import org.eclipse.swt.graphics.Point;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.application.ActionBarAdvisor;
import org.eclipse.ui.application.IActionBarConfigurer;
import org.eclipse.ui.application.IWorkbenchWindowConfigurer;
import org.eclipse.ui.application.WorkbenchWindowAdvisor;

import com.geojmodelbuilder.ui.editors.ModelEditor;
import com.geojmodelbuilder.ui.editors.ModelEditorInput;

public class ApplicationWorkbenchWindowAdvisor extends WorkbenchWindowAdvisor {

    public ApplicationWorkbenchWindowAdvisor(IWorkbenchWindowConfigurer configurer) {
        super(configurer);
    }

    public ActionBarAdvisor createActionBarAdvisor(IActionBarConfigurer configurer) {
        return new ApplicationActionBarAdvisor(configurer);
    }
    
    public void preWindowOpen() {
        IWorkbenchWindowConfigurer configurer = getWindowConfigurer();
        configurer.setInitialSize(new Point(600, 400));
        configurer.setShowCoolBar(true);
        configurer.setShowStatusLine(true);
        configurer.setTitle("GeoJModelBuilder"); 
    }
    
    @Override
   	public void postWindowCreate() {
   		try {
   			IWorkbenchPage page = PlatformUI.getWorkbench()
   					.getActiveWorkbenchWindow().getActivePage();
   			page.openEditor(new ModelEditorInput("ModelEditorInput"), ModelEditor.ID,false);
   			
   			//maximize the window
   			getWindowConfigurer().getWindow().getShell().setMaximized(true);  
   		} catch (Exception e) {
   			e.printStackTrace();
   		}
   	}
}
