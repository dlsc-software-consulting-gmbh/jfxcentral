package com.dlsc.jfxcentral.views.master;

import com.dlsc.jfxcentral.data.model.Tutorial;
import com.dlsc.jfxcentral.views.RootPane;
import com.dlsc.jfxcentral.views.View;

public class TutorialsMasterView extends MasterViewWithListView<Tutorial> {

    public TutorialsMasterView(RootPane rootPane) {
        super(rootPane, View.TUTORIALS);

        getStyleClass().add("tutorials-master-view");
    }
}
