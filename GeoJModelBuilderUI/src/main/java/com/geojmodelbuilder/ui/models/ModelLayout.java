/**
 * Copyright (C) 2013 - 2016 Wuhan University,
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

import java.util.ArrayList;
import java.util.List;

import org.eclipse.draw2d.geometry.Rectangle;

import com.geojmodelbuilder.ui.models.links.NodeLink;

/**
 * @author Mingda Zhang
 *
 */
public class ModelLayout {
	private static ModelLayout instance;
	
	private ModelLayout(){}
	
	public static ModelLayout getInstance(){
		if (instance == null) {
			instance = new ModelLayout();
		}
		
		return instance;
	}
	
	public void addLayout(Workflow workflow){
		int xLoc = 80;
		int yLoc = 30;
		List<WorkflowProcess> processHeads = new ArrayList<WorkflowProcess>();
		for(WorkflowProcess process:workflow.getAllProcess()){
			if (process.getInLinks().size() == 0) {
				processHeads.add(process);
			}
		}
		
		for(WorkflowProcess process:processHeads){
			addLayoutFromHead(process,new Rectangle(xLoc, yLoc, WorkflowNode.WIDTH_DEFAULT, WorkflowNode.HEIGHT_DEFAULT));
			yLoc += WorkflowNode.HEIGHT_DEFAULT * 1.5;
		}
	}
	
	private void addLayoutFromHead(WorkflowProcess processHead,Rectangle layoutInit){
		if(processHead.getLayout()!=null)
			return;
		
		int xInit = layoutInit.x;
		int yInit = layoutInit.y;
		int width = layoutInit.width;
		int height = layoutInit.height;
		processHead.setLayout(layoutInit);
		xInit += width * 1.2;
		for(ProcessOutputArtifact output:processHead.getOutputArtifacts()){
			output.setLayout(new Rectangle(xInit,yInit,width,height));
			xInit += width * 1.5;
			
			for(NodeLink link: output.getOutLinks()){
				if (!(link.getTargetNode() instanceof WorkflowProcess)) {
					continue;
				}
				
				WorkflowProcess process = (WorkflowProcess)link.getTargetNode();
				addLayoutFromHead(process, new Rectangle(xInit, yInit, width, height));
				yInit += height;
			}
		}
		
		/*for(NodeLink link: processHead.getOutLinks()){
			if (!(link.getTargetNode() instanceof WorkflowProcess)) {
				continue;
			}
			
			WorkflowProcess process = (WorkflowProcess)link.getTargetNode();
			addLayoutFromHead(process, new Rectangle(xInit, yInit, width, height));
			yInit += height;
		}*/
	}
}
