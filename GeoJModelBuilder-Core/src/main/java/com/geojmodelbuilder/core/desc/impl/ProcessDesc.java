package com.geojmodelbuilder.core.desc.impl;

import java.util.ArrayList;
import java.util.List;

import com.geojmodelbuilder.core.desc.IInputParameterDesc;
import com.geojmodelbuilder.core.desc.IOutputParameterDesc;
import com.geojmodelbuilder.core.desc.IProcessDesc;
import com.geojmodelbuilder.core.impl.AbstractProcessImpl;
import com.geojmodelbuilder.core.impl.ExecEnv;

public class ProcessDesc extends AbstractProcessImpl<IInputParameterDesc, IOutputParameterDesc> implements IProcessDesc{

	private String title;
	private List<Metadata> metadatas = new ArrayList<Metadata>();
	private ExecEnv execEnv;
	
	public void setTitle(String title) {
		this.title = title;
	}

	public String getTitle() {
		return this.title;
	}

	public void addMeta(Metadata metadata) {
		this.metadatas.add(metadata);
	}

	public List<Metadata> getMetas() {
		return this.metadatas;
	}

	public void setExecEnv(ExecEnv execEnv) {
		this.execEnv = execEnv;
	}

	public ExecEnv getExecEnv() {
		return this.execEnv;
	}

}
