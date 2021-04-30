package com.dlsc.jfxcentral.views;

import com.dlsc.jfxcentral.ImageManager;
import com.dlsc.jfxcentral.model.Book;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;

public class BookView extends PageView {

    public BookView() {
        super();
    }

    class BookCell extends ListCell<Book> {

        private final ImageView coverImageView = new ImageView();
        private final Label titleLabel = new Label();
        private final Label subtitleLabel = new Label();

        public BookCell() {
            getStyleClass().add("book-list-cell");

            coverImageView.setFitWidth(100);
            coverImageView.setPreserveRatio(true);

            titleLabel.getStyleClass().add("title-label");
            subtitleLabel.getStyleClass().add("subtitle-label");

            GridPane gridPane = new GridPane();
            gridPane.getStyleClass().add("grid-pane");
            gridPane.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);

            gridPane.add(coverImageView, 0, 0);
            gridPane.add(titleLabel, 1, 0);
            gridPane.add(subtitleLabel, 1, 1);

            GridPane.setRowSpan(coverImageView, 2);

            GridPane.setHgrow(coverImageView, Priority.NEVER);
            GridPane.setHgrow(titleLabel, Priority.ALWAYS);
            GridPane.setHgrow(subtitleLabel, Priority.ALWAYS);

            RowConstraints row1 = new RowConstraints();
            RowConstraints row2 = new RowConstraints();

            row1.setPercentHeight(50);
            row2.setPercentHeight(50);

            gridPane.getRowConstraints().setAll(row1, row2);

            setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
            setGraphic(gridPane);
        }

        @Override
        protected void updateItem(Book book, boolean empty) {
            super.updateItem(book, empty);

            if (!empty && book != null) {
                titleLabel.setText(book.getTitle());
                subtitleLabel.setText(book.getSubtitle());
                String coverImage = book.getImage();
                if (coverImage != null && !coverImage.trim().isBlank()) {
                    coverImageView.setVisible(true);
                    coverImageView.imageProperty().bind(ImageManager.getInstance().bookCoverImageProperty(book));
                } else {
                    coverImageView.setVisible(false);
                    coverImageView.imageProperty().unbind();
                }
            } else {
                titleLabel.setText("");
                subtitleLabel.setText("");
                coverImageView.setVisible(false);
            }
        }
    }
}