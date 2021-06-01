package com.dlsc.jfxcentral.views.page;

import com.dlsc.jfxcentral.views.RootPane;
import com.dlsc.jfxcentral.views.View;
import com.dlsc.jfxcentral.views.master.MasterView;
import com.dlsc.jfxcentral.model.Tutorial;
import com.dlsc.jfxcentral.views.detail.DetailView;
import com.dlsc.jfxcentral.views.detail.TutorialsDetailView;
import com.dlsc.jfxcentral.views.master.TutorialsMasterView;

public class TutorialsPage extends Page<Tutorial> {

    public TutorialsPage(RootPane rootPane) {
        super(rootPane, View.TUTORIALS);
    }

    @Override
    protected MasterView<Tutorial> createMasterView() {
        TutorialsMasterView view = new TutorialsMasterView(getRootPane());
        selectedItemProperty().bindBidirectional(view.selectedItemProperty());
        return view;
    }

    @Override
    protected DetailView<Tutorial> createDetailView() {
        TutorialsDetailView view = new TutorialsDetailView(getRootPane());
        selectedItemProperty().bindBidirectional(view.selectedItemProperty());
        return view;
    }
}
