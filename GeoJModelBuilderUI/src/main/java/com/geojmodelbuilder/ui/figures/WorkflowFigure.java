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

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.XYLayout;
import org.eclipse.draw2d.geometry.Rectangle;
/**
 * 
 * @author Mingda Zhang
 *
 */
public class WorkflowFigure extends Figure {
	private Label labelName;
	private Label labelProcessCount;

	public WorkflowFigure() {
		setLayoutManager(new XYLayout());

		
		labelName = new Label();
		labelName.setForegroundColor(ColorConstants.blue);
		add(labelName);
		setConstraint(labelName, new Rectangle(5, 5, -1, -1));
		// add a name

		labelProcessCount = new Label();
		labelProcessCount.setForegroundColor(ColorConstants.green);
		//add a process count
		//add(labelProcessCount);
		//setConstraint(labelProcessCount, new Rectangle(5, 17, -1, -1));

		//setBorder(new LineBorder(ColorConstants.lightGray));
	}

	public void setBound(Rectangle rect) {
		getParent().setConstraint(this, rect);
	}

	public void setName(String name) {
		labelName.setText(name);
	}

	public void setCount(int num) {
		labelProcessCount.setText(num + "");
	}
}
