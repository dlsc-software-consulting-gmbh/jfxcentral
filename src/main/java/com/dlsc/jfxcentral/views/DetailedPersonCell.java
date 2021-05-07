package com.dlsc.jfxcentral.views;

import com.dlsc.jfxcentral.ImageManager;
import com.dlsc.jfxcentral.PhotoView;
import com.dlsc.jfxcentral.model.Person;
import com.dlsc.jfxcentral.util.Util;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import org.apache.commons.lang3.StringUtils;
import org.kordamp.ikonli.fontawesome5.FontAwesomeBrands;
import org.kordamp.ikonli.javafx.FontIcon;
import org.kordamp.ikonli.material.Material;

public class DetailedPersonCell extends AdvancedListCell<Person> {

    private HBox socialBox;
    private PhotoView photoView = new PhotoView();
    private Label nameLabel = new Label();
    private Label descriptionLabel = new Label();
    private javafx.scene.image.ImageView championImageView = new javafx.scene.image.ImageView();
    private javafx.scene.image.ImageView rockstarImageView = new javafx.scene.image.ImageView();

    public DetailedPersonCell() {
        getStyleClass().add("detailed-person-cell");

        photoView.setEditable(false);

        nameLabel.getStyleClass().addAll("header1", "name-label");
        nameLabel.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(nameLabel, Priority.ALWAYS);

        descriptionLabel.setWrapText(true);
        descriptionLabel.setMinHeight(Region.USE_PREF_SIZE);
        descriptionLabel.getStyleClass().add("description-label");
        HBox.setHgrow(descriptionLabel, Priority.ALWAYS);

        championImageView.getStyleClass().add("champion-image");
        championImageView.setPreserveRatio(true);
        championImageView.setFitHeight(16);

        rockstarImageView.getStyleClass().add("rockstar-image");
        rockstarImageView.setPreserveRatio(true);
        rockstarImageView.setFitHeight(16);

        HBox badgesBox = new HBox(championImageView, rockstarImageView);
        badgesBox.getStyleClass().add("badges-box");
        nameLabel.setGraphic(badgesBox);
        nameLabel.setContentDisplay(ContentDisplay.RIGHT);

        socialBox = new HBox();
        socialBox.getStyleClass().add("social-box");

        VBox vBox = new VBox(nameLabel, descriptionLabel, badgesBox, socialBox);
        vBox.getStyleClass().add("vbox");
        vBox.setFillWidth(true);
        HBox.setHgrow(vBox, Priority.ALWAYS);

        HBox hbox = new HBox(vBox, photoView);
        hbox.getStyleClass().add("hbox");
        hbox.visibleProperty().bind(itemProperty().isNotNull());

        setGraphic(hbox);
        setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
    }

    @Override
    protected void updateItem(Person person, boolean empty) {
        super.updateItem(person, empty);

        if (!empty && person != null) {
            nameLabel.setText(person.getName());
            descriptionLabel.setText(person.getDescription());
            championImageView.setVisible(person.isChampion());
            rockstarImageView.setVisible(person.isRockstar());
            photoView.photoProperty().bind(ImageManager.getInstance().personImageProperty(person));
            socialBox.getChildren().clear();

            if (StringUtils.isNotEmpty(person.getTwitter())) {
                Button twitter = new Button("Twitter");
                twitter.getStyleClass().addAll("social-button", "twitter");
                twitter.setOnAction(evt -> Util.browse("https://twitter.com/" + person.getTwitter()));
                twitter.setGraphic(new FontIcon(FontAwesomeBrands.TWITTER));
                socialBox.getChildren().add(twitter);
            }

            if (StringUtils.isNotEmpty(person.getLinkedIn())) {
                Button linkedIn = new Button("LinkedIn");
                linkedIn.getStyleClass().addAll("social-button", "linkedin");
                linkedIn.setOnAction(evt -> {
                    System.out.println("https://www.linkedin.com/in/" + person.getLinkedIn());
                    Util.browse("https://www.linkedin.com/in/" + person.getLinkedIn());
                });
                linkedIn.setGraphic(new FontIcon(FontAwesomeBrands.LINKEDIN));
                socialBox.getChildren().add(linkedIn);
            }

            if (StringUtils.isNotEmpty(person.getBlogId())) {
                Button blog = new Button("Blog");
                blog.getStyleClass().addAll("social-button", "blog");
                blog.setOnAction(evt -> Util.browse(""));
                blog.setGraphic(new FontIcon(FontAwesomeBrands.BLOGGER));
                socialBox.getChildren().add(blog);
            }

            if (StringUtils.isNotEmpty(person.getWebsite())) {
                Button website = new Button("Website");
                website.getStyleClass().addAll("social-button", "website");
                website.setOnAction(evt -> Util.browse(person.getWebsite()));
                website.setGraphic(new FontIcon(FontAwesomeBrands.SAFARI));
                socialBox.getChildren().add(website);
            }

            if (StringUtils.isNotEmpty(person.getEmail())) {
                Button website = new Button("Mail");
                website.getStyleClass().addAll("social-button", "mail");
                website.setOnAction(evt -> Util.browse("mailto:" + person.getEmail() + "?subject=JFXCentral%20Mail%20Contact"));
                website.setGraphic(new FontIcon(Material.MAIL));
                socialBox.getChildren().add(website);
            }

            if (StringUtils.isNotEmpty(person.getGitHub())) {
                Button github = new Button("GitHub");
                github.getStyleClass().addAll("social-button", "github");
                github.setOnAction(evt -> Util.browse("https://github.com/" + person.getGitHub()));
                github.setGraphic(new FontIcon(FontAwesomeBrands.GITHUB));
                socialBox.getChildren().add(github);
            }
        }
    }
}
