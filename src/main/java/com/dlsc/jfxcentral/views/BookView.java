package com.dlsc.jfxcentral.views;

import com.dlsc.jfxcentral.ImageManager;
import com.dlsc.jfxcentral.RootPane;
import com.dlsc.jfxcentral.model.Book;
import com.dlsc.jfxcentral.panels.SectionPane;
import com.dlsc.jfxcentral.util.Util;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import org.apache.commons.lang3.StringUtils;
import org.kordamp.ikonli.fontawesome5.FontAwesomeBrands;
import org.kordamp.ikonli.javafx.FontIcon;

import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

public class BookView extends PageView {

    private ImageView coverImageView = new ImageView();
    private Label titleLabel = new Label();
    private Label subtitleLabel = new Label();
    private Label descriptionLabel = new Label();
    private Label authorsLabel = new Label();
    private Label isbnLabel = new Label();
    private Label publishDateLabel = new Label();

    private HBox linksBox = new HBox();

    public BookView(RootPane rootPane) {
        super(rootPane);

        getStyleClass().add("book-view");

        createTitleBox();

        bookProperty().addListener(it -> updateView());
    }

    private void createTitleBox() {
        coverImageView.setFitHeight(200);
        coverImageView.setPreserveRatio(true);

        titleLabel.getStyleClass().add("title-label");
        titleLabel.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(titleLabel, Priority.ALWAYS);

        subtitleLabel.getStyleClass().add("subtitle-label");
        subtitleLabel.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(subtitleLabel, Priority.ALWAYS);

        descriptionLabel.setWrapText(true);
        descriptionLabel.setMinHeight(Region.USE_PREF_SIZE);
        descriptionLabel.setMaxHeight(Double.MAX_VALUE);
        descriptionLabel.getStyleClass().add("description-label");
        HBox.setHgrow(descriptionLabel, Priority.ALWAYS);

        authorsLabel = new Label();
        authorsLabel.getStyleClass().add("authors-label");

        isbnLabel = new Label();
        isbnLabel.getStyleClass().add("isbn-label");

        linksBox = new HBox();
        linksBox.getStyleClass().add("social-box");

        publishDateLabel = new Label();
        publishDateLabel.getStyleClass().add("publish-date-label");

        HBox miscBox = new HBox(10, publishDateLabel, isbnLabel);

        VBox vBox = new VBox(titleLabel, subtitleLabel, authorsLabel, linksBox, descriptionLabel, miscBox);

        vBox.getStyleClass().add("vertical-box");
        vBox.setFillWidth(true);
        vBox.setMinHeight(Region.USE_PREF_SIZE);

        HBox.setHgrow(vBox, Priority.ALWAYS);

        HBox titleBox = new HBox(coverImageView, vBox);
        titleBox.setMinHeight(Region.USE_PREF_SIZE);
        titleBox.getStyleClass().add("horizontal-box");

        SectionPane sectionPane = new SectionPane(titleBox);
        sectionPane.getStyleClass().add("title-section");
        sectionPane.setMinHeight(Region.USE_PREF_SIZE);

        getChildren().addAll(sectionPane);
    }

    private void updateView() {
        Book book = getBook();
        if (book != null) {
            titleLabel.setText(book.getTitle());
            subtitleLabel.setText(book.getSubtitle());
            descriptionLabel.setText(book.getDescription());
            coverImageView.imageProperty().bind(ImageManager.getInstance().bookCoverImageProperty(book));
            authorsLabel.setText(book.getAuthors());
            isbnLabel.setText("ISBN: " + book.getIsbn());
            publishDateLabel.setText("Publish Date: " + DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM).format(book.getPublishedDate()));

            linksBox.getChildren().clear();

            if (StringUtils.isNotEmpty(book.getUrl())) {
                Button website = new Button("Website");
                website.getStyleClass().addAll("social-button", "website");
                website.setOnAction(evt -> Util.browse(book.getUrl()));
                website.setGraphic(new FontIcon(FontAwesomeBrands.SAFARI));
                linksBox.getChildren().add(website);
            }

            if (StringUtils.isNotEmpty(book.getAmazon())) {
                Button amazon = new Button("Amazon");
                amazon.getStyleClass().addAll("social-button", "amazon");
                amazon.setOnAction(evt -> Util.browse(book.getAmazon()));
                amazon.setGraphic(new FontIcon(FontAwesomeBrands.AMAZON));
                linksBox.getChildren().add(amazon);
            }
        }
    }

    private final ObjectProperty<Book> book = new SimpleObjectProperty<>(this, "book");

    public Book getBook() {
        return book.get();
    }

    public ObjectProperty<Book> bookProperty() {
        return book;
    }

    public void setBook(Book book) {
        this.book.set(book);
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