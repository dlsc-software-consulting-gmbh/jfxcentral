package com.dlsc.jfxcentral.views.mobile;

import javafx.scene.control.ContentDisplay;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import org.kordamp.ikonli.javafx.FontIcon;
import org.kordamp.ikonli.materialdesign.MaterialDesign;

public class FontSizeSelector extends HBox {

    public FontSizeSelector() {
        getStyleClass().add("font-size-selector");

        FontIcon smallerIcon = new FontIcon(MaterialDesign.MDI_FORMAT_SIZE);
        smallerIcon.getStyleClass().addAll("font-size-icon", "smaller");

        ToggleButton smallerButton = createButton(smallerIcon);
        smallerButton.setGraphic(smallerIcon);
        smallerButton.setOnAction(evt -> {
            getScene().getRoot().getStyleClass().removeAll("large");
            getScene().getRoot().getStyleClass().add("small");
        });

        FontIcon normalSizeIcon = new FontIcon(MaterialDesign.MDI_FORMAT_SIZE);
        normalSizeIcon.getStyleClass().addAll("font-size-icon", "normal");

        ToggleButton normalButton = createButton(normalSizeIcon);
        normalButton.setSelected(true);
        normalButton.setOnAction(evt -> {
            getScene().getRoot().getStyleClass().remove("small");
            getScene().getRoot().getStyleClass().remove("large");
        });

        FontIcon largerIcon = new FontIcon(MaterialDesign.MDI_FORMAT_SIZE);
        largerIcon.getStyleClass().addAll("font-size-icon", "larger");

        ToggleButton largerButton = createButton(largerIcon);
        largerButton.setOnAction(evt -> {
            getScene().getRoot().getStyleClass().removeAll("small");
            getScene().getRoot().getStyleClass().add("large");
        });

        setFillHeight(false);

        getChildren().setAll(smallerButton, normalButton, largerButton);
    }

    private ToggleGroup toggleGroup = new ToggleGroup();

    private ToggleButton createButton(FontIcon icon) {
        ToggleButton button = new ToggleButton();
        button.setFocusTraversable(false);
        toggleGroup.getToggles().add(button);
        button.setGraphic(icon);
        button.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
        button.setMaxWidth(Double.MAX_VALUE);
        button.setMaxHeight(Double.MAX_VALUE);
        HBox.setHgrow(button, Priority.ALWAYS);
        return button;
    }
}
