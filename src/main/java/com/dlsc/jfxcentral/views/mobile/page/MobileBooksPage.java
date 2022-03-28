package com.dlsc.jfxcentral.views.mobile.page;

import com.dlsc.jfxcentral.data.model.Book;
import com.dlsc.jfxcentral.views.RootPane;
import com.dlsc.jfxcentral.views.View;
import com.dlsc.jfxcentral.views.detail.BooksDetailView;
import com.dlsc.jfxcentral.views.detail.DetailView;
import com.dlsc.jfxcentral.views.master.MasterView;
import com.dlsc.jfxcentral.views.mobile.MobilePage;
import com.dlsc.jfxcentral.views.mobile.master.MobileBooksMasterView;
import javafx.beans.binding.Bindings;

public class MobileBooksPage extends MobilePage<Book> {

    public MobileBooksPage(RootPane rootPane) {
        super(rootPane, View.BOOKS);

        titleProperty().bind(Bindings.createStringBinding(() -> getSelectedItem() != null ?
                "Book - " + getSelectedItem().getName() + " - for JavaFX developers" :
                "Books for JavaFX developers", selectedItemProperty()));

        descriptionProperty().bind(Bindings.createStringBinding(() -> getSelectedItem() != null ?
                "Information about the JavaFX book '" + getSelectedItem().getName() + "'" :
                "Collection of books covering JavaFX technology."));
    }

    @Override
    protected MasterView<Book> createMasterView() {
        return new MobileBooksMasterView(getRootPane());
    }

    @Override
    protected DetailView<Book> createDetailView() {
        return new BooksDetailView(getRootPane());
    }
}
