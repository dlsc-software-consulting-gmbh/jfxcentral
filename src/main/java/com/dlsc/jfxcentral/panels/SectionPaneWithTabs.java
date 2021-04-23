package com.dlsc.jfxcentral.panels;

import javafx.beans.DefaultProperty;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Skin;

@DefaultProperty("tabs")
public class SectionPaneWithTabs extends SectionPaneBase {

    public SectionPaneWithTabs() {
        getStyleClass().add("section-pane-with-tabs");
    }

    public SectionPaneWithTabs(Tab... tabs) {
        this();
        getTabs().addAll(tabs);
    }

    @Override
    protected Skin<?> createDefaultSkin() {
        return new SectionPaneWithTabsSkin(this);
    }

    private final ListProperty<Tab> tabs = new SimpleListProperty<>(this, "tabs", FXCollections.observableArrayList());

    public ObservableList<Tab> getTabs() {
        return tabs.get();
    }

    public ListProperty<Tab> tabsProperty() {
        return tabs;
    }

    public void setTabs(ObservableList<Tab> tabs) {
        this.tabs.set(tabs);
    }
}
