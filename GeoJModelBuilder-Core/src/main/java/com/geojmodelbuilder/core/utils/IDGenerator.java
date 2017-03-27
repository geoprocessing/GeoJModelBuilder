/**
 * Copyright (C) 2013 - 2016 Wuhan University,
 *                           Center for Geographic Analysis, Harvard University
 * 
 * This program is free software; you can redistribute and/or modify it under 
 * the terms of the GNU General Public License version 2 as published by the 
 * Free Software Foundation.
 * 
 * This program is distributed WITHOUT ANY WARRANTY; even without the implied
 * WARRANTY OF MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 */
package com.geojmodelbuilder.core.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import com.geojmodelbuilder.core.IDataFlow;
import com.geojmodelbuilder.core.IExchange;
import com.geojmodelbuilder.core.IIdentifiable;
import com.geojmodelbuilder.core.ILink;
import com.geojmodelbuilder.core.IProcess;

/**
 * @author Mingda Zhang
 *
 */
public class IDGenerator {
	public static Date prevDate = new Date();
	
	public static String dateID(){
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmssSSS");
		Date currDate = new Date();
		if(!currDate.after(prevDate)){
			currDate.setTime(prevDate.getTime()+1);
		}
		prevDate = currDate;
		return formatter.format(currDate);
	}
	
	public static String uuid(){
		String s = UUID.randomUUID().toString(); 
        return s.substring(0,8)+s.substring(9,13)+s.substring(14,18)+s.substring(19,23)+s.substring(24); 
	}
	
	public static String uri(IIdentifiable obj){
		return obj.getNamespace() + obj.getID();
	}
	
	public static String exchangeID(IExchange exchangeItem){
		IProcess process = exchangeItem.getOwner();
		return process.getID() + "_" + exchangeItem.getName();
	}
	
	public static String linkID(ILink link){
		IProcess srcProcess = link.getSourceProcess();
		IProcess tgtProcess = link.getSourceProcess();
		String id = srcProcess.getID();
		if(link instanceof IDataFlow){
			IDataFlow dataFlow = (IDataFlow)link;
			IExchange srcExchange = dataFlow.getSourceExchange();
			IExchange tgtExchange = dataFlow.getTargetExchange();
			id += "-"+srcExchange.getName();
			id += "_"+tgtProcess.getID() + "-"+tgtExchange.getName();
		}else {
			id += "_" + tgtProcess.getID();
		}
		return id;
	}
	
	public static String getUri(IIdentifiable obj,String defaultNS){
		String id = obj.getID();
		
		String namespace = obj.getNamespace();
		if(ValidateUtil.isStrEmpty(namespace))
			namespace = defaultNS;
		
		return namespace + id;
	}
	
	public static void main(String[] args){
		for(int i=0;i<10;i++){
			System.out.println(dateID());
		}
		
		System.out.println(uuid());
	}
}
