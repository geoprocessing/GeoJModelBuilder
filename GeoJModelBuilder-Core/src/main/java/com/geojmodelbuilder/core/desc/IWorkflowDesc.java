package com.geojmodelbuilder.core.desc;

import com.geojmodelbuilder.core.IWorkflow;

public interface IWorkflowDesc extends IWorkflow<IProcessDesc>{
	String getTitle();
	void setTitle(String title);
}
