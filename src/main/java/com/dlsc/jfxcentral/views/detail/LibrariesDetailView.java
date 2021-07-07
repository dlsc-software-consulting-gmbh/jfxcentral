package com.dlsc.jfxcentral.views.detail;

import com.dlsc.jfxcentral.JFXCentralApp;
import com.dlsc.jfxcentral.data.DataRepository;
import com.dlsc.jfxcentral.data.ImageManager;
import com.dlsc.jfxcentral.data.model.Library;
import com.dlsc.jfxcentral.panels.SectionPane;
import com.dlsc.jfxcentral.util.Util;
import com.dlsc.jfxcentral.views.MarkdownView;
import com.dlsc.jfxcentral.views.RootPane;
import com.dlsc.jfxcentral.views.View;
import javafx.beans.binding.Bindings;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import org.apache.commons.lang3.StringUtils;
import org.kordamp.ikonli.fontawesome5.FontAwesomeBrands;
import org.kordamp.ikonli.javafx.FontIcon;
import org.kordamp.ikonli.material.Material;


public class LibrariesDetailView extends ModelObjectDetailView<Library> {

    private FlowPane buttonBox;
    private ImageView iconView = new ImageView();
    private MarkdownView descriptionMarkdownView = new MarkdownView();
    private ThumbnailScrollPane thumbnailScrollPane;

    public LibrariesDetailView(RootPane rootPane) {
        super(rootPane, View.LIBRARIES);

        getStyleClass().add("libraries-detail-view");

        iconView.visibleProperty().bind(iconView.imageProperty().isNotNull());
        iconView.managedProperty().bind(iconView.imageProperty().isNotNull());

        createTitleBox();
        createEnsembleBox();
        createScreenshotsBox();
        createCoordinatesBox();
        createReadMeBox(library -> DataRepository.BASE_URL + "libraries/" + library.getId(), library -> DataRepository.getInstance().libraryReadMeProperty(library));
        createStandardBoxes();
    }

    protected boolean isUsingMasterView() {
        return true;
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
                String url = "https://www.jfx-ensemble.com/?page=project/" + selectedItem.getName();
                markdownView.setMdString("Online demos are available for this library on the JFX-Ensemble website. These demos can be [run in the browser](" + url + ") via JPro (free for open source projects).");
                Util.setLink(markdownView, url, selectedItem.getName());
                Util.setLink(imageView, url, selectedItem.getName());
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

    @Override
    protected void createTitleBox() {
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
        sectionPane.titleProperty().bind(Bindings.createStringBinding(() -> getSelectedItem() != null ? getSelectedItem().getName() : "", selectedItemProperty()));
        sectionPane.subtitleProperty().bind(Bindings.createStringBinding(() -> getSelectedItem() != null ? getSelectedItem().getSummary() : "", selectedItemProperty()));
        sectionPane.getStyleClass().add("title-section");
        sectionPane.setExtras(iconView);

        sectionPane.getNodes().add(hBox);

        content.getChildren().addAll(sectionPane);
    }

    @Override
    protected void updateView(Library oldObject, Library library) {
        if (library != null) {
            descriptionMarkdownView.setMdString(library.getDescription());

            thumbnailScrollPane.setLibrary(library);
            thumbnailScrollPane.libraryInfoProperty().bind(DataRepository.getInstance().libraryInfoProperty(library));

            iconView.imageProperty().bind(ImageManager.getInstance().libraryImageProperty(library));

            buttonBox.getChildren().clear();

            if (StringUtils.isNotEmpty(library.getHomepage())) {
                Button twitter = new Button("Homepage");
                twitter.getStyleClass().addAll("social-button", "homepage");
                Util.setLink(twitter, library.getHomepage(), library.getName());
                twitter.setGraphic(new FontIcon(FontAwesomeBrands.TWITTER));
                buttonBox.getChildren().add(twitter);
            }

            if (StringUtils.isNotEmpty(library.getRepository())) {
                Button linkedIn = new Button("Repository");
                linkedIn.getStyleClass().addAll("social-button", "repository");
                Util.setLink(linkedIn, library.getRepository(), library.getName());
                linkedIn.setGraphic(new FontIcon(FontAwesomeBrands.GITHUB));
                buttonBox.getChildren().add(linkedIn);
            }

            if (StringUtils.isNotEmpty(library.getIssueTracker())) {
                Button blog = new Button("Issues Tracker");
                blog.getStyleClass().addAll("social-button", "issues");
                Util.setLink(blog, library.getIssueTracker(), library.getName());
                blog.setGraphic(new FontIcon(Material.BUG_REPORT));
                buttonBox.getChildren().add(blog);
            }

            if (StringUtils.isNotEmpty(library.getDiscussionBoard())) {
                Button website = new Button("Discussions");
                website.getStyleClass().addAll("social-button", "discussion");
                Util.setLink(website, library.getDiscussionBoard(), library.getName());
                website.setGraphic(new FontIcon(Material.COMMENT));
                buttonBox.getChildren().add(website);
            }

            if (StringUtils.isNotEmpty(library.getJavadocs())) {
                Button website = new Button("API");
                website.getStyleClass().addAll("social-button", "api");
                Util.setLink(website, library.getJavadocs(), library.getName());
                website.setGraphic(new FontIcon(Material.CODE));
                buttonBox.getChildren().add(website);
            }

            if (StringUtils.isNotEmpty(library.getDocumentation())) {
                Button website = new Button("Docs");
                website.getStyleClass().addAll("social-button", "docs");
                Util.setLink(website, library.getDocumentation(), library.getName());
                website.setGraphic(new FontIcon(Material.BOOK));
                buttonBox.getChildren().add(website);
            }
        }
    }
}