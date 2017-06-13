package com.geojmodelbuilder.ui.actions;

import org.eclipse.ui.IWorkbenchWindow;
import org.geojmodelbuilder.semantic.serialization.Template2RDF;

import com.geojmodelbuilder.ui.models.WorkflowAspect;

public class WorkflowTemplateSaveAction extends WorkflowAspectSaveAction {

	public static final String ID = "workflow.template.save";
	public WorkflowTemplateSaveAction(IWorkbenchWindow window) {
		super(window,WorkflowAspect.Template);
	}
	
	@Override
	boolean saveAspect() {
		Template2RDF template2rdf = new Template2RDF(this.workflow, true);
		return template2rdf.save(this.filePath);
	}
}
