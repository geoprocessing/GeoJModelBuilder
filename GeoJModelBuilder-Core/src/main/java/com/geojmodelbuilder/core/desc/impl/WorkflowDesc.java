package com.geojmodelbuilder.core.desc.impl;

import com.geojmodelbuilder.core.desc.IProcessDesc;
import com.geojmodelbuilder.core.desc.IWorkflowDesc;
import com.geojmodelbuilder.core.impl.AbstractWorkflowImpl;

public class WorkflowDesc extends AbstractWorkflowImpl<IProcessDesc> implements IWorkflowDesc{
	private String title;

	public String getTitle() {
		return this.title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	
}
