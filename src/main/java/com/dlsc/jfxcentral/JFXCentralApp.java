package com.dlsc.jfxcentral;

import com.dlsc.jfxcentral.views.Display;
import com.dlsc.jfxcentral.views.RootPane;
import com.gluonhq.attach.display.DisplayService;
import com.jpro.webapi.WebAPI;
import fr.brouillard.oss.cssfx.CSSFX;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class JFXCentralApp extends Application {

    private RootPane rootPane = new RootPane();

    @Override
    public void start(Stage primaryStage) {

        rootPane = new RootPane();
        rootPane.setMaxWidth(1200);

        StackPane wrapper = new StackPane(rootPane);
        wrapper.getStyleClass().add("root-wrapper");

        Scene scene = new Scene(wrapper);
        scene.setFill(Color.rgb(68, 131, 160));
        scene.getStylesheets().add(JFXCentralApp.class.getResource("styles.css").toExternalForm());

//        scene.setOnKeyPressed(evt -> {
//            if (KeyCharacterCombination.valueOf("shortcut+i").match(evt)) {
//                ScenicView.show(scene);
//            }
//        });

        DisplayService.create().ifPresentOrElse(service -> {
            if (service.isDesktop()) {
                rootPane.setDisplay(Display.DESKTOP);
                System.out.println("starting CSSFX");
                CSSFX.start();
            } else if (service.isPhone()) {
                rootPane.setDisplay(Display.PHONE);
            } else if (service.isTablet()) {
                rootPane.setDisplay(Display.TABLET);
            }
        }, () -> {
            CSSFX.start();

            if (WebAPI.isBrowser()) {
                rootPane.setDisplay(Display.WEB);
            } else {
                rootPane.setDisplay(Display.DESKTOP);
            }
        });

        primaryStage.setScene(scene);
        primaryStage.setWidth(1200);
        primaryStage.setHeight(1200);
        primaryStage.centerOnScreen();
        primaryStage.setTitle("JFX-Central");
        primaryStage.show();
    }

    public static void main(String args[]) {
        System.setProperty("prism.lcdtext", "false");
        launch(args);
    }
}
