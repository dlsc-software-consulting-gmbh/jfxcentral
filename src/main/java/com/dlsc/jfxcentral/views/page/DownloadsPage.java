package com.dlsc.jfxcentral.views.page;

import com.dlsc.jfxcentral.model.Download;
import com.dlsc.jfxcentral.views.RootPane;
import com.dlsc.jfxcentral.views.View;
import com.dlsc.jfxcentral.views.detail.DetailView;
import com.dlsc.jfxcentral.views.detail.DownloadsDetailView;

public class DownloadsPage extends Page<Download> {

    public DownloadsPage(RootPane rootPane) {
        super(rootPane, View.DOWNLOADS);

        setTitle("Downloads");
        setDescription("Various downloads related to JavaFX. Presentations, applications, documentation.");
    }

    @Override
    protected DetailView createDetailView() {
        DownloadsDetailView view = new DownloadsDetailView(getRootPane());
        selectedItemProperty().bindBidirectional(view.selectedItemProperty());
        return view;
    }
}
