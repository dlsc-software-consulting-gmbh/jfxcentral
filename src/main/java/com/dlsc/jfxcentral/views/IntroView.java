package com.dlsc.jfxcentral.views;

import com.dlsc.jfxcentral.JFXCentralApp;
import com.gluonhq.attach.audio.AudioService;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.media.AudioClip;
import javafx.scene.paint.Color;

public class IntroView extends StackPane {

    public IntroView(RootPane rootPane) {
        sceneProperty().addListener(it -> {
            Scene scene = getScene();
            if (scene != null) {
                scene.setFill(Color.rgb(68, 131, 160));
            }
        });

        DukeAnimationView animationView = new DukeAnimationView(() -> rootPane.setView(View.HOME));
        animationView.setOnMouseClicked(evt -> getScene().setFill(Color.rgb(224, 229, 234))); // reduce flickering
        animationView.sceneProperty().addListener(it -> {
            if (animationView.getScene() != null) {
                System.out.println("starting");
                animationView.play();
            } else {
                System.out.println("stopping");
                animationView.stop();
            }
        });

        getChildren().add(animationView);

        AudioService.create().ifPresent(service -> service.loadSound(JFXCentralApp.class.getResource("sound.wav")).ifPresent(audio -> audio.play()));

        AudioClip plonkSound = new AudioClip(JFXCentralApp.class.getResource("sound.wav").toExternalForm());
        plonkSound.setVolume(.5);
        plonkSound.setCycleCount(1);

        Thread thread = new Thread(() -> {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            plonkSound.play();
        });

        thread.setName("Audio Thread");
        thread.setDaemon(true);
        thread.start();
    }
}