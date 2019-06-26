package com.geojmodelbuilder.xml.util;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.xmlbeans.XmlOptions;

public class UtilFactory {
	
	public static Map NSMap(){
		Map nsMap = new HashMap(); 
		nsMap.put("http://geos.whu.edu.cn/xpso/1.0","xpso");
		nsMap.put("http://www.opengis.net/wps/2.0","wps");
		nsMap.put( "http://www.opengis.net/ows/2.0","ows");
		nsMap.put("http://www.w3.org/1999/xlink","xlink");
		nsMap.put("http://www.w3.org/ns/prov#","prov");
		return nsMap;
	}
	
	public static XmlOptions XmlOptions()
	{
		XmlOptions xmlOptions = new XmlOptions();
		xmlOptions.setUseDefaultNamespace();
		xmlOptions.setSaveSuggestedPrefixes(UtilFactory.NSMap());
		xmlOptions.setSavePrettyPrint();
		xmlOptions.setSaveAggressiveNamespaces();
		return xmlOptions;
	}
	
	public static Calendar ToCalendar(Date date){
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return cal;
	}
}
