package com.geojmodelbuilder.xml.util;

import java.util.HashMap;
import java.util.Map;

public class NSFactory {
	
	public static Map NSMap(){
		Map nsMap = new HashMap(); 
		nsMap.put("http://geos.whu.edu.cn/wls/1.0","wls");
		nsMap.put("http://www.opengis.net/wps/2.0","wps");
		nsMap.put( "http://www.opengis.net/ows/2.0","ows");
		nsMap.put("http://www.w3.org/1999/xlink","xlink");
		return nsMap;
	}
}
