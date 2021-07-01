package com.dlsc.jfxcentral.views.detail.cells;

import com.dlsc.gemsfx.DialogPane;
import com.dlsc.jfxcentral.data.ImageManager;
import com.dlsc.jfxcentral.data.model.Video;
import com.dlsc.jfxcentral.util.Util;
import com.dlsc.jfxcentral.views.RootPane;
import com.jpro.webapi.HTMLView;
import com.jpro.webapi.WebAPI;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.web.WebView;
import org.kordamp.ikonli.javafx.FontIcon;
import org.kordamp.ikonli.materialdesign.MaterialDesign;

public class DetailVideoCell extends DetailCell<Video> {

    private final Button playButton = new Button("Play");
    private final Button playOnYouTubeButton = new Button("YouTube");
    private final RootPane rootPane;
    private final ResponsiveBox responsiveBox;

    public DetailVideoCell(RootPane rootPane, boolean largeImage) {
        this.rootPane = rootPane;

        getStyleClass().add("detail-video-cell");

        setPrefWidth(0);

        playButton.setGraphic(new FontIcon(MaterialDesign.MDI_PLAY));
        playButton.setOnAction(evt -> showVideo(getItem()));

        playOnYouTubeButton.setGraphic(new FontIcon(MaterialDesign.MDI_YOUTUBE_PLAY));

        responsiveBox = new ResponsiveBox(rootPane.isMobile() ? ResponsiveBox.ImageLocation.BANNER : largeImage ? ResponsiveBox.ImageLocation.LARGE_ON_SIDE : ResponsiveBox.ImageLocation.SMALL_ON_SIDE);
        responsiveBox.getExtraControls().addAll(playButton, playOnYouTubeButton);

        setGraphic(responsiveBox);
        setContentDisplay(ContentDisplay.GRAPHIC_ONLY);

        responsiveBox.visibleProperty().bind(itemProperty().isNotNull());
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
            responsiveBox.setTitle(video.getTitle());
            responsiveBox.setDescription(video.getDescription());
            responsiveBox.imageProperty().bind(ImageManager.getInstance().youTubeImageProperty(video));
        }
    }
}
