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
package com.geojmodelbuilder.ui.editparts.links;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.MidpointLocator;
import org.eclipse.draw2d.PolygonDecoration;
import org.eclipse.draw2d.PolylineConnection;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.editparts.AbstractConnectionEditPart;
import org.eclipse.gef.editpolicies.ConnectionEndpointEditPolicy;
import org.eclipse.swt.SWT;

import com.geojmodelbuilder.ui.editpolicies.LinkDeleteEditPolicy;
/**
 * 
 * @author Mingda Zhang
 *
 */
public class LinkEditPart extends AbstractConnectionEditPart {
	
	private String name;
	private int lineStyle = SWT.LINE_SOLID;
	private int lineWidth = 2;
	private boolean showName = false;
	
	public LinkEditPart(){
		super();
	}
	
	@Override
	protected IFigure createFigure() {
		PolylineConnection conn = (PolylineConnection)super.createFigure();
		
		conn.setLineWidth(lineWidth);
		conn.setLineStyle(lineStyle);
		
		PolygonDecoration decoration = new PolygonDecoration();
        decoration.setTemplate(PolygonDecoration.TRIANGLE_TIP);
        conn.setTargetDecoration(decoration);
        
        if (showName && name!=null && !name.equals("")) {
        	 Label label = new Label();
             label.setText(name);
             label.setOpaque( true );
             conn.add(label, new MidpointLocator(conn, 0));
		}
       
		return conn;
	}
	
	@Override
	protected void createEditPolicies() {
		 installEditPolicy(EditPolicy.CONNECTION_ENDPOINTS_ROLE, new ConnectionEndpointEditPolicy());
		 installEditPolicy(EditPolicy.CONNECTION_ROLE, new LinkDeleteEditPolicy());
	}
	
	protected void setLineStyle(int lineStyle){
		this.lineStyle = lineStyle;
	}
	
	protected void setLineWidth(int width){
		this.lineWidth = width;
	}

	protected void setShowName(boolean isShow){
		this.showName = isShow;
	}
	protected void setName(String name){
		this.name = name;
	}
}
