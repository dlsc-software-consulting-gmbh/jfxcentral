package com.dlsc.jfxcentral;

import com.jpro.web.Util;
import com.jpro.web.sessionmanager.SessionManager;
import javafx.beans.binding.Bindings;
import javafx.geometry.Pos;
import javafx.scene.control.MenuButton;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import org.kordamp.ikonli.javafx.FontIcon;
import org.kordamp.ikonli.material.Material;

public class NavigationView extends HBox {

    public NavigationView() {
        getStyleClass().add("navigation-view");

        setFillHeight(true);
        setAlignment(Pos.CENTER);

        MenuButton back = new MenuButton();
        back.setGraphic(new FontIcon(Material.ARROW_BACK));
        back.setOnAction(evt -> Util.goBack(this));
        back.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(back, Priority.ALWAYS);

        MenuButton forward = new MenuButton();
        forward.setGraphic(new FontIcon(Material.ARROW_FORWARD));
        forward.setOnAction(evt -> Util.goForward(this));
        forward.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(forward, Priority.ALWAYS);

        sceneProperty().addListener(it -> {
            if (getScene() != null) {
                SessionManager sessionManager = Util.getSessionManager(this);
                back.disableProperty().bind(Bindings.isEmpty(sessionManager.getHistoryBackward()));
                forward.disableProperty().bind(Bindings.isEmpty(sessionManager.getHistoryForwards()));
            }
        });

        getChildren().setAll(back, forward);
    }
}
