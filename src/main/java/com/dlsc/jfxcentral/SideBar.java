package com.dlsc.jfxcentral;

import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

public class SideBar extends ViewPane {

    private final TopMenu topMenu;
    private final CategoryPane categoryPane;

    public SideBar(RootPane rootPane) {
        getStyleClass().add("side-bar");

        HBox hBox = new HBox();
        hBox.setFillHeight(true);

        topMenu = new TopMenu(rootPane);
        viewProperty().bindBidirectional(topMenu.viewProperty());

        categoryPane = new CategoryPane(rootPane);
        categoryPane.viewProperty().bind(viewProperty());

        hBox.getChildren().addAll(topMenu, categoryPane);

        Region topSpacer = new Region();
        topSpacer.getStyleClass().add("top-spacer");

        VBox vBox = new VBox(topSpacer, hBox);
        VBox.setVgrow(hBox, Priority.ALWAYS);

        getChildren().add(vBox);
    }

    public TopMenu getTopMenu() {
        return topMenu;
    }

    public CategoryPane getCategoryPane() {
        return categoryPane;
    }
}
