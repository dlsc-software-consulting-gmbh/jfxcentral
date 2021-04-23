package com.dlsc.jfxcentral.panels;

import javafx.beans.Observable;
import javafx.css.PseudoClass;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.SkinBase;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.List;

public class TabPaneSkin extends SkinBase<TabPane> {

    private static final PseudoClass SELECTED = PseudoClass.getPseudoClass("selected");

    private final HBox header;
    private final VBox content;
    private TabView selectedTab;

    /**
     * Constructor for all SkinBase instances.
     *
     * @param control The control for which this Skin should attach to.
     */
    public TabPaneSkin(TabPane control) {
        super(control);

        header = new HBox();
        header.getStyleClass().add("tab-header");

        content = new VBox();
        content.getStyleClass().add("tab-content");

        control.getTabs().addListener((Observable obs) -> buildTabs());
        control.fillHeaderProperty().addListener(obs -> buildTabs());

        buildTabs();

        VBox vBox = new VBox(header, content);
        getChildren().setAll(vBox);
    }

    private void buildTabs() {
        List<TabView> tabs = new ArrayList<>();
        TabView first = null;

        for (Tab tab : getSkinnable().getTabs()) {
            TabView view = new TabView(tab);
            tabs.add(view);

            if (getSkinnable().isFillHeader()) {
                HBox.setHgrow(view, Priority.ALWAYS);
                view.setMaxWidth(Double.MAX_VALUE);
            }

            view.setMaxHeight(Double.MAX_VALUE);

            if (first == null) {
                first = view;
            }
        }

        header.getChildren().setAll(tabs);
        setSelectedTab(first);
    }

    private void setSelectedTab(TabView selectedTab) {
        if (this.selectedTab != null) {
            this.selectedTab.pseudoClassStateChanged(SELECTED, false);
        }

        this.selectedTab = selectedTab;

        if (this.selectedTab != null && this.selectedTab.getContent() != null) {
            getSkinnable().setSelectedTab(this.selectedTab.getTab());
            this.selectedTab.pseudoClassStateChanged(SELECTED, true);
            content.getChildren().setAll(this.selectedTab.getContent());
            VBox.setVgrow(this.selectedTab.getContent(), Priority.ALWAYS);
        } else {
            getSkinnable().setSelectedTab(null);
            content.getChildren().clear();
        }
    }

    private class TabView extends Label {

        private final Tab tab;

        TabView(Tab tab) {
            this.tab = tab;

            setOnMouseClicked(evt -> setSelectedTab(this));
            getStyleClass().add("tab");

            if (tab.getIcon() != null) {
                getStyleClass().add(tab.getIcon());
            }

            if (tab.getName() != null) {
                setText(tab.getName());
            }

            if (tab.getGraphic() != null) {
                setGraphic(tab.getGraphic());
            }

            setAlignment(Pos.CENTER_LEFT);
        }

        public Tab getTab() {
            return tab;
        }

        public Node getContent() {
            return tab.getContent();
        }

    }
}
