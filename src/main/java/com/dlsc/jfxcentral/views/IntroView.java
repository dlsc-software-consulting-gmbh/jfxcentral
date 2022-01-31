package com.dlsc.jfxcentral.views;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;

public class IntroView extends StackPane {

//    private final AudioClip plonkSound;
    private final DukeAnimationView animationView;

    public IntroView(Runnable callback) {
        getStyleClass().add("intro-view");

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
                animationView.play();
            } else {
                animationView.stop();
            }
        });

        getChildren().add(animationView);

        CheckBox disableEffectsBox = new CheckBox("Disable effects");
        disableEffectsBox.selectedProperty().bindBidirectional(disableEffectsProperty());
        StackPane.setAlignment(disableEffectsBox, Pos.BOTTOM_CENTER);
        StackPane.setMargin(disableEffectsBox, new Insets(20));
       // getChildren().add(disableEffectsBox);

//        AudioService.create().ifPresent(service -> service.loadSound(JFXCentralApp.class.getResource("sound.wav")).ifPresent(audio -> audio.play()));
//
//        plonkSound = new AudioClip(JFXCentralApp.class.getResource("sound.wav").toExternalForm());
//        plonkSound.setVolume(.5);
//        plonkSound.setCycleCount(1);
//
//        Thread thread = new Thread(() -> {
//            try {
//                Thread.sleep(2000);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//
////            // don't bother if user already clicked
////            if (getScene() != null && !Boolean.getBoolean("mute")) {
////                plonkSound.setVolume(.2);
////                plonkSound.play();
////            }
//        });
//
//        thread.setName("Audio Thread");
//        thread.setDaemon(true);
//        thread.start();

        animationView.setCursor(Cursor.HAND);
        animationView.setOnMouseClicked(evt -> {
            if (animationView.isReady()) {
                callback.run();
            }
        });
    }

    private final BooleanProperty disableEffects = new SimpleBooleanProperty(this, "disableEffects", true);

    public boolean isDisableEffects() {
        return disableEffects.get();
    }

    public BooleanProperty disableEffectsProperty() {
        return disableEffects;
    }

    public void setDisableEffects(boolean disableEffects) {
        this.disableEffects.set(disableEffects);
    }

    public DukeAnimationView getAnimationView() {
        return animationView;
    }
}
