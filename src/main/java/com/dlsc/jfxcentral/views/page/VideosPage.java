package com.dlsc.jfxcentral.views.page;

import com.dlsc.jfxcentral.model.Video;
import com.dlsc.jfxcentral.views.RootPane;
import com.dlsc.jfxcentral.views.View;
import com.dlsc.jfxcentral.views.detail.DetailView;
import com.dlsc.jfxcentral.views.detail.VideosDetailView;

public class VideosPage extends Page<Video> {

    public VideosPage(RootPane rootPane) {
        super(rootPane, View.VIDEOS);
    }

    @Override
    protected DetailView createDetailView() {
        VideosDetailView view = new VideosDetailView(getRootPane());
        view.selectedItemProperty().bindBidirectional(selectedItemProperty());
        return view;
    }
}
