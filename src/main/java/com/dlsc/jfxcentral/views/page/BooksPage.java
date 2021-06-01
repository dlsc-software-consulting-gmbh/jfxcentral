package com.dlsc.jfxcentral.views.page;

import com.dlsc.jfxcentral.views.RootPane;
import com.dlsc.jfxcentral.views.View;
import com.dlsc.jfxcentral.views.master.BooksMasterView;
import com.dlsc.jfxcentral.views.master.MasterView;
import com.dlsc.jfxcentral.model.Book;
import com.dlsc.jfxcentral.views.detail.BooksDetailView;
import com.dlsc.jfxcentral.views.detail.DetailView;

public class BooksPage extends Page<Book> {

    public BooksPage(RootPane rootPane) {
        super(rootPane, View.HOME);
    }

    @Override
    protected MasterView createMasterView() {
        BooksMasterView view = new BooksMasterView(getRootPane());
        view.selectedItemProperty().bindBidirectional(selectedItemProperty());
        return view;
    }

    @Override
    protected DetailView createDetailView() {
        BooksDetailView view = new BooksDetailView(getRootPane());
        view.selectedItemProperty().bindBidirectional(selectedItemProperty());
        return view;
    }
}
