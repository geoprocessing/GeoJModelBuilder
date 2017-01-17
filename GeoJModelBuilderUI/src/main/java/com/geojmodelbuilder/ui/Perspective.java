package com.geojmodelbuilder.ui;

import org.eclipse.ui.IFolderLayout;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;

import com.geojmodelbuilder.ui.views.WPSResourceTreeView;

public class Perspective implements IPerspectiveFactory {

	public static final String ID_TABS_FOLDER = "Left_Tab_Folder";

	public void createInitialLayout(IPageLayout layout) {

		// layout.addView(DemoView.ID, IPageLayout.LEFT, .20f,
		// layout.getEditorArea());

		IFolderLayout tabs = layout.createFolder(ID_TABS_FOLDER,
				IPageLayout.LEFT, 0.3f, layout.getEditorArea());
		tabs.addView(WPSResourceTreeView.ID);
		
		//tabs.addView(DemoView.ID);
		//tabs.addView(ResourceTreeView.ID);
		//tabs.addPlaceholder(IPageLayout.ID_PROP_SHEET);
		// layout.getViewLayout(DemoView.ID).setCloseable(false);
	}
}
