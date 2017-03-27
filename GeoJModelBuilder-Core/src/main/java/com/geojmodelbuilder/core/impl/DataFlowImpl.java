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
public class DataFlowImpl extends AbstractLinkImpl<IProcess,IProcess> implements IDataFlow {

	private IExchange sourceExchange;
	private IExchange targetExchange;

	public DataFlowImpl(IProcess sourceProcess, IExchange sourceExchange,
			IProcess targetProcess, IExchange targetExchange) {
		setSourceProcess(sourceProcess);
		setTargetProcess(targetProcess);
		this.sourceExchange = sourceExchange;
		this.targetExchange = targetExchange;
	}


	public IExchange getSourceExchange() {
		return this.sourceExchange;
	}

	public IExchange getTargetExchange() {
		return this.targetExchange;
	}

}
