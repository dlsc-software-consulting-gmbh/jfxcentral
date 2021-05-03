package com.dlsc.jfxcentral.panels;

import javafx.beans.DefaultProperty;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

@DefaultProperty("tabs")
public class SectionPaneWithTabs extends SectionPaneBase {

    public SectionPaneWithTabs() {
        getStyleClass().add("section-pane-with-tabs");

        TabPane tabPane = new TabPane();
        tabPane.setFillHeader(true);
        Bindings.bindContent(tabPane.getTabs(), getTabs());
        getChildren().setAll(tabPane);
    }

    public SectionPaneWithTabs(Tab... tabs) {
        this();
        getTabs().addAll(tabs);
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
