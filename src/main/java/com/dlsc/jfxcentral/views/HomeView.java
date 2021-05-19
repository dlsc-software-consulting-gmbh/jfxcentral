package com.dlsc.jfxcentral.views;

import com.dlsc.jfxcentral.DukeAnimationView;
import com.dlsc.jfxcentral.RootPane;
import javafx.scene.layout.VBox;

public class HomeView extends PageView {

    private final VBox content = new VBox();

    public HomeView(RootPane rootPane) {
        super(rootPane);
        getStyleClass().add("home-view");

        createDukeHeader();

        setContent(content);
    }

    private void createDukeHeader() {
        DukeAnimationView dukeAnimationView = new DukeAnimationView(null);
        dukeAnimationView.setMaxWidth(Double.MAX_VALUE);
        dukeAnimationView.setMinHeight(400);
        content.getChildren().add(dukeAnimationView);
    }
}