package com.dlsc.jfxcentral;

import com.dlsc.jfxcentral.categories.BooksView;
import com.dlsc.jfxcentral.categories.LibrariesView;
import com.dlsc.jfxcentral.categories.PeopleView;
import com.dlsc.jfxcentral.model.Book;
import com.dlsc.jfxcentral.views.BookView;
import com.dlsc.jfxcentral.views.NewsView;
import com.dlsc.jfxcentral.views.VideosView;

class CategoryPane extends ViewPane {

    private NewsView newsView;
    private PeopleView peopleView;
    private BooksView booksView;
    private VideosView videosView;
    private LibrariesView librariesView;

    public CategoryPane(RootPane rootPane) {
        getStyleClass().add("category-pane");

        newsView = new NewsView(rootPane);
        peopleView = new PeopleView(rootPane);
        booksView = new BooksView(rootPane);
        videosView = new VideosView(rootPane);
        librariesView = new LibrariesView(rootPane);

        viewProperty().addListener(it -> updateView(rootPane));
        updateView(rootPane);

        rootPane.registerOpenHandler(Book.class, book -> {
            rootPane.setView(View.HOME);
            ((BookView) (booksView.getDetailPane())).setBook(book);
        });
    }

    private void updateView(RootPane rootPane) {
        switch (getView()) {
            case HOME:
                getChildren().clear();
                rootPane.getRightPane().setContent(newsView);
                break;
            case OPENJFX:
                break;
            case PEOPLE:
                getChildren().setAll(peopleView);
                rootPane.getRightPane().setContent(peopleView.getDetailPane());
                break;
            case BOOKS:
                getChildren().setAll(booksView);
                rootPane.getRightPane().setContent(booksView.getDetailPane());
                break;
            case VIDEOS:
                getChildren().clear();
                rootPane.getRightPane().setContent(videosView);
                break;
            case LEARN:
                break;
            case LIBRARIES:
                getChildren().setAll(librariesView);
                rootPane.getRightPane().setContent(librariesView.getDetailPane());
                break;
            case BLOGS:
                break;
        }
    }
}