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
package com.geojmodelbuilder.ui.models.links;


/**
 * 
 * @author Mingda Zhang
 *
 */
public class FalseThenFlow extends ControlFlow {
	@Override
	public void connect() {
		super.connect();
		getCondition().setFalseThenFlow(this);
	}
	
	@Override
	public void disconnect() {
		super.disconnect();
		getCondition().setFalseThenFlow(null);
	}

	@Override
	public boolean isTrue() {
		return false;
	}
}
