package com.dlsc.jfxcentral.views;

import com.dlsc.gemsfx.DialogPane;
import com.dlsc.jfxcentral.DataRepository;
import com.dlsc.jfxcentral.ImageManager;
import com.dlsc.jfxcentral.RootPane;
import com.dlsc.jfxcentral.model.Video;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.VPos;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.RowConstraints;
import javafx.scene.web.WebView;

public class VideosView extends PageView {

    public VideosView(RootPane rootPane) {
        super(rootPane);

        ListView<Video> listView = new ListView<>();
        listView.setMinWidth(Region.USE_PREF_SIZE);
        listView.setCellFactory(view -> new VideoCell());
        listView.itemsProperty().bind(DataRepository.getInstance().videosProperty());
        listView.getSelectionModel().selectedItemProperty().addListener(it -> setVideo(listView.getSelectionModel().getSelectedItem()));
        setContent(listView);
    }

    private final ObjectProperty<Video> video = new SimpleObjectProperty<>(this, "video");

    public Video getVideo() {
        return video.get();
    }

    public ObjectProperty<Video> videoProperty() {
        return video;
    }

    public void setVideo(Video video) {
        this.video.set(video);
    }

    private void showVideo(Video video) {
        WebView webView = new WebView();
        webView.getEngine().load("https://www.youtube.com/embed/" + video.getId());
        getRootPane().getDialogPane().showNode(DialogPane.Type.BLANK, video.getTitle(), webView, true);
    }

    class VideoCell extends ListCell<Video> {

        private final Label nameLabel = new Label();
        private final ImageView thumbnailView = new ImageView();

        public VideoCell() {
            getStyleClass().add("video-list-cell");

            nameLabel.getStyleClass().add("name-label");

            thumbnailView.setFitHeight(180);
            thumbnailView.setPreserveRatio(true);

            GridPane gridPane = new GridPane();
            gridPane.getStyleClass().add("grid-pane");
            gridPane.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);

            gridPane.add(nameLabel, 1, 0);

            GridPane.setHgrow(nameLabel, Priority.ALWAYS);
            GridPane.setVgrow(nameLabel, Priority.ALWAYS);
            GridPane.setValignment(nameLabel, VPos.BOTTOM);

            RowConstraints row1 = new RowConstraints();
            RowConstraints row2 = new RowConstraints();

            row1.setPercentHeight(50);
            row2.setPercentHeight(50);

            gridPane.getRowConstraints().setAll(row1, row2);

            setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
            setGraphic(thumbnailView);

            setOnMouseClicked(evt -> {
                if (evt.getClickCount() == 2) {
                    showVideo(getItem());
                }
            });
        }

        @Override
        protected void updateItem(Video video, boolean empty) {
            super.updateItem(video, empty);

            if (!empty && video != null) {
                nameLabel.setText(video.getTitle());
                thumbnailView.setVisible(true);
                thumbnailView.setManaged(true);
                thumbnailView.imageProperty().bind(ImageManager.getInstance().youTubeImageProperty(video));
            } else {
                nameLabel.setText("");
                thumbnailView.imageProperty().unbind();
                thumbnailView.setVisible(false);
                thumbnailView.setManaged(false);
            }
        }
    }
}