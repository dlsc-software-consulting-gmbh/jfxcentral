package com.dlsc.jfxcentral.categories;

import com.dlsc.jfxcentral.DataRepository;
import com.dlsc.jfxcentral.ImageManager;
import com.dlsc.jfxcentral.RootPane;
import com.dlsc.jfxcentral.model.Book;
import com.dlsc.jfxcentral.views.AdvancedListCell;
import com.dlsc.jfxcentral.views.BookView;
import javafx.beans.Observable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.ListView;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import org.apache.commons.lang3.StringUtils;

import java.util.Comparator;

public class BooksView extends CategoryView {

    private BookView bookView;

    private ListView<Book> listView = new ListView<>();

    public BooksView(RootPane rootPane) {
        super(rootPane);

        getStyleClass().add("books-view");


        listView.setMinWidth(Region.USE_PREF_SIZE);
        listView.setCellFactory(view -> new BookListCell());

        listView.setItems(createSortedAndFilteredList(DataRepository.getInstance().booksProperty(),
                Comparator.comparing(Book::getTitle),
                book -> StringUtils.isBlank(getFilterText()) || StringUtils.containsIgnoreCase(book.getTitle(), getFilterText())));

        filterTextProperty().addListener(it -> System.out.println("filer: " + getFilterText()));

        listView.getItems().addListener((Observable it) -> performDefaultSelection());
        VBox.setVgrow(listView, Priority.ALWAYS);

        setCenter(listView);

        performDefaultSelection();
    }

    private void performDefaultSelection() {
        if (!listView.getItems().isEmpty()) {
            listView.getSelectionModel().select(0);
        } else {
            listView.getSelectionModel().clearSelection();
        }
    }

    @Override
    public Node getDetailPane() {
        if (bookView == null) {
            bookView = new BookView(getRootPane());
            bookView.bookProperty().bind(listView.getSelectionModel().selectedItemProperty());
        }

        return bookView;
    }

    class BookListCell extends AdvancedListCell<Book> {

        private final ImageView coverImageView = new ImageView();

        public BookListCell() {
            getStyleClass().add("book-list-cell");

            coverImageView.setFitWidth(100);
            coverImageView.setPreserveRatio(true);

            setAlignment(Pos.CENTER);
            setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
            setGraphic(coverImageView);

            coverImageView.visibleProperty().bind(emptyProperty().not());
        }

        @Override
        protected void updateItem(Book book, boolean empty) {
            super.updateItem(book, empty);

            coverImageView.imageProperty().unbind();

            if (!empty && book != null) {
                String coverImage = book.getImage();
                if (coverImage != null && !coverImage.trim().isBlank()) {
                    coverImageView.imageProperty().bind(ImageManager.getInstance().bookCoverImageProperty(book));
                }
            }
        }
    }
}