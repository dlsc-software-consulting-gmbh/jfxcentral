package com.dlsc.jfxcentral.views.mobile.page;

import com.dlsc.jfxcentral.data.model.News;
import com.dlsc.jfxcentral.views.RootPane;
import com.dlsc.jfxcentral.views.View;
import com.dlsc.jfxcentral.views.detail.DetailView;
import com.dlsc.jfxcentral.views.detail.HomeDetailView;
import com.dlsc.jfxcentral.views.mobile.MobilePage;

public class MobileHomePage extends MobilePage<News> {

    public MobileHomePage(RootPane rootPane) {
        super(rootPane, View.HOME);

        setTitle("JFX-Central Home");
        setDescription("Homepage of JFX-Central, the one-stop destination for all things related to JavaFX.");
    }

    @Override
    protected DetailView<News> createDetailView() {
        return new HomeDetailView(getRootPane());
    }
}
