package com.dlsc.jfxcentral.json;

import com.dlsc.jfxcentral.model.Image;
import com.dlsc.jfxcentral.model.Library;
import com.dlsc.jfxcentral.model.License;
import com.dlsc.jfxcentral.model.Video;

import java.util.ArrayList;
import java.util.List;

public class WriteLibrary {

    public static void main(String[] args) {
        Library library = new Library();
        library.setId("gemsfx");
        library.setTitle("GemsFX");
        library.setPersonId("d.lemmermann");
        library.setLogoImageFile("gemsfx.png");
        library.setDescription("A collection of useful and highly advanced JavaFX controls.");
        library.setLicense(License.APACHE2);
        library.setCompanyId("dlsc");
        library.setHomepage("https://github.com/dlsc-software-consulting-gmbh/GemsFX");
        library.setRepository("https://github.com/dlsc-software-consulting-gmbh/GemsFX");
        library.setIssueTracker("https://github.com/dlsc-software-consulting-gmbh/GemsFX");

        Image image = new Image();
        image.setDescription("image description");
        image.setPath("gemsfx/image1");
        image.setTitle("Image Title");
        library.getImages().add(image);

        Video video = new Video();
        video.setDescription("video description");
        video.setId("OflZ745MYT0");
        library.getVideos().add(video);

        List<Library> list = new ArrayList<>();
        list.add(library);

//        Gson gson = new GsonBuilder().setPrettyPrinting().create();
//        System.out.println(gson.toJson(list));
    }
}
