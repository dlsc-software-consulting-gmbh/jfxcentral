package com.dlsc.jfxcentral.panels;

import javafx.beans.DefaultProperty;
import javafx.beans.InvalidationListener;
import javafx.beans.binding.Bindings;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.layout.*;
import org.kordamp.ikonli.javafx.FontIcon;
import org.kordamp.ikonli.materialdesign.MaterialDesign;

@DefaultProperty("nodes")
public class SectionPane extends SectionPaneBase {

    private final FancyLabel titleLabel = new FancyLabel();

    private final FancyLabel subtitleLabel = new FancyLabel();

    private final BorderPane topPane = new BorderPane();
    private final Node placeholder;
    private final VBox childrenBox;
    private final MenuButton menuButton;

    public SectionPane() {
        super();

        setMinHeight(Region.USE_PREF_SIZE);

        menuButton = new MenuButton();
        menuButton.setGraphic(new FontIcon(MaterialDesign.MDI_MENU));
        menuButton.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
        menuButton.getStyleClass().add("more-menu-button");

        Bindings.bindContent(menuButton.getItems(), getMenuItems());

        Label titleLabel = getTitleLabel();
        titleLabel.getStyleClass().add("title-label");
        titleLabel.textProperty().bind(titleProperty());
        titleLabel.managedProperty().bind(titleProperty().isNotEmpty());
        titleLabel.setMinHeight(Region.USE_PREF_SIZE);

        HBox titleExtrasBox = new HBox();
        Bindings.bindContent(titleExtrasBox.getChildren(), titleExtrasProperty());
        titleLabel.setGraphic(titleExtrasBox);
        titleLabel.setContentDisplay(ContentDisplay.RIGHT);
        titleExtrasBox.setMinHeight(Region.USE_PREF_SIZE);

        Label subtitleLabel = getSubtitleLabel();
        subtitleLabel.getStyleClass().add("subtitle-label");
        subtitleLabel.textProperty().bind(subtitleProperty());
        subtitleLabel.managedProperty().bind(subtitleProperty().isNotEmpty());
        subtitleLabel.setMinHeight(Region.USE_PREF_SIZE);

        VBox titleBox = new VBox(titleLabel, subtitleLabel);
        titleBox.setFillWidth(true);
        titleBox.setAlignment(Pos.TOP_LEFT);
        titleBox.getStyleClass().add("title-box");
        titleBox.setMinHeight(Region.USE_PREF_SIZE);

        topPane.getStyleClass().add("top-pane");
        topPane.setCenter(titleBox);
        topPane.visibleProperty().bind(menuItemsProperty().emptyProperty().not().or(extrasProperty().isNotNull().or(titleProperty().isNotEmpty()).or(subtitleProperty().isNotEmpty())));
        topPane.managedProperty().bind(topPane.visibleProperty());
        topPane.setMinHeight(Region.USE_PREF_SIZE);

        childrenBox = new VBox();
        childrenBox.getStyleClass().add("children-box");
        childrenBox.setFillWidth(true);
        childrenBox.visibleProperty().bind(expandedProperty());
        childrenBox.managedProperty().bind(expandedProperty());
        childrenBox.setMinHeight(Region.USE_PREF_SIZE);

        VBox.setVgrow(childrenBox, Priority.ALWAYS);

        Bindings.bindContent(childrenBox.getChildren(), getNodes());

        placeholder = createPlaceholder();

        InvalidationListener invalidationListener = it -> updateFooterMargin();
        footerProperty().addListener(invalidationListener);
        expandedProperty().addListener(invalidationListener);

        updateFooterMargin();

        InvalidationListener updateViewListener = it -> updateView();
        expandedProperty().addListener(updateViewListener);
        footerProperty().addListener(updateViewListener);
        menuItemsProperty().addListener(updateViewListener);

        updateView();
    }

    private void updateView() {
        VBox vbox = new VBox();
        vbox.getStyleClass().add("section-pane-container");
        vbox.setMinHeight(Region.USE_PREF_SIZE);

        HBox extrasBox = new HBox();
        extrasBox.getStyleClass().add("extras-box");

        if (getExtras() != null) {
            extrasBox.getChildren().add(getExtras());
        }

        if (!getMenuItems().isEmpty()) {
            extrasBox.getChildren().add(menuButton);
        }

        if (!extrasBox.getChildren().isEmpty()) {
            topPane.setRight(extrasBox);
        } else {
            topPane.setRight(null);
        }

        if (isExpanded()) {
            vbox.getChildren().setAll(topPane, childrenBox);
        } else {
            vbox.getChildren().setAll(topPane, placeholder);
        }

        Node footer = getFooter();
        if (footer != null) {
            vbox.getChildren().add(footer);
        }

        getChildren().setAll(vbox);
    }

    private void updateFooterMargin() {
        Node footer = getFooter();
        if (footer != null) {
            BorderPane.setMargin(footer, new Insets(isExpanded() ? 10 : 20, 0, 0, 0));
        }
    }

    public SectionPane(Node... content) {
        this();
        getNodes().addAll(content);
    }

    protected Node createPlaceholder() {
        Label placeholder = new Label();
        placeholder.getStyleClass().add("placeholder-label");
        placeholder.textProperty().bind(placeholderProperty());
        placeholder.visibleProperty().bind(expandedProperty().not().and(placeholderProperty().isNotEmpty()));
        placeholder.managedProperty().bind(expandedProperty().not().and(placeholderProperty().isNotEmpty()));
        StackPane.setAlignment(placeholder, Pos.TOP_LEFT);
        VBox.setVgrow(placeholder, Priority.ALWAYS);
        return placeholder;
    }

    public final FancyLabel getTitleLabel() {
        return titleLabel;
    }

    public final FancyLabel getSubtitleLabel() {
        return subtitleLabel;
    }

    private final StringProperty title = new SimpleStringProperty(this, "title");

    public final String getTitle() {
        return title.get();
    }

    public final StringProperty titleProperty() {
        return title;
    }

    public final void setTitle(String title) {
        this.title.set(title);
    }

    private final StringProperty subtitle = new SimpleStringProperty(this, "subtitle");

    public final String getSubtitle() {
        return subtitle.get();
    }

    public final StringProperty subtitleProperty() {
        return subtitle;
    }

    public final void setSubtitle(String subtitle) {
        this.subtitle.set(subtitle);
    }

    private final ListProperty<Node> nodes = new SimpleListProperty<>(this, "nodes", FXCollections.observableArrayList());

    public final ObservableList<Node> getNodes() {
        return nodes.get();
    }

    public ListProperty<Node> nodesProperty() {
        return nodes;
    }

    public final void setNodes(ObservableList<Node> nodes) {
        this.nodes.set(nodes);
    }

    private final ObjectProperty<Node> footer = new SimpleObjectProperty<>(this, "footer");

    public final Node getFooter() {
        return footer.get();
    }

    public final ObjectProperty<Node> footerProperty() {
        return footer;
    }

    public final void setFooter(Node footer) {
        this.footer.set(footer);
    }

    private final ObjectProperty<Node> extras = new SimpleObjectProperty<>(this, "extras");

    public final Node getExtras() {
        return extras.get();
    }

    public final ObjectProperty<Node> extrasProperty() {
        return extras;
    }

    public final void setExtras(Node extras) {
        this.extras.set(extras);
    }
}
