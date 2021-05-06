package com.dlsc.jfxcentral;

import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

public class SideBar extends ViewPane {

    public SideBar(RootPane rootPane) {
        getStyleClass().add("side-bar");

        HBox hBox = new HBox();
        hBox.setFillHeight(true);

        TopMenu topMenu = new TopMenu(rootPane);
        viewProperty().bindBidirectional(topMenu.viewProperty());

        CategoryPane categoryMenu = new CategoryPane(rootPane);
        categoryMenu.viewProperty().bind(viewProperty());

        hBox.getChildren().addAll(topMenu, categoryMenu);

        Region topSpacer = new Region();
        topSpacer.getStyleClass().add("top-spacer");

        VBox vBox = new VBox(topSpacer, hBox);
        VBox.setVgrow(hBox, Priority.ALWAYS);

        getChildren().add(vBox);
    }
}
