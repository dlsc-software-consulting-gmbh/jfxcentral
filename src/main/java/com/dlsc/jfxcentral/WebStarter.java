package com.dlsc.jfxcentral;

import com.jpro.web.sessionmanager.SessionManager;
import com.jpro.webapi.JProApplication;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class WebStarter extends Application {
    public static void main(String[] args)
    {
        launch(args);
    }

    @Override
    public void start(Stage stage)
    {
        WebApp app = new WebApp(stage);

        Scene scene = new Scene(app);
        scene.getStylesheets().add(JFXCentralApp.class.getResource("styles.css").toExternalForm());

        stage.setScene(scene);
        stage.show();
        app.start(SessionManager.getDefault(app,stage));
    }
}
