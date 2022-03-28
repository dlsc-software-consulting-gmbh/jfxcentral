package com.dlsc.jfxcentral.views.page;

import com.dlsc.jfxcentral.views.RootPane;
import com.dlsc.jfxcentral.views.View;
import com.dlsc.jfxcentral.views.detail.DetailView;
import com.dlsc.jfxcentral.views.detail.HomeDetailView;

public class HomePage extends Page {

    public HomePage(RootPane rootPane) {
        super(rootPane, View.HOME);

        setTitle("JFX-Central Home - JavaFX Community Website");
        setDescription("Homepage of JFX-Central, the one-stop destination for all things related to JavaFX.");
    }

    @Override
    protected DetailView createDetailView() {
        return new HomeDetailView(getRootPane());
    }
}
