package com.dlsc.jfxcentral.views;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;

public class OverlayPane extends StackPane {

    public OverlayPane() {
        getStyleClass().add("overlay-pane");

        visibleProperty().bind(contentProperty().isNotNull());
        managedProperty().bind(contentProperty().isNotNull());

        Button close = new Button("close");
        close.setOnAction(evt -> setContent(null));
        StackPane.setAlignment(close, Pos.TOP_RIGHT);
        StackPane.setMargin(close, new Insets(20));

        getChildren().add(close);

        contentProperty().addListener((obs, oldContent, newContent) -> {
            if (oldContent != null) {
                getChildren().removeAll(oldContent);
            }
            if (newContent != null) {
                StackPane.setAlignment(newContent, Pos.CENTER);
                getChildren().add(newContent);
            }

            close.toFront();
        });

        setPrefSize(0, 0);
    }

    private final ObjectProperty<Node> content = new SimpleObjectProperty<>(this, "content");

    public Node getContent() {
        return content.get();
    }

    public ObjectProperty<Node> contentProperty() {
        return content;
    }

    public void setContent(Node content) {
        this.content.set(content);
    }
}
