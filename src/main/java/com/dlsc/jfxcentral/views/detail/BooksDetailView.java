package com.dlsc.jfxcentral.views.detail;

import com.dlsc.jfxcentral.data.DataRepository;
import com.dlsc.jfxcentral.data.ImageManager;
import com.dlsc.jfxcentral.data.model.Book;
import com.dlsc.jfxcentral.panels.SectionPane;
import com.dlsc.jfxcentral.util.Util;
import com.dlsc.jfxcentral.views.RootPane;
import com.dlsc.jfxcentral.views.View;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import org.apache.commons.lang3.StringUtils;
import org.kordamp.ikonli.fontawesome5.FontAwesomeBrands;
import org.kordamp.ikonli.javafx.FontIcon;

import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

public class BooksDetailView extends ModelObjectDetailView<Book> {

    private final DateTimeFormatter dateTimeFormatter;
    private ImageView coverImageView = new ImageView();
    private Label titleLabel = new Label();
    private Label subtitleLabel = new Label();
    private Label authorsLabel = new Label();
    private Label isbnLabel = new Label();
    private Label publisherLabel = new Label();
    private Label publishDateLabel = new Label();

    private FlowPane linksBox = new FlowPane();

    public BooksDetailView(RootPane rootPane) {
        super(rootPane, View.BOOKS);

        getStyleClass().add("books-detail-view");

        dateTimeFormatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM).withLocale(rootPane.getLocale());

        createTitleBox();
        createReadMeBox(book -> DataRepository.getInstance().getRepositoryDirectoryURL() + "books/" + book.getId(), book -> DataRepository.getInstance().bookTextProperty(book));
        createStandardBoxes();
    }

    @Override
    protected void createTitleBox() {
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

        VBox vBox = new VBox(titleLabel, subtitleLabel, authorsLabel, miscBox, spacer, linksBox);

        vBox.getStyleClass().add("vertical-box");
        vBox.setFillWidth(true);
        vBox.setMinHeight(Region.USE_PREF_SIZE);

        HBox.setHgrow(vBox, Priority.ALWAYS);

        HBox hBox = new HBox(vBox, coverImageView);
        hBox.setMinHeight(Region.USE_PREF_SIZE);
        hBox.getStyleClass().add("horizontal-box");

        SectionPane sectionPane = new SectionPane(hBox);
        sectionPane.getStyleClass().add("title-section");
        sectionPane.setMinHeight(Region.USE_PREF_SIZE);

        content.getChildren().add(sectionPane);
    }

    protected boolean isUsingMasterView() {
        return true;
    }

    @Override
    protected void updateView(Book oldBook, Book book) {
        if (book != null) {
            titleLabel.setText(book.getName());
            subtitleLabel.setText(book.getSubtitle());
            coverImageView.imageProperty().bind(ImageManager.getInstance().bookCoverImageProperty(book));
            authorsLabel.setText(book.getAuthors());
            isbnLabel.setText("ISBN: " + book.getIsbn());
            publisherLabel.setText("Publisher: " + book.getPublisher());
            publishDateLabel.setText("Publish Date: " + dateTimeFormatter.format(book.getPublishedDate()));

            linksBox.getChildren().clear();

            if (StringUtils.isNotEmpty(book.getUrl())) {
                Button website = new Button("Website");
                website.getStyleClass().addAll("social-button", "website");
                Util.setLink(website, book.getUrl(), book.getName());
                website.setGraphic(new FontIcon(FontAwesomeBrands.SAFARI));
                linksBox.getChildren().add(website);
            }

            if (StringUtils.isNotEmpty(book.getAmazonASIN())) {
                Button amazon = new Button("Amazon");
                amazon.getStyleClass().addAll("social-button", "amazon");
                Util.setLink(amazon, "http://www.amazon.com/dp/" + book.getAmazonASIN(), book.getName());
                amazon.setGraphic(new FontIcon(FontAwesomeBrands.AMAZON));
                linksBox.getChildren().add(amazon);
            }
        }
    }
}