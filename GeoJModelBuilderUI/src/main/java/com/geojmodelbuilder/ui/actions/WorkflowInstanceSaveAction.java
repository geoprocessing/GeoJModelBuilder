package com.geojmodelbuilder.ui.actions;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ui.IWorkbenchWindow;
import org.geojmodelbuilder.semantic.serialization.Instance2RDF;

import com.geojmodelbuilder.core.instance.IWorkflowInstance;
import com.geojmodelbuilder.ui.models.WorkflowAspect;
import com.geojmodelbuilder.ui.run.UIModles2Instance;

public class WorkflowInstanceSaveAction extends WorkflowAspectSaveAction {

	public static final String ID = "workflow.instance.save";
	public WorkflowInstanceSaveAction(IWorkbenchWindow window) {
		super(window,WorkflowAspect.Instance);
	}
	
	@Override
	boolean saveAspect() {
		UIModles2Instance uiModles2Instance = new UIModles2Instance(workflow);
		IWorkflowInstance workflowInstance = uiModles2Instance.getExecutableWorkflow();
		if (workflowInstance == null) {
			String err = uiModles2Instance.getErrInfo();
			MessageDialog.openInformation(window.getShell(), "Message", "Failed to get the workflow instance. "+err);
			return false;
		}
		workflow.addInstance(workflowInstance);
		Instance2RDF instance2rdf = new Instance2RDF(workflowInstance);
		return instance2rdf.save(this.filePath);
	}
}
