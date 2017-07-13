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
package com.geojmodelbuilder.ui.views.wps;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.PlatformUI;

import com.geojmodelbuilder.core.resource.ogc.wps.WPSProcess;
import com.geojmodelbuilder.core.resource.ogc.wps.WPService;
import com.geojmodelbuilder.ui.dialogs.ServiceAddDialog;
import com.geojmodelbuilder.ui.dialogs.TreeNodeSearchDialog;
import com.geojmodelbuilder.ui.workspace.WPSCacheThread;

/**
 * @author Mingda Zhang
 *
 */
public class WPSTreeActionProvider {
	private TreeViewer treeViewer;
	private Tree tree;
	private Shell shell;
	public WPSTreeActionProvider(TreeViewer treeViewer){
		this.treeViewer = treeViewer;
		this.tree = treeViewer.getTree();
		this.shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
	}
	
	public Action getAddAction(){
		Action addAction = new Action("Add") {
			@Override
			public void run() {
				System.out.println("Add action");
				TreeItem treeItem = tree.getSelection()[0];
				Object selectedObj = treeItem.getData();
				if(selectedObj instanceof GeoprocessingNode){
//					Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
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
	
	public Action getUpdateAction(){
		Action updateAction = new Action("Update") {
			@Override
			public void run() {
				System.out.println("Update Action");
				if(tree.getSelectionCount()==0)
					return;
				
				TreeItem treeItem = tree.getSelection()[0];
				Object selectedObj = treeItem.getData();
				if(selectedObj instanceof WPSNode){
					WPSNode wpsNode = (WPSNode)selectedObj;
					WPService wpsService = new WPService();
					wpsService.setName(wpsNode.getName());
					wpsService.setUrl(wpsNode.getUrl());
					
					if(!wpsService.reparseService()){
						MessageDialog.openWarning(shell, "Warnning", "Failed to parse the service");
						return;
					}
					
					WPSNode wpsNode2 = new WPSNode(wpsService.getName(), wpsNode.getParent());
					wpsNode2.setUrl(wpsService.getUrl());
					
					for(WPSProcess process:wpsService.getProcesses()){
						ProcessNode processNode = new ProcessNode(process.getName(), wpsNode);
						wpsNode2.addProcess(processNode);
					}
			
					wpsNode.getParent().addWPS(wpsNode2);
					wpsNode.getParent().removeChild(wpsNode);
					
					treeViewer.refresh();
					
					WPSCacheThread wpsCacheThread = new WPSCacheThread(wpsService.getUrl(),true);
					wpsCacheThread.start();
				}
			}
		};
		
		return updateAction;
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
				if(tree.getSelectionCount() == 0)
					return;
				
				TreeNodeSearchDialog dialog = new TreeNodeSearchDialog(shell);
				if(dialog.open()!=Window.OK)
					return;
				
				String pattern = dialog.getPattern();
				TreeItem selectedItem= tree.getSelection()[0];
				Object selectObj = selectedItem.getData();
				List<TreeItem> targetItems = null;
				if(selectObj instanceof GeoprocessingNode){
					targetItems = findInGeoprocessingNode(selectedItem, pattern); 
				}else if (selectObj instanceof WPSNode) {
					targetItems = findInWPSNode(selectedItem, pattern);
				}
				if(targetItems != null && targetItems.size()>0)
					tree.setSelection(targetItems.toArray(new TreeItem[targetItems.size()]));
				
			}
		};
		
		return searchAction;
	}
	
	private List<TreeItem> findInGeoprocessingNode(TreeItem geoprocessingNodeItem,String pattern){
		List<TreeItem> items = new ArrayList<TreeItem>();
		
		for(TreeItem wpsNodeItem:geoprocessingNodeItem.getItems()){
			items.addAll(findInWPSNode(wpsNodeItem,pattern));
		}
		
		return items;
	}
	
	private List<TreeItem> findInWPSNode(TreeItem wpsNodeItem, String pattern){
		List<TreeItem> items = new ArrayList<TreeItem>();
		
		for(TreeItem processItem:wpsNodeItem.getItems()){
			int n = processItem.getText().toLowerCase().indexOf(pattern.toLowerCase());
			if(n!=-1)
				items.add(processItem);
		}
		
		return items;
	}
}
