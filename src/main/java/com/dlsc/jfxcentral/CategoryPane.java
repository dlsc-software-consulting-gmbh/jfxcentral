package com.dlsc.jfxcentral;

import com.dlsc.jfxcentral.categories.*;
import com.dlsc.jfxcentral.model.Book;
import com.dlsc.jfxcentral.views.*;

class CategoryPane extends ViewPane {

    private HomeView homeView;
    private NewsView newsView;
    private BlogsView blogsView;
    private PeopleView peopleView;
    private BooksView booksView;
    private VideosView videosView;
    private OpenJFXView openJFXView;
    private LibrariesView librariesView;
    private CompaniesView companiesView;
    private ToolsView toolsView;
    private RealWorldAppsView appsView;

    public CategoryPane(RootPane rootPane) {
        getStyleClass().add("category-pane");

        homeView = new HomeView(rootPane);
        newsView = new NewsView(rootPane);
        blogsView = new BlogsView(rootPane);
        peopleView = new PeopleView(rootPane);
        booksView = new BooksView(rootPane);
        videosView = new VideosView(rootPane);
        toolsView = new ToolsView(rootPane);
        librariesView = new LibrariesView(rootPane);
        companiesView = new CompaniesView(rootPane);
        appsView = new RealWorldAppsView(rootPane);
        openJFXView = new OpenJFXView(rootPane);

        viewProperty().addListener(it -> updateView(rootPane));
        updateView(rootPane);

        rootPane.registerOpenHandler(Book.class, book -> {
            rootPane.setView(View.HOME);
            ((BookView) (booksView.getDetailPane())).setBook(book);
        });
    }

    public HomeView getHomeView() {
        return homeView;
    }

    public NewsView getNewsView() {
        return newsView;
    }

    public BlogsView getBlogsView() {
        return blogsView;
    }

    public PeopleView getPeopleView() {
        return peopleView;
    }

    public BooksView getBooksView() {
        return booksView;
    }

    public VideosView getVideosView() {
        return videosView;
    }

    public LibrariesView getLibrariesView() {
        return librariesView;
    }

    public ToolsView getToolsView() {
        return toolsView;
    }

    private void updateView(RootPane rootPane) {
        switch (getView()) {
            case HOME:
                getChildren().clear();
                rootPane.getRightPane().setContent(homeView);
                break;
            case NEWS:
                getChildren().clear();
                rootPane.getRightPane().setContent(newsView);
                break;
            case OPENJFX:
                getChildren().clear();
                rootPane.getRightPane().setContent(openJFXView);
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
            case COMPANIES:
                getChildren().clear();
                rootPane.getRightPane().setContent(companiesView);
            case TUTORIALS:
                break;
            case REALWORLD:
                getChildren().setAll(appsView);
                rootPane.getRightPane().setContent(appsView.getDetailPane());
                break;
            case TOOLS:
                getChildren().setAll(toolsView);
                rootPane.getRightPane().setContent(toolsView.getDetailPane());
                break;
            case LIBRARIES:
                getChildren().setAll(librariesView);
                rootPane.getRightPane().setContent(librariesView.getDetailPane());
                break;
            case BLOGS:
                getChildren().setAll(blogsView);
                rootPane.getRightPane().setContent(blogsView.getDetailPane());
                break;
        }
    }
}