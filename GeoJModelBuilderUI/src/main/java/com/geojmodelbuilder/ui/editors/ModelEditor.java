/**
 * Copyright (C) 2013 - 2016 Wuhan University
 * 
 * This program is free software; you can redistribute and/or modify it under 
 * the terms of the GNU General Public License version 2 as published by the 
 * Free Software Foundation.
 * 
 * This program is distributed WITHOUT ANY WARRANTY; even without the implied
 * WARRANTY OF MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 */
package com.geojmodelbuilder.ui.editors;

import java.util.ArrayList;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.draw2d.ColorConstants;
import org.eclipse.gef.ContextMenuProvider;
import org.eclipse.gef.DefaultEditDomain;
import org.eclipse.gef.GraphicalViewer;
import org.eclipse.gef.KeyHandler;
import org.eclipse.gef.KeyStroke;
import org.eclipse.gef.dnd.TemplateTransferDragSourceListener;
import org.eclipse.gef.editparts.ScalableRootEditPart;
import org.eclipse.gef.editparts.ZoomManager;
import org.eclipse.gef.palette.CombinedTemplateCreationEntry;
import org.eclipse.gef.palette.ConnectionCreationToolEntry;
import org.eclipse.gef.palette.MarqueeToolEntry;
import org.eclipse.gef.palette.PaletteGroup;
import org.eclipse.gef.palette.PaletteRoot;
import org.eclipse.gef.palette.PaletteSeparator;
import org.eclipse.gef.palette.SelectionToolEntry;
import org.eclipse.gef.ui.actions.ActionRegistry;
import org.eclipse.gef.ui.actions.GEFActionConstants;
import org.eclipse.gef.ui.actions.ZoomInAction;
import org.eclipse.gef.ui.actions.ZoomOutAction;
import org.eclipse.gef.ui.palette.PaletteViewer;
import org.eclipse.gef.ui.parts.GraphicalEditorWithPalette;
import org.eclipse.jface.action.IAction;
import org.eclipse.swt.SWT;
import org.eclipse.ui.actions.ActionFactory;

import com.geojmodelbuilder.ui.actions.NodeCopyAction;
import com.geojmodelbuilder.ui.actions.NodePasteAction;
import com.geojmodelbuilder.ui.actions.NodeRenameAction;
import com.geojmodelbuilder.ui.dnd.NodeTemplateTransferDropTargetListener;
import com.geojmodelbuilder.ui.editparts.WorkflowEditPartFactory;
import com.geojmodelbuilder.ui.models.StandaloneArtifact;
import com.geojmodelbuilder.ui.models.Workflow;
import com.geojmodelbuilder.ui.models.WorkflowCondition;
import com.geojmodelbuilder.ui.models.WorkflowProcess;
import com.geojmodelbuilder.ui.models.links.DataFlow;
import com.geojmodelbuilder.ui.models.links.FalseThenFlow;
import com.geojmodelbuilder.ui.models.links.TrueThenFlow;
import com.geojmodelbuilder.ui.requests.AbstractElementCreationFactory;
/**
 * 
 * @author Mingda Zhang
 *
 */
public class ModelEditor extends GraphicalEditorWithPalette {
	public static final String ID = "com.geojmodelbuilder.ui.editor.modeleditor";

	/**
	 * Every model editor will hold a workflow.
	 */
	private Workflow workflow;
	public ModelEditor() {
		setEditDomain(new DefaultEditDomain(this));
	}

	/**
	 * Every editor will hold a workflow
	 * @return
	 */
	private Workflow createWorkflow() {
		workflow = new Workflow();
		workflow.setName("Workflow");
		
		setWorkflow(workflow);

		//modelTest(workflow);
		
		return workflow;
	}

	/*
	 	private void modelTest(Workflow workflow){
		WorkflowProcess process = new WorkflowProcess(workflow);
		process.setName("Process \nDemo");
		process.setLayout(new Rectangle(50, 25, 80, 60));
		workflow.addProcess(process);

		ProcessOutputArtifact output = new ProcessOutputArtifact(process);
		output.setName("output");
		output.setLayout(new Rectangle(210,25,80,60));
		process.addOutputArtifact(output);;
		
		NodeLink nodeLink = new ProcessOutputLink(process,output);
		nodeLink.connect();
		
		StandaloneArtifact artifact = new StandaloneArtifact(workflow);
		artifact.setName("Dataset");
		artifact.setLayout(new Rectangle(150, 120, 80, 60));
		workflow.addArtifact(artifact);
		
		WorkflowCondition condition = new WorkflowCondition(workflow);
		condition.setName("Condition");
		condition.setLayout(new Rectangle(300,150,80,60));
		workflow.addCondtion(condition);

		WorkflowProcess process2 = new WorkflowProcess(workflow);
		process2.setName("Process \nwith 2 outputs");
		process2.setLayout(new Rectangle(300,300,80,60));
		workflow.addProcess(process2);
		
		ProcessOutputArtifact output21 = new ProcessOutputArtifact(process2);
		output21.setName("output1");
		output21.setLayout(new Rectangle(400,220,80,60));
		process2.addOutputArtifact(output21);
		NodeLink processoutputlink1 = new ProcessOutputLink(process2,output21);
		processoutputlink1.connect();
		
		ProcessOutputArtifact output22 = new ProcessOutputArtifact(process2);
		output22.setName("output2");
		output22.setLayout(new Rectangle(400,320,80,60));
		process2.addOutputArtifact(output22);
		NodeLink processOutputLink2 = new ProcessOutputLink(process2, output22);
		processOutputLink2.connect();
		
		ProcessInputArtifact inputArtifact = new ProcessInputArtifact();
		inputArtifact.setName("input");
		process2.addInputArtifact(inputArtifact);
	}
	*/
	
	@Override
	protected void configureGraphicalViewer() {
		super.configureGraphicalViewer();

		// set the factory to produce the appropriate edit part according to the models
		GraphicalViewer view = getGraphicalViewer();
		view.setEditPartFactory(new WorkflowEditPartFactory());

		// add zoom support
		ScalableRootEditPart rootEditPart = new ScalableRootEditPart();
		view.setRootEditPart(rootEditPart);

		ZoomManager manager = rootEditPart.getZoomManager();
		getActionRegistry().registerAction(new ZoomInAction(manager));
		getActionRegistry().registerAction(new ZoomOutAction(manager));

		double zoomLevels[] = new double[] { 0.25, 0.5, 0.75, 1.0, 1.5, 2.0,
				2.5, 3.0, 4.0, 5.0, 10.0, 20.0 };
		manager.setZoomLevels(zoomLevels);

		ArrayList<String> zoomContributions = new ArrayList<String>(3);
		zoomContributions.add(ZoomManager.FIT_ALL);
		zoomContributions.add(ZoomManager.FIT_WIDTH);
		zoomContributions.add(ZoomManager.FIT_HEIGHT);
		manager.setZoomLevelContributions(zoomContributions);

		// keyboard shortcuts
		KeyHandler keyHandler = new KeyHandler();

		keyHandler.put(KeyStroke.getPressed(SWT.DEL, 127, 0),
				getActionRegistry().getAction(ActionFactory.DELETE.getId()));

		keyHandler.put(KeyStroke.getPressed('+', SWT.KEYPAD_ADD, 0),
				getActionRegistry().getAction(GEFActionConstants.ZOOM_IN));
		keyHandler.put(KeyStroke.getPressed('-', SWT.KEYPAD_SUBTRACT, 0),
				getActionRegistry().getAction(GEFActionConstants.ZOOM_OUT));

		keyHandler.put(KeyStroke.getReleased((char) 0x03, 99, SWT.CTRL),
				getActionRegistry().getAction(ActionFactory.COPY.getId()));
		keyHandler.put(KeyStroke.getReleased((char) 0x16, 118, SWT.CTRL),
				getActionRegistry().getAction(ActionFactory.PASTE.getId()));
		
		keyHandler.put(KeyStroke.getReleased((char) 0x1a, 0x7a, SWT.CTRL),
				getActionRegistry().getAction(ActionFactory.UNDO.getId()));
	
		view.setKeyHandler(keyHandler);
		
		/*
		 * zoom using mouse wheel zoom handler
		 * view.setProperty(MouseWheelHandler.KeyGenerator.getKey(SWT.NONE),
		 * MouseWheelZoomHandler.SINGLETON);
		 */

		// menu binded to right mouse click
		ContextMenuProvider provider = new ModelEditorContextMenuProvider(view,
				getActionRegistry());
		view.setContextMenu(provider);
	}

	// support zoom
	@Override
	public Object getAdapter(Class type) {
		if (type == ZoomManager.class)
			return ((ScalableRootEditPart) getGraphicalViewer()
					.getRootEditPart()).getZoomManager();
		else
			return super.getAdapter(type);
	}

	@Override
	protected void initializeGraphicalViewer() {
		GraphicalViewer viewer = getGraphicalViewer();
		viewer.setContents(createWorkflow());
		viewer.addDropTargetListener(new NodeTemplateTransferDropTargetListener(
				viewer));
		
		setPartName(this.workflow.getName());
		//viewer.addDropTargetListener(listener);
		//viewer.addDropTargetListener(new ProcessDropListener(viewer,TextTransfer.getInstance()));
	}

	@Override
	protected void initializePaletteViewer() {
		super.initializePaletteViewer();
		PaletteViewer viewer = getPaletteViewer();
		viewer.addDragSourceListener(new TemplateTransferDragSourceListener(
				viewer));
		
		viewer.getControl().setBackground(ColorConstants.lightGray);
		
	}

	@Override
	public void doSave(IProgressMonitor monitor) {
	}

	@Override
	protected void createActions() {
		super.createActions();

		ActionRegistry registry = getActionRegistry();
		IAction action = new NodeRenameAction(this);
		registry.registerAction(action);
		
		//pop menu
		getSelectionActions().add(action.getId());

		IAction copyAction = new NodeCopyAction(this);
		registry.registerAction(copyAction);
		getSelectionActions().add(copyAction.getId());

		IAction pasteAction = new NodePasteAction(this);
		registry.registerAction(pasteAction);
		getSelectionActions().add(pasteAction.getId());
	}

	@Override
	protected PaletteRoot getPaletteRoot() {
		PaletteRoot root = new PaletteRoot();
		
		PaletteGroup manipGroup = new PaletteGroup("Selection");
		SelectionToolEntry selectionToolEntry = new SelectionToolEntry();
		manipGroup.add(selectionToolEntry);
		manipGroup.add(new MarqueeToolEntry());
		root.add(manipGroup);
		root.add(new PaletteSeparator("Selection"));
		
		/* support creation but not drag
		PaletteGroup createGroup = new PaletteGroup("Element");
		createGroup.add(new CreationToolEntry("Process",
				"Create workflow process", new NodeCreationFactory(
						WorkflowProcess.class), null, null));
		root.add(createGroup);
		*/

		PaletteGroup dndGroup = new PaletteGroup("Element");
		dndGroup.add(new CombinedTemplateCreationEntry("Data",
				"Creation an Artifact", StandaloneArtifact.class,
				new AbstractElementCreationFactory(StandaloneArtifact.class), null, null));
		
		dndGroup.add(new CombinedTemplateCreationEntry("Process",
				"Creation a process", WorkflowProcess.class,
				new AbstractElementCreationFactory(WorkflowProcess.class), null, null));
		
		dndGroup.add(new CombinedTemplateCreationEntry("Condition",
				"Creation a Condition", WorkflowCondition.class,
				new AbstractElementCreationFactory(WorkflowCondition.class), null, null));
		
		root.add(dndGroup);
		root.add(new PaletteSeparator());
		
		PaletteGroup connGroup = new PaletteGroup("Flow");
		connGroup.add(new ConnectionCreationToolEntry("DataFlow","Create a data flow",new AbstractElementCreationFactory(DataFlow.class),null,null));
		connGroup.add(new ConnectionCreationToolEntry("TrueThen","Create a control flow",new AbstractElementCreationFactory(TrueThenFlow.class),null,null));
		connGroup.add(new ConnectionCreationToolEntry("FalseThen","Create a control flow",new AbstractElementCreationFactory(FalseThenFlow.class),null,null));
		root.add(connGroup);
		
		root.setDefaultEntry(selectionToolEntry);

		return root;
	}
	
	public void setWorkflow(Workflow workflow){
		this.workflow = workflow;
	}
	
	public Workflow getWorkflow(){
		return this.workflow;
	}
}
