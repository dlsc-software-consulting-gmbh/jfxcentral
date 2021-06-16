package com.dlsc.jfxcentral.views.detail.cells;

import com.dlsc.jfxcentral.data.DataRepository;
import com.dlsc.jfxcentral.data.ImageManager;
import com.dlsc.jfxcentral.data.model.Download;
import com.dlsc.jfxcentral.util.Util;
import com.dlsc.jfxcentral.views.MarkdownView;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import org.apache.commons.lang3.StringUtils;
import org.kordamp.ikonli.javafx.FontIcon;
import org.kordamp.ikonli.materialdesign.MaterialDesign;

public class DetailDownloadCell extends DetailCell<Download> {

    private final Label titleLabel = new Label();
    private final MarkdownView descriptionMarkdownView = new MarkdownView();
    private final ImageView imageView = new ImageView();
    private final FlowPane buttonBox;

    public DetailDownloadCell(boolean largeThumbnail) {

        getStyleClass().add("detail-download-cell");
        setPrefWidth(0);

        titleLabel.getStyleClass().addAll("header3", "title-label");
        titleLabel.setWrapText(true);
        titleLabel.setMinHeight(Region.USE_PREF_SIZE);

        descriptionMarkdownView.getStyleClass().add("description-label");
        VBox.setVgrow(descriptionMarkdownView, Priority.ALWAYS);

        imageView.setFitWidth(largeThumbnail ? 320 : 160);
        imageView.setPreserveRatio(true);

        StackPane thumbnailWrapper = new StackPane(imageView);
        thumbnailWrapper.getStyleClass().add("thumbnail-wrapper");
        thumbnailWrapper.setMaxHeight(Region.USE_PREF_SIZE);
        StackPane.setAlignment(imageView, Pos.TOP_LEFT);

        buttonBox = new FlowPane();
        buttonBox.getStyleClass().add("button-box");
        buttonBox.setMinHeight(Region.USE_PREF_SIZE);
        buttonBox.setAlignment(Pos.BOTTOM_LEFT);

        VBox vBox = new VBox(titleLabel, descriptionMarkdownView, buttonBox);
        vBox.setAlignment(Pos.TOP_LEFT);
        vBox.setFillWidth(true);
        vBox.getStyleClass().add("vbox");

        HBox.setHgrow(vBox, Priority.ALWAYS);

        HBox hBox = new HBox(vBox, thumbnailWrapper);
        hBox.setFillHeight(true);
        hBox.getStyleClass().add("hbox");
        hBox.setAlignment(Pos.TOP_LEFT);

        setGraphic(hBox);
        setContentDisplay(ContentDisplay.GRAPHIC_ONLY);

        hBox.visibleProperty().bind(itemProperty().isNotNull());
    }

    private void downloadFile(Download.DownloadFile downloadFile) {
        if (StringUtils.isNotBlank(downloadFile.getUrl())) {
            Util.browse(this, downloadFile.getUrl(), false);
        } else {
            Util.browse(this, DataRepository.getInstance().getBaseUrl() + "downloads/" + getItem().getId() + "/" + downloadFile.getFileName(), false);
        }
    }

    @Override
    protected void updateItem(Download download, boolean empty) {
        super.updateItem(download, empty);

        buttonBox.getChildren().clear();

        if (!empty && download != null) {
            titleLabel.setText(download.getTitle());
            descriptionMarkdownView.mdStringProperty().bind(DataRepository.getInstance().downloadTextProperty(download));
            imageView.setVisible(true);
            imageView.setManaged(true);
            imageView.imageProperty().bind(ImageManager.getInstance().downloadBannerImageProperty(download));

            download.getFiles().forEach(file -> {
                Button downloadButton = new Button(file.getName());
                downloadButton.setGraphic(new FontIcon(MaterialDesign.MDI_DOWNLOAD));
                downloadButton.setOnAction(evt -> downloadFile(file));
                buttonBox.getChildren().add(downloadButton);
            });

        } else {
            titleLabel.setText("");
            descriptionMarkdownView.mdStringProperty().unbind();
            imageView.setVisible(false);
            imageView.setManaged(false);
            imageView.imageProperty().unbind();
        }
    }
}