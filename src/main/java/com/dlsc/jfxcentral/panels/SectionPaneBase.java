package com.dlsc.jfxcentral.panels;

import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.css.PseudoClass;
import javafx.scene.Node;
import javafx.scene.control.Control;
import javafx.scene.control.MenuItem;

public class SectionPaneBase extends Control {

    public SectionPaneBase() {
        getStyleClass().add("section-pane");
        noBorder.addListener(it -> updateBorder());
    }

    private final ListProperty<MenuItem> menuItems = new SimpleListProperty<>(this, "menuItems", FXCollections.observableArrayList());

    public final ObservableList<MenuItem> getMenuItems() {
        return menuItems.get();
    }

    public final ListProperty<MenuItem> menuItemsProperty() {
        return menuItems;
    }

    public final void setMenuItems(ObservableList<MenuItem> menuItems) {
        this.menuItems.set(menuItems);
    }

    private final ListProperty<Node> titleExtras = new SimpleListProperty<>(this, "titleExtras", FXCollections.observableArrayList());

    public final ObservableList<Node> getTitleExtras() {
        return titleExtras.get();
    }

    public final ListProperty<Node> titleExtrasProperty() {
        return titleExtras;
    }

    public final void setTitleExtras(ObservableList<Node> titleExtras) {
        this.titleExtras.set(titleExtras);
    }

    private void updateBorder() {
        pseudoClassStateChanged(PseudoClass.getPseudoClass("no-border"), isNoBorder());
    }

    private final BooleanProperty expanded = new SimpleBooleanProperty(this, "expanded", true);

    public boolean isExpanded() {
        return expanded.get();
    }

    public BooleanProperty expandedProperty() {
        return expanded;
    }

    public void setExpanded(boolean expanded) {
        this.expanded.set(expanded);
    }

    private final StringProperty placeholder = new SimpleStringProperty(this, "placeholder", "No data");

    public String getPlaceholder() {
        return placeholder.get();
    }

    public StringProperty placeholderProperty() {
        return placeholder;
    }

    public void setPlaceholder(String placeholder) {
        this.placeholder.set(placeholder);
    }

    private final BooleanProperty noBorder = new SimpleBooleanProperty(this, "noBorder", false);

    public boolean isNoBorder() {
        return noBorder.get();
    }

    public BooleanProperty noBorderProperty() {
        return noBorder;
    }

    public void setNoBorder(boolean noBorder) {
        this.noBorder.set(noBorder);
    }
}
