package com.dlsc.jfxcentral.views.detail.cells;

import com.dlsc.jfxcentral.data.DataRepository;
import com.dlsc.jfxcentral.data.ImageManager;
import com.dlsc.jfxcentral.data.model.News;
import com.dlsc.jfxcentral.views.RootPane;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.layout.Region;

import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

public class DetailNewsCell extends DetailCell<News> {

    private final Label authorLabel = new Label();
    private final ResponsiveBox responsiveBox;
    private final DateTimeFormatter dateTimeFormatter;

    public DetailNewsCell(RootPane rootPane, boolean largeImage) {
        setPrefWidth(0);

        dateTimeFormatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM).withLocale(rootPane.getLocale());

        getStyleClass().add("detail-news-cell");

        authorLabel.getStyleClass().add("author-label");
        authorLabel.setWrapText(true);
        authorLabel.setMinHeight(Region.USE_PREF_SIZE);
        authorLabel.setMaxWidth(Double.MAX_VALUE);

        responsiveBox = new ResponsiveBox(rootPane.isMobile() ? ResponsiveBox.ImageLocation.BANNER : largeImage ? ResponsiveBox.ImageLocation.LARGE_ON_SIDE : ResponsiveBox.ImageLocation.SMALL_ON_SIDE);
        responsiveBox.visibleProperty().bind(itemProperty().isNotNull());
        responsiveBox.setFooter(authorLabel);

        setGraphic(responsiveBox);
        setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
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
            responsiveBox.setTitle(news.getName());
            responsiveBox.setSubtitle(news.getSubtitle());
            responsiveBox.imageProperty().bind(ImageManager.getInstance().newsBannerImageProperty(news));
            responsiveBox.descriptionProperty().bind(DataRepository.getInstance().newsTextProperty(news));
            authorLabel.setText(news.getAuthor() + createSuffix(news));
        }
    }

    private String createSuffix(News news) {
        if (news.getModifiedOn().isAfter(news.getCreatedOn())) {
            return " - Updated on: " + dateTimeFormatter.format(news.getModifiedOn());
        }
        return " - Published on: " + dateTimeFormatter.format(news.getCreatedOn());
    }
}
