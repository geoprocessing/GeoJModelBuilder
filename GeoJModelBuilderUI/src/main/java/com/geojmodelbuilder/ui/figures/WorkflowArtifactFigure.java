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

import org.eclipse.draw2d.Ellipse;
/**
 * 
 * @author Mingda Zhang
 *
 */
public class WorkflowArtifactFigure extends WorkflowNodeFigure {
	
	public WorkflowArtifactFigure(){
		super(new Ellipse());
		/*getSymbolFigure().setBackgroundColor(ColorConstants.red);
		getSymbolFigure().setOpaque(true);*/
//		setColor(ColorConstants.red);
	}
	

}
