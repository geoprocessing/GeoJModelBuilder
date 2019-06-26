package com.geojmodelbuilder.core.impl;

public class WPSEnv extends ExecEnv{
	private String url, mode;

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getMode() {
		return mode;
	}

	public void setMode(String mode) {
		this.mode = mode;
	}
}
