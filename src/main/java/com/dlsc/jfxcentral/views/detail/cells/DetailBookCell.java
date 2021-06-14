package com.dlsc.jfxcentral.views.detail.cells;

import com.dlsc.jfxcentral.data.DataRepository;
import com.dlsc.jfxcentral.data.ImageManager;
import com.dlsc.jfxcentral.data.model.Book;
import com.dlsc.jfxcentral.util.PageUtil;
import com.dlsc.jfxcentral.util.Util;
import com.dlsc.jfxcentral.views.MarkdownView;
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
import org.kordamp.ikonli.materialdesign.MaterialDesign;

public class DetailBookCell extends DetailCell<Book> {

    private final Button detailsButton;
    private final Button homepageButton;
    private final Button amazonButton;

    private Label titleLabel = new Label();
    private Label subtitleLabel = new Label();
    private Label authorsLabel = new Label();
    private MarkdownView descriptionLabel = new MarkdownView();

    private ImageView coverImageView = new ImageView();
    private HBox buttonBox = new HBox();

    public DetailBookCell(boolean largeImage) {
        getStyleClass().add("detail-book-cell");

        titleLabel.getStyleClass().addAll("header3", "title-label");
        titleLabel.setMaxWidth(Double.MAX_VALUE);
        titleLabel.setWrapText(true);
        titleLabel.setMinHeight(Region.USE_PREF_SIZE);

        subtitleLabel.getStyleClass().add("subtitle-label");
        subtitleLabel.setMaxWidth(Double.MAX_VALUE);
        subtitleLabel.setWrapText(true);
        subtitleLabel.setMinHeight(Region.USE_PREF_SIZE);

        authorsLabel.getStyleClass().add("authors-label");
        authorsLabel.setMaxWidth(Double.MAX_VALUE);
        authorsLabel.setWrapText(true);
        authorsLabel.setMinHeight(Region.USE_PREF_SIZE);

        descriptionLabel.getStyleClass().add("description-label");
        descriptionLabel.setMaxWidth(Double.MAX_VALUE);

        buttonBox.getStyleClass().add("button-box");

        detailsButton = new Button("Details");
        detailsButton.getStyleClass().addAll("library-button", "details");
        detailsButton.setGraphic(new FontIcon(MaterialDesign.MDI_MORE));
        buttonBox.getChildren().add(detailsButton);

        homepageButton = new Button("Homepage");
        homepageButton.getStyleClass().addAll("library-button", "homepage");
        homepageButton.setGraphic(new FontIcon(MaterialDesign.MDI_WEB));
        buttonBox.getChildren().add(homepageButton);

        amazonButton = new Button("Amazon");
        amazonButton.getStyleClass().addAll("library-button", "amazon");
        amazonButton.setGraphic(new FontIcon(FontAwesomeBrands.AMAZON));
        buttonBox.getChildren().add(amazonButton);

        titleLabel.setContentDisplay(ContentDisplay.RIGHT);
        titleLabel.setGraphicTextGap(20);

        VBox vBox = new VBox(titleLabel, subtitleLabel, authorsLabel, descriptionLabel, buttonBox);
        vBox.getStyleClass().add("vbox");
        HBox.setHgrow(vBox, Priority.ALWAYS);

        coverImageView.setFitWidth(largeImage ? 320 : 160);
        coverImageView.setPreserveRatio(true);

        HBox hBox = new HBox(vBox, coverImageView);
        hBox.getStyleClass().add("hbox");

        setGraphic(hBox);
        setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
    }

    @Override
    protected void updateItem(Book item, boolean empty) {
        super.updateItem(item, empty);

        if (!empty && item != null) {
            coverImageView.imageProperty().bind(ImageManager.getInstance().bookCoverImageProperty(item));
            coverImageView.setVisible(true);

            titleLabel.setText(item.getTitle());
            subtitleLabel.setText(item.getSubtitle());
            authorsLabel.setText(item.getAuthors());

            descriptionLabel.mdStringProperty().bind(DataRepository.getInstance().bookTextProperty(item));

            homepageButton.setVisible(StringUtils.isNotEmpty(item.getUrl()));
            homepageButton.setManaged(StringUtils.isNotEmpty(item.getUrl()));

            amazonButton.setVisible(StringUtils.isNotEmpty(item.getAmazonASIN()));
            amazonButton.setManaged(StringUtils.isNotEmpty(item.getAmazonASIN()));

            buttonBox.setVisible(homepageButton.isVisible() || amazonButton.isVisible());
            buttonBox.setManaged(buttonBox.isVisible());

            Util.setLink(homepageButton, item.getUrl(), item.getTitle());
            Util.setLink(detailsButton, PageUtil.getLink(item), item.getTitle());
            Util.setLink(amazonButton, "http://www.amazon.com/dp/" + item.getAmazonASIN(), item.getTitle());

        } else {
            coverImageView.imageProperty().unbind();
            coverImageView.setVisible(false);
            buttonBox.setVisible(false);
            buttonBox.setManaged(false);
        }
    }
}
