package com.dlsc.jfxcentral;

import com.jpro.web.View;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;

public class MobileNotSupportedView extends View {

    @Override
    public String title() {
        return "Mobile not supported";
    }

    @Override
    public String description() {
        return "Mobile not supported";
    }

    @Override
    public boolean fullscreen() {
        return true;
    }

    @Override
    public Node content() {
        StackPane pane = new StackPane();
        pane.getChildren().add(new Label("Mobile support will follow soon!\n\nPlease use your desktop browser!"));
        pane.getStyleClass().add("no-mobile-pane");
        return pane;
    }
}
