package com.dlsc.jfxcentral.views.master;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import org.controlsfx.control.textfield.CustomTextField;
import org.kordamp.ikonli.javafx.FontIcon;
import org.kordamp.ikonli.material.Material;

public class MasterViewHeader extends HBox {

    public MasterViewHeader() {
        getStyleClass().add("master-view-header");

        setAlignment(Pos.CENTER);

        FontIcon backIcon = new FontIcon(Material.ARROW_BACK);
        Button backButton = new Button();
        backButton.setGraphic(backIcon);
        backButton.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
        backButton.getStyleClass().add("back-button");

        CustomTextField searchField = new CustomTextField();
        searchField.setFocusTraversable(false);
        searchField.textProperty().bindBidirectional(filterText);
        searchField.getStyleClass().add("search-field");
        searchField.setMaxWidth(Double.MAX_VALUE);
        searchField.setPromptText("Filter ....");
        HBox.setHgrow(searchField, Priority.ALWAYS);

        FontIcon clearSearch = new FontIcon(Material.CLEAR);
        clearSearch.setOnMouseClicked(evt -> searchField.clear());
        clearSearch.visibleProperty().bind(searchField.textProperty().isNotEmpty());
        clearSearch.managedProperty().bind(searchField.textProperty().isNotEmpty());
        clearSearch.setCursor(Cursor.DEFAULT);

        searchField.setRight(clearSearch);

        getChildren().addAll(searchField);
    }

    private StringProperty filterText = new SimpleStringProperty(this, "filterText");

    public String getFilterText() {
        return filterText.get();
    }

    public StringProperty filterTextProperty() {
        return filterText;
    }

    public void setFilterText(String filterText) {
        this.filterText.set(filterText);
    }
}
