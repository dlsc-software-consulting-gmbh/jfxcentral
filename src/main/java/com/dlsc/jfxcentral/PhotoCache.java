package com.dlsc.jfxcentral;

import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.image.Image;

import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class PhotoCache extends HashMap<String, ObjectProperty<Image>> {

    private final ExecutorService service = Executors.newFixedThreadPool(8);

    private static final PhotoCache instance = new PhotoCache();

    private PhotoCache() {
    }

    public static synchronized PhotoCache getInstance() {
        return instance;
    }

    public ObjectProperty<Image> imageProperty(String photoFileName) {
        return computeIfAbsent(photoFileName, key -> {
            ObjectProperty<Image> property = new SimpleObjectProperty<>();
            String url = "https://raw.githubusercontent.com/dlemmermann/jfxcentral-data/main/people/" + photoFileName;
            System.out.println(url);
            service.submit(() -> {
                Image image = new Image(url, false);
                Platform.runLater(() -> {
                    property.set(image);
                });
            });
            return property;
        });
    }
}
