package com.dlsc.jfxcentral.views;

import com.dlsc.jfxcentral.RootPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

public class PageView extends VBox {

    private final RootPane rootPane;

    protected PageView(RootPane rootPane) {
        this.rootPane = rootPane;
        getStyleClass().add("page");
        setMinHeight(Region.USE_PREF_SIZE);
    }

    public RootPane getRootPane() {
        return rootPane;
    }
}
