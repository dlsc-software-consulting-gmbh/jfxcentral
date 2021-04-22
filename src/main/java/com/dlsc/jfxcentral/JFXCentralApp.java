package com.dlsc.jfxcentral;

import com.gluonhq.attach.audio.AudioService;
import com.gluonhq.attach.orientation.OrientationService;
import fr.brouillard.oss.cssfx.CSSFX;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCharacterCombination;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.scenicview.ScenicView;

public class JFXCentralApp extends Application {

    @Override
    public void start(Stage primaryStage) {
        Scene scene;

        if (false) {
            ImageView imageView = new ImageView(JFXCentralApp.class.getResource("duke-jfx.gif").toExternalForm());
            imageView.setPreserveRatio(true);
            imageView.fitWidthProperty().bind(primaryStage.widthProperty().multiply(.6));

            OrientationService.create().ifPresent(service -> {
                service.orientationProperty().addListener(it -> {
                    System.out.println("orientation now: " + service.orientationProperty().get());
                    service.getOrientation().ifPresent(orientation -> {
                        switch (service.getOrientation().get()) {
                            case HORIZONTAL:
                                imageView.fitHeightProperty().bind(primaryStage.heightProperty().multiply(.6));
                                break;
                            case VERTICAL:
                                imageView.fitWidthProperty().bind(primaryStage.widthProperty().multiply(.6));
                                break;
                        }
                    });
                });
            });

            AudioService.create().ifPresent(service -> service.loadSound(JFXCentralApp.class.getResource("sound.wav")).ifPresent(audio -> audio.play()));

            StackPane stackPane = new StackPane(imageView);

            scene = new Scene(stackPane);
            scene.setFill(Color.rgb(68, 131, 160));
            stackPane.setOnMouseClicked(evt -> scene.setRoot(new RootPane()));
        } else {
            scene = new Scene(new RootPane());
        }

        scene.getStylesheets().add(JFXCentralApp.class.getResource("styles.css").toExternalForm());
        scene.setOnKeyPressed(evt -> {
            if (KeyCharacterCombination.valueOf("shortcut+i").match(evt)) {
                ScenicView.show(scene);
            }
        });

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
