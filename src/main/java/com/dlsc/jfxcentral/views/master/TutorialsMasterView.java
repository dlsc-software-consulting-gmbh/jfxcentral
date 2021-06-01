package com.dlsc.jfxcentral.views.master;

import com.dlsc.jfxcentral.model.Tutorial;
import com.dlsc.jfxcentral.views.RootPane;
import com.dlsc.jfxcentral.views.View;

public class TutorialsMasterView extends MasterView<Tutorial> {

    public TutorialsMasterView(RootPane rootPane) {
        super(rootPane, View.TUTORIALS);

//        listView.getItems().addListener((Observable it) -> performDefaultSelection(listView));
//        performDefaultSelection(listView);

    }
}
