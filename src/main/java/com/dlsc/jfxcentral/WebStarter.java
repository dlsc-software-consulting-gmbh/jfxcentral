package com.dlsc.jfxcentral;

import com.dlsc.jfxcentral.util.Util;
import com.jpro.web.sessionmanager.SessionManager;
import com.jpro.webapi.WebAPI;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class WebStarter extends Application {

    @Override
    public void start(Stage stage) {
        WebApp app = new WebApp(stage);

        Scene scene = new Scene(app);
        scene.setFill(Color.TRANSPARENT);
        scene.getStylesheets().add(JFXCentralApp.class.getResource("styles.css").toExternalForm());

        stage.setScene(scene);
        stage.show();

        Util.WEB_API = WebAPI.getWebAPI(scene);

        app.start(SessionManager.getDefault(app, stage));
    }

    public static void main(String[] args) {
        launch(args);
    }
}
