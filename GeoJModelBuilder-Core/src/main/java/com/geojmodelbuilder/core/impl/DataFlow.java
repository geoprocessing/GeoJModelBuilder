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
package com.geojmodelbuilder.core.impl;

import com.geojmodelbuilder.core.IDataFlow;
import com.geojmodelbuilder.core.IExchange;
import com.geojmodelbuilder.core.IProcess;
/**
 * 
 * @author Mingda Zhang
 *
 */
public class DataFlow implements IDataFlow {

	private IProcess sourceProcess;
	private IProcess targetProcess;
	private IExchange sourceExchange;
	private IExchange targetExchange;

	public DataFlow(IProcess sourceProcess, IExchange sourceExchange,
			IProcess targetProcess, IExchange targetExchange) {
		this.sourceProcess = sourceProcess;
		this.sourceExchange = sourceExchange;
		this.targetProcess = targetProcess;
		this.targetExchange = targetExchange;
	}

	public IProcess getSourceProcess() {
		return this.sourceProcess;
	}

	public IProcess getTargetProcess() {
		return this.targetProcess;
	}

	public IExchange getSourceExchange() {
		return this.sourceExchange;
	}

	public IExchange getTargetExchange() {
		return this.targetExchange;
	}

}
