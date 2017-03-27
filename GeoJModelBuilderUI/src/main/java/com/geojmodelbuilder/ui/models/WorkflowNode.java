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
package com.geojmodelbuilder.ui.models;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.graphics.Color;
import org.eclipse.ui.views.properties.IPropertySource;

import com.geojmodelbuilder.ui.models.links.NodeLink;
import com.geojmodelbuilder.ui.properties.NodePropertySource;
/**
 * 
 * @author Mingda Zhang
 *
 */
public class WorkflowNode extends AbstractWorkflowElement implements IAdaptable,Cloneable {
	private String name;
	private Rectangle layout;
	
	private PropertyChangeSupport listeners = new PropertyChangeSupport(this);
	
	public final static String NODE_XYLAYOUT = "node_XYlayout";
	public final static String PROPERTY_NAME = "node_rename";
	public final static String PROPERTY_COLOR = "node_color";
	public final static String ADD_IN_LINK= "add_in_link";
	public final static String REMOVE_IN_LINK= "remove_in_link";
	public final static String ADD_OUT_LINK= "add_out_link";
	public final static String REMOVE_OUT_LINK= "remove_out_link";
	public final static String ADD_NODE= "remove_self";
	
	public final static int WIDTH_DEFAULT = 80;
	public final static int HEIGHT_DEFAULT = 60;
	
	private List<NodeLink> inLinks;
	private List<NodeLink> outLinks;

	
	private NodePropertySource propertySource;

	private Color color;
	 
	private Workflow parent;

	public WorkflowNode() {
		color = ColorConstants.white;
		propertySource = new NodePropertySource(this);
		inLinks = new ArrayList<NodeLink>();
		outLinks = new ArrayList<NodeLink>();
	}

	public WorkflowNode(String name){
		this.name = name;
	}
	public WorkflowNode(Workflow parent){
		this();
		this.parent = parent;
	}
	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		Color oldColor = this.color;
		this.color = color;
		this.listeners.firePropertyChange(PROPERTY_COLOR, oldColor, color);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		String oldName = this.name;
		this.name = name;
		this.listeners.firePropertyChange(PROPERTY_NAME, oldName, name);
	}

	public Rectangle getLayout() {
		return layout;
	}

	public void setLayout(Rectangle layout) {
		Rectangle oldValue = this.layout;
		this.layout = layout;
		listeners.firePropertyChange(NODE_XYLAYOUT, oldValue, this.layout);
	}

	public void addListener(PropertyChangeListener listener) {
		this.listeners.addPropertyChangeListener(listener);
	}

	public void removeListener(PropertyChangeListener listener) {
		this.listeners.removePropertyChangeListener(listener);
	}

	public void setWorkflow(Workflow workflow){
		this.parent = workflow;
	}
	
	public Workflow getWorkflow(){
		return this.parent;
	}
	
	public void addInLink(NodeLink inLink){
		this.inLinks.add(inLink);
		this.listeners.firePropertyChange(ADD_IN_LINK, null, inLink);
	}
	
	public void removeInLink(NodeLink link){
		if(this.inLinks.contains(link)){
			this.inLinks.remove(link);
			this.listeners.firePropertyChange(REMOVE_IN_LINK, link, null);
		}
	}
	
	public void removeOutLink(NodeLink link){
		if(this.outLinks.contains(link)){
			this.outLinks.remove(link);
			this.listeners.firePropertyChange(REMOVE_OUT_LINK,link,null);
		}
	}
	
	public void addOutLink(NodeLink outLink){
		this.outLinks.add(outLink);
		this.listeners.firePropertyChange(ADD_OUT_LINK, null, outLink);
	}
	
	public List<NodeLink> getInLinks(){
		return this.inLinks;
	}
	
	private void setInLinks(List<NodeLink> inLinks){
		this.inLinks = inLinks;
	}
	
	public List<NodeLink> getOutLinks(){
		return this.outLinks;
	}
	
	private void setOutLinks(List<NodeLink> outLinks){
		this.outLinks = outLinks;
	}
	
	public void removeSelf() {
		getWorkflow().removeNode(this);
	}
	
	public void reloadSelf(){
		getWorkflow().addNode(this);
	}
	
	/*
	 * make the clone() method available by changing the type from protected to public
	 */
	@Override
	public Object clone() throws CloneNotSupportedException {
		WorkflowNode node = (WorkflowNode)super.clone();
		if(this.layout!=null)
			node.setLayout(new Rectangle(this.layout.x+10,this.layout.y+10,this.layout.width,this.layout.height));
		node.setInLinks(new ArrayList<NodeLink>());;
		node.setOutLinks(new ArrayList<NodeLink>());;
		return node;
	}
	
	
	@Override
	public Object getAdapter(Class adapter) {
		if (adapter == IPropertySource.class) {
			return propertySource;
		}
		return null;
	}
}
