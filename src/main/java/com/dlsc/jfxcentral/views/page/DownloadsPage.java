package com.dlsc.jfxcentral.views.page;

import com.dlsc.jfxcentral.model.RealWorldApp;
import com.dlsc.jfxcentral.views.RootPane;
import com.dlsc.jfxcentral.views.View;

public class DownloadsPage extends Page<RealWorldApp> {

    public DownloadsPage(RootPane rootPane) {
        super(rootPane, View.DOWNLOADS);

        setTitle("Downloads");
        setDescription("Various downloads related to JavaFX. Presentations, applications, documentation.");
    }

//    @Override
//    protected MasterView createMasterView() {
//        RealWorldAppsMasterView view = new RealWorldAppsMasterView(getRootPane());
//        selectedItemProperty().bindBidirectional(view.selectedItemProperty());
//        return view;
//    }
//
//    @Override
//    protected DetailView createDetailView() {
//        RealWorldAppsDetailView view = new RealWorldAppsDetailView(getRootPane());
//        selectedItemProperty().bindBidirectional(view.selectedItemProperty());
//        return view;
//    }
}
