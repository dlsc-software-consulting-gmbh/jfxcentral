package com.dlsc.jfxcentral.categories;

import com.dlsc.jfxcentral.RootPane;

import java.util.Objects;

import javafx.scene.layout.BorderPane;

public class CategoryView extends BorderPane {

    private final RootPane rootPane;

    protected CategoryView(RootPane rootPane) {
        this.rootPane = Objects.requireNonNull(rootPane);

        CategoryHeader header = new CategoryHeader();
        setTop(header);
    }

    public RootPane getRootPane() {
        return rootPane;
    }
}
