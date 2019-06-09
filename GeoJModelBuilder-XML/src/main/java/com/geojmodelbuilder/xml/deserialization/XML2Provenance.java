package com.geojmodelbuilder.xml.deserialization;

import java.io.File;
import java.util.Calendar;

import javax.xml.namespace.QName;

import org.apache.xmlbeans.XmlException;

import cn.edu.whu.geos.xpso.x10.ProcessExecType;
import cn.edu.whu.geos.xpso.x10.WorkflowExecInfoDocument;
import cn.edu.whu.geos.xpso.x10.WorkflowExecInfoType;
import cn.edu.whu.geos.xpso.x10.WorkflowExecType;

import com.geojmodelbuilder.core.instance.impl.WorkflowInstance;
import com.geojmodelbuilder.core.provenance.IWorkflowProv;
import com.geojmodelbuilder.core.provenance.impl.ProcessProv;
import com.geojmodelbuilder.core.provenance.impl.WorkflowProv;

public class XML2Provenance {

	private StringBuffer errInfo = new StringBuffer();

	public IWorkflowProv parse(String xmlText) {
		WorkflowExecInfoDocument empDoc = null;
		try {
			empDoc = WorkflowExecInfoDocument.Factory.parse(xmlText);
		} catch (XmlException e) {
			e.printStackTrace();
			errInfo.append(e.getMessage());
			return null;
		}
		return parse(empDoc);
	}

	public IWorkflowProv parse(File xmlFile) {
		WorkflowExecInfoDocument empDoc = null;
		try {
			empDoc = WorkflowExecInfoDocument.Factory.parse(xmlFile);
		} 
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			errInfo.append(e.getMessage());
			return null;
		}

		return parse(empDoc);
	}

	public WorkflowProv parse(WorkflowExecInfoDocument empDoc) {
		WorkflowInstance workflowInstance = new WorkflowInstance();

		WorkflowProv workflowProv = new WorkflowProv(workflowInstance);
		
		WorkflowExecInfoType execInfoType = empDoc.getWorkflowExecInfo();
		String workflowProvId = execInfoType.getId().toString();
		workflowProv.setID(workflowProvId);
		
		WorkflowExecType execType = execInfoType.getWorkflowExec();
		Calendar startTime = execType.getStartTime();
		if(startTime!=null)
			workflowProv.setStartTime(startTime.getTime());
		
		Calendar endTime = execType.getEndTime();
		if(endTime!=null)
			workflowProv.setEndTime(endTime.getTime());
		
		workflowProv.setStatus(execType.getSuccessed());
		
		
		for (ProcessExecType procExecType:execInfoType.getProcessExecArray()){
			ProcessProv proProv = new ProcessProv();
			QName idQ = procExecType.getId();
			if(idQ!=null)
				proProv.setID(idQ.toString());
		}
		return workflowProv;
	}
	// return the detailed information if error occurs
	public String getErrInfo() {
		return this.errInfo.toString();
	}
}
