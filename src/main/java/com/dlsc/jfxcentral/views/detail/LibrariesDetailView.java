package com.dlsc.jfxcentral.views.detail;

import com.dlsc.jfxcentral.data.DataRepository;
import com.dlsc.jfxcentral.data.ImageManager;
import com.dlsc.jfxcentral.data.model.Library;
import com.dlsc.jfxcentral.panels.SectionPane;
import com.dlsc.jfxcentral.util.Util;
import com.dlsc.jfxcentral.views.MarkdownView;
import com.dlsc.jfxcentral.views.RootPane;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.ImageView;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import org.apache.commons.lang3.StringUtils;
import org.kordamp.ikonli.fontawesome5.FontAwesomeBrands;
import org.kordamp.ikonli.javafx.FontIcon;
import org.kordamp.ikonli.material.Material;
import org.kordamp.ikonli.materialdesign.MaterialDesign;

import java.text.MessageFormat;


public class LibrariesDetailView extends DetailView<Library> {

    private HBox linksBox;
    private ImageView iconView = new ImageView();
    private MarkdownView descriptionMarkdownView = new MarkdownView();
    private MarkdownView readmeMarkdownView = new MarkdownView();
    private VBox content = new VBox();
    private Label repositoryCoordinatesLabel = new Label();
    private ThumbnailScrollPane thumbnailScrollPane;
    private MarkdownView versionBadgeMarkdownView;

    public LibrariesDetailView(RootPane rootPane) {
        super(rootPane);

        getStyleClass().add("libraries-detail-view");

        repositoryCoordinatesLabel.getStyleClass().add("coordinates-label");

        iconView.visibleProperty().bind(iconView.imageProperty().isNotNull());
        iconView.managedProperty().bind(iconView.imageProperty().isNotNull());

        createTitleBox();
        createScreenshotsBox();
        createCoordinatesBox();
        createReadmeBox();

        setContent(content);

        selectedItemProperty().addListener(it -> updateView());
        updateView();
    }

    public enum BuildTool {
        MAVEN,
        GRADLE
    }

    private final ObjectProperty<BuildTool> buildTool = new SimpleObjectProperty<>(this, "buildTool", BuildTool.MAVEN);

    public BuildTool getBuildTool() {
        return buildTool.get();
    }

    public ObjectProperty<BuildTool> buildToolProperty() {
        return buildTool;
    }

    public void setBuildTool(BuildTool buildTool) {
        this.buildTool.set(buildTool);
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

    private void createCoordinatesBox() {
        SectionPane sectionPane = new SectionPane();
        sectionPane.setTitle("Coordinates");
        sectionPane.setSubtitle("Repository group and artifact IDs");
        sectionPane.getStyleClass().add("coordinates-pane");

        versionBadgeMarkdownView = new MarkdownView();
        sectionPane.setExtras(versionBadgeMarkdownView);

        RadioButton mavenButton = new RadioButton("Maven");
        RadioButton gradleButton = new RadioButton("Gradle");
        ToggleGroup toggleGroup = new ToggleGroup();
        toggleGroup.getToggles().addAll(mavenButton, gradleButton);
        mavenButton.setOnAction(evt -> setBuildTool(BuildTool.MAVEN));
        gradleButton.setOnAction(evt -> setBuildTool(BuildTool.GRADLE));
        mavenButton.setSelected(true);

        Button copyButton = new Button();
        copyButton.getStyleClass().add("copy-button");
        copyButton.setGraphic(new FontIcon(MaterialDesign.MDI_CLIPBOARD));
        copyButton.setOnAction(evt -> {
            Clipboard clipboard = Clipboard.getSystemClipboard();
            ClipboardContent content = new ClipboardContent();
            content.putString(repositoryCoordinatesLabel.getText());
            clipboard.setContent(content);
        });

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        HBox header = new HBox(mavenButton, gradleButton, spacer, copyButton);
        header.getStyleClass().add("header");

        VBox vBox = new VBox(header, repositoryCoordinatesLabel);
        vBox.getStyleClass().add("vbox");

        sectionPane.getNodes().add(vBox);

        selectedItemProperty().addListener(it -> {
            Library library = getSelectedItem();
            if (library != null) {
                sectionPane.setVisible(StringUtils.isNotBlank(library.getGroupId()) && StringUtils.isNotBlank(library.getArtifactId()));
                sectionPane.setManaged(StringUtils.isNotBlank(library.getGroupId()) && StringUtils.isNotBlank(library.getArtifactId()));
            }
        });

        content.getChildren().add(sectionPane);
    }

    private void createTitleBox() {
        iconView.setFitWidth(128);
        iconView.setFitHeight(64);
        iconView.setPreserveRatio(true);

        descriptionMarkdownView.getStyleClass().add("description-label");
        descriptionMarkdownView.setHyperlinkCallback(link -> Util.browse(link));
        HBox.setHgrow(descriptionMarkdownView, Priority.ALWAYS);

        linksBox = new HBox();
        linksBox.getStyleClass().add("social-box");

        VBox vBox = new VBox(descriptionMarkdownView, linksBox);
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
        readmeMarkdownView.setHyperlinkCallback(url -> Util.browse(url));
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

            String groupId = library.getGroupId();
            String artifactId = library.getArtifactId();

            if (StringUtils.isNotBlank(groupId) && StringUtils.isNotBlank(artifactId)) {
                versionBadgeMarkdownView.setMdString(MessageFormat.format("[![Maven Central](https://img.shields.io/maven-central/v/{0}/{1}.png?label=Maven%20Central)](https://search.maven.org/search?q=g:%22{0}%22%20AND%20a:%22{1}%22)", groupId, artifactId));

                StringProperty versionProperty = DataRepository.getInstance().getArtifactVersion(library);

                repositoryCoordinatesLabel.textProperty().bind(Bindings.createStringBinding(() -> {
                    if (getBuildTool().equals(BuildTool.MAVEN)) {
                        return "<dependency>\n    <groupId>" + groupId + "</groupId>\n    <artifactId>" + artifactId + "</artifactId>\n    <version>" + versionProperty.get() + "</version>\n</dependency>";
                    }
                    return "dependencies {\n    implementation '" + groupId + ":" + artifactId + ":" + versionProperty.get() + "'\n}";
                }, versionProperty, buildTool));

            } else {
                versionBadgeMarkdownView.setMdString("");

                repositoryCoordinatesLabel.textProperty().unbind();
            }


            iconView.imageProperty().bind(ImageManager.getInstance().libraryImageProperty(library));

            linksBox.getChildren().clear();

            if (StringUtils.isNotEmpty(library.getHomepage())) {
                Button twitter = new Button("Homepage");
                twitter.getStyleClass().addAll("social-button", "homepage");
                twitter.setOnAction(evt -> Util.browse(library.getHomepage()));
                twitter.setGraphic(new FontIcon(FontAwesomeBrands.TWITTER));
                linksBox.getChildren().add(twitter);
            }

            if (StringUtils.isNotEmpty(library.getRepository())) {
                Button linkedIn = new Button("Repository");
                linkedIn.getStyleClass().addAll("social-button", "repository");
                linkedIn.setOnAction(evt -> Util.browse(library.getRepository()));
                linkedIn.setGraphic(new FontIcon(FontAwesomeBrands.GITHUB));
                linksBox.getChildren().add(linkedIn);
            }

            if (StringUtils.isNotEmpty(library.getIssueTracker())) {
                Button blog = new Button("Issues Tracker");
                blog.getStyleClass().addAll("social-button", "issues");
                blog.setOnAction(evt -> Util.browse(library.getIssueTracker()));
                blog.setGraphic(new FontIcon(Material.BUG_REPORT));
                linksBox.getChildren().add(blog);
            }

            if (StringUtils.isNotEmpty(library.getDiscussionBoard())) {
                Button website = new Button("Discussions");
                website.getStyleClass().addAll("social-button", "discussion");
                website.setOnAction(evt -> Util.browse(library.getDiscussionBoard()));
                website.setGraphic(new FontIcon(Material.COMMENT));
                linksBox.getChildren().add(website);
            }

            if (StringUtils.isNotEmpty(library.getJavadocs())) {
                Button website = new Button("API");
                website.getStyleClass().addAll("social-button", "api");
                website.setOnAction(evt -> Util.browse(library.getJavadocs()));
                website.setGraphic(new FontIcon(Material.CODE));
                linksBox.getChildren().add(website);
            }

            if (StringUtils.isNotEmpty(library.getDocumentation())) {
                Button website = new Button("Docs");
                website.getStyleClass().addAll("social-button", "docs");
                website.setOnAction(evt -> Util.browse(library.getDocumentation()));
                website.setGraphic(new FontIcon(Material.BOOK));
                linksBox.getChildren().add(website);
            }
        }
    }
}