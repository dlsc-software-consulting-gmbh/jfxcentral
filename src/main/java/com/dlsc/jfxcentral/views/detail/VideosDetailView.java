package com.dlsc.jfxcentral.views.detail;

import com.dlsc.gemsfx.DialogPane;
import com.dlsc.gemsfx.FilterView;
import com.dlsc.jfxcentral.data.DataRepository;
import com.dlsc.jfxcentral.data.ImageManager;
import com.dlsc.jfxcentral.views.RootPane;
import com.dlsc.jfxcentral.model.Person;
import com.dlsc.jfxcentral.model.Video;
import com.dlsc.jfxcentral.panels.PrettyListView;
import com.dlsc.jfxcentral.panels.SectionPaneWithFilterView;
import com.dlsc.jfxcentral.util.Util;
import com.dlsc.jfxcentral.views.AdvancedListCell;
import com.jpro.webapi.HTMLView;
import com.jpro.webapi.WebAPI;
import javafx.beans.Observable;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.web.WebView;
import org.apache.commons.lang3.StringUtils;
import org.kordamp.ikonli.javafx.FontIcon;
import org.kordamp.ikonli.materialdesign.MaterialDesign;

import java.util.*;

public class VideosDetailView extends DetailView<Video> {

    private final FilterView.FilterGroup<Video> typeGroup = new FilterView.FilterGroup<>("Type");
    private final FilterView.FilterGroup<Video> eventGroup = new FilterView.FilterGroup<>("Event");
    private final FilterView.FilterGroup<Video> speakerGroup = new FilterView.FilterGroup<>("Speaker");
    private final FilterView.FilterGroup<Video> platformGroup = new FilterView.FilterGroup<>("Platform");
    private final FilterView.FilterGroup<Video> domainGroup = new FilterView.FilterGroup<>("Domain");

    public VideosDetailView(RootPane rootPane) {
        super(rootPane);

        SectionPaneWithFilterView sectionPane = new SectionPaneWithFilterView();
        sectionPane.setTitle("Videos");
        sectionPane.setEnableAutoSubtitle(true);

        FilterView<Video> filterView = sectionPane.getFilterView();
        filterView.setItems(DataRepository.getInstance().videosProperty());
        filterView.getFilterGroups().setAll(typeGroup, eventGroup, speakerGroup, platformGroup, domainGroup);
        filterView.setTextFilterProvider(text -> video -> {
            if (video.getTitle().toLowerCase().contains(text)) {
                return true;
            }
            if (video.getDescription().toLowerCase().contains(text)) {
                return true;
            }
            return false;
        });

        PrettyListView<Video> listView = new PrettyListView<>();
        listView.setCellFactory(view -> new VideoCell(rootPane, true));
        listView.itemsProperty().bind(filterView.filteredItemsProperty());
        listView.getSelectionModel().selectedItemProperty().addListener(it -> setSelectedItem(listView.getSelectionModel().getSelectedItem()));
        VBox.setVgrow(listView, Priority.ALWAYS);
        sectionPane.getNodes().add(listView);

        setContent(sectionPane);

        selectedItemProperty().addListener(it -> listView.getSelectionModel().select(getSelectedItem()));

        DataRepository.getInstance().videosProperty().addListener((Observable it) -> updateFilters());

        updateFilters();
    }

    private void updateFilters() {
        eventGroup.getFilters().clear();
        typeGroup.getFilters().clear();
        domainGroup.getFilters().clear();
        speakerGroup.getFilters().clear();
        platformGroup.getFilters().clear();

        updateEventGroup();
        updateDomainGroup();
        updateTypeGroup();
        updatePlatformGroup();
        updateSpeakersGroup();
    }

    private void updateSpeakersGroup() {
        List<String> speakersList = new ArrayList<>();

        DataRepository.getInstance().getVideos().forEach(video -> {
            List<String> personIds = video.getPersonIds();
            if (personIds != null) {
                for (String id : personIds) {
                    if (!speakersList.contains(id.trim())) {
                        speakersList.add(id.trim());
                    }
                }
            }
        });

        List<FilterView.Filter<Video>> filters = new ArrayList<>();

        speakersList.forEach(item -> {
            Optional<Person> personById = DataRepository.getInstance().getPersonById(item);
            if (personById.isPresent()) {
                filters.add(new FilterView.Filter<>(personById.get().getName()) {
                    @Override
                    public boolean test(Video video) {
                        return video.getPersonIds().contains(item);
                    }
                });
            }
        });

        filters.sort(Comparator.comparing(FilterView.Filter::getName));
        speakerGroup.getFilters().setAll(filters);
    }

    private void updateEventGroup() {
        List<String> itemList = new ArrayList<>();
        DataRepository.getInstance().getVideos().forEach(video -> {
            String event = video.getEvent();
            if (StringUtils.isNotBlank(event)) {
                StringTokenizer st = new StringTokenizer(event, ",");
                while (st.hasMoreTokens()) {
                    String token = st.nextToken().trim();
                    if (!itemList.contains(token)) {
                        itemList.add(token);
                    }
                }
            }
        });

        Collections.sort(itemList);
        itemList.forEach(item -> eventGroup.getFilters().add(new FilterView.Filter<>(item) {
            @Override
            public boolean test(Video video) {
                return StringUtils.containsIgnoreCase(video.getEvent(), item);
            }
        }));
    }

    private void updateDomainGroup() {
        List<String> itemList = new ArrayList<>();
        DataRepository.getInstance().getVideos().forEach(video -> {
            String domain = video.getDomain();
            if (StringUtils.isNotBlank(domain)) {
                StringTokenizer st = new StringTokenizer(domain, ",");
                while (st.hasMoreTokens()) {
                    String token = st.nextToken().trim();
                    if (!itemList.contains(token)) {
                        itemList.add(token);
                    }
                }
            }
        });

        Collections.sort(itemList);
        itemList.forEach(item -> domainGroup.getFilters().add(new FilterView.Filter<>(item) {
            @Override
            public boolean test(Video video) {
                return StringUtils.containsIgnoreCase(video.getDomain(), item);
            }
        }));
    }

    private void updatePlatformGroup() {
        List<String> itemList = new ArrayList<>();
        DataRepository.getInstance().getVideos().forEach(video -> {
            String platform = video.getPlatform();
            if (StringUtils.isNotBlank(platform)) {
                StringTokenizer st = new StringTokenizer(platform, ",");
                while (st.hasMoreTokens()) {
                    String token = st.nextToken().trim();
                    if (!itemList.contains(token)) {
                        itemList.add(token);
                    }
                }
            }
        });

        Collections.sort(itemList);
        itemList.forEach(item -> platformGroup.getFilters().add(new FilterView.Filter<>(item) {
            @Override
            public boolean test(Video video) {
                return StringUtils.containsIgnoreCase(video.getPlatform(), item);
            }
        }));
    }

    private void updateTypeGroup() {
        List<String> itemList = new ArrayList<>();
        DataRepository.getInstance().getVideos().forEach(video -> {
            String type = video.getType();
            if (StringUtils.isNotBlank(type)) {
                StringTokenizer st = new StringTokenizer(type, ",");
                while (st.hasMoreTokens()) {
                    String token = st.nextToken().trim();
                    if (!itemList.contains(token)) {
                        itemList.add(token);
                    }
                }
            }
        });

        Collections.sort(itemList);
        itemList.forEach(item -> typeGroup.getFilters().add(new FilterView.Filter<>(item) {
            @Override
            public boolean test(Video video) {
                return StringUtils.containsIgnoreCase(video.getType(), item);
            }
        }));
    }

    static class VideoCell extends AdvancedListCell<Video> {

        private final Label titleLabel = new Label();
        private final Label descriptionLabel = new Label();
        private final ImageView thumbnailView = new ImageView();
        private final Button playButton = new Button("Play");
        private final Button playOnYouTubeButton = new Button("YouTube");
        private final RootPane rootPane;

        public VideoCell(RootPane rootPane, boolean insideListView) {
            this.rootPane = rootPane;

            getStyleClass().add("video-cell");

            if (insideListView) {
                setPrefWidth(0);
            }

            playButton.setGraphic(new FontIcon(MaterialDesign.MDI_PLAY));
            playButton.setOnAction(evt -> showVideo(getItem()));

            playOnYouTubeButton.setGraphic(new FontIcon(MaterialDesign.MDI_YOUTUBE_PLAY));
            playOnYouTubeButton.setOnAction(evt -> Util.browse("https://youtu.be/" + getItem().getId()));

            titleLabel.getStyleClass().addAll("header3", "title-label");
            titleLabel.setWrapText(true);
            titleLabel.setMinHeight(Region.USE_PREF_SIZE);

            descriptionLabel.getStyleClass().add("description-label");
            descriptionLabel.setWrapText(true);
            descriptionLabel.setMinHeight(Region.USE_PREF_SIZE);

            thumbnailView.setPreserveRatio(true);
            thumbnailView.fitWidthProperty().bind(coverImageWidthProperty());

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
                        System.out.println("Unloading");
                        webView.getEngine().loadContent("empty");
                    }
                });
            }
        }

        private final DoubleProperty coverImageWidth = new SimpleDoubleProperty(this, "coverImageWidth", 320);

        public double getCoverImageWidth() {
            return coverImageWidth.get();
        }

        public DoubleProperty coverImageWidthProperty() {
            return coverImageWidth;
        }

        public void setCoverImageWidth(double coverImageWidth) {
            this.coverImageWidth.set(coverImageWidth);
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
            }
        }
    }
}