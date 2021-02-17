package com.dlsc.jfxcentral.categories;

import com.dlsc.jfxcentral.RootPane;

import java.util.Objects;

import javafx.scene.layout.StackPane;

public class CategoryView extends StackPane {

    private final RootPane rootPane;

    protected CategoryView(RootPane rootPane) {
        this.rootPane = Objects.requireNonNull(rootPane);
    }

    public RootPane getRootPane() {
        return rootPane;
    }
}
