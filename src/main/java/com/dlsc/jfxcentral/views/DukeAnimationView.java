package com.dlsc.jfxcentral.views;

import com.dlsc.jfxcentral.JFXCentralApp;
import com.gluonhq.attach.orientation.OrientationService;
import javafx.animation.*;
import javafx.application.Platform;
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
import org.eclipse.jgit.lib.ProgressMonitor;

import java.text.NumberFormat;

public class DukeAnimationView extends StackPane implements ProgressMonitor {

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
    private final Label label;

    public DukeAnimationView(Runnable callback) {
        getStyleClass().add("duke-animation-view");

        getStylesheets().add(JFXCentralApp.class.getResource("styles.css").toExternalForm());

        label = new Label();
        label.getStyleClass().add("start-message");
        label.setVisible(callback != null);
        label.setManaged(callback != null);
        label.setOnMousePressed(evt -> {
            if (ready) {
                callback.run();
            }
        });

        VBox vBox = new VBox(40, imageView, label);
        vBox.setAlignment(Pos.CENTER);
        VBox.setVgrow(vBox, Priority.ALWAYS);

        getChildren().add(vBox);

        imageView.setPreserveRatio(true);
        imageView.fitWidthProperty().bind(vBox.widthProperty().multiply(.6));
        imageView.fitHeightProperty().bind(vBox.heightProperty().multiply(.6));

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

    public Label getLabel() {
        return label;
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

    public void showLastImage() {
        stop();
        imageView.setImage(images[images.length - 1]);
    }

    public void stop() {
        timeline.stop();
    }

    public boolean isReady() {
        return ready;
    }

    int progress = 0;
    int totalWork = 0;
    String title = "";
    boolean ready = true;

    @Override
    public void start(int totalTasks) {
        Platform.runLater(() -> {
            label.setText("Loading data ...");
        });

        ready = false;
    }

    @Override
    public void beginTask(String title, int work) {
        progress = 0;
        totalWork = work;
        this.title = title.replace("remote: ", "");
    }

    @Override
    public void update(int completed) {
        progress += completed;
        double p = (double) progress / (double) totalWork;
        if (p > 0) {
            Platform.runLater(() -> label.setText(title + ": " + NumberFormat.getPercentInstance().format(p)));
        }
    }

    @Override
    public void endTask() {
        Platform.runLater(() -> label.setText(getEndText()));
        ready = true;
    }

    private String endText = "Click to continue";

    public String getEndText() {
        return endText;
    }

    public void setEndText(String endText) {
        this.endText = endText;
    }

    @Override
    public boolean isCancelled() {
        return false;
    }
}
