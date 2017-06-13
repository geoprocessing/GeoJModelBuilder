package com.geojmodelbuilder.ui.actions;

import java.util.List;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.window.Window;
import org.eclipse.ui.IWorkbenchWindow;
import org.geojmodelbuilder.semantic.serialization.Provenance2RDF;

import com.geojmodelbuilder.core.provenance.IWorkflowProv;
import com.geojmodelbuilder.ui.dialogs.ProvenanceSaveDialog;
import com.geojmodelbuilder.ui.models.WorkflowAspect;

public class WorkflowProvenanceSaveAction extends WorkflowAspectSaveAction {

	public static final String ID = "workflow.provenance.save";
	public WorkflowProvenanceSaveAction(IWorkbenchWindow window) {
		super(window,WorkflowAspect.Provenance);
	}
	
	@Override
	boolean saveAspect() {
		List<IWorkflowProv> provs = this.workflow.getWorkflowProvs();
		
		if(provs.size() == 0){
			MessageDialog.openInformation(window.getShell(),"Message", "There is no executions.");
			return false;
		}
		
		ProvenanceSaveDialog dialog = new ProvenanceSaveDialog(window.getShell(), this.workflow);
		if(dialog.open() != Window.OK)
			return false;
		
		int index = dialog.getIndex();
		IWorkflowProv prov = provs.get(index);
		Provenance2RDF provenance2rdf = new Provenance2RDF(prov);
		return provenance2rdf.save(this.filePath);
	}
}
