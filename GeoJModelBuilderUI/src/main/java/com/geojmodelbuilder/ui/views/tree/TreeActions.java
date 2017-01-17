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
package com.geojmodelbuilder.ui.views.tree;

import java.io.IOException;

import net.opengis.wps.x100.ProcessDescriptionType;

import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.PlatformUI;
import org.n52.wps.client.WPSClientException;
import org.n52.wps.client.WPSClientSession;

import com.geojmodelbuilder.core.resource.ogc.wps.WPSProcess;
import com.geojmodelbuilder.core.resource.ogc.wps.WPService;
import com.geojmodelbuilder.ui.dialogs.ServiceAddDialog;
import com.geojmodelbuilder.ui.dialogs.WarningDialog;
import com.geojmodelbuilder.ui.workspace.cache.WPSCacheThread;

/**
 * @author Mingda Zhang
 *
 */
public class TreeActions {
	private TreeViewer treeViewer;
	private Tree tree;
	public TreeActions(TreeViewer treeViewer){
		this.treeViewer = treeViewer;
		this.tree = treeViewer.getTree();
	}
	
	public Action getAddAction(){
		Action addAction = new Action("Add") {
			@Override
			public void run() {
				System.out.println("Add action");
				TreeItem treeItem = tree.getSelection()[0];
				Object selectedObj = treeItem.getData();
				if(selectedObj instanceof GeoprocessingNode){
					Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
					ServiceAddDialog dialog = new ServiceAddDialog(shell);
						
					if(dialog.open() == Window.OK){
						WPService wpsService = dialog.getWPS();
//						wpService.parseService();
						if(!wpsService.parseService()){
							MessageDialog.openWarning(shell, "Warnning", "Failed to parse the service");
							return;
						}
						
						WPSCacheThread wpsCacheThread = new WPSCacheThread(wpsService.getUrl());
						wpsCacheThread.start();
						
						GeoprocessingNode parent = (GeoprocessingNode)selectedObj;
						WPSNode wpsNode = new WPSNode(wpsService.getName(), parent);
						wpsNode.setUrl(wpsService.getUrl());
						
						for(WPSProcess process:wpsService.getProcesses()){
							ProcessNode processNode = new ProcessNode(process.getName(), wpsNode);
							wpsNode.addProcess(processNode);
						}
				
						parent.addWPS(wpsNode);
						treeViewer.refresh();
					}
				}
			}
		};
		
		return addAction;
	}
	
	public Action getDeleteAction(){
		Action deleteAction = new Action("Delete") {
			@Override
			public void run() {
				System.out.println("Delete Action");
				if(tree.getSelectionCount()==0)
					return;
				
				TreeItem treeItem = tree.getSelection()[0];
				Object selectedObj = treeItem.getData();
				if(selectedObj instanceof WPSNode){
					WPSNode wpsNode = (WPSNode)selectedObj;
					wpsNode.getParent().removeChild(wpsNode);
					treeViewer.refresh();
				}
			}
		};
		return deleteAction;
	}
	
	
	public Action getSearchAction(){
		Action searchAction = new Action("Search") {
			@Override
			public void run() {
				System.out.println("Search Action");
			}
		};
		
		return searchAction;
	}
}
