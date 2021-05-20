package com.dlsc.jfxcentral;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;

public class HeaderPane extends HBox {

    public HeaderPane() {
        getStyleClass().add("header-pane");

        setAlignment(Pos.CENTER_LEFT);

        Label title = new Label("JFX-Central");
        title.setMaxWidth(Double.MAX_VALUE);
        title.getStyleClass().add("title");
        StackPane.setAlignment(title, Pos.CENTER_LEFT);

        Label title2 = new Label("JFX-Central");
        title2.setMaxWidth(Double.MAX_VALUE);
        title2.getStyleClass().addAll("title", "title-shadow");
        StackPane.setAlignment(title2, Pos.CENTER_LEFT);

        StackPane stackPane = new StackPane(title2, title);
        HBox.setHgrow(stackPane, Priority.ALWAYS);

        Button refreshButton = new Button("Refresh");
        refreshButton.setOnAction(evt -> DataRepository.getInstance().refresh());

        ImageView imageView = new ImageView(JFXCentralApp.class.getResource("duke.png").toExternalForm());
        imageView.setFitHeight(48);
        imageView.setPreserveRatio(true);
        StackPane.setAlignment(imageView, Pos.TOP_RIGHT);

        getChildren().addAll(refreshButton, stackPane);
    }
}
