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
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
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
    private MarkdownView descriptionLabel = new MarkdownView();
    private Label authorsLabel = new Label();
    private Label isbnLabel = new Label();
    private Label publishDateLabel = new Label();

    private HBox linksBox = new HBox();

    public BookView(RootPane rootPane) {
        super(rootPane);

        getStyleClass().add("book-view");

        // book section
        coverImageView.setFitWidth(150);
        coverImageView.setPreserveRatio(true);

        titleLabel.getStyleClass().addAll("header1", "title-label");
        titleLabel.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(titleLabel, Priority.ALWAYS);

        subtitleLabel.getStyleClass().add("subtitle-label");
        subtitleLabel.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(subtitleLabel, Priority.ALWAYS);

        descriptionLabel.getStyleClass().add("description-label");
        descriptionLabel.setHyperlinkCallback(url -> Util.browse(url));
        HBox.setHgrow(descriptionLabel, Priority.ALWAYS);

        authorsLabel.getStyleClass().add("authors-label");
        isbnLabel.getStyleClass().add("isbn-label");
        linksBox.getStyleClass().add("social-box");
        publishDateLabel.getStyleClass().add("publish-date-label");

        HBox miscBox = new HBox(10, publishDateLabel, isbnLabel);

        VBox vBox = new VBox(titleLabel, subtitleLabel, authorsLabel, linksBox, descriptionLabel, miscBox);

        vBox.getStyleClass().add("vertical-box");
        vBox.setFillWidth(true);
        vBox.setMinHeight(Region.USE_PREF_SIZE);

        HBox.setHgrow(vBox, Priority.ALWAYS);

        HBox titleBox = new HBox(vBox, coverImageView);
        titleBox.setMinHeight(Region.USE_PREF_SIZE);
        titleBox.getStyleClass().add("horizontal-box");

        SectionPane bookSectionPane = new SectionPane(titleBox);
        bookSectionPane.getStyleClass().add("title-section");
        bookSectionPane.setMinHeight(Region.USE_PREF_SIZE);

        // authors section
        AdvancedListView<Person> authorListView = new AdvancedListView<>();
        authorListView.setCellFactory(view -> new DetailedPersonCell());
        authorListView.setPaging(true);
        authorListView.setVisibleRowCount(1000);
        authorListView.setItems(authorsProperty());
        authorListView.visibleProperty().bind(Bindings.isNotEmpty(authors));
        authorListView.managedProperty().bind(Bindings.isNotEmpty(authors));

        SectionPane authorSectionPane = new SectionPane(authorListView);
        authorSectionPane.setMinHeight(Region.USE_PREF_SIZE);
        authorSectionPane.visibleProperty().bind(authorListView.itemsProperty().emptyProperty().not());
        authorSectionPane.managedProperty().bind(authorListView.itemsProperty().emptyProperty().not());

        VBox content = new VBox(bookSectionPane, authorSectionPane);

        setContent(content);

        bookProperty().addListener(it -> updateView());
    }

    private void updateView() {
        Book book = getBook();
        if (book != null) {
            titleLabel.setText(book.getTitle());
            subtitleLabel.setText(book.getSubtitle());
            descriptionLabel.setMdString(book.getDescription());
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

    class AuthorCell extends AdvancedListCell<Person> {

        private final PhotoView photoView = new PhotoView();
        private final Label nameLabel = new Label();

        public AuthorCell() {
            getStyleClass().add("author-list-cell");

            photoView.setEditable(false);

            nameLabel.getStyleClass().add("name-label");

            HBox hBox = new HBox(photoView, nameLabel);
            hBox.setAlignment(Pos.CENTER_LEFT);
            hBox.getStyleClass().add("hbox");

            setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
            setGraphic(hBox);
        }

        @Override
        protected void updateItem(Person person, boolean empty) {
            super.updateItem(person, empty);

            if (!empty && person != null) {
                nameLabel.setText(person.getName());
                photoView.photoProperty().bind(ImageManager.getInstance().personImageProperty(person));
                photoView.setVisible(true);
            } else {
                nameLabel.setText("");
                photoView.setVisible(false);
            }
        }
    }
}