package com.dlsc.jfxcentral.views.ikonli;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class IkonliBrowserApp extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        IkonliBrowser browser = new IkonliBrowser();
        Scene scene = new Scene(browser);
        primaryStage.setScene(scene);
        primaryStage.sizeToScene();
        primaryStage.centerOnScreen();
        primaryStage.setTitle("Ikonli Browser");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
