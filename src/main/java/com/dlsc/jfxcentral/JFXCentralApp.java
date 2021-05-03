package com.dlsc.jfxcentral;

import com.gluonhq.attach.audio.AudioService;
import com.gluonhq.attach.display.DisplayService;
import fr.brouillard.oss.cssfx.CSSFX;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class JFXCentralApp extends Application {

    private RootPane rootPane = new RootPane();
    private Scene scene;

    @Override
    public void start(Stage primaryStage) {
        if (true) {
            DukeAnimationView animationView = new DukeAnimationView(() -> showRootPane());
            animationView.sceneProperty().addListener(it -> {
                if (animationView.getScene() != null) {
                    System.out.println("starting");
                    animationView.play();
                } else {
                    System.out.println("stopping");
                    animationView.stop();
                }
            });
            AudioService.create().ifPresent(service -> service.loadSound(JFXCentralApp.class.getResource("sound.wav")).ifPresent(audio -> audio.play()));

            scene = new Scene(animationView);
            scene.setFill(Color.rgb(68, 131, 160));
            animationView.setOnMouseClicked(evt -> showRootPane());
        } else {
            scene = new Scene(new RootPane());
        }

        scene.getStylesheets().add(JFXCentralApp.class.getResource("styles.css").toExternalForm());
//        scene.setOnKeyPressed(evt -> {
//            if (KeyCharacterCombination.valueOf("shortcut+i").match(evt)) {
//                ScenicView.show(scene);
//            }
//        });

        DisplayService.create().ifPresentOrElse(service -> {
            if (service.isDesktop()) {
                System.out.println("starting CSSFX");
                CSSFX.start();
            }
        }, () -> CSSFX.start());

        primaryStage.setScene(scene);
        primaryStage.setWidth(1200);
        primaryStage.setHeight(1200);
        primaryStage.centerOnScreen();
        primaryStage.setTitle("JFXCentral");
        primaryStage.show();

        Platform.runLater(() -> {
            rootPane.setView(View.BOOKS);
        });
    }

    private void showRootPane() {
        scene.setRoot(rootPane);
    }

    public static void main(String args[]) {
        launch(args);
    }
}
