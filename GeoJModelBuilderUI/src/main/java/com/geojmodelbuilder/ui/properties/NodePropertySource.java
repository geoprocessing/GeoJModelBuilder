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
package com.geojmodelbuilder.ui.properties;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.ui.views.properties.ColorPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertySource;
import org.eclipse.ui.views.properties.PropertyDescriptor;
import org.eclipse.ui.views.properties.TextPropertyDescriptor;

import com.geojmodelbuilder.ui.models.WorkflowNode;
/**
 * 
 * @author Mingda Zhang
 *
 */
public class NodePropertySource implements IPropertySource {

	private WorkflowNode model;

	public NodePropertySource(WorkflowNode model) {
		this.model = model;
	}

	@Override
	public Object getEditableValue() {
		return null;
	}

	@Override
	public IPropertyDescriptor[] getPropertyDescriptors() {
		List<PropertyDescriptor> properties = new ArrayList<PropertyDescriptor>();
		properties.add(new ColorPropertyDescriptor(WorkflowNode.PROPERTY_COLOR,
				"Color"));

		properties.add(new TextPropertyDescriptor(WorkflowNode.PROPERTY_NAME,
				"Name"));
		
		return properties.toArray(new IPropertyDescriptor[0]);
	}

	@Override
	public Object getPropertyValue(Object id) {
		if (id.equals(WorkflowNode.PROPERTY_COLOR)) {
			return model.getColor().getRGB();
		} else if (id.equals(WorkflowNode.PROPERTY_NAME)) {
			return model.getName();
		}
		return null;
	}

	@Override
	public boolean isPropertySet(Object id) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void resetPropertyValue(Object id) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setPropertyValue(Object id, Object value) {
		if (id.equals(WorkflowNode.PROPERTY_NAME)) {
			model.setName(value.toString());
		}else if (id.equals(WorkflowNode.PROPERTY_COLOR)) {
			model.setColor(new Color(null, (RGB)value));
		}
	}

}
