package com.dlsc.jfxcentral.views;

import com.dlsc.jfxcentral.*;
import com.dlsc.jfxcentral.model.Book;
import com.dlsc.jfxcentral.model.Person;
import com.dlsc.jfxcentral.panels.SectionPane;
import com.dlsc.jfxcentral.util.Util;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ListProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
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
import java.util.stream.Collectors;

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

        coverImageView.setFitWidth(150);
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

        authorsLabel.getStyleClass().add("authors-label");

        isbnLabel.getStyleClass().add("isbn-label");

        linksBox.getStyleClass().add("social-box");

        publishDateLabel = new Label();
        publishDateLabel.getStyleClass().add("publish-date-label");

        HBox miscBox = new HBox(10, publishDateLabel, isbnLabel);

        AdvancedListView<Person> authorListView = new AdvancedListView<>();
        authorListView.setCellFactory(view -> new AuthorCell());
        authorListView.setPaging(true);
        authorListView.setVisibleRowCount(1000);
        authorListView.setItems(authorsProperty());
        authorListView.visibleProperty().bind(Bindings.isNotEmpty(authors));
        authorListView.managedProperty().bind(Bindings.isNotEmpty(authors));

        Label authorsTitleLabel = new Label();
        authorsTitleLabel.getStyleClass().add("author-title-label");
        authorsTitleLabel.textProperty().bind(Bindings.createStringBinding(() -> authors.size() > 1 ? "Authors" : "Author", authors));
        authorsTitleLabel.visibleProperty().bind(Bindings.isNotEmpty(authors));
        authorsTitleLabel.managedProperty().bind(Bindings.isNotEmpty(authors));

        VBox vBox = new VBox(titleLabel, subtitleLabel, authorsLabel, linksBox, descriptionLabel, miscBox, authorsTitleLabel, authorListView);

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

        setContent(new VBox(sectionPane));

        bookProperty().addListener(it -> updateView());
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

            getAuthors().setAll(DataRepository.getInstance().getPeople().stream().filter(person -> book.getPersonIds().contains(person.getId())).collect(Collectors.toList()));
        }
    }

    private final ListProperty<Person> authors = new SimpleListProperty<>(this, "authors", FXCollections.observableArrayList());

    public ObservableList<Person> getAuthors() {
        return authors.get();
    }

    public ListProperty<Person> authorsProperty() {
        return authors;
    }

    public void setAuthors(ObservableList<Person> authors) {
        this.authors.set(authors);
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

    class AuthorCell extends ListCell<Person> {

        private final PhotoView photoView = new PhotoView();
        private final Label nameLabel = new Label();
        private final ImageView championImageView = new ImageView();
        private final ImageView rockstarImageView = new ImageView();

        public AuthorCell() {
            getStyleClass().add("author-list-cell");

            photoView.setEditable(false);

            nameLabel.getStyleClass().add("name-label");

            championImageView.getStyleClass().add("champion-image");
            championImageView.setPreserveRatio(true);
            championImageView.setFitHeight(16);

            rockstarImageView.getStyleClass().add("rockstar-image");
            rockstarImageView.setPreserveRatio(true);
            rockstarImageView.setFitHeight(16);

            HBox badgesBox = new HBox(championImageView, rockstarImageView);
            badgesBox.getStyleClass().add("badges");
            badgesBox.setAlignment(Pos.TOP_LEFT);

            GridPane gridPane = new GridPane();
            gridPane.getStyleClass().add("grid-pane");
            gridPane.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);

            gridPane.add(photoView, 0, 0);
            gridPane.add(nameLabel, 1, 0);
            gridPane.add(badgesBox, 1, 1);

            GridPane.setRowSpan(photoView, 2);
            GridPane.setHgrow(nameLabel, Priority.ALWAYS);
            GridPane.setHgrow(badgesBox, Priority.ALWAYS);
            GridPane.setVgrow(nameLabel, Priority.ALWAYS);
            GridPane.setVgrow(badgesBox, Priority.ALWAYS);
            GridPane.setValignment(nameLabel, VPos.BOTTOM);
            GridPane.setValignment(badgesBox, VPos.TOP);

            RowConstraints row1 = new RowConstraints();
            RowConstraints row2 = new RowConstraints();

            row1.setPercentHeight(50);
            row2.setPercentHeight(50);

            gridPane.getRowConstraints().setAll(row1, row2);

            setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
            setGraphic(gridPane);
        }

        @Override
        protected void updateItem(Person person, boolean empty) {
            super.updateItem(person, empty);

            if (!empty && person != null) {
                nameLabel.setText(person.getName());
                championImageView.setVisible(person.isChampion());
                championImageView.setManaged(person.isChampion());
                rockstarImageView.setVisible(person.isRockstar());
                rockstarImageView.setManaged(person.isRockstar());
                String photo = person.getPhoto();
                if (photo != null && !photo.trim().isBlank()) {
                    photoView.photoProperty().bind(ImageManager.getInstance().personImageProperty(person));
                } else {
                    photoView.photoProperty().unbind();
                }
                photoView.setVisible(true);
            } else {
                nameLabel.setText("");
                championImageView.setVisible(false);
                championImageView.setManaged(false);
                rockstarImageView.setVisible(false);
                rockstarImageView.setManaged(false);
                photoView.setVisible(false);
            }
        }
    }
}