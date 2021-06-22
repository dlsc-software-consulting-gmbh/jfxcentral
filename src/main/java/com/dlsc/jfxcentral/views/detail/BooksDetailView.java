package com.dlsc.jfxcentral.views.detail;

import com.dlsc.jfxcentral.data.DataRepository;
import com.dlsc.jfxcentral.data.ImageManager;
import com.dlsc.jfxcentral.data.model.Book;
import com.dlsc.jfxcentral.data.model.Person;
import com.dlsc.jfxcentral.panels.SectionPane;
import com.dlsc.jfxcentral.util.Util;
import com.dlsc.jfxcentral.views.AdvancedListView;
import com.dlsc.jfxcentral.views.MarkdownView;
import com.dlsc.jfxcentral.views.RootPane;
import com.dlsc.jfxcentral.views.detail.cells.DetailPersonCell;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import org.apache.commons.lang3.StringUtils;
import org.kordamp.ikonli.fontawesome5.FontAwesomeBrands;
import org.kordamp.ikonli.javafx.FontIcon;

import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.stream.Collectors;

public class BooksDetailView extends DetailView<Book> {

    private ImageView coverImageView = new ImageView();
    private Label titleLabel = new Label();
    private Label subtitleLabel = new Label();
    private MarkdownView descriptionMarkdownView = new MarkdownView();
    private Label authorsLabel = new Label();
    private Label isbnLabel = new Label();
    private Label publisherLabel = new Label();
    private Label publishDateLabel = new Label();

    private FlowPane linksBox = new FlowPane();

    public BooksDetailView(RootPane rootPane) {
        super(rootPane);

        getStyleClass().add("books-detail-view");

        // book section
        coverImageView.setFitWidth(128);
        coverImageView.setPreserveRatio(true);

        titleLabel.getStyleClass().addAll("header1", "title-label");
        titleLabel.setMaxWidth(Double.MAX_VALUE);
        titleLabel.setWrapText(true);
        titleLabel.setMinHeight(Region.USE_PREF_SIZE);

        HBox.setHgrow(titleLabel, Priority.ALWAYS);

        subtitleLabel.getStyleClass().add("subtitle-label");
        subtitleLabel.setMaxWidth(Double.MAX_VALUE);
        subtitleLabel.setWrapText(true);
        subtitleLabel.setMinHeight(Region.USE_PREF_SIZE);
        HBox.setHgrow(subtitleLabel, Priority.ALWAYS);

        descriptionMarkdownView.getStyleClass().add("description-markdown-view");
        HBox.setHgrow(descriptionMarkdownView, Priority.ALWAYS);

        authorsLabel.setWrapText(true);
        authorsLabel.setMinHeight(Region.USE_PREF_SIZE);
        authorsLabel.getStyleClass().add("authors-label");

        isbnLabel.getStyleClass().add("isbn-label");
        linksBox.getStyleClass().add("social-box");
        publisherLabel.getStyleClass().add("publisher-label");
        publishDateLabel.getStyleClass().add("publish-date-label");

        FlowPane miscBox = new FlowPane(publisherLabel, publishDateLabel, isbnLabel);
        miscBox.setVgap(10);
        miscBox.setHgap(10);

        Region spacer = new Region();
        VBox.setVgrow(spacer, Priority.ALWAYS);

        VBox vBox = new VBox(titleLabel, subtitleLabel, authorsLabel, spacer, miscBox, linksBox);

        vBox.getStyleClass().add("vertical-box");
        vBox.setFillWidth(true);
        vBox.setMinHeight(Region.USE_PREF_SIZE);

        HBox.setHgrow(vBox, Priority.ALWAYS);

        HBox titleBox = new HBox(vBox, coverImageView);
        titleBox.setMinHeight(Region.USE_PREF_SIZE);
        titleBox.getStyleClass().add("horizontal-box");

        VBox content = new VBox(titleBox, descriptionMarkdownView);
        content.getStyleClass().add("content");

        SectionPane bookSectionPane = new SectionPane(content);
        bookSectionPane.getStyleClass().add("title-section");
        bookSectionPane.setMinHeight(Region.USE_PREF_SIZE);

        // authors section
        AdvancedListView<Person> authorListView = new AdvancedListView<>();
        authorListView.setCellFactory(view -> new DetailPersonCell(rootPane, true));
        authorListView.setPaging(true);
        authorListView.setVisibleRowCount(1000);
        authorListView.setItems(authorsProperty());
        authorListView.visibleProperty().bind(Bindings.isNotEmpty(authors));
        authorListView.managedProperty().bind(Bindings.isNotEmpty(authors));

        SectionPane authorSectionPane = new SectionPane(authorListView);
        authorSectionPane.setMinHeight(Region.USE_PREF_SIZE);
        authorSectionPane.visibleProperty().bind(authorListView.itemsProperty().emptyProperty().not());
        authorSectionPane.managedProperty().bind(authorListView.itemsProperty().emptyProperty().not());

        VBox content2 = new VBox(bookSectionPane, authorSectionPane);

        setContent(content2);

        selectedItemProperty().addListener(it -> updateView());
    }

    private void updateView() {
        Book book = getSelectedItem();
        if (book != null) {
            titleLabel.setText(book.getTitle());
            subtitleLabel.setText(book.getSubtitle());
            descriptionMarkdownView.mdStringProperty().bind(DataRepository.getInstance().bookTextProperty(book));
            coverImageView.imageProperty().bind(ImageManager.getInstance().bookCoverImageProperty(book));
            authorsLabel.setText(book.getAuthors());
            isbnLabel.setText("ISBN: " + book.getIsbn());
            publisherLabel.setText("Publisher: " + book.getPublisher());
            publishDateLabel.setText("Publish Date: " + DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM).format(book.getPublishedDate()));

            linksBox.getChildren().clear();

            if (StringUtils.isNotEmpty(book.getUrl())) {
                Button website = new Button("Website");
                website.getStyleClass().addAll("social-button", "website");
                Util.setLink(website, book.getUrl(), book.getTitle());
                website.setGraphic(new FontIcon(FontAwesomeBrands.SAFARI));
                linksBox.getChildren().add(website);
            }

            if (StringUtils.isNotEmpty(book.getAmazonASIN())) {
                Button amazon = new Button("Amazon");
                amazon.getStyleClass().addAll("social-button", "amazon");
                Util.setLink(amazon, "http://www.amazon.com/dp/" + book.getAmazonASIN(), book.getTitle());
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
}