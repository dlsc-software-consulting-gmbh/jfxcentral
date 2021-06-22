package com.dlsc.jfxcentral.views.detail.cells;

import com.dlsc.jfxcentral.data.DataRepository;
import com.dlsc.jfxcentral.data.ImageManager;
import com.dlsc.jfxcentral.data.model.Blog;
import com.dlsc.jfxcentral.data.model.Person;
import com.dlsc.jfxcentral.util.Util;
import com.dlsc.jfxcentral.views.RootPane;
import com.dlsc.jfxcentral.views.detail.cells.ResponsiveBox.ImageLocation;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import org.apache.commons.lang3.StringUtils;
import org.kordamp.ikonli.fontawesome5.FontAwesomeBrands;
import org.kordamp.ikonli.javafx.FontIcon;
import org.kordamp.ikonli.material.Material;

import java.util.Optional;

public class DetailPersonCell extends DetailCell<Person> {

    private final ResponsiveBoxWithPhotoView responsiveBox;
    private ImageView championImageView = new ImageView();
    private ImageView rockstarImageView = new ImageView();

    public DetailPersonCell(RootPane rootPane, boolean largeImage) {
        getStyleClass().add("detail-person-cell");

        championImageView.getStyleClass().add("champion-image");
        championImageView.setPreserveRatio(true);
        championImageView.setFitHeight(16);

        rockstarImageView.getStyleClass().add("rockstar-image");
        rockstarImageView.setPreserveRatio(true);
        rockstarImageView.setFitHeight(16);

        HBox badgesBox = new HBox(championImageView, rockstarImageView);
        badgesBox.getStyleClass().add("badges-box");

        responsiveBox = new ResponsiveBoxWithPhotoView(rootPane.isMobile() ? ImageLocation.HIDE : largeImage ? ImageLocation.LARGE_ON_SIDE : ImageLocation.SMALL_ON_SIDE);
        responsiveBox.visibleProperty().bind(emptyProperty().not());

        setGraphic(responsiveBox);
        setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
    }

    @Override
    protected void updateItem(Person person, boolean empty) {
        super.updateItem(person, empty);

        if (!empty && person != null) {
            responsiveBox.setTitle(person.getName());
            responsiveBox.descriptionProperty().bind(DataRepository.getInstance().personDescriptionProperty(person));
            championImageView.setVisible(person.isChampion());
            rockstarImageView.setVisible(person.isRockstar());
            responsiveBox.imageProperty().bind(ImageManager.getInstance().personImageProperty(person));
            responsiveBox.getButtons().clear();

            if (StringUtils.isNotEmpty(person.getTwitter())) {
                Button twitter = new Button("Twitter");
                twitter.getStyleClass().addAll("social-button", "twitter");
                Util.setLink(twitter, "https://twitter.com/" + person.getTwitter(), person.getName());
                twitter.setGraphic(new FontIcon(FontAwesomeBrands.TWITTER));
                responsiveBox.getButtons().add(twitter);
            }

            if (StringUtils.isNotEmpty(person.getLinkedIn())) {
                Button linkedIn = new Button("LinkedIn");
                linkedIn.getStyleClass().addAll("social-button", "linkedin");
                Util.setLink(linkedIn, "https://www.linkedin.com/in/" + person.getLinkedIn(), person.getName());
                linkedIn.setGraphic(new FontIcon(FontAwesomeBrands.LINKEDIN));
                responsiveBox.getButtons().add(linkedIn);
            }

            if (StringUtils.isNotEmpty(person.getBlogId())) {
                Optional<Blog> blogById = DataRepository.getInstance().getBlogById(person.getBlogId());
                if (blogById.isPresent()) {
                    Button blog = new Button("Blog");
                    blog.getStyleClass().addAll("social-button", "blog");
                    Util.setLink(blog, blogById.get().getUrl(), blogById.get().getSummary());
                    blog.setGraphic(new FontIcon(FontAwesomeBrands.BLOGGER));
                    responsiveBox.getButtons().add(blog);
                }
            }

            if (StringUtils.isNotEmpty(person.getWebsite())) {
                Button website = new Button("Website");
                website.getStyleClass().addAll("social-button", "website");
                Util.setLink(website, person.getWebsite(), person.getName());
                website.setGraphic(new FontIcon(FontAwesomeBrands.SAFARI));
                responsiveBox.getButtons().add(website);
            }

            if (StringUtils.isNotEmpty(person.getEmail())) {
                Button website = new Button("Mail");
                website.getStyleClass().addAll("social-button", "mail");
                Util.setLink(website, "mailto:" + person.getEmail() + "?subject=JFXCentral%20Mail%20Contact", person.getName());
                website.setGraphic(new FontIcon(Material.MAIL));
                responsiveBox.getButtons().add(website);
            }

            if (StringUtils.isNotEmpty(person.getGitHub())) {
                Button github = new Button("GitHub");
                github.getStyleClass().addAll("social-button", "github");
                Util.setLink(github, "https://github.com/" + person.getGitHub(), person.getName());
                github.setGraphic(new FontIcon(FontAwesomeBrands.GITHUB));
                responsiveBox.getButtons().add(github);
            }
        }
    }
}
