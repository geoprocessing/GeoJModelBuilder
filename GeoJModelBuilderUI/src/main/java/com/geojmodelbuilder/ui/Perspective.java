package com.geojmodelbuilder.ui;

import org.eclipse.ui.IFolderLayout;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;

import com.geojmodelbuilder.ui.views.WPSResourceTreeView;

public class Perspective implements IPerspectiveFactory {

	public static final String ID_TABS_FOLDER = "Left_Tab_Folder";

	public void createInitialLayout(IPageLayout layout) {

		IFolderLayout tabs = layout.createFolder(ID_TABS_FOLDER,
				IPageLayout.LEFT, 0.3f, layout.getEditorArea());
		tabs.addView(WPSResourceTreeView.ID);
		
		//add the view to the show view menu
		layout.addShowViewShortcut(WPSResourceTreeView.ID);
		//layout.getViewLayout(WPSResourceTreeView.ID).setCloseable(false);
	}
}
