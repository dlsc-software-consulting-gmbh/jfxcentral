package com.dlsc.jfxcentral;

import com.jpro.web.sessionmanager.SessionManager;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class WebStarter extends Application {

    @Override
    public void start(Stage stage) {
        WebApp app = new WebApp(stage);

        Scene scene = new Scene(app);
        scene.getStylesheets().add(JFXCentralApp.class.getResource("styles.css").toExternalForm());

        stage.setScene(scene);
        stage.show();

        app.start(SessionManager.getDefault(app, stage));
    }

    public static void main(String[] args) {
        launch(args);
    }
}
