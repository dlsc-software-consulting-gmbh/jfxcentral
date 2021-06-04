package com.dlsc.jfxcentral.views.detail;

import com.dlsc.jfxcentral.views.RootPane;
import com.dlsc.jfxcentral.data.model.Tutorial;

public class TutorialsDetailView extends DetailView<Tutorial> {

    public TutorialsDetailView(RootPane rootPane) {
        super(rootPane);

        getStyleClass().add("tutorials-detail-view");
    }
}
