package com.dlsc.jfxcentral.views.page;

import com.dlsc.jfxcentral.views.RootPane;
import com.dlsc.jfxcentral.views.View;
import com.dlsc.jfxcentral.views.detail.DetailView;
import com.dlsc.jfxcentral.views.detail.NewsDetailView;

public class NewsPage extends Page {

    public NewsPage(RootPane rootPane) {
        super(rootPane, View.NEWS);
    }

    @Override
    protected DetailView createDetailView() {
        NewsDetailView view = new NewsDetailView(getRootPane());
        selectedItemProperty().bindBidirectional(view.selectedItemProperty());
        return view;
    }
}
