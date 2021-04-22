package com.dlsc.jfxcentral.views;

import com.dlsc.gemsfx.PhotoView;
import com.dlsc.jfxcentral.PhotoCache;
import com.dlsc.jfxcentral.model.Person;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import org.apache.commons.lang3.StringUtils;
import org.kordamp.ikonli.fontawesome5.FontAwesomeBrands;
import org.kordamp.ikonli.javafx.FontIcon;

public class PersonView extends VBox {

    private HBox socialBox;
    private PhotoView photoView = new PhotoView();
    private Label nameLabel = new Label();
    private Label descriptionLabel = new Label();
    private ImageView championImageView = new ImageView();
    private ImageView rockstarImageView = new ImageView();

    public PersonView() {
        getStyleClass().add("person-view");

        photoView.setEditable(false);

        nameLabel.getStyleClass().add("name-label");
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
        HBox titleBox = new HBox(vBox, photoView);

        titleBox.getStyleClass().add("title-box");

        getChildren().add(titleBox);

        personProperty().addListener(it -> updateView());
    }

    private void updateView() {
        Person person = getPerson();
        if (person != null) {
            nameLabel.setText(person.getName());
            descriptionLabel.setText(person.getDescription());
            championImageView.setVisible(person.isChampion());
            rockstarImageView.setVisible(person.isRockstar());

            if (person.hasPhoto()) {
                photoView.photoProperty().bind(PhotoCache.getInstance().imageProperty(person.getPhoto()));
                photoView.setVisible(true);
            } else {
                photoView.photoProperty().unbind();
                photoView.setVisible(false);
            }

            socialBox.getChildren().clear();

            if (StringUtils.isNotEmpty(person.getTwitter())) {
                Button twitter = new Button("Twitter");
                twitter.getStyleClass().addAll("social-button", "twitter");
                twitter.setOnAction(evt -> System.out.println("twitter"));
                twitter.setGraphic(new FontIcon(FontAwesomeBrands.TWITTER));
                socialBox.getChildren().add(twitter);
            }

            if (StringUtils.isNotEmpty(person.getLinkedIn())) {
                Button twitter = new Button("LinkedIn");
                twitter.getStyleClass().addAll("social-button", "linkedin");
                twitter.setOnAction(evt -> System.out.println("linkedin"));
                twitter.setGraphic(new FontIcon(FontAwesomeBrands.LINKEDIN));
                socialBox.getChildren().add(twitter);
            }

            if (StringUtils.isNotEmpty(person.getBlogId())) {
                Button twitter = new Button("Blog");
                twitter.getStyleClass().addAll("social-button", "blog");
                twitter.setOnAction(evt -> System.out.println("blog"));
                twitter.setGraphic(new FontIcon(FontAwesomeBrands.BLOGGER));
                socialBox.getChildren().add(twitter);
            }

            if (StringUtils.isNotEmpty(person.getWebsite())) {
                Button twitter = new Button("Website");
                twitter.getStyleClass().addAll("social-button", "website");
                twitter.setOnAction(evt -> System.out.println("website"));
                twitter.setGraphic(new FontIcon(FontAwesomeBrands.SAFARI));
                socialBox.getChildren().add(twitter);
            }

        }
    }

    private final ObjectProperty<Person> person = new SimpleObjectProperty<>(this, "person");

    public Person getPerson() {
        return person.get();
    }

    public ObjectProperty<Person> personProperty() {
        return person;
    }

    public void setPerson(Person person) {
        this.person.set(person);
    }
}
