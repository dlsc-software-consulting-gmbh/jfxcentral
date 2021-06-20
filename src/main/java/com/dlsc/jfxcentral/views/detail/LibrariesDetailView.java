package com.dlsc.jfxcentral.views.detail;

import com.dlsc.jfxcentral.JFXCentralApp;
import com.dlsc.jfxcentral.data.DataRepository;
import com.dlsc.jfxcentral.data.ImageManager;
import com.dlsc.jfxcentral.data.model.Download;
import com.dlsc.jfxcentral.data.model.Library;
import com.dlsc.jfxcentral.data.model.Tutorial;
import com.dlsc.jfxcentral.data.model.Video;
import com.dlsc.jfxcentral.panels.SectionPane;
import com.dlsc.jfxcentral.util.Util;
import com.dlsc.jfxcentral.views.AdvancedListView;
import com.dlsc.jfxcentral.views.MarkdownView;
import com.dlsc.jfxcentral.views.RootPane;
import com.dlsc.jfxcentral.views.detail.cells.DetailDownloadCell;
import com.dlsc.jfxcentral.views.detail.cells.DetailTutorialCell;
import com.dlsc.jfxcentral.views.detail.cells.DetailVideoCell;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import org.apache.commons.lang3.StringUtils;
import org.kordamp.ikonli.fontawesome5.FontAwesomeBrands;
import org.kordamp.ikonli.javafx.FontIcon;
import org.kordamp.ikonli.material.Material;


public class LibrariesDetailView extends DetailView<Library> {

    private FlowPane buttonBox;
    private ImageView iconView = new ImageView();
    private MarkdownView descriptionMarkdownView = new MarkdownView();
    private MarkdownView readmeMarkdownView = new MarkdownView();
    private VBox content = new VBox();
    private ThumbnailScrollPane thumbnailScrollPane;

    public LibrariesDetailView(RootPane rootPane) {
        super(rootPane);

        getStyleClass().add("libraries-detail-view");

        iconView.visibleProperty().bind(iconView.imageProperty().isNotNull());
        iconView.managedProperty().bind(iconView.imageProperty().isNotNull());

        createTitleBox();
        createEnsembleBox();
        createScreenshotsBox();
        createCoordinatesBox();
        createVideoBox();
        createDownloadsBox();
        createTutorialsBox();
        createReadmeBox();

        setContent(content);

        selectedItemProperty().addListener(it -> updateView());
        updateView();
    }

    private void createScreenshotsBox() {
        SectionPane sectionPane = new SectionPane();
        sectionPane.setTitle("Screenshots");
        sectionPane.setSubtitle("Impressions of the controls inside this library");
        sectionPane.getStyleClass().add("screenshots-pane");

        thumbnailScrollPane = new ThumbnailScrollPane(getRootPane());

        sectionPane.getNodes().add(thumbnailScrollPane);
        sectionPane.visibleProperty().bind(thumbnailScrollPane.visibleProperty());
        sectionPane.managedProperty().bind(thumbnailScrollPane.managedProperty());

        content.getChildren().add(sectionPane);
    }

    private void createVideoBox() {
        AdvancedListView<Video> listView = new AdvancedListView<>();
        listView.setPaging(true);
        listView.setVisibleRowCount(3);
        listView.setCellFactory(view -> new DetailVideoCell(getRootPane(), false));

        SectionPane sectionPane = new SectionPane();
        sectionPane.setTitle("Videos");
        sectionPane.getNodes().add(listView);

        selectedItemProperty().addListener(it -> {
            Library library = getSelectedItem();
            if (library != null) {
                sectionPane.setSubtitle("Videos relevant for library " + library.getTitle());
                listView.setItems(DataRepository.getInstance().getVideosByModelObject(library));
            } else {
                sectionPane.setSubtitle("");
                listView.setItems(FXCollections.observableArrayList());
            }
        });

        sectionPane.visibleProperty().bind(listView.itemsProperty().emptyProperty().not());
        sectionPane.managedProperty().bind(listView.itemsProperty().emptyProperty().not());

        content.getChildren().add(sectionPane);
    }

    private void createDownloadsBox() {
        AdvancedListView<Download> listView = new AdvancedListView<>();
        listView.setPaging(true);
        listView.setVisibleRowCount(3);
        listView.setCellFactory(view -> new DetailDownloadCell(false));

        SectionPane sectionPane = new SectionPane();
        sectionPane.setTitle("Downloads");
        sectionPane.getNodes().add(listView);

        selectedItemProperty().addListener(it -> {
            Library library = getSelectedItem();
            if (library != null) {
                sectionPane.setSubtitle("Downloads related to library " + library.getTitle());
                listView.setItems(DataRepository.getInstance().getDownloadsByModelObject(library));
            } else {
                sectionPane.setSubtitle("");
                listView.setItems(FXCollections.observableArrayList());
            }
        });

        sectionPane.visibleProperty().bind(listView.itemsProperty().emptyProperty().not());
        sectionPane.managedProperty().bind(listView.itemsProperty().emptyProperty().not());

        content.getChildren().add(sectionPane);
    }

    private void createTutorialsBox() {
        AdvancedListView<Tutorial> listView = new AdvancedListView<>();
        listView.setPaging(true);
        listView.setVisibleRowCount(3);
        listView.setCellFactory(view -> new DetailTutorialCell(getRootPane(), false));

        SectionPane sectionPane = new SectionPane();
        sectionPane.setTitle("Tutorials");
        sectionPane.getNodes().add(listView);

        selectedItemProperty().addListener(it -> {
            Library library = getSelectedItem();
            if (library != null) {
                sectionPane.setSubtitle("Tutorials for library " + library.getTitle());
                listView.setItems(DataRepository.getInstance().getTutorialsByModelObject(library));
            } else {
                sectionPane.setSubtitle("");
                listView.setItems(FXCollections.observableArrayList());
            }
        });

        sectionPane.visibleProperty().bind(listView.itemsProperty().emptyProperty().not());
        sectionPane.managedProperty().bind(listView.itemsProperty().emptyProperty().not());

        content.getChildren().add(sectionPane);
    }

    private void createEnsembleBox() {
        SectionPane sectionPane = new SectionPane();
        sectionPane.getStyleClass().add("ensemble-pane");

        ImageView imageView = new ImageView(JFXCentralApp.class.getResource("jpro-logo.png").toExternalForm());
        imageView.setFitHeight(48);
        imageView.setPreserveRatio(true);

        MarkdownView markdownView = new MarkdownView();
        markdownView.setMinHeight(Region.USE_PREF_SIZE);

        HBox.setHgrow(markdownView, Priority.ALWAYS);

        selectedItemProperty().addListener(it -> {
            Library selectedItem = getSelectedItem();
            if (selectedItem != null) {
                String url = "https://www.jfx-ensemble.com/?page=project/" + selectedItem.getTitle();
                markdownView.setMdString("Online demos are available for this library on the JFX-Ensemble website. These demos can be [run in the browser](" + url + ") via JPro (free for open source projects).");
                Util.setLink(markdownView, url, selectedItem.getTitle());
                Util.setLink(imageView, url, selectedItem.getTitle());
            }
        });

        HBox hBox = new HBox(imageView, markdownView);
        hBox.getStyleClass().add("hbox");

        sectionPane.getNodes().add(hBox);

        sectionPane.visibleProperty().bind(Bindings.createBooleanBinding(() -> getSelectedItem() != null && getSelectedItem().isEnsemble(), selectedItemProperty()));
        sectionPane.managedProperty().bind(Bindings.createBooleanBinding(() -> getSelectedItem() != null && getSelectedItem().isEnsemble(), selectedItemProperty()));

        content.getChildren().add(sectionPane);
    }

    private void createCoordinatesBox() {
        CoordinatesPane sectionPane = new CoordinatesPane();
        sectionPane.coordinatesProperty().bind(selectedItemProperty());
        content.getChildren().add(sectionPane);
    }

    private void createTitleBox() {
        iconView.setFitWidth(128);
        iconView.setFitHeight(64);
        iconView.setPreserveRatio(true);

        descriptionMarkdownView.getStyleClass().add("description-label");
        HBox.setHgrow(descriptionMarkdownView, Priority.ALWAYS);

        buttonBox = new FlowPane();
        buttonBox.getStyleClass().add("button-box");

        VBox vBox = new VBox(descriptionMarkdownView, buttonBox);
        vBox.getStyleClass().add("vbox");
        vBox.setFillWidth(true);
        HBox.setHgrow(vBox, Priority.ALWAYS);

        HBox hBox = new HBox(vBox);
        hBox.getStyleClass().add("hbox");

        SectionPane sectionPane = new SectionPane();
        sectionPane.titleProperty().bind(Bindings.createStringBinding(() -> getSelectedItem() != null ? getSelectedItem().getTitle() : "", selectedItemProperty()));
        sectionPane.subtitleProperty().bind(Bindings.createStringBinding(() -> getSelectedItem() != null ? getSelectedItem().getSummary() : "", selectedItemProperty()));
        sectionPane.getStyleClass().add("title-section");
        sectionPane.setExtras(iconView);

        sectionPane.getNodes().add(hBox);

        content.getChildren().addAll(sectionPane);
    }

    private void createReadmeBox() {
        SectionPane sectionPane = new SectionPane();
        sectionPane.setTitle("Readme");
        sectionPane.setSubtitle("Basic information on this library to get you started");
        sectionPane.getNodes().add(readmeMarkdownView);
        content.getChildren().add(sectionPane);
    }

    private void updateView() {
        Library library = getSelectedItem();
        if (library != null) {
            descriptionMarkdownView.setMdString(library.getDescription());
            readmeMarkdownView.setBaseURL(DataRepository.getInstance().getBaseUrl() + "libraries/" + library.getId());
            readmeMarkdownView.mdStringProperty().bind(DataRepository.getInstance().libraryReadMeProperty(library));

            thumbnailScrollPane.setLibrary(library);
            thumbnailScrollPane.libraryInfoProperty().bind(DataRepository.getInstance().libraryInfoProperty(library));

            iconView.imageProperty().bind(ImageManager.getInstance().libraryImageProperty(library));

            buttonBox.getChildren().clear();

            if (StringUtils.isNotEmpty(library.getHomepage())) {
                Button twitter = new Button("Homepage");
                twitter.getStyleClass().addAll("social-button", "homepage");
                Util.setLink(twitter, library.getHomepage(), library.getTitle());
                twitter.setGraphic(new FontIcon(FontAwesomeBrands.TWITTER));
                buttonBox.getChildren().add(twitter);
            }

            if (StringUtils.isNotEmpty(library.getRepository())) {
                Button linkedIn = new Button("Repository");
                linkedIn.getStyleClass().addAll("social-button", "repository");
                Util.setLink(linkedIn, library.getRepository(), library.getTitle());
                linkedIn.setGraphic(new FontIcon(FontAwesomeBrands.GITHUB));
                buttonBox.getChildren().add(linkedIn);
            }

            if (StringUtils.isNotEmpty(library.getIssueTracker())) {
                Button blog = new Button("Issues Tracker");
                blog.getStyleClass().addAll("social-button", "issues");
                Util.setLink(blog, library.getIssueTracker(), library.getTitle());
                blog.setGraphic(new FontIcon(Material.BUG_REPORT));
                buttonBox.getChildren().add(blog);
            }

            if (StringUtils.isNotEmpty(library.getDiscussionBoard())) {
                Button website = new Button("Discussions");
                website.getStyleClass().addAll("social-button", "discussion");
                Util.setLink(website, library.getDiscussionBoard(), library.getTitle());
                website.setGraphic(new FontIcon(Material.COMMENT));
                buttonBox.getChildren().add(website);
            }

            if (StringUtils.isNotEmpty(library.getJavadocs())) {
                Button website = new Button("API");
                website.getStyleClass().addAll("social-button", "api");
                Util.setLink(website, library.getJavadocs(), library.getTitle());
                website.setGraphic(new FontIcon(Material.CODE));
                buttonBox.getChildren().add(website);
            }

            if (StringUtils.isNotEmpty(library.getDocumentation())) {
                Button website = new Button("Docs");
                website.getStyleClass().addAll("social-button", "docs");
                Util.setLink(website, library.getDocumentation(), library.getTitle());
                website.setGraphic(new FontIcon(Material.BOOK));
                buttonBox.getChildren().add(website);
            }
        }
    }
}