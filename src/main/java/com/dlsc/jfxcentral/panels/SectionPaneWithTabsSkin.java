package com.dlsc.jfxcentral.panels;

import javafx.beans.binding.Bindings;
import javafx.scene.control.SkinBase;

public class SectionPaneWithTabsSkin extends SkinBase<SectionPaneWithTabs> {

    public SectionPaneWithTabsSkin(SectionPaneWithTabs pane) {
        super(pane);

        TabPane tabPane = new TabPane();
        tabPane.setFillHeader(true);
        Bindings.bindContent(tabPane.getTabs(), pane.getTabs());
        getChildren().setAll(tabPane);
    }
}
