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

import java.io.File;
import java.net.URL;
import java.text.DateFormat;
import java.util.Date;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.resource.LocalResourceManager;
import org.eclipse.jface.resource.ResourceManager;
import org.eclipse.jface.viewers.DelegatingStyledCellLabelProvider;
import org.eclipse.jface.viewers.DelegatingStyledCellLabelProvider.IStyledLabelProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.StyledString;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.TreeViewerColumn;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;
import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;
/**
 * @author Mingda Zhang
 *
 */
public class ResourceTreeView extends ViewPart{
	public static final String ID = "com.geojmodelbuilder.ui.view.resourcetree"; 
	private TreeViewer viewer;
	 private static final DateFormat dateFormat = DateFormat.getDateInstance();
	@Override
	public void createPartControl(Composite parent) {
		viewer = new TreeViewer(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL);
        viewer.setContentProvider(new ViewContentProvider());
        viewer.getTree().setHeaderVisible(true);

        TreeViewerColumn mainColumn = new TreeViewerColumn(viewer, SWT.NONE);
        mainColumn.getColumn().setText("Name");
        mainColumn.getColumn().setWidth(300);
        mainColumn.setLabelProvider(
                        new DelegatingStyledCellLabelProvider(
                                        new ViewLabelProvider(createImageDescriptor())));

        TreeViewerColumn modifiedColumn = new TreeViewerColumn(viewer, SWT.NONE);
        modifiedColumn.getColumn().setText("Last Modified");
        modifiedColumn.getColumn().setWidth(100);
        modifiedColumn.getColumn().setAlignment(SWT.RIGHT);
        modifiedColumn
                        .setLabelProvider(new DelegatingStyledCellLabelProvider(
                                        new FileModifiedLabelProvider(dateFormat)));

        TreeViewerColumn fileSizeColumn = new TreeViewerColumn(viewer, SWT.NONE);
        fileSizeColumn.getColumn().setText("Size");
        fileSizeColumn.getColumn().setWidth(100);
        fileSizeColumn.getColumn().setAlignment(SWT.RIGHT);
        fileSizeColumn.setLabelProvider(new DelegatingStyledCellLabelProvider(
                        new FileSizeLabelProvider()));

        viewer.setInput(File.listRoots());
	}
	
	

	 private ImageDescriptor createImageDescriptor() {
         Bundle bundle = FrameworkUtil.getBundle(ResourceTreeView.class);
//         Bundle bundle = FrameworkUtil.getBundle(ViewLabelProvider.class);
         URL url = FileLocator.find(bundle, new Path("icons/folder.png"), null);
         url = bundle.getEntry("icons/folder.png");
         return ImageDescriptor.createFromURL(url);
 }

 class ViewContentProvider implements ITreeContentProvider {
         public void inputChanged(Viewer v, Object oldInput, Object newInput) {
         }

         @Override
         public void dispose() {
         }

         @Override
         public Object[] getElements(Object inputElement) {
                 return (File[]) inputElement;
         }

         @Override
         public Object[] getChildren(Object parentElement) {
                 File file = (File) parentElement;
                 return file.listFiles();
         }

         @Override
         public Object getParent(Object element) {
                 File file = (File) element;
                 return file.getParentFile();
         }

         @Override
         public boolean hasChildren(Object element) {
                 File file = (File) element;
                 if (file.isDirectory()) {
                         return true;
                 }
                 return false;
         }

 }

 class ViewLabelProvider extends LabelProvider implements IStyledLabelProvider {

         private ImageDescriptor directoryImage;
         private ResourceManager resourceManager;

         public ViewLabelProvider(ImageDescriptor directoryImage) {
                 this.directoryImage = directoryImage;
         }

         @Override
         public StyledString getStyledText(Object element) {
                 if (element instanceof File) {
                         File file = (File) element;
                         StyledString styledString = new StyledString(getFileName(file));
                         String[] files = file.list();
                         if (files != null) {
                                 styledString.append(" ( " + files.length + " ) ", StyledString.COUNTER_STYLER);
                         }
                         return styledString;
                 }
                 return null;
         }

         @Override
         public Image getImage(Object element) {
                 if (element instanceof File) {
                         if (((File) element).isDirectory()) {
                                 return getResourceManager().createImage(directoryImage);
                         }
                 }

                 return super.getImage(element);
         }

         @Override
         public void dispose() {
                 // garbage collection system resources
                 if (resourceManager != null) {
                         resourceManager.dispose();
                         resourceManager = null;
                 }
         }

         protected ResourceManager getResourceManager() {
                 if (resourceManager == null) {
                         resourceManager = new LocalResourceManager(JFaceResources.getResources());
                 }
                 return resourceManager;
         }

         private String getFileName(File file) {
                 String name = file.getName();
                 return name.isEmpty() ? file.getPath() : name;
         }
 }

 class FileModifiedLabelProvider extends LabelProvider implements IStyledLabelProvider {

         private DateFormat dateLabelFormat;

         public FileModifiedLabelProvider(DateFormat dateFormat) {
                 dateLabelFormat = dateFormat;
         }

         @Override
         public StyledString getStyledText(Object element) {
                 if (element instanceof File) {
                         File file = (File) element;
                         long lastModified = file.lastModified();
                         return new StyledString(dateLabelFormat.format(new Date(lastModified)));
                 }
                 return null;
         }
 }

 class FileSizeLabelProvider extends LabelProvider implements IStyledLabelProvider {

         @Override
         public StyledString getStyledText(Object element) {
                 if (element instanceof File) {
                         File file = (File) element;
                         if (file.isDirectory()) {
                                 // a directory is just a container and has no size
                                 return new StyledString("0");
                         }
                         return new StyledString(String.valueOf(file.length()));
                 }
                 return null;
         }
 }
	@Override
	public void setFocus() {
		viewer.getControl().setFocus();
	}

}
