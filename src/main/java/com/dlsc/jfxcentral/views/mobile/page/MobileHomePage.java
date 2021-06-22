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
    }

    @Override
    protected DetailView<News> createDetailView() {
        return new HomeDetailView(getRootPane());
    }
}
