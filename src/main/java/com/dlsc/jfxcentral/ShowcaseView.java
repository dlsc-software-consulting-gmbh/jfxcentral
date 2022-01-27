package com.dlsc.jfxcentral;

import com.dlsc.jfxcentral.util.FileUploadStackPane;
import com.dlsc.showcase.CssShowcaseView;
import com.dlsc.showcase.CssShowcaseView.CssConfiguration;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;

import java.io.File;
import java.net.MalformedURLException;

public class ShowcaseView extends com.jpro.web.View {


    @Override
    public String title() {
        return "ShowcaseFX";
    }

    @Override
    public String description() {
        return "Nifty tool to debug JavaFX CSS stylesheets";
    }

    @Override
    public boolean fullscreen() {
        return true;
    }

    @Override
    public Node content() {
        CssShowcaseView view = new CssShowcaseView();
        view.sceneProperty().addListener(it -> {
            Scene scene = view.getScene();
            if (scene != null) {
                System.out.println("removing stylesheets");
                scene.getStylesheets().remove(JFXCentralApp.class.getResource("styles.css").toExternalForm());
                scene.getStylesheets().remove(JFXCentralApp.class.getResource("markdown.css").toExternalForm());

                // add and remove .. to make sure we do not end up with same stylesheet twice
                scene.getStylesheets().remove(JFXCentralApp.class.getResource("showcase.css").toExternalForm());
                scene.getStylesheets().add(JFXCentralApp.class.getResource("showcase.css").toExternalForm());
            }
        });

        FileUploadStackPane fileUploadStackPane = new FileUploadStackPane();
        fileUploadStackPane.setText("Drop a JavaFX CSS stylesheet file (*.css) anywhere onto this view.");
        fileUploadStackPane.setDefaultText(fileUploadStackPane.getText());

        fileUploadStackPane.uploadedFileProperty().addListener(it -> {
            File uploadedFile = fileUploadStackPane.getUploadedFile();
            if (uploadedFile != null && uploadedFile.getName().toLowerCase().endsWith(".css")) {
                try {
                    CssConfiguration config = new CssConfiguration(uploadedFile.getName(), uploadedFile.toURI().toURL().toExternalForm());
                    view.getConfigurations().add(config);
                    view.setSelectedConfiguration(config);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
            }
        });

        Label label = new Label();
        label.getStyleClass().add("drop-info-label");
        label.textProperty().bind(fileUploadStackPane.textProperty());

        BorderPane borderPane = new BorderPane();
        borderPane.setTop(label);
        borderPane.setCenter(view);

        fileUploadStackPane.getChildren().add(borderPane);

        return fileUploadStackPane;
    }
}
