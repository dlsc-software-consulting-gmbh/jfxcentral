package com.dlsc.jfxcentral.views.detail.cells;

import com.dlsc.gemsfx.DialogPane;
import com.dlsc.jfxcentral.data.ImageManager;
import com.dlsc.jfxcentral.data.model.Video;
import com.dlsc.jfxcentral.util.Util;
import com.dlsc.jfxcentral.views.RootPane;
import com.jpro.webapi.HTMLView;
import com.jpro.webapi.WebAPI;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.web.WebView;
import org.kordamp.ikonli.javafx.FontIcon;
import org.kordamp.ikonli.materialdesign.MaterialDesign;

public class DetailVideoCell extends DetailCell<Video> {

    private final Label titleLabel = new Label();
    private final Label descriptionLabel = new Label();
    private final ImageView thumbnailView = new ImageView();
    private final Button playButton = new Button("Play");
    private final Button playOnYouTubeButton = new Button("YouTube");
    private final RootPane rootPane;

    public DetailVideoCell(RootPane rootPane, boolean largeImage) {
        this.rootPane = rootPane;

        getStyleClass().add("video-cell");

        setPrefWidth(0);

        playButton.setGraphic(new FontIcon(MaterialDesign.MDI_PLAY));
        playButton.setOnAction(evt -> showVideo(getItem()));

        playOnYouTubeButton.setGraphic(new FontIcon(MaterialDesign.MDI_YOUTUBE_PLAY));

        titleLabel.getStyleClass().addAll("header3", "title-label");
        titleLabel.setWrapText(true);
        titleLabel.setMinHeight(Region.USE_PREF_SIZE);

        descriptionLabel.getStyleClass().add("description-label");
        descriptionLabel.setWrapText(true);
        descriptionLabel.setMinHeight(Region.USE_PREF_SIZE);

        thumbnailView.setPreserveRatio(true);
        thumbnailView.setFitWidth(largeImage ? 320 : 160);

        StackPane thumbnailWrapper = new StackPane(thumbnailView);
        thumbnailWrapper.getStyleClass().add("thumbnail-wrapper");
        thumbnailWrapper.setMaxHeight(Region.USE_PREF_SIZE);
        StackPane.setAlignment(thumbnailView, Pos.TOP_LEFT);

        HBox buttonBox = new HBox(10, playButton, playOnYouTubeButton);
        buttonBox.setMinHeight(Region.USE_PREF_SIZE);
        buttonBox.setAlignment(Pos.BOTTOM_LEFT);

        VBox vBox = new VBox(titleLabel, descriptionLabel, buttonBox);
        vBox.setAlignment(Pos.TOP_LEFT);
        vBox.setFillWidth(true);
        vBox.getStyleClass().add("vbox");

        HBox.setHgrow(vBox, Priority.ALWAYS);

        HBox hBox = new HBox(vBox, thumbnailWrapper);
        hBox.getStyleClass().add("hbox");
        hBox.setAlignment(Pos.TOP_LEFT);

        setGraphic(hBox);
        setContentDisplay(ContentDisplay.GRAPHIC_ONLY);

        hBox.visibleProperty().bind(itemProperty().isNotNull());

        setOnMouseClicked(evt -> {
            if (evt.getClickCount() == 2) {
                showVideo(getItem());
            }
        });
    }

    private void showVideo(Video video) {
        if (WebAPI.isBrowser()) {
            HTMLView htmlView = new HTMLView();
            htmlView.setContent("<iframe width=\"960\" height=\"540\" src=\"https://www.youtube.com/embed/" + video.getId() + "\" title=\"YouTube video player\" frameborder=\"0\" allow=\"accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture\" allowfullscreen></iframe>");
            htmlView.setPrefSize(960, 540);
            htmlView.setMaxSize(960, 540);
            htmlView.setMinSize(960, 540);
            rootPane.getDialogPane().showNode(DialogPane.Type.BLANK, video.getTitle(), htmlView, false);
        } else {
            WebView webView = new WebView();
            webView.setMaxSize(960, 540);
            webView.getEngine().load("https://www.youtube.com/embed/" + video.getId());
            rootPane.getDialogPane().showNode(DialogPane.Type.BLANK, video.getTitle(), webView, false);
            webView.sceneProperty().addListener(it -> {
                if (webView.getScene() == null) {
                    webView.getEngine().loadContent("empty");
                }
            });
        }
    }

    @Override
    protected void updateItem(Video video, boolean empty) {
        super.updateItem(video, empty);

        if (!empty && video != null) {
            Util.setLink(playOnYouTubeButton, "https://youtu.be/" + getItem().getId(), "https://youtu.be/" + getItem().getId());

            titleLabel.setText(video.getTitle());
            descriptionLabel.setText(video.getDescription());
            thumbnailView.setVisible(true);
            thumbnailView.setManaged(true);
            thumbnailView.imageProperty().bind(ImageManager.getInstance().youTubeImageProperty(video));
        }
    }
}
