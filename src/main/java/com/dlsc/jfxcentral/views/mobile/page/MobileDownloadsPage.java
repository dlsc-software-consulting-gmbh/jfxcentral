package com.dlsc.jfxcentral.views.mobile.page;

import com.dlsc.jfxcentral.data.model.Download;
import com.dlsc.jfxcentral.views.RootPane;
import com.dlsc.jfxcentral.views.View;
import com.dlsc.jfxcentral.views.detail.DetailView;
import com.dlsc.jfxcentral.views.detail.DownloadsDetailView;
import com.dlsc.jfxcentral.views.mobile.MobilePage;

public class MobileDownloadsPage extends MobilePage<Download> {

    public MobileDownloadsPage(RootPane rootPane) {
        super(rootPane, View.DOWNLOADS);

        setTitle("Downloads");
        setDescription("Various downloads related to JavaFX. Presentations, applications, documentation.");
    }
    
    @Override
    protected DetailView<Download> createDetailView() {
        return new DownloadsDetailView(getRootPane());
    }
}
