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

import org.eclipse.gef.dnd.TemplateTransfer;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.resource.LocalResourceManager;
import org.eclipse.jface.resource.ResourceManager;
import org.eclipse.jface.viewers.DelegatingStyledCellLabelProvider.IStyledLabelProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.StyledString;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.part.ViewPart;

import com.geojmodelbuilder.ui.utils.ImageDescriptorProvider;
import com.geojmodelbuilder.ui.views.tree.GeoprocessingNode;
import com.geojmodelbuilder.ui.views.tree.ITreeNode;
import com.geojmodelbuilder.ui.views.tree.ProcessNode;
import com.geojmodelbuilder.ui.views.tree.ResourceRoot;
import com.geojmodelbuilder.ui.views.tree.TreeActions;
import com.geojmodelbuilder.ui.views.tree.WPSNode;
import com.geojmodelbuilder.ui.workspace.WorkspaceReader;

/**
 * @author Mingda Zhang
 *
 */
public class WPSResourceTreeView extends ViewPart {
	public static final String ID = "com.geojmodelbuilder.ui.view.wpstree";
	private TreeViewer treeViewer;
	private Tree tree;
	private TreeActions treeActions;
	private ResourceRoot root;
	@Override
	public void createPartControl(Composite parent) {
		treeViewer = new TreeViewer(parent, SWT.MULTI | SWT.H_SCROLL
				| SWT.V_SCROLL);
		treeViewer.setContentProvider(new ViewContentProvider());
		treeViewer.getTree().setHeaderVisible(false);
		
		treeActions = new TreeActions(treeViewer);
		
		initializeTree();
		treeViewer.setInput(root);
		treeViewer.setLabelProvider(new ViewLabelProvider());
		treeViewer.expandAll();

		tree = treeViewer.getTree();
		tree.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		tree.addMouseListener(new MouseAdapter() {
			public void mouseDown(MouseEvent e) {
				initializeMenu();
			}
		});
		
		Transfer[] types = new Transfer[]{TemplateTransfer.getInstance()};
		treeViewer.addDragSupport(DND.DROP_COPY, types, new ProcessDragListener(tree));
	}


	private void initializeMenu() {
		if(tree.getSelectionCount() == 0)
			return;
		TreeItem treeItem = tree.getSelection()[0];
		Object selectedObj = treeItem.getData();
		MenuManager mgr = new MenuManager();
		
		if(selectedObj instanceof GeoprocessingNode){
			mgr.add(this.treeActions.getSearchAction());
			mgr.add(this.treeActions.getAddAction());
		}else if (selectedObj instanceof WPSNode) {
			mgr.add(this.treeActions.getSearchAction());
			mgr.add(this.treeActions.getDeleteAction());
		}
		
		Menu menu = mgr.createContextMenu(tree);
		tree.setMenu(menu);
	}

	private ResourceRoot initializeTree() {
		root = new ResourceRoot("Resources");
		
		GeoprocessingNode wpsNodes = WorkspaceReader.getInstance().getWPSResouces();
		if(wpsNodes!=null){
			wpsNodes.setParent(root);
			root.addGeoprocessing(wpsNodes);
		}
		/*GeoprocessingNode wpsList = new GeoprocessingNode(
				"GeoprocessingServices",root);
		root.addGeoprocessing(wpsList);
		
		WPSNode wps1 = new WPSNode("GeoPW", wpsList);
		wps1.setUrl("http://geos.whu.edu.cn:8080/wps10/WebProcessingService");
		wps1.addProcess(new ProcessNode("GeoOverlayProcess", wps1));
		wps1.addProcess(new ProcessNode("RasterColorsProcess", wps1));
		wps1.addProcess(new ProcessNode("RasterMapcalcProcess", wps1));
		wps1.addProcess(new ProcessNode("RasterBinaryProcess", wps1));
		wps1.addProcess(new ProcessNode("RasterBlendProcess", wps1));
		wpsList.addWPS(wps1);*/
		return root;
	}

	class ViewContentProvider implements ITreeContentProvider {
		public void inputChanged(Viewer v, Object oldInput, Object newInput) {
		}

		@Override
		public void dispose() {

		}

		@Override
		public Object[] getElements(Object inputElement) {
			return getChildren(inputElement);
		}

		@Override
		public Object[] getChildren(Object element) {
			
			if (element instanceof ITreeNode) {
				return ((ITreeNode)element).getChildren();
			}
			
			return null;
		}

		@Override
		public Object getParent(Object element) {
			if(element instanceof ITreeNode)
				return ((ITreeNode)element).getParent();
			
			return null;
		}

		@Override
		public boolean hasChildren(Object element) {
			
			if(element instanceof ITreeNode)
				return ((ITreeNode)element).hasChildren();
			
			return false;
		}

	}

	class ViewLabelProvider extends LabelProvider implements
			IStyledLabelProvider {
		
		private ResourceManager resourceManager;
		
		public ViewLabelProvider() {
		}

		 protected ResourceManager getResourceManager() {
             if (resourceManager == null) {
                     resourceManager = new LocalResourceManager(JFaceResources.getResources());
             }
             return resourceManager;
     }
		@Override
		public String getText(Object element) {
			if(element instanceof ITreeNode)
				return ((ITreeNode)element).getName();
			return null;
		}

		@Override
		public StyledString getStyledText(Object element) {
			return null;
		}
		
		@Override
		public Image getImage(Object element) {
			ImageDescriptor descriptor;
			if(element instanceof ProcessNode){
				descriptor = ImageDescriptorProvider.getInstance().getImageDescriptor(ImageDescriptorProvider.IMG_RESOURCE_TREE_CHILD);
				return getResourceManager().createImage(descriptor);
			}else {
				descriptor = ImageDescriptorProvider.getInstance().getImageDescriptor(ImageDescriptorProvider.IMG_RESOURCE_TREE_PARENT);
				return getResourceManager().createImage(descriptor);
			}
		}
		
		@Override
		public void dispose() {
			super.dispose();
			if (resourceManager != null) {
                resourceManager.dispose();
                resourceManager = null;
        }
		}

	}

	@Override
	public void setFocus() {
		treeViewer.getControl().setFocus();
	}

}
