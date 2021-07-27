package com.dlsc.jfxcentral.views.detail;

import com.dlsc.jfxcentral.data.DataRepository;
import com.dlsc.jfxcentral.data.ImageManager;
import com.dlsc.jfxcentral.data.model.Blog;
import com.dlsc.jfxcentral.data.model.Person;
import com.dlsc.jfxcentral.panels.SectionPane;
import com.dlsc.jfxcentral.util.PageUtil;
import com.dlsc.jfxcentral.util.Util;
import com.dlsc.jfxcentral.views.RootPane;
import com.dlsc.jfxcentral.views.View;
import com.dlsc.jfxcentral.views.detail.cells.ResponsiveBoxWithPhotoView.ImageLocation;
import com.dlsc.jfxcentral.views.detail.cells.ResponsiveBoxWithPhotoView;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import org.apache.commons.lang3.StringUtils;
import org.kordamp.ikonli.fontawesome5.FontAwesomeBrands;
import org.kordamp.ikonli.javafx.FontIcon;
import org.kordamp.ikonli.material.Material;

public class PeopleDetailView extends ModelObjectDetailView<Person> {

    private ImageView championImageView = new ImageView();
    private ImageView rockstarImageView = new ImageView();
    private ResponsiveBoxWithPhotoView responsiveBox;

    public PeopleDetailView(RootPane rootPane) {
        super(rootPane, View.PEOPLE);

        getStyleClass().add("people-detail-view");

        createTitleBox();
        createStandardBoxes();
    }

    protected boolean isUsingMasterView() {
        return true;
    }

    @Override
    protected void createTitleBox() {
        championImageView.getStyleClass().add("champion-image");
        championImageView.setPreserveRatio(true);
        championImageView.setFitHeight(16);

        rockstarImageView.getStyleClass().add("rockstar-image");
        rockstarImageView.setPreserveRatio(true);
        rockstarImageView.setFitHeight(16);

        HBox badgesBox = new HBox(championImageView, rockstarImageView);
        badgesBox.getStyleClass().add("badges-box");

        responsiveBox = new ResponsiveBoxWithPhotoView(getRootPane().isMobile() ? ImageLocation.BANNER : ImageLocation.LARGE_ON_SIDE);
        responsiveBox.getTitleLabel().setGraphic(badgesBox);

        SectionPane sectionPane = new SectionPane(responsiveBox);
        sectionPane.getStyleClass().add("title-section");

        content.getChildren().addAll(sectionPane);
    }

    protected void updateView(Person oldPerson, Person person) {
        if (person != null) {

            championImageView.setVisible(person.isChampion());
            rockstarImageView.setVisible(person.isRockstar());

            responsiveBox.setTitle(person.getName());
            responsiveBox.descriptionProperty().bind(DataRepository.getInstance().personDescriptionProperty(person));
            responsiveBox.imageProperty().bind(ImageManager.getInstance().personImageProperty(person));
            responsiveBox.getExtraControls().clear();

            if (StringUtils.isNotEmpty(person.getTwitter())) {
                Button twitter = new Button("Twitter");
                twitter.getStyleClass().addAll("social-button", "twitter");
                Util.setLink(twitter, "https://twitter.com/" + person.getTwitter(), person.getName());
                twitter.setGraphic(new FontIcon(FontAwesomeBrands.TWITTER));
                responsiveBox.getExtraControls().add(twitter);
            }

            if (StringUtils.isNotEmpty(person.getLinkedIn())) {
                Button linkedIn = new Button("LinkedIn");
                linkedIn.getStyleClass().addAll("social-button", "linkedin");
                Util.setLink(linkedIn, "https://www.linkedin.com/in/" + person.getLinkedIn(), person.getName());
                linkedIn.setGraphic(new FontIcon(FontAwesomeBrands.LINKEDIN));
                responsiveBox.getExtraControls().add(linkedIn);
            }

            if (StringUtils.isNotEmpty(person.getBlogId())) {
                Button blog = new Button("Blog");
                blog.getStyleClass().addAll("social-button", "blog");
                Blog blogEntity = DataRepository.getInstance().getBlogById(person.getBlogId()).get();
                String link = PageUtil.getLink(blogEntity);
                Util.setLink(blog, link, blog.getText());
                blog.setGraphic(new FontIcon(FontAwesomeBrands.BLOGGER));
                responsiveBox.getExtraControls().add(blog);
            }

            if (StringUtils.isNotEmpty(person.getWebsite())) {
                Button website = new Button("Website");
                website.getStyleClass().addAll("social-button", "website");
                Util.setLink(website, person.getWebsite(), person.getName());
                website.setGraphic(new FontIcon(FontAwesomeBrands.SAFARI));
                responsiveBox.getExtraControls().add(website);
            }

            if (StringUtils.isNotEmpty(person.getEmail())) {
                Button website = new Button("Mail");
                website.getStyleClass().addAll("social-button", "mail");
                Util.setLink(website, "mailto:" + person.getEmail() + "?subject=JFXCentral%20Mail%20Contact", person.getName());
                website.setGraphic(new FontIcon(Material.MAIL));
                responsiveBox.getExtraControls().add(website);
            }

            if (StringUtils.isNotEmpty(person.getGitHub())) {
                Button github = new Button("GitHub");
                github.getStyleClass().addAll("social-button", "github");
                Util.setLink(github, "https://github.com/" + person.getGitHub(), person.getName());
                github.setGraphic(new FontIcon(FontAwesomeBrands.GITHUB));
                responsiveBox.getExtraControls().add(github);
            }
        }
    }
}
