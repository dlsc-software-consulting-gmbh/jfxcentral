package com.dlsc.jfxcentral;

import com.dlsc.jfxcentral.views.PersonView;

public class RightPane extends ViewPane {

    public RightPane(RootPane rootPane) {
        getStyleClass().add("right-pane");
        viewProperty().addListener(it -> updateView());
        updateView();
    }

    private void updateView() {
        switch (getView()) {
            case HOME:
                break;
            case OPENJFX:
                break;
            case PEOPLE:
                PersonView personView = new PersonView();

                break;
            case LEARN:
                break;
            case LIBS:
                break;
            case BLOGS:
                break;
        }
    }
}
