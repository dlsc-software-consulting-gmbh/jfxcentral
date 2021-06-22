package com.dlsc.jfxcentral.views.mobile;

import javafx.geometry.Side;
import javafx.scene.layout.HBox;
import org.controlsfx.control.HiddenSidesPane;
import org.kordamp.ikonli.javafx.FontIcon;
import org.kordamp.ikonli.materialdesign.MaterialDesign;

public class MobileHeader extends HBox {

    public MobileHeader(HiddenSidesPane hiddenSidesPane) {
        getStyleClass().add("mobile-header");

        FontIcon hamburgerIcon = new FontIcon(MaterialDesign.MDI_MENU);
        hamburgerIcon.getStyleClass().add("menu-icon");
        hamburgerIcon.setOnMouseClicked(evt -> {
            if (hiddenSidesPane.getPinnedSide() != null) {
                hiddenSidesPane.setPinnedSide(null);
            } else {
                hiddenSidesPane.setPinnedSide(Side.LEFT);
            }
        });

        getChildren().add(hamburgerIcon);
    }
}
