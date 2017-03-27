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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Mingda Zhang
 *
 */
public class FileTools {
	
	private static FileTools instance;
	private Logger logger;
	private FileTools(){
		logger = LoggerFactory.getLogger(FileTools.class);
	}
	
	public static FileTools getInstance(){
		if(instance == null)
			instance = new FileTools();
		
		return instance;
	}
	/**
	 * Writes InputStream to the file.
	 * @param in
	 * 			input stream.
	 * @param dst
	 * 			target file.
	 * @return
	 */
	public boolean copyFile(InputStream in, File dst) {
		try {
			OutputStream out = new FileOutputStream(dst);

			byte[] buf = new byte[1024];
			int len;
			while ((len = in.read(buf)) > 0) {
				out.write(buf, 0, len);
			}
			in.close();
			out.close();
			logger.info("Writes inputstream to "+dst.getAbsolutePath());
			return true;
		} catch (Exception e) {
			logger.error("Error occured when writes inputstrem to "+dst.getAbsolutePath());
			logger.error(e.getMessage());
			return false;
		}

	}
	
	public boolean copyFile(File src, File dst) {
		try {
			InputStream in = new FileInputStream(src);
			OutputStream out = new FileOutputStream(dst);

			byte[] buf = new byte[1024];
			int len;
			while ((len = in.read(buf)) > 0) {
				out.write(buf, 0, len);
			}
			in.close();
			out.close();
			logger.info("Copy file from "+src.getAbsolutePath()+" to "+dst.getAbsolutePath());
			return true;
		} catch (Exception e) {
			logger.error("Error occured when copy file from "+src.getAbsolutePath()+" to "+dst.getAbsolutePath());
			logger.error(e.getMessage());
			return false;
		}

	}
	
	public boolean copyFile(URL srcUrl, String destination){
		
		try {
			File destinationFile = new File(destination);
			if(!destinationFile.exists())
				destinationFile.createNewFile();
			
			InputStream in = srcUrl.openStream();
			OutputStream out = new FileOutputStream(destination);

			byte[] buf = new byte[1024];
			int len;
			while ((len = in.read(buf)) > 0) {
				out.write(buf, 0, len);
			}
			
			in.close();
			out.close();
			logger.info("Copy file from "+srcUrl.toString()+" to "+destination);
			return true;
		} catch (Exception e) {
			logger.error("Error occured when copy file from "+srcUrl.toString()+" to "+destination);
			logger.error(e.getMessage());
			return false;
		}
	}
	
	
}
