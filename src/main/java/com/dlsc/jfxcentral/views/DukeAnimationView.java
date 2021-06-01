package com.dlsc.jfxcentral.views;

import com.dlsc.jfxcentral.JFXCentralApp;
import com.gluonhq.attach.orientation.OrientationService;
import javafx.animation.*;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

public class DukeAnimationView extends StackPane {

    private static Image[] images = new Image[89];

    static {
        for (int i = 0; i < 89; i++) {
            String counter = Integer.toString(i);
            if (i < 10) {
                counter = "0" + counter;
            }
            images[i] = new Image(JFXCentralApp.class.getResource("dukesmall/duke_jfx" + counter + ".png").toExternalForm());
        }
    }

    private final ImageView imageView = new ImageView();

    private final Timeline timeline = new Timeline();

    public DukeAnimationView(Runnable callback) {
        getStyleClass().add("duke-animation-view");

        Label button = new Label("Press to continue");
        button.getStyleClass().add("start-message");
        button.setOnMousePressed(evt -> callback.run());
        button.setVisible(callback != null);
        button.setManaged(callback != null);

        VBox vBox = new VBox(40, imageView, button);
        vBox.setAlignment(Pos.CENTER);
        VBox.setVgrow(vBox, Priority.ALWAYS);

        getChildren().add(vBox);

        imageView.setPreserveRatio(true);
        imageView.fitWidthProperty().bind(vBox.widthProperty().multiply(.6));

        OrientationService.create().ifPresent(service -> service.orientationProperty().addListener(it -> {
            System.out.println("orientation now: " + service.orientationProperty().get());
            service.getOrientation().ifPresent(orientation -> {
                switch (service.getOrientation().get()) {
                    case HORIZONTAL:
                        imageView.fitHeightProperty().bind(heightProperty().multiply(.6));
                        break;
                    case VERTICAL:
                        imageView.fitWidthProperty().bind(widthProperty().multiply(.6));
                        break;
                }
            });
        }));
    }

    public void play() {
        if (!timeline.getStatus().equals(Animation.Status.STOPPED)) {
            timeline.stop();
        }

        imageView.setImage(images[0]);

        IntegerProperty frameIndex = new SimpleIntegerProperty();
        frameIndex.addListener(it -> imageView.setImage(images[frameIndex.get()]));
        KeyValue keyValue = new KeyValue(frameIndex, images.length - 1, Interpolator.LINEAR);

        // frame 1 updates the images, frame 2 causes a longer delay before animation reverses and then starts over again
        KeyFrame keyFrame1 = new KeyFrame(Duration.seconds(images.length / 25), keyValue);
        KeyFrame keyFrame2 = new KeyFrame(Duration.seconds(images.length / 25).add(Duration.seconds(5)));

        timeline.setDelay(Duration.seconds(1));
        timeline.setAutoReverse(true);
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.getKeyFrames().setAll(keyFrame1, keyFrame2);
        timeline.play();
    }

    public void stop() {
        timeline.stop();
    }
}
