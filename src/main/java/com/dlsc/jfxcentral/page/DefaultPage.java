package com.dlsc.jfxcentral.page;

import com.dlsc.jfxcentral.DataRepository;
import com.dlsc.jfxcentral.HeaderPane;
import com.dlsc.jfxcentral.RootPane;
import com.dlsc.jfxcentral.View;
import com.dlsc.jfxcentral.categories.*;
import com.dlsc.jfxcentral.model.*;
import com.dlsc.jfxcentral.views.LibraryView;
import com.dlsc.jfxcentral.views.PersonView;
import com.jpro.webapi.WebAPI;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;


public class DefaultPage extends com.jpro.web.View {

    String initialURL;

    public DefaultPage(String initialURL) {
        this.initialURL = initialURL;
    }

    @Override
    public String title() {
        return "Title: Hello JPro";
    }

    @Override
    public String description() {
        return "I'm the description. Google will find me!";
    }

    @Override
    public boolean fullscreen() {
        return true;
    }

    RootPane rootPane = new RootPane();
    @Override
    public Node content() {
        handleURL(initialURL);

        return rootPane;
    }

    // IF this returns true, we don't query for a new page
    @Override public boolean handleURL(String s) {
        View view = PageUtil.getViewFromURL(s);
        String id = PageUtil.getIdFromURL(s);

        System.out.println("VIEW: " + view);
        System.out.println("ID: " + id);

        rootPane.setView(view);

        if(id == null) return true;
        switch (view) {
            case BLOGS:
                BlogsView blogsView = rootPane.getSideBar().getCategoryPane().getBlogsView();
                Blog blog = DataRepository.getInstance().getBlogById(id).get();
                blogsView.setItem(blog);
                break;
            case BOOKS:
                BooksView booksView = rootPane.getSideBar().getCategoryPane().getBooksView();
                Book book = DataRepository.getInstance().getBookById(id).get();
                booksView.setItem(book);
                break;
            case LIBRARIES:
                LibrariesView libraryView = rootPane.getSideBar().getCategoryPane().getLibrariesView();
                Library library = DataRepository.getInstance().getLibraryById(id).get();
                libraryView.setItem(library);
                break;
            case PEOPLE:
                PeopleView peopleView = rootPane.getSideBar().getCategoryPane().getPeopleView();
                Person person = DataRepository.getInstance().getPersonById(id).get();
                peopleView.setItem(person);
                break;
            case REALWORLD:
                RealWorldAppsView appsView = rootPane.getSideBar().getCategoryPane().getRealWorldAppsView();
                RealWorldApp app = DataRepository.getInstance().getRealWorldAppById(id).get();
                appsView.setItem(app);
                break;
            case TOOLS:
                ToolsView toolesView = rootPane.getSideBar().getCategoryPane().getToolsView();
                Tool tool = DataRepository.getInstance().getToolById(id).get();
                toolesView.setItem(tool);
                break;
        }
        return true;
    }
}
