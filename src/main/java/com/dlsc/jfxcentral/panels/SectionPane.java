package com.dlsc.jfxcentral.panels;

import javafx.beans.DefaultProperty;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.Skin;

@DefaultProperty("nodes")
public class SectionPane extends SectionPaneBase {

    private final FancyLabel titleLabel = new FancyLabel();

    private final FancyLabel subtitleLabel = new FancyLabel();

    public SectionPane() {
    }

    public SectionPane(Node... content) {
        this();
        getNodes().addAll(content);
    }

    @Override
    protected Skin<?> createDefaultSkin() {
        return new SectionPaneSkin(this);
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
