package com.dlsc.jfxcentral.views.detail;

import com.dlsc.jfxcentral.data.DataRepository;
import com.dlsc.jfxcentral.data.model.Coordinates;
import com.dlsc.jfxcentral.panels.SectionPane;
import com.dlsc.jfxcentral.views.MarkdownView;
import com.jpro.webapi.WebAPI;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import org.apache.commons.lang3.StringUtils;
import org.kordamp.ikonli.javafx.FontIcon;
import org.kordamp.ikonli.materialdesign.MaterialDesign;

import java.text.MessageFormat;

public class CoordinatesPane extends SectionPane {

    private final MarkdownView versionBadgeMarkdownView;
    private final Label repositoryCoordinatesLabel;

    public CoordinatesPane() {
        setTitle("Coordinates");
        setSubtitle("Repository group and artifact IDs");
        getStyleClass().add("coordinates-pane");

        versionBadgeMarkdownView = new MarkdownView();
        setExtras(versionBadgeMarkdownView);

        repositoryCoordinatesLabel = new Label();
        repositoryCoordinatesLabel.getStyleClass().add("coordinates-label");

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

        HBox header = new HBox(mavenButton, gradleButton, spacer);
        header.getStyleClass().add("header");
        if (!WebAPI.isBrowser()) {
            header.getChildren().add(copyButton);
        }

        VBox vBox = new VBox(header, repositoryCoordinatesLabel);
        vBox.getStyleClass().add("vbox");

        getNodes().add(vBox);

        coordinatesProperty().addListener(it -> {
            Coordinates coordinates = getCoordinates();
            if (coordinates != null) {
                setVisible(StringUtils.isNotBlank(coordinates.getGroupId()) && StringUtils.isNotBlank(coordinates.getArtifactId()));
                setManaged(StringUtils.isNotBlank(coordinates.getGroupId()) && StringUtils.isNotBlank(coordinates.getArtifactId()));

                String groupId = coordinates.getGroupId();
                String artifactId = coordinates.getArtifactId();

                if (StringUtils.isNotBlank(groupId) && StringUtils.isNotBlank(artifactId)) {
                    versionBadgeMarkdownView.setMdString(MessageFormat.format("[![Maven Central](https://img.shields.io/maven-central/v/{0}/{1}.png?label=Maven%20Central)](https://search.maven.org/search?q=g:%22{0}%22%20AND%20a:%22{1}%22)", groupId, artifactId));

                    StringProperty versionProperty = DataRepository.getInstance().getArtifactVersion(coordinates);

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

            }
        });
    }

    private final ObjectProperty<Coordinates> coordinates = new SimpleObjectProperty<>(this, "coordinates");

    public Coordinates getCoordinates() {
        return coordinates.get();
    }

    public ObjectProperty<Coordinates> coordinatesProperty() {
        return coordinates;
    }

    public void setCoordinates(Coordinates coordinates) {
        this.coordinates.set(coordinates);
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

}
