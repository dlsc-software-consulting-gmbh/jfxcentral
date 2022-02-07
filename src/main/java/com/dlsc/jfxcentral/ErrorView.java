package com.dlsc.jfxcentral;

import com.jpro.web.View;
import javafx.scene.Node;
import javafx.scene.control.Label;

public class ErrorView extends View {

    private final String path;

    public ErrorView(String path) {
        this.path = path;
    }

    @Override
    public String title() {
        return "Error";
    }

    @Override
    public String description() {
        return "JFX-Central Error Page";
    }

    @Override
    public Node content() {
        return new Label("invalid path: " + path);
    }
}
