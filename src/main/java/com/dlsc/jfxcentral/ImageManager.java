package com.dlsc.jfxcentral;

import com.dlsc.jfxcentral.model.*;
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
    private static final Image MISSING_IMAGE = new Image(JFXCentralApp.class.getResource("missing-image.jpg").toExternalForm());
    private static final Image MISSING_VIDEO_IMAGE = new Image(JFXCentralApp.class.getResource("missing-video-image.png").toExternalForm());
    public static final String JFX_CENTRAL_BASE_URL = "https://raw.githubusercontent.com/dlemmermann/jfxcentral-data/main";

    private final ExecutorService service = Executors.newFixedThreadPool(8);

    private static final ImageManager instance = new ImageManager();

    private ImageManager() {
    }

    public static synchronized ImageManager getInstance() {
        return instance;
    }

    public ObjectProperty<Image> newsBannerImageProperty(News news) {
        return imageProperty(DataRepository.getInstance().getNewsBaseUrl(news) + "/", news.getBanner(), "banner-" + news.getId(), MISSING_IMAGE);
    }

    public ObjectProperty<Image> personImageProperty(Person person) {
        return imageProperty(JFX_CENTRAL_BASE_URL + "/images/people/", person.getPhoto());
    }

    public ObjectProperty<Image> bookCoverImageProperty(Book book) {
        return imageProperty(JFX_CENTRAL_BASE_URL + "/images/books/", book.getImage());
    }

    public ObjectProperty<Image> libraryImageProperty(Library library) {
        return imageProperty(JFX_CENTRAL_BASE_URL + "/libraries/" + library.getId() + "/", library.getLogoImageFile());
    }

    public ObjectProperty<Image> libraryImageProperty(Library library, String imagePath) {
        return imageProperty(JFX_CENTRAL_BASE_URL + "/libraries/" + library.getId() + "/", imagePath, imagePath, MISSING_IMAGE);
    }

    public ObjectProperty<Image> youTubeImageProperty(Video video) {
        return imageProperty("https://img.youtube.com/vi/" + video.getId(), "/0.jpg", video.getId(), MISSING_VIDEO_IMAGE);
    }

    private ObjectProperty<Image> imageProperty(String baseURL, String photoFileName) {
        return imageProperty(baseURL, photoFileName, photoFileName, null);
    }

    private ObjectProperty<Image> imageProperty(String baseURL, String photoFileName, String photoKey, Image placeholderImage) {
        if (StringUtils.isBlank(photoFileName) || StringUtils.isBlank(photoKey)) {
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
