package com.dlsc.jfxcentral.views;

import com.dlsc.jfxcentral.JFXCentralApp;
import com.gluonhq.attach.audio.AudioService;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.media.AudioClip;
import javafx.scene.paint.Color;

public class IntroView extends StackPane {

    private final AudioClip plonkSound;
    private final DukeAnimationView animationView;

    public IntroView(Runnable callback) {

        sceneProperty().addListener(it -> {
            Scene scene = getScene();
            if (scene != null) {
                scene.setFill(Color.rgb(68, 131, 160));
            }
        });

        animationView = new DukeAnimationView(callback);
        animationView.setEndText("Click to continue!");

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

        plonkSound = new AudioClip(JFXCentralApp.class.getResource("sound.wav").toExternalForm());
        plonkSound.setVolume(.5);
        plonkSound.setCycleCount(1);

        Thread thread = new Thread(() -> {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            // don't bother if user already clicked
            if (getScene() != null && !Boolean.getBoolean("mute")) {
                plonkSound.setVolume(.2);
//                plonkSound.play();
            }
        });

        thread.setName("Audio Thread");
        thread.setDaemon(true);
        thread.start();

        animationView.setCursor(Cursor.HAND);
        animationView.setOnMouseClicked(evt -> {
            if (animationView.isReady()) {
                callback.run();
            }
        });
    }

    public DukeAnimationView getAnimationView() {
        return animationView;
    }
}
