package com.dlsc.jfxcentral.views.page;

import com.dlsc.jfxcentral.views.RootPane;
import com.dlsc.jfxcentral.views.View;
import com.dlsc.jfxcentral.views.master.MasterView;
import com.dlsc.jfxcentral.views.master.RealWorldAppsMasterView;
import com.dlsc.jfxcentral.model.RealWorldApp;
import com.dlsc.jfxcentral.views.detail.DetailView;
import com.dlsc.jfxcentral.views.detail.RealWorldAppsDetailView;

public class RealWorldAppsPage extends Page<RealWorldApp> {

    public RealWorldAppsPage(RootPane rootPane) {
        super(rootPane, View.REALWORLD);
    }

    @Override
    protected MasterView createMasterView() {
        RealWorldAppsMasterView view = new RealWorldAppsMasterView(getRootPane());
        view.selectedItemProperty().bindBidirectional(selectedItemProperty());
        return view;
    }

    @Override
    protected DetailView createDetailView() {
        RealWorldAppsDetailView view = new RealWorldAppsDetailView(getRootPane());
        view.selectedItemProperty().bindBidirectional(selectedItemProperty());
        return view;
    }
}
