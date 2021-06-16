package com.dlsc.jfxcentral.views.detail.cells;

import com.dlsc.jfxcentral.data.DataRepository;
import com.dlsc.jfxcentral.data.ImageManager;
import com.dlsc.jfxcentral.data.model.News;
import com.dlsc.jfxcentral.views.MarkdownView;
import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;

import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.HashMap;
import java.util.Map;

public class DetailNewsCell extends DetailCell<News> {

    private final Label titleLabel = new Label();
    private final Label subtitleLabel = new Label();
    private final Label authorLabel = new Label();
    private final MarkdownView markdownView = new MarkdownView();
    private final ImageView bannerView = new ImageView();
    private final Button readMoreButton = new Button("Read more ...");
    private final Map<News, BooleanProperty> readMoreMap = new HashMap<>();

    public DetailNewsCell() {
        setPrefWidth(0);

        getStyleClass().add("detail-news-cell");

        readMoreButton.getStyleClass().add("read-more-button");

        titleLabel.getStyleClass().addAll("title-label");
        titleLabel.setWrapText(true);
        titleLabel.setMinHeight(Region.USE_PREF_SIZE);
        titleLabel.setMaxWidth(Double.MAX_VALUE);

        subtitleLabel.getStyleClass().addAll("subtitle-label");
        subtitleLabel.setWrapText(true);
        subtitleLabel.setMinHeight(Region.USE_PREF_SIZE);
        subtitleLabel.setMaxWidth(Double.MAX_VALUE);

        authorLabel.getStyleClass().add("author-label");
        authorLabel.setWrapText(true);
        authorLabel.setMinHeight(Region.USE_PREF_SIZE);
        authorLabel.setMaxWidth(Double.MAX_VALUE);

        VBox.setMargin(authorLabel, new Insets(10, 0, 0, 0));
        VBox.setMargin(markdownView, new Insets(20, 0, 0, 0));
        VBox.setMargin(readMoreButton, new Insets(10, 0, 0, 0));

        bannerView.setFitWidth(300);
        bannerView.setPreserveRatio(true);

        itemProperty().addListener(it -> {
            News item = getItem();
            if (item != null) {
                BooleanProperty readMoreProperty = readMoreMap.computeIfAbsent(item, key -> new SimpleBooleanProperty());
                markdownView.showImagesProperty().bind(readMoreProperty);
                readMoreButton.textProperty().bind(Bindings.createStringBinding(() -> readMoreProperty.get() ? "Show less ..." : "Read more ...", readMoreProperty));
                readMoreButton.setOnAction(evt -> readMoreProperty.set(!readMoreProperty.get()));

                markdownView.setBaseURL(DataRepository.getInstance().getNewsBaseUrl(item));
                markdownView.mdStringProperty().bind(Bindings.createStringBinding(() -> {
                    String text = DataRepository.getInstance().newsTextProperty(item).get();
                    if (text == null) {
                        text = "";
                    }
                    if (readMoreProperty.get()) {
                        return text;
                    } else {
                        String clipText = text.substring(0, Math.min(300, text.length()));
                        if (clipText.length() < text.length()) {
                            setShowReadMoreLink(true);
                            return clipText + " ...";
                        }
                        setShowReadMoreLink(false);
                        return clipText;
                    }

                }, readMoreProperty, DataRepository.getInstance().newsTextProperty(item)));

            } else {
                markdownView.showImagesProperty().unbind();
                readMoreButton.textProperty().unbind();
                markdownView.mdStringProperty().unbind();
            }
        });

        readMoreButton.visibleProperty().bind(showReadMoreLinkProperty());
        readMoreButton.managedProperty().bind(showReadMoreLinkProperty());

        StackPane imageWrapper = new StackPane(bannerView);
        imageWrapper.setMaxHeight(Region.USE_PREF_SIZE);
        imageWrapper.getStyleClass().add("banner-image-wrapper");
        StackPane.setAlignment(bannerView, Pos.TOP_LEFT);

        VBox vBox = new VBox(titleLabel, subtitleLabel, authorLabel, markdownView, readMoreButton);
        vBox.setAlignment(Pos.TOP_LEFT);
        vBox.setFillWidth(true);
        vBox.getStyleClass().add("vbox");

        HBox.setHgrow(vBox, Priority.ALWAYS);

        HBox hBox = new HBox(vBox, imageWrapper);
        hBox.getStyleClass().add("hbox");
        hBox.setAlignment(Pos.TOP_LEFT);

        setGraphic(hBox);
        setContentDisplay(ContentDisplay.GRAPHIC_ONLY);

        hBox.visibleProperty().bind(itemProperty().isNotNull());
    }

    private final BooleanProperty showReadMoreLink = new SimpleBooleanProperty(this, "showReadMoreLink", false);

    public BooleanProperty showReadMoreLinkProperty() {
        return showReadMoreLink;
    }

    public void setShowReadMoreLink(boolean showReadMoreLink) {
        this.showReadMoreLink.set(showReadMoreLink);
    }

    @Override
    protected void updateItem(News news, boolean empty) {
        super.updateItem(news, empty);

        if (!empty && news != null) {
            titleLabel.setText(news.getTitle());
            subtitleLabel.setText(news.getSubtitle());
            authorLabel.setText(news.getAuthor() + createSuffix(news));

            bannerView.setVisible(true);
            bannerView.setManaged(true);
            bannerView.imageProperty().bind(ImageManager.getInstance().newsBannerImageProperty(news));
        }
    }

    private String createSuffix(News news) {
        if (news.getModifiedOn().isAfter(news.getCreatedOn())) {
            return " - Updated on: " + DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM).format(news.getModifiedOn());
        }
        return " - Published on: " + DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM).format(news.getCreatedOn());
    }
}
