package com.geojmodelbuilder.core.desc;

import java.util.List;

import com.geojmodelbuilder.core.IProcess;
import com.geojmodelbuilder.core.desc.impl.Metadata;
import com.geojmodelbuilder.core.impl.ExecEnv;

public interface IProcessDesc extends IProcess{
	/**
	 * Get input parameter by name.
	 * @param name
	 */
	IInputParameterDesc getInput(String name);
	
	/**
	 * Get all the input parameters
	 */
	List<IInputParameterDesc> getInputs();
	
	/**
	 * Get output parameter by name.
	 * @param name
	 */
	IOutputParameterDesc getOutput(String name);
	
	/**
	 * Get all output parameters
	 */
	List<IOutputParameterDesc> getOutputs();
	
	void setDescription(String description);
	String getDescription();
	
	void setTitle(String title);
	String getTitle();
	
	void addMeta(Metadata metadata);
	List<Metadata> getMetas();

	void setExecEnv(ExecEnv execEnv);
	ExecEnv getExecEnv();
}
