package com.dlsc.jfxcentral;

import com.gluonhq.attach.display.DisplayService;
import com.jpro.webapi.WebAPI;
import javafx.application.Application;
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

//            AudioService.create().ifPresent(service -> service.loadSound(JFXCentralApp.class.getResource("sound.wav")).ifPresent(audio -> audio.play()));

//            final AudioClip plonkSound = new AudioClip(JFXCentralApp.class.getResource("sound.wav").toExternalForm());
//            plonkSound.setVolume(.5);
//            plonkSound.setCycleCount(1);

            scene = new Scene(animationView);
            scene.setFill(Color.rgb(68, 131, 160));
            animationView.setOnMouseClicked(evt -> {
//                plonkSound.setVolume(0);
//                plonkSound.stop();
                showRootPane();
                scene.setFill(Color.rgb(224, 229, 234));
            });

            Thread thread = new Thread(() -> {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
//                plonkSound.play();
            });
            thread.setName("Audio Thread");
            thread.setDaemon(true);
            thread.start();
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
                rootPane.setDisplay(Display.DESKTOP);
                System.out.println("starting CSSFX");
//                CSSFX.start();
            } else if (service.isPhone()) {
                rootPane.setDisplay(Display.PHONE);
            } else if (service.isTablet()) {
                rootPane.setDisplay(Display.TABLET);
            }
        }, () -> {
//            CSSFX.start();

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
        primaryStage.setTitle("JFXCentral");
        primaryStage.show();
    }

    private void showRootPane() {
        scene.setRoot(rootPane);
    }

    public static void main(String args[]) {
        System.setProperty("prism.lcdtext", "false");
        launch(args);
    }
}
