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

import org.eclipse.draw2d.ChopboxAnchor;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
/**
 * 
 * @author Mingda Zhang
 *
 */
public class VertexAnchor extends ChopboxAnchor {
	public VertexAnchor(IFigure owner) {
		super(owner);
	}

	public Point getLocation(Point reference) {
		Rectangle r = Rectangle.SINGLETON;
		r.setBounds(getBox());
		r.translate(-1, -1);
		r.resize(1, 1);

		getOwner().translateToAbsolute(r);
		float centerX = r.x + 0.5f * r.width;
		float centerY = r.y + 0.5f * r.height;

		float dx = reference.x - centerX;
		float dy = reference.y - centerY;

		if (Math.abs(dx) <= Math.abs(dy)) {
			if (dy > 0) {
				centerY += r.height / 2;
			} else {
				centerY -= r.height / 2;
			}
		} else {
			if (dx > 0) {
				centerX += r.width / 2;
			} else {
				centerX -= r.width / 2;
			}
		}

		return new Point(Math.round(centerX), Math.round(centerY));
	}
}
