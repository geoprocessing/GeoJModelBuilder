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

import java.util.ArrayList;
import java.util.List;

import com.geojmodelbuilder.core.IProcess;
/**
 * 
 * @author Mingda Zhang
 *
 */
public class ProcessExecutors extends ArrayList<ProcessExecutor>{
	private static final long serialVersionUID = 1L;
	
	public ProcessExecutors(){
		super();
	}
	
	public ProcessExecutors(List<? extends IProcess> processes){
		super();
		for(IProcess process:processes){
			this.add(new ProcessExecutor(process));
		}
	}
	
	public ProcessExecutor getExecutor(ProcessExecutor executor){
		for(ProcessExecutor processExecutor:this){
			if(processExecutor == executor)
				return processExecutor;
		}
		
		return null;
	}
	
	public ProcessExecutor getExecutor(IProcess process){
		for(ProcessExecutor executor:this){
			if(executor.getProcess() == process)
				return executor;
		}
		return null;
	}
	
	@Override
	public boolean remove(Object o) {
		if(o instanceof IProcess){
			return super.remove(getExecutor((IProcess)o));
		}
		
		return super.remove(o);
		
	}
}
