package com.geojmodelbuilder.ui;

import org.eclipse.jface.action.ICoolBarManager;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.action.ToolBarContributionItem;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.swt.SWT;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.actions.ActionFactory.IWorkbenchAction;
import org.eclipse.ui.application.ActionBarAdvisor;
import org.eclipse.ui.application.IActionBarConfigurer;

import com.geojmodelbuilder.ui.actions.DemoAction;

public class ApplicationActionBarAdvisor extends ActionBarAdvisor {

	private DemoAction demoAction = null;
	private  IWorkbenchAction existAction = null;
	private  IWorkbenchAction saveAction = null;
	private  IWorkbenchAction newWindowAction = null;
	private  IWorkbenchAction aboutAction = null;

	public ApplicationActionBarAdvisor(IActionBarConfigurer configurer) {
		super(configurer);
	}

	protected void makeActions(IWorkbenchWindow window) {
		demoAction = new DemoAction(window);
		
		saveAction = ActionFactory.SAVE.create(window);
		register(saveAction);
		
		newWindowAction = ActionFactory.OPEN_NEW_WINDOW.create(window);
		register(newWindowAction);
		
		existAction = ActionFactory.QUIT.create(window);
		register(existAction);
		
		aboutAction = ActionFactory.ABOUT.create(window);
		register(aboutAction);
		aboutAction.setText("About");
	}

	protected void fillMenuBar(IMenuManager menuBar) {
		
		MenuManager fileMenu = new MenuManager("&File",IWorkbenchActionConstants.M_FILE);
		fileMenu.add(newWindowAction);
		fileMenu.add(saveAction);
		fileMenu.add(new Separator());
		fileMenu.add(existAction);
		menuBar.add(fileMenu);
		
		MenuManager helpMenu = new MenuManager("&Help", IWorkbenchActionConstants.M_HELP);
		helpMenu.add(aboutAction);
		menuBar.add(aboutAction);
		
		/*MenuManager demoMenu = new MenuManager("&Demo", "");
		demoMenu.add(demoAction);
		menuBar.add(demoMenu);*/
	}

	protected void fillCoolBar(ICoolBarManager coolBar) {
		ToolBarManager toolbar = new ToolBarManager(SWT.FLAT | SWT.RIGHT);
		coolBar.add(new ToolBarContributionItem(toolbar, "main"));
		//toolbar.add(demoAction);
	}

}
