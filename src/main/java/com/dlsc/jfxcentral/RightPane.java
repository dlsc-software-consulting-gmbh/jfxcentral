package com.dlsc.jfxcentral;

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
