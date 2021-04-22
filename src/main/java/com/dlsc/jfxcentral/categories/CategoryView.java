package com.dlsc.jfxcentral.categories;

import com.dlsc.jfxcentral.RootPane;
import javafx.scene.Node;
import javafx.scene.layout.BorderPane;

import java.util.Objects;

public abstract class CategoryView extends BorderPane {

    private final RootPane rootPane;

    protected CategoryView(RootPane rootPane) {
        this.rootPane = Objects.requireNonNull(rootPane);

        CategoryHeader header = new CategoryHeader();
        setTop(header);
    }

    public RootPane getRootPane() {
        return rootPane;
    }

    public abstract Node getPanel();
}
