package com.dlsc.jfxcentral.views;

import com.dlsc.gemsfx.DialogPane;
import com.dlsc.gemsfx.FilterView;
import com.dlsc.jfxcentral.AdvancedListView;
import com.dlsc.jfxcentral.DataRepository;
import com.dlsc.jfxcentral.ImageManager;
import com.dlsc.jfxcentral.RootPane;
import com.dlsc.jfxcentral.model.Video;
import com.dlsc.jfxcentral.panels.SectionPaneWithFilterView;
import com.dlsc.jfxcentral.util.Util;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.web.WebView;
import org.kordamp.ikonli.javafx.FontIcon;
import org.kordamp.ikonli.materialdesign.MaterialDesign;

public class VideosView extends PageView {

    public VideosView(RootPane rootPane) {
        super(rootPane);

        AdvancedListView<Video> listView = new AdvancedListView<>();
        listView.setMaxHeight(Double.MAX_VALUE);
        listView.setMinWidth(Region.USE_PREF_SIZE);
        listView.setCellFactory(view -> new VideoCell());
        listView.itemsProperty().bind(DataRepository.getInstance().videosProperty());
        listView.getSelectionModel().selectedItemProperty().addListener(it -> setVideo(listView.getSelectionModel().getSelectedItem()));
        VBox.setVgrow(listView, Priority.ALWAYS);

        SectionPaneWithFilterView sectionPane = new SectionPaneWithFilterView(listView);
        sectionPane.setTitle("Videos");
        sectionPane.setSubtitle("Found 89 videos");

        FilterView filterView = sectionPane.getFilterView();
        filterView.setTextFilterProvider(text -> {
            return true;
        });

        // TYPE GROUP

        FilterView.FilterGroup<Video> typeGroup = new FilterView.FilterGroup<>("Type");
        typeGroup.getFilters().add(new FilterView.Filter("Real-World Application") {
            @Override
            public boolean test(Object o) {
                return false;
            }
        });
        typeGroup.getFilters().add(new FilterView.Filter("Library") {
            @Override
            public boolean test(Object o) {
                return false;
            }
        });
        typeGroup.getFilters().add(new FilterView.Filter("Tips & Tricks") {
            @Override
            public boolean test(Object o) {
                return false;
            }
        });
        typeGroup.getFilters().add(new FilterView.Filter("Keynote") {
            @Override
            public boolean test(Object o) {
                return false;
            }
        });
        typeGroup.getFilters().add(new FilterView.Filter("Tutorial") {
            @Override
            public boolean test(Object o) {
                return false;
            }
        });

        filterView.getFilterGroups().add(typeGroup);

        // EVENT GROUP

        FilterView.FilterGroup<Video> eventGroup = new FilterView.FilterGroup<>("Event");
        eventGroup.getFilters().add(new FilterView.Filter("JFX-Days 2020") {
            @Override
            public boolean test(Object o) {
                return false;
            }
        });
        eventGroup.getFilters().add(new FilterView.Filter("JFX-Days 2019") {
            @Override
            public boolean test(Object o) {
                return false;
            }
        });
        eventGroup.getFilters().add(new FilterView.Filter("JFX-Days 2018") {
            @Override
            public boolean test(Object o) {
                return false;
            }
        });

        filterView.getFilterGroups().add(eventGroup);

        // PLATFORM GROUP

        FilterView.FilterGroup<Video> platformGroup = new FilterView.FilterGroup<>("Platform");

        platformGroup.getFilters().add(new FilterView.Filter("Desktop") {
            @Override
            public boolean test(Object o) {
                return false;
            }
        });

        platformGroup.getFilters().add(new FilterView.Filter("Mobile") {
            @Override
            public boolean test(Object o) {
                return false;
            }
        });

        platformGroup.getFilters().add(new FilterView.Filter("Web") {
            @Override
            public boolean test(Object o) {
                return false;
            }
        });

        platformGroup.getFilters().add(new FilterView.Filter("Embedded") {
            @Override
            public boolean test(Object o) {
                return false;
            }
        });

        filterView.getFilterGroups().add(platformGroup);

        setContent(sectionPane);
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
        webView.sceneProperty().addListener(it -> {
            if (webView.getScene() == null) {
                System.out.println("Unloading");
                webView.getEngine().loadContent("empty");
            }
        });
    }

    class VideoCell extends ListCell<Video> {

        private final Label titleLabel = new Label();
        private final Label descriptionLabel = new Label();
        private final ImageView thumbnailView = new ImageView();
        private final Button playButton = new Button("Play");
        private final Button playOnYouTubeButton = new Button("YouTube");

        public VideoCell() {
            getStyleClass().add("video-cell");

            playButton.setGraphic(new FontIcon(MaterialDesign.MDI_PLAY));
            playButton.setOnAction(evt -> showVideo(getItem()));

            playOnYouTubeButton.setGraphic(new FontIcon(MaterialDesign.MDI_YOUTUBE_PLAY));
            playOnYouTubeButton.setOnAction(evt -> Util.browse("https://youtu.be/" + getItem().getId()));

            titleLabel.getStyleClass().add("title-label");
            titleLabel.setWrapText(true);
            titleLabel.setMinHeight(Region.USE_PREF_SIZE);

            descriptionLabel.getStyleClass().add("description-label");
            descriptionLabel.setWrapText(true);
            descriptionLabel.setMinHeight(Region.USE_PREF_SIZE);

            thumbnailView.setPreserveRatio(true);
            thumbnailView.setFitWidth(320);

            StackPane coverImageWrapper = new StackPane(thumbnailView);
            StackPane.setAlignment(thumbnailView, Pos.TOP_LEFT);

            HBox buttonBox = new HBox(10, playButton, playOnYouTubeButton);

            GridPane.setRowSpan(coverImageWrapper, 3);

            GridPane gridPane = new GridPane();
            gridPane.getStyleClass().add("grid-pane");
            gridPane.setPrefWidth(0);
            gridPane.setMinHeight(Region.USE_PREF_SIZE);
            gridPane.setMinSize(0, 0);
            gridPane.add(coverImageWrapper, 0, 0);
            gridPane.add(titleLabel, 1, 0);
            gridPane.add(descriptionLabel, 1, 1);
            gridPane.add(buttonBox, 1, 2);

            RowConstraints row1 = new RowConstraints();
            RowConstraints row2 = new RowConstraints();
            RowConstraints row3 = new RowConstraints();

            row1.setValignment(VPos.TOP);
            row2.setValignment(VPos.TOP);
            row3.setValignment(VPos.BOTTOM);

            gridPane.getRowConstraints().setAll(row1, row2);

            setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
            setGraphic(gridPane);

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
                titleLabel.setText(video.getTitle());
                descriptionLabel.setText(video.getDescription());
                thumbnailView.setVisible(true);
                thumbnailView.setManaged(true);
                thumbnailView.imageProperty().bind(ImageManager.getInstance().youTubeImageProperty(video));
            } else {
                titleLabel.setText("");
                descriptionLabel.setText("");
                thumbnailView.imageProperty().unbind();
                thumbnailView.setVisible(false);
                thumbnailView.setManaged(false);
            }
        }
    }
}