package com.dlsc.jfxcentral.views.page;

import com.dlsc.jfxcentral.views.RootPane;
import com.dlsc.jfxcentral.views.View;
import com.dlsc.jfxcentral.views.detail.DetailView;
import com.dlsc.jfxcentral.views.detail.HomeDetailsView;

public class HomePage extends Page {

    public HomePage(RootPane rootPane) {
        super(rootPane, View.HOME);
    }

    @Override
    protected DetailView createDetailView() {
        return new HomeDetailsView(getRootPane());
    }
}
