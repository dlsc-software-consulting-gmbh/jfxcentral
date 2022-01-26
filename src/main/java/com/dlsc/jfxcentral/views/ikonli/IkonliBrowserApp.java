package com.dlsc.jfxcentral.views.ikonli;

import com.dlsc.jfxcentral.JFXCentralApp;
import fr.brouillard.oss.cssfx.CSSFX;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class IkonliBrowserApp extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        IkonliBrowser browser = new IkonliBrowser();
        Scene scene = new Scene(browser);
        scene.getStylesheets().add(JFXCentralApp.class.getResource("styles.css").toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.setWidth(1000);
        primaryStage.setHeight(800);
        primaryStage.centerOnScreen();
        primaryStage.setTitle("Ikonli Browser");
        primaryStage.show();

        CSSFX.start();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
