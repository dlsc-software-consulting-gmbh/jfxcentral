package com.dlsc.jfxcentral.views.mobile;

import com.dlsc.jfxcentral.views.HiddenSidesPane;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
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

        Label title1 = new Label("JFX-Central");
        title1.setMaxWidth(Double.MAX_VALUE);
        title1.getStyleClass().add("title");
        StackPane.setAlignment(title1, Pos.CENTER_LEFT);

        Label title2 = new Label("JFX-Central");
        title2.setMaxWidth(Double.MAX_VALUE);
        title2.getStyleClass().addAll("title", "title-shadow");
        StackPane.setAlignment(title2, Pos.CENTER_LEFT);

        StackPane stackPane = new StackPane(title2, title1);
        stackPane.getStyleClass().add("title-wrapper");
        HBox.setHgrow(stackPane, Priority.ALWAYS);
        com.jpro.web.Util.setLink(stackPane, "/home", "JFX-Central");

        getChildren().addAll(stackPane, spacer, hamburgerIcon);
    }
}
