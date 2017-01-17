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
package com.geojmodelbuilder.ui.workspace;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.geojmodelbuilder.ui.views.tree.GeoprocessingNode;
import com.geojmodelbuilder.ui.views.tree.ProcessNode;
import com.geojmodelbuilder.ui.views.tree.WPSNode;

/**
 * @author Mingda Zhang
 *
 */
public class WorkspaceReader {
	private static WorkspaceReader instance;
	private Logger logger;
	private WorkspaceReader(){
		logger = LoggerFactory.getLogger(WorkspaceReader.class);
	}
	
	public static WorkspaceReader getInstance(){
		if(instance == null)
			instance = new WorkspaceReader();
		
		return instance;
	}
	
	public GeoprocessingNode getWPSResouces(){
		String configPath = Workspace.getInstance().getServiceConfig();
		if(configPath == null || configPath.equals(""))
			return null;
		
		File configFile = new File(configPath);
		if(!configFile.exists()){
			logger.error("There is no configuaration file in this workspace.");
			return null;
		}
		
		DocumentBuilder builder = null;
		try {
			builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
			logger.error("failed to read the configuration "+ configPath);
			logger.error(e.getMessage());
			return null;
		}
		
		Document doc = null;
		try {
			doc = builder.parse(new File(configPath));
		} catch (SAXException | IOException e) {
			e.printStackTrace();
			logger.error("failed to parse the configuration "+ configPath);
			logger.error(e.getMessage());
			return null;
		}
		
		if(doc == null)
			return null;
		
		NodeList wpsList = doc.getElementsByTagName("WPSService");
		if(wpsList == null || wpsList.getLength() == 0)
			return null;
		
		GeoprocessingNode wpsTreeNodes = new GeoprocessingNode(
				"GeoprocessingServices",null);
		
		for(int i=0;i<wpsList.getLength();i++){
			Node wpsNode = wpsList.item(i);
			NamedNodeMap attrMap = wpsNode.getAttributes();
			
			Node node;
			node = attrMap.getNamedItem("name");
			if(node == null)
				continue;
			String name = node.getNodeValue();
			
			node = attrMap.getNamedItem("url");
			if(node == null)
				continue;
			String url = node.getNodeValue();
			
			WPSNode wpsTreeNode = new WPSNode(name, wpsTreeNodes);
			wpsTreeNode.setUrl(url);
			wpsTreeNodes.addWPS(wpsTreeNode);
			
			NodeList processList = wpsNode.getChildNodes();
			
			for(int j=0;j<processList.getLength();j++){
				Node process = processList.item(j);
				String processIdentifier = process.getTextContent();
				if(processIdentifier == null || processIdentifier.trim().equals(""))
					continue;
				wpsTreeNode.addProcess(new ProcessNode(processIdentifier.trim(), wpsTreeNode));
			}
		}
		
		return wpsTreeNodes;
	}
}
