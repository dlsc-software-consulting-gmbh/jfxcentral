package com.dlsc.jfxcentral.views.mobile;

import com.dlsc.jfxcentral.views.HiddenSidesPane;
import javafx.geometry.Side;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import org.kordamp.ikonli.javafx.FontIcon;
import org.kordamp.ikonli.materialdesign.MaterialDesign;

public class MobileHeader extends HBox {

    public MobileHeader(HiddenSidesPane hiddenSidesPane) {
        getStyleClass().add("mobile-header");

        FontIcon hamburgerIcon = new FontIcon(MaterialDesign.MDI_MENU);
        hamburgerIcon.getStyleClass().add("menu-icon");
        hamburgerIcon.setOnMouseClicked(evt -> {
            if (hiddenSidesPane.getProperties().containsKey("showPane")) {
                hiddenSidesPane.hide();
            } else {
                hiddenSidesPane.show(Side.LEFT);
            }
        });

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        getChildren().addAll(hamburgerIcon, spacer, new FontSizeSelector());
    }
}
