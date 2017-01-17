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
package com.geojmodelbuilder.ui.utils;

/**
 * 
 * @author Mingda Zhang
 *
 */
public class CloneUtils {
	/*public static <T extends Serializable> T clone(T obj){  
        T cloneObj = null;  
        try {  
            ByteArrayOutputStream out = new ByteArrayOutputStream();  
            ObjectOutputStream obs = new ObjectOutputStream(out);  
            obs.writeObject(obj);  
            obs.close();  
              
            ByteArrayInputStream ios = new ByteArrayInputStream(out.toByteArray());  
            ObjectInputStream ois = new ObjectInputStream(ios);  
            
            cloneObj = (T) ois.readObject();  
            ois.close();  
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
        return cloneObj;  
    }  */
}
