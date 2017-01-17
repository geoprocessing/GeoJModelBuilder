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

import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.Shape;
import org.eclipse.draw2d.geometry.PointList;
import org.eclipse.draw2d.geometry.Rectangle;

/**
 * Draws a diamond whose size is determined by the bounds set to it.
 * @author Mingda Zhang
 *
 */
public class DiamondFigure extends Shape {

	private PointList points = new PointList(4);

	@Override
	protected void fillShape(Graphics graphics) {
		graphics.fillPolygon(updatePoints());
	}

	@Override
	protected void outlineShape(Graphics graphics) {
		graphics.drawPolygon(updatePoints());
	}

	private PointList updatePoints() {
		Rectangle bounds = getBounds();
		points.removeAllPoints();
		points.addPoint(bounds.getTop());
		points.addPoint(bounds.getRight());
		points.addPoint(bounds.getBottom());
		points.addPoint(bounds.getLeft());
		return points;
	}
}
