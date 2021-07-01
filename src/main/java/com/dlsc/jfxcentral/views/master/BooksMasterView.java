package com.dlsc.jfxcentral.views.master;

import com.dlsc.jfxcentral.data.DataRepository;
import com.dlsc.jfxcentral.data.model.Book;
import com.dlsc.jfxcentral.views.RootPane;
import com.dlsc.jfxcentral.views.View;
import com.dlsc.jfxcentral.views.master.cells.MasterBookCell;
import org.apache.commons.lang3.StringUtils;

import java.util.Comparator;

public class BooksMasterView extends MasterViewWithListView<Book> {

    public BooksMasterView(RootPane rootPane) {
        super(rootPane, View.BOOKS);

        getStyleClass().add("books-master-view");

        listView.setCellFactory(view -> new MasterBookCell());
        listView.setItems(createSortedAndFilteredList(DataRepository.getInstance().booksProperty(),
                Comparator.comparing(Book::getPublisher),
                book -> StringUtils.isBlank(getFilterText()) || StringUtils.containsIgnoreCase(book.getTitle(), getFilterText())));
    }
}