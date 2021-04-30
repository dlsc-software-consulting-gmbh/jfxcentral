package com.dlsc.jfxcentral.categories;

import com.dlsc.jfxcentral.DataRepository;
import com.dlsc.jfxcentral.ImageManager;
import com.dlsc.jfxcentral.RootPane;
import com.dlsc.jfxcentral.model.Book;
import com.dlsc.jfxcentral.views.BookView;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

public class BooksView extends CategoryView {

    private BookView bookView;

    public BooksView(RootPane rootPane) {
        super(rootPane);

        getStyleClass().add("books-view");

        ListView<Book> listView = new ListView<>();
        listView.setMinWidth(Region.USE_PREF_SIZE);
        listView.setCellFactory(view -> new BookCell());
        listView.itemsProperty().bind(DataRepository.getInstance().booksProperty());

        VBox.setVgrow(listView, Priority.ALWAYS);

        setCenter(listView);
    }

    @Override
    public Node getPanel() {
        if (bookView == null) {
            bookView = new BookView();
        }

        return bookView;
    }

    class BookCell extends ListCell<Book> {

        private final ImageView coverImageView = new ImageView();

        public BookCell() {
            getStyleClass().add("book-list-cell");

            coverImageView.setFitWidth(100);
            coverImageView.setPreserveRatio(true);

            setAlignment(Pos.CENTER);
            setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
            setGraphic(coverImageView);
        }

        @Override
        protected void updateItem(Book book, boolean empty) {
            super.updateItem(book, empty);

            if (!empty && book != null) {
                String coverImage = book.getImage();
                if (coverImage != null && !coverImage.trim().isBlank()) {
                    coverImageView.setVisible(true);
                    coverImageView.imageProperty().bind(ImageManager.getInstance().bookCoverImageProperty(book));
                } else {
                    coverImageView.setVisible(false);
                    coverImageView.imageProperty().unbind();
                }
            } else {
                coverImageView.setVisible(false);
            }
        }
    }
}