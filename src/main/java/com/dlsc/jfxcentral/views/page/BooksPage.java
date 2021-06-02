package com.dlsc.jfxcentral.views.page;

import com.dlsc.jfxcentral.data.model.Book;
import com.dlsc.jfxcentral.views.RootPane;
import com.dlsc.jfxcentral.views.View;
import com.dlsc.jfxcentral.views.detail.BooksDetailView;
import com.dlsc.jfxcentral.views.detail.DetailView;
import com.dlsc.jfxcentral.views.master.BooksMasterView;
import com.dlsc.jfxcentral.views.master.MasterView;
import javafx.beans.binding.Bindings;

public class BooksPage extends Page<Book> {

    public BooksPage(RootPane rootPane) {
        super(rootPane, View.HOME);

        titleProperty().bind(Bindings.createStringBinding(() -> getSelectedItem() != null ?
                "Book - " + getSelectedItem().getTitle() :
                "Books", selectedItemProperty()));

        descriptionProperty().bind(Bindings.createStringBinding(() -> getSelectedItem() != null ?
                "Information about the JavaFX book '" + getSelectedItem().getTitle() + "'" :
                "Collection of books covering JavaFX technology."));
    }

    @Override
    protected MasterView createMasterView() {
        BooksMasterView view = new BooksMasterView(getRootPane());
        selectedItemProperty().bindBidirectional(view.selectedItemProperty());
        return view;
    }

    @Override
    protected DetailView createDetailView() {
        BooksDetailView view = new BooksDetailView(getRootPane());
        selectedItemProperty().bindBidirectional(view.selectedItemProperty());
        return view;
    }
}
