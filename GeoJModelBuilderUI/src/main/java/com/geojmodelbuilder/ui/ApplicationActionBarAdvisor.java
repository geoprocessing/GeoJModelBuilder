package com.geojmodelbuilder.ui;

import org.eclipse.jface.action.IContributionItem;
import org.eclipse.jface.action.ICoolBarManager;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.actions.ActionFactory.IWorkbenchAction;
import org.eclipse.ui.actions.ContributionItemFactory;
import org.eclipse.ui.application.ActionBarAdvisor;
import org.eclipse.ui.application.IActionBarConfigurer;

import com.geojmodelbuilder.ui.actions.OpenWorkflowAction;
import com.geojmodelbuilder.ui.actions.ProcessAddAction;
import com.geojmodelbuilder.ui.actions.ProcessEditAction;
import com.geojmodelbuilder.ui.actions.SaveWorkflowAction;
import com.geojmodelbuilder.ui.actions.WorkflowExecuteAction;

public class ApplicationActionBarAdvisor extends ActionBarAdvisor {

//	private DemoAction demoAction = null;
	private  IWorkbenchAction existAction = null;
	private  IWorkbenchAction saveAction = null;
	private  IWorkbenchAction newWindowAction = null;
	private  IWorkbenchAction aboutAction = null;
	private IContributionItem showViewItem = null;
	private IWorkbenchAction newEditor = null;
	private WorkflowExecuteAction runAction = null;
	private OpenWorkflowAction openAction = null;
	private SaveWorkflowAction saveWorkflowAction = null;
	private ProcessAddAction addProcessAction = null;

	public ApplicationActionBarAdvisor(IActionBarConfigurer configurer) {
		super(configurer);
	}

	protected void makeActions(IWorkbenchWindow window) {
//		demoAction = new DemoAction(window);
		runAction = new WorkflowExecuteAction(window);
		register(runAction);
		
		addProcessAction = new ProcessAddAction(window);
		register(addProcessAction);
		
		saveAction = ActionFactory.SAVE.create(window);
		register(saveAction);
		
		newWindowAction = ActionFactory.OPEN_NEW_WINDOW.create(window);
		register(newWindowAction);
		newWindowAction.setText("New Window");
		
		existAction = ActionFactory.QUIT.create(window);
		register(existAction);
		
		aboutAction = ActionFactory.ABOUT.create(window);
		register(aboutAction);
		aboutAction.setText("About");
		
		showViewItem = ContributionItemFactory.VIEWS_SHORTLIST.create(window);
		newEditor = ActionFactory.NEW_EDITOR.create(window);
		
		openAction = new OpenWorkflowAction(window);
		saveWorkflowAction = new SaveWorkflowAction(window);
	}

	protected void fillMenuBar(IMenuManager menuBar) {
		
		MenuManager fileMenu = new MenuManager("&File",IWorkbenchActionConstants.M_FILE);
//		fileMenu.add(newWindowAction);
//		fileMenu.add(saveAction);
		fileMenu.add(new Separator());
		fileMenu.add(openAction);
		fileMenu.add(saveWorkflowAction);
		fileMenu.add(existAction);
		menuBar.add(fileMenu);
		
		MenuManager editMenu = new MenuManager("&Edit",IWorkbenchActionConstants.M_EDIT);
		editMenu.add(addProcessAction);
		menuBar.add(editMenu);
		
		MenuManager windowMenu = new MenuManager("&Window",IWorkbenchActionConstants.M_WINDOW);
		windowMenu.add(newWindowAction);
		
		windowMenu.add(newEditor);
		
		MenuManager showViewMenuMgr = new MenuManager("Show View", "showView");
		showViewMenuMgr.add(showViewItem);
		windowMenu.add(showViewMenuMgr);
		
		menuBar.add(windowMenu);
		
		MenuManager helpMenu = new MenuManager("&Help", IWorkbenchActionConstants.M_HELP);
		helpMenu.add(aboutAction);
		menuBar.add(helpMenu);
		
		
		
		/*MenuManager demoMenu = new MenuManager("&Demo", "");
		demoMenu.add(demoAction);
		menuBar.add(demoMenu);*/
	}

	protected void fillCoolBar(ICoolBarManager coolBar) {
		super.fillCoolBar(coolBar);
		/*ToolBarManager toolbar = new ToolBarManager(SWT.FLAT | SWT.LEFT);
		coolBar.add(new ToolBarContributionItem(toolbar, "main"));
		toolbar.add(runAction);*/
		//toolbar.add(demoAction);
	}

}
