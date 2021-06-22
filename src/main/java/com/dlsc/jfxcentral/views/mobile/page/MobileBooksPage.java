package com.dlsc.jfxcentral.views.mobile.page;

import com.dlsc.jfxcentral.data.model.Book;
import com.dlsc.jfxcentral.views.RootPane;
import com.dlsc.jfxcentral.views.View;
import com.dlsc.jfxcentral.views.detail.BooksDetailView;
import com.dlsc.jfxcentral.views.detail.DetailView;
import com.dlsc.jfxcentral.views.master.MasterView;
import com.dlsc.jfxcentral.views.mobile.MobilePage;
import com.dlsc.jfxcentral.views.mobile.master.MobileBooksMasterView;

public class MobileBooksPage extends MobilePage<Book> {

    public MobileBooksPage(RootPane rootPane) {
        super(rootPane, View.BOOKS);
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
