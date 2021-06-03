package com.dlsc.jfxcentral.views.master;

import com.dlsc.jfxcentral.data.model.Tutorial;
import com.dlsc.jfxcentral.views.RootPane;
import com.dlsc.jfxcentral.views.View;

public class TutorialsMasterView extends MasterView<Tutorial> {

    public TutorialsMasterView(RootPane rootPane) {
        super(rootPane, View.TUTORIALS);

        getStyleClass().add("tutorials-master-view");

//        listView.getItems().addListener((Observable it) -> performDefaultSelection(listView));
//        performDefaultSelection(listView);

    }
}
