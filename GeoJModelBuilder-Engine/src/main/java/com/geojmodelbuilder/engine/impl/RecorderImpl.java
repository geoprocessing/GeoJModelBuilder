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
package com.geojmodelbuilder.engine.impl;

import com.geojmodelbuilder.engine.IRecorder;

/**
 * @author Mingda Zhang
 *
 */
public class RecorderImpl implements IRecorder{

	private StringBuffer strBuf;
	
	public RecorderImpl(){
		strBuf = new StringBuffer();
	}
	
	public void appendMsg(String msg) {
		this.strBuf.append(msg);
	}

	public String getRecord() {
		return this.strBuf.toString();
	}

}
