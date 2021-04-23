package com.dlsc.jfxcentral.panels;

import javafx.beans.InvalidationListener;
import javafx.beans.binding.Bindings;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import org.kordamp.ikonli.javafx.FontIcon;
import org.kordamp.ikonli.materialdesign.MaterialDesign;

public class SectionPaneSkin extends SectionPaneBaseSkin<SectionPane> {

    private final BorderPane topPane = new BorderPane();
    private final Node placeholder;
    private final VBox childrenBox;
    private final MenuButton menuButton;

    public SectionPaneSkin(SectionPane pane) {
        super(pane);

        menuButton = new MenuButton();
        menuButton.setGraphic(new FontIcon(MaterialDesign.MDI_MENU));
        menuButton.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
        menuButton.getStyleClass().add("more-menu-button");

        Bindings.bindContent(menuButton.getItems(), pane.getMenuItems());

        Label titleLabel = pane.getTitleLabel();
        titleLabel.getStyleClass().add("title-label");
        titleLabel.textProperty().bind(pane.titleProperty());
        titleLabel.managedProperty().bind(pane.titleProperty().isNotEmpty());

        HBox titleExtrasBox = new HBox();
        Bindings.bindContent(titleExtrasBox.getChildren(), pane.titleExtrasProperty());
        titleLabel.setGraphic(titleExtrasBox);
        titleLabel.setContentDisplay(ContentDisplay.RIGHT);

        Label subtitleLabel = pane.getSubtitleLabel();
        subtitleLabel.getStyleClass().add("subtitle-label");
        subtitleLabel.textProperty().bind(pane.subtitleProperty());
        subtitleLabel.managedProperty().bind(pane.subtitleProperty().isNotEmpty());

        VBox titleBox = new VBox(titleLabel, subtitleLabel);
        titleBox.setFillWidth(true);
        titleBox.setAlignment(Pos.TOP_LEFT);
        titleBox.getStyleClass().add("title-box");

        topPane.getStyleClass().add("top-pane");
        topPane.setCenter(titleBox);
        topPane.visibleProperty().bind(pane.menuItemsProperty().emptyProperty().not().or(pane.extrasProperty().isNotNull().or(pane.titleProperty().isNotEmpty()).or(pane.subtitleProperty().isNotEmpty())));
        topPane.managedProperty().bind(topPane.visibleProperty());

        childrenBox = new VBox();
        childrenBox.getStyleClass().add("children-box");
        childrenBox.setFillWidth(true);
        childrenBox.visibleProperty().bind(pane.expandedProperty());
        childrenBox.managedProperty().bind(pane.expandedProperty());

        VBox.setVgrow(childrenBox, Priority.ALWAYS);

        Bindings.bindContent(childrenBox.getChildren(), pane.getNodes());

        placeholder = createPlaceholder();

        InvalidationListener invalidationListener = it -> updateFooterMargin();
        pane.footerProperty().addListener(invalidationListener);
        pane.expandedProperty().addListener(invalidationListener);

        updateFooterMargin();

        InvalidationListener updateViewListener = it -> updateView();
        pane.expandedProperty().addListener(updateViewListener);
        pane.footerProperty().addListener(updateViewListener);
        pane.menuItemsProperty().addListener(updateViewListener);

        updateView();
    }

    private void updateView() {
        VBox vbox = new VBox();
        vbox.getStyleClass().add("section-pane-container");
        SectionPane pane = getSkinnable();

        HBox extrasBox = new HBox();
        extrasBox.getStyleClass().add("extras-box");

        if (pane.getExtras() != null) {
            extrasBox.getChildren().add(pane.getExtras());
        }

        if (!pane.getMenuItems().isEmpty()) {
            extrasBox.getChildren().add(menuButton);
        }

        if (!extrasBox.getChildren().isEmpty()) {
            topPane.setRight(extrasBox);
        } else {
            topPane.setRight(null);
        }

        if (pane.isExpanded()) {
            vbox.getChildren().setAll(topPane, childrenBox);
        } else {
            vbox.getChildren().setAll(topPane, placeholder);
        }

        Node footer = pane.getFooter();
        if (footer != null) {
            vbox.getChildren().add(footer);
        }

        getChildren().setAll(vbox);
    }

    private void updateFooterMargin() {
        Node footer = getSkinnable().getFooter();
        if (footer != null) {
            BorderPane.setMargin(footer, new Insets(getSkinnable().isExpanded() ? 10 : 20, 0, 0, 0));
        }
    }
}
