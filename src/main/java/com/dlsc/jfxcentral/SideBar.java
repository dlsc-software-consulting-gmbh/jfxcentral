package com.dlsc.jfxcentral;

import javafx.scene.layout.HBox;

public class SideBar extends ViewPane {

    public SideBar(RootPane rootPane) {
        HBox hBox = new HBox();
        hBox.setFillHeight(true);

        TopMenu topMenu = new TopMenu(rootPane);
        viewProperty().bindBidirectional(topMenu.viewProperty());

        CategoryPane categoryMenu = new CategoryPane(rootPane);
        categoryMenu.viewProperty().bind(viewProperty());

        hBox.getChildren().addAll(topMenu, categoryMenu);

        getChildren().add(hBox);
    }
}
