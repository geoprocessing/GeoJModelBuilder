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
package com.geojmodelbuilder.ui.figures;

import org.eclipse.draw2d.BorderLayout;
import org.eclipse.draw2d.ConnectionAnchor;
import org.eclipse.draw2d.EllipseAnchor;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.RectangleFigure;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.graphics.Color;
/**
 * 
 * @author Mingda Zhang
 *
 */
public class WorkflowNodeFigure extends Figure {
	private Label labelName;
	private Figure symbol;
	private Color color;

	// The default figure is rectangle
	public WorkflowNodeFigure() {
		this(new RectangleFigure());
	}

	public WorkflowNodeFigure(Figure symbol) {
		this.symbol = symbol;
		this.labelName = new Label();
//		this.labelName.setFont(new Font(null, null));

		this.symbol.setLayoutManager(new BorderLayout());
		this.symbol.add(labelName, BorderLayout.CENTER);

		this.setLayoutManager(new BorderLayout());
		this.add(this.symbol, BorderLayout.CENTER);

		// setColor(ColorConstants.white);
		this.symbol.setOpaque(true);
	}

	public void setColor(Color color) {
		this.color = color;
		this.symbol.setBackgroundColor(color);
	}

	public Color getColor() {
		return color;
	}

	public void setLayout(Rectangle layout) {
		getParent().setConstraint(this, layout);
	}

	public void setName(String name) {
		this.labelName.setText(name);
	}

	protected Figure getSymbolFigure() {
		return this.symbol;
	}
	
	public ConnectionAnchor getSourceConnectionAnchor(){
		return new EllipseAnchor(getSymbolFigure());
	}
	
	public ConnectionAnchor getTargetConnectionAnchor(){
		return new EllipseAnchor(getSymbolFigure());
	}
}
