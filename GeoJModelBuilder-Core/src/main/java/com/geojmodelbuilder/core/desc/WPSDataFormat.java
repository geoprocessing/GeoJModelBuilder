package com.geojmodelbuilder.core.desc;

public class WPSDataFormat {
	private String mimeType, encoding;
	private boolean defaultF;
	
	public WPSDataFormat(){}

	public WPSDataFormat(String mimeType, String encoding){
		this.mimeType = mimeType;
		this.encoding = encoding;
	}
	
	public String getMimeType() {
		return mimeType;
	}
	
	

	public void setMimeType(String mimeType) {
		this.mimeType = mimeType;
	}

	public String getEncoding() {
		return encoding;
	}

	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}

	public boolean isDefaultF() {
		return defaultF;
	}

	public void setDefaultF(boolean defaultF) {
		this.defaultF = defaultF;
	}
	
}
