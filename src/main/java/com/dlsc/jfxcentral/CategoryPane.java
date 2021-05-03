package com.dlsc.jfxcentral;

import com.dlsc.jfxcentral.categories.BooksView;
import com.dlsc.jfxcentral.categories.PeopleView;

class CategoryPane extends ViewPane {

    private PeopleView peopleView;
    private BooksView booksView;

    public CategoryPane(RootPane rootPane) {
        getStyleClass().add("category-pane");

        peopleView = new PeopleView(rootPane);
        booksView = new BooksView(rootPane);

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
                rootPane.getRightPane().setContent(peopleView.getPanel());
                break;
            case BOOKS:
                getChildren().setAll(booksView);
                rootPane.getRightPane().setContent(booksView.getPanel());
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