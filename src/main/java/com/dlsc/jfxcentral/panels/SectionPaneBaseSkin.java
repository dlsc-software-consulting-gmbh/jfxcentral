package com.dlsc.jfxcentral.panels;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.SkinBase;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public abstract class SectionPaneBaseSkin<T extends SectionPaneBase> extends SkinBase<T> {

    public SectionPaneBaseSkin(T t) {
        super(t);
    }

    protected Node createPlaceholder() {
        T pane = getSkinnable();
        Label placeholder = new Label();
        placeholder.getStyleClass().add("placeholder-label");
        placeholder.textProperty().bind(pane.placeholderProperty());
        placeholder.visibleProperty().bind(pane.expandedProperty().not().and(pane.placeholderProperty().isNotEmpty()));
        placeholder.managedProperty().bind(pane.expandedProperty().not().and(pane.placeholderProperty().isNotEmpty()));
        StackPane.setAlignment(placeholder, Pos.TOP_LEFT);
        VBox.setVgrow(placeholder, Priority.ALWAYS);
        return placeholder;
    }
}
