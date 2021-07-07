package com.dlsc.jfxcentral.views.mobile.master;

import com.dlsc.jfxcentral.data.DataRepository;
import com.dlsc.jfxcentral.data.model.Book;
import com.dlsc.jfxcentral.panels.PrettyScrollPane;
import com.dlsc.jfxcentral.views.RootPane;
import com.dlsc.jfxcentral.views.View;
import com.dlsc.jfxcentral.views.mobile.MobileMasterViewWithAdvancedListView;
import com.dlsc.jfxcentral.views.mobile.master.cells.MobileMasterBookCell;
import javafx.scene.layout.Region;
import org.apache.commons.lang3.StringUtils;

import java.util.Comparator;

public class MobileBooksMasterView extends MobileMasterViewWithAdvancedListView<Book> {

    public MobileBooksMasterView(RootPane rootPane) {
        super(rootPane, View.BOOKS);

        getStyleClass().addAll("books-master-view", "mobile-books-master-view");

        listView.setMinWidth(Region.USE_PREF_SIZE);
        listView.setPaging(true);
        listView.setVisibleRowCount(Integer.MAX_VALUE);
        listView.setCellFactory(view -> new MobileMasterBookCell());
        listView.setItems(createSortedAndFilteredList(DataRepository.getInstance().booksProperty(),
                Comparator.comparing(Book::getPublisher),
                book -> StringUtils.isBlank(getFilterText()) || StringUtils.containsIgnoreCase(book.getName(), getFilterText())));

        filterTextProperty().addListener(it -> System.out.println("filer: " + getFilterText()));

        PrettyScrollPane scrollPane = new PrettyScrollPane(listView);
        scrollPane.setMobile(true);
        scrollPane.setShowScrollToTopButton(true);
        scrollPane.setFitToWidth(true);
        setCenter(scrollPane);
    }
}