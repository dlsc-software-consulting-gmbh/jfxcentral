package com.dlsc.jfxcentral;

import fr.brouillard.oss.cssfx.CSSFX;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class JFXCentralApp extends Application {

    @Override
    public void start(Stage primaryStage) {
        Scene scene = new Scene(new RootPane());
        scene.getStylesheets().add(JFXCentralApp.class.getResource("styles.css").toExternalForm());

        CSSFX.start();

        primaryStage.setScene(scene);
        primaryStage.setWidth(1200);
        primaryStage.setHeight(900);
        primaryStage.centerOnScreen();
        primaryStage.setTitle("JFXCentral");
        primaryStage.show();
    }

    public static void main(String args[]) {
        launch(args);
    }
}
