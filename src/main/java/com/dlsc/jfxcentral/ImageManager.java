package com.dlsc.jfxcentral;

import com.dlsc.jfxcentral.model.Book;
import com.dlsc.jfxcentral.model.Library;
import com.dlsc.jfxcentral.model.Person;
import com.dlsc.jfxcentral.model.Video;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.image.Image;
import org.apache.commons.lang3.StringUtils;

import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ImageManager extends HashMap<String, ObjectProperty<Image>> {

    private static final Image USER_IMAGE = new Image(JFXCentralApp.class.getResource("user.png").toExternalForm());
    private static final Image LIBRARY_IMAGE = new Image(JFXCentralApp.class.getResource("document_cup.png").toExternalForm());
    private static final Image MISSING_VIDEO_IMAGE = new Image(JFXCentralApp.class.getResource("missing-video-image.png").toExternalForm());

    private final ExecutorService service = Executors.newFixedThreadPool(8);

    private static final ImageManager instance = new ImageManager();

    private ImageManager() {
    }

    public static synchronized ImageManager getInstance() {
        return instance;
    }

    public ObjectProperty<Image> personImageProperty(Person person) {
        return imageProperty("https://raw.githubusercontent.com/dlemmermann/jfxcentral-data/main/images/people/", person.getPhoto(), person.getPhoto(), USER_IMAGE);
    }

    public ObjectProperty<Image> bookCoverImageProperty(Book book) {
        return imageProperty("https://raw.githubusercontent.com/dlemmermann/jfxcentral-data/main/images/books/", book.getImage());
    }

    public ObjectProperty<Image> libraryImageProperty(Library library) {
        return imageProperty("https://raw.githubusercontent.com/dlemmermann/jfxcentral-data/main/libraries/" + library.getId() + "/", library.getLogoImageFile(), library.getLogoImageFile(), LIBRARY_IMAGE);
    }

    public ObjectProperty<Image> youTubeImageProperty(Video video) {
        return imageProperty("https://img.youtube.com/vi/" + video.getId(), "/0.jpg", video.getId(), MISSING_VIDEO_IMAGE);
    }

    private ObjectProperty<Image> imageProperty(String baseURL, String photoFileName) {
        return imageProperty(baseURL, photoFileName, photoFileName, null);
    }

    private ObjectProperty<Image> imageProperty(String baseURL, String photoFileName, String photoKey, Image placeholderImage) {
        if (StringUtils.isBlank(photoKey)) {
            return new SimpleObjectProperty<>(placeholderImage);
        }

        return computeIfAbsent(photoKey, key -> {
            ObjectProperty<Image> property = new SimpleObjectProperty<>(placeholderImage);
            String url = baseURL + photoFileName;
            System.out.println(url);
            Image image = new Image(url + "?" + ZonedDateTime.now().toInstant(), true);
            image.progressProperty().addListener(it -> {
                if (image.getProgress() == 1) {
                    property.set(image);
                }
            });

            return property;
        });
    }
}
