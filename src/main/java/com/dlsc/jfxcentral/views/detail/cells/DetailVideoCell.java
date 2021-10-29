package com.dlsc.jfxcentral.views.detail.cells;

import com.dlsc.jfxcentral.data.ImageManager;
import com.dlsc.jfxcentral.data.model.Video;
import com.dlsc.jfxcentral.util.PageUtil;
import com.dlsc.jfxcentral.util.Util;
import com.dlsc.jfxcentral.views.RootPane;
import com.jpro.webapi.HTMLView;
import com.jpro.webapi.WebAPI;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.layout.Region;
import javafx.scene.web.WebView;
import org.kordamp.ikonli.javafx.FontIcon;
import org.kordamp.ikonli.materialdesign.MaterialDesign;

public class DetailVideoCell extends DetailCell<Video> {

    private final Button playButton = new Button("Play");
    private final Button playOnYouTubeButton = new Button("YouTube");
    private final RootPane rootPane;
    private final ResponsiveBox responsiveBox;
    private final boolean primaryView;

    public DetailVideoCell(RootPane rootPane, boolean primaryView) {
        this.rootPane = rootPane;
        this.primaryView = primaryView;

        getStyleClass().add("detail-video-cell");

        setPrefWidth(0);

        playButton.setGraphic(new FontIcon(MaterialDesign.MDI_PLAY));
        playButton.setOnAction(evt -> showVideo(getItem()));

        playOnYouTubeButton.setGraphic(new FontIcon(MaterialDesign.MDI_YOUTUBE_PLAY));

        responsiveBox = new ResponsiveBox(rootPane.isMobile() ? ResponsiveBox.ImageLocation.BANNER : primaryView ? ResponsiveBox.ImageLocation.LARGE_ON_SIDE : ResponsiveBox.ImageLocation.SMALL_ON_SIDE);
        responsiveBox.getExtraControls().addAll(playButton, playOnYouTubeButton);
        responsiveBox.getImageView().setOnMouseClicked(evt -> {
            if (evt.isStillSincePress()) {
                showVideo(getItem());
            }
        });
        responsiveBox.getImageView().setCursor(Cursor.HAND);

        setGraphic(responsiveBox);
        setContentDisplay(ContentDisplay.GRAPHIC_ONLY);

        responsiveBox.visibleProperty().bind(itemProperty().isNotNull());
    }

    private void showVideo(Video video) {
        if (WebAPI.isBrowser()) {
            HTMLView htmlView = new HTMLView();
            htmlView.setContent("<div class=\"yt\"><iframe width=\"960\" height=\"540\" src=\"https://www.youtube.com/embed/" + video.getId() + "\" allowfullscreen></iframe></div></body></html>\n");
            htmlView.parentProperty().addListener(it -> {
                Parent parent = htmlView.getParent();
                if (parent != null) {
                    htmlView.prefWidthProperty().bind((((Region) parent).widthProperty().multiply(.9)));
                    htmlView.prefHeightProperty().bind(htmlView.prefWidthProperty().divide(16).multiply(9));

                    htmlView.minWidthProperty().bind((((Region) parent).widthProperty().multiply(.9)));
                    htmlView.minHeightProperty().bind(htmlView.minWidthProperty().divide(16).multiply(9));

                    htmlView.maxWidthProperty().bind((((Region) parent).widthProperty().multiply(.9)));
                    htmlView.maxHeightProperty().bind(htmlView.maxWidthProperty().divide(16).multiply(9));
                }
            });

            rootPane.getOverlayPane().setContent(htmlView);
        } else {
            WebView webView = new WebView();
            webView.parentProperty().addListener(it -> {
                Parent parent = webView.getParent();
                if (parent != null) {
                    webView.prefWidthProperty().bind((((Region) parent).widthProperty().multiply(.9)));
                    webView.prefHeightProperty().bind(webView.prefWidthProperty().divide(16).multiply(9));

                    webView.minWidthProperty().bind((((Region) parent).widthProperty().multiply(.9)));
                    webView.minHeightProperty().bind(webView.minWidthProperty().divide(16).multiply(9));

                    webView.maxWidthProperty().bind((((Region) parent).widthProperty().multiply(.9)));
                    webView.maxHeightProperty().bind(webView.maxWidthProperty().divide(16).multiply(9));
                }
            });
            webView.getEngine().load("https://www.youtube.com/embed/" + video.getId());

            rootPane.getOverlayPane().setContent(webView);
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
            responsiveBox.setTitle(video.getName());
            if (!primaryView) {
                Util.setLink(responsiveBox.getTitleLabel(), PageUtil.getLink(video), video.getName());
            }
            responsiveBox.setDescription(video.getDescription());
            responsiveBox.imageProperty().bind(ImageManager.getInstance().youTubeImageProperty(video));
        }
    }
}
