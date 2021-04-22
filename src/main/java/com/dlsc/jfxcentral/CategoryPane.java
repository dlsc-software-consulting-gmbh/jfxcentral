package com.dlsc.jfxcentral;

import com.dlsc.jfxcentral.categories.PeopleView;

class CategoryPane extends ViewPane {

    private final PeopleView peopleView;

    public CategoryPane(RootPane rootPane) {
        getStyleClass().add("category-pane");

        peopleView = new PeopleView(rootPane);

        viewProperty().addListener(it -> updateView(rootPane));
        updateView(rootPane);
    }

    private void updateView(RootPane rootPane) {
        switch (getView()) {
            case HOME:
                break;
            case OPENJFX:
                break;
            case PEOPLE:
                getChildren().setAll(peopleView);
                rootPane.getRightPane().getChildren().setAll(peopleView.getPanel());
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