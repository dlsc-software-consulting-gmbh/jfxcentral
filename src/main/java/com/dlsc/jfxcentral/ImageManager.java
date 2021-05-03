package com.dlsc.jfxcentral;

import com.dlsc.jfxcentral.model.Book;
import com.dlsc.jfxcentral.model.Library;
import com.dlsc.jfxcentral.model.Person;
import com.dlsc.jfxcentral.model.Video;
import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.image.Image;

import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ImageManager extends HashMap<String, ObjectProperty<Image>> {

    private final ExecutorService service = Executors.newFixedThreadPool(8);

    private static final ImageManager instance = new ImageManager();

    private ImageManager() {
    }

    public static synchronized ImageManager getInstance() {
        return instance;
    }

    public ObjectProperty<Image> personImageProperty(Person person) {
        return imageProperty("https://raw.githubusercontent.com/dlemmermann/jfxcentral-data/main/images/people/", person.getPhoto());
    }

    public ObjectProperty<Image> bookCoverImageProperty(Book book) {
        return imageProperty("https://raw.githubusercontent.com/dlemmermann/jfxcentral-data/main/images/books/", book.getImage());
    }

    public ObjectProperty<Image> libraryImageProperty(Library library) {
        return imageProperty("https://raw.githubusercontent.com/dlemmermann/jfxcentral-data/main/libraries/" + library.getId() + "/", library.getLogoImageFile());
    }

    public ObjectProperty<Image> youTubeImageProperty(Video video) {
        return imageProperty("https://img.youtube.com/vi/" + video.getId(), "/0.jpg", video.getId());
    }

    private ObjectProperty<Image> imageProperty(String baseURL, String photoFileName) {
        return imageProperty(baseURL, photoFileName, photoFileName);
    }

    private ObjectProperty<Image> imageProperty(String baseURL, String photoFileName, String photoKey) {
        return computeIfAbsent(photoKey, key -> {
            ObjectProperty<Image> property = new SimpleObjectProperty<>();
            String url = baseURL + photoFileName;
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
