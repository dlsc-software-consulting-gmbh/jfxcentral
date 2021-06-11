package com.dlsc.jfxcentral.views.detail.cells;

import com.dlsc.jfxcentral.data.ImageManager;
import com.dlsc.jfxcentral.data.model.Blog;
import com.dlsc.jfxcentral.util.PageUtil;
import com.dlsc.jfxcentral.util.Util;
import com.jpro.webapi.WebAPI;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import org.apache.commons.lang3.StringUtils;
import org.kordamp.ikonli.javafx.FontIcon;
import org.kordamp.ikonli.materialdesign.MaterialDesign;

public class DetailBlogCell extends DetailCell<Blog> {

    private final Button detailsButton;
    private final Button visitButton;

    private Label titleLabel = new Label();
    private Label descriptionLabel = new Label();

    private ImageView pageImageView = new ImageView();
    private HBox buttonBox = new HBox();

    public DetailBlogCell() {
        getStyleClass().add("blog-cell");

        titleLabel.getStyleClass().addAll("header3", "title-label");
        titleLabel.setMaxWidth(Double.MAX_VALUE);
        titleLabel.setWrapText(true);
        titleLabel.setMinHeight(Region.USE_PREF_SIZE);

        descriptionLabel.getStyleClass().add("description-label");
        descriptionLabel.setMaxWidth(Double.MAX_VALUE);
        descriptionLabel.setWrapText(true);
        descriptionLabel.setMinHeight(Region.USE_PREF_SIZE);

        buttonBox.getStyleClass().add("button-box");

        detailsButton = new Button("Details");
        detailsButton.getStyleClass().addAll("library-button", "details");
        detailsButton.setGraphic(new FontIcon(MaterialDesign.MDI_MORE));

        buttonBox.getChildren().add(detailsButton);

        visitButton = new Button("Visit");
        visitButton.getStyleClass().addAll("library-button", "visit");
        visitButton.setGraphic(new FontIcon(MaterialDesign.MDI_WEB));
        buttonBox.getChildren().add(visitButton);

        titleLabel.setContentDisplay(ContentDisplay.RIGHT);
        titleLabel.setGraphicTextGap(20);

        Region spacer = new Region();
        VBox.setVgrow(spacer, Priority.ALWAYS);

        VBox vBox = new VBox(titleLabel, descriptionLabel, spacer, buttonBox);
        vBox.getStyleClass().add("vbox");
        HBox.setHgrow(vBox, Priority.ALWAYS);

        pageImageView.setFitWidth(160);
        pageImageView.setPreserveRatio(true);

        HBox hBox = new HBox(vBox, pageImageView);
        hBox.getStyleClass().add("hbox");

        setGraphic(hBox);
        setContentDisplay(ContentDisplay.GRAPHIC_ONLY);

        if (WebAPI.isBrowser()) {
            setMouseTransparent(true);
        }
    }

    @Override
    protected void updateItem(Blog item, boolean empty) {
        super.updateItem(item, empty);

        if (!empty && item != null) {
            pageImageView.imageProperty().bind(ImageManager.getInstance().blogPageImageProperty(item));
            pageImageView.setVisible(true);

            titleLabel.setText(item.getTitle());
            descriptionLabel.setText(StringUtils.abbreviate(item.getSummary(), 250));

            visitButton.setVisible(StringUtils.isNotEmpty(item.getUrl()));
            visitButton.setManaged(StringUtils.isNotEmpty(item.getUrl()));

            buttonBox.setVisible(visitButton.isVisible() || detailsButton.isVisible());
            buttonBox.setManaged(buttonBox.isVisible());

            Util.setLink(detailsButton, PageUtil.getLink(item), item.getSummary());
            Util.setLink(visitButton, item.getUrl(), item.getSummary());
        } else {
            pageImageView.imageProperty().unbind();
            pageImageView.setVisible(false);
            buttonBox.setVisible(false);
            buttonBox.setManaged(false);
        }
    }
}
