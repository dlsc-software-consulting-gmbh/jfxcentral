package com.dlsc.jfxcentral;

import com.dlsc.jfxcentral.categories.PeopleView;

class CategoryPane extends ViewPane {

    private final PeopleView peopleView;

    public CategoryPane(RootPane rootPane) {
        getStyleClass().add("category-pane");

        peopleView = new PeopleView(rootPane);

        viewProperty().addListener(it -> {
            switch (getView()) {
                case HOME:
                    break;
                case OPENJFX:
                    break;
                case PEOPLE:
                    getChildren().setAll(peopleView);
                    break;
                case LEARN:
                    break;
                case LIBS:
                    break;
                case BLOGS:
                    break;
            }
        });
    }
}