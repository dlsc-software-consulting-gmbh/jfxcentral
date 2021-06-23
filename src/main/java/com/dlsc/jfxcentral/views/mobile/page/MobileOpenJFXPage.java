package com.dlsc.jfxcentral.views.mobile.page;

import com.dlsc.jfxcentral.views.RootPane;
import com.dlsc.jfxcentral.views.View;
import com.dlsc.jfxcentral.views.detail.DetailView;
import com.dlsc.jfxcentral.views.detail.OpenJFXDetailView;
import com.dlsc.jfxcentral.views.mobile.MobilePage;

public class MobileOpenJFXPage extends MobilePage {

    public MobileOpenJFXPage(RootPane rootPane) {
        super(rootPane, View.OPENJFX);
        setTitle("OpenJFX");
        setDescription("Information about the open source project where JavaFX gets developed.");
    }

    @Override
    protected DetailView createDetailView() {
        return new OpenJFXDetailView(getRootPane());
    }
}
