package com.dlsc.jfxcentral.views;

import com.dlsc.jfxcentral.AdvancedListView;
import com.dlsc.jfxcentral.DataRepository;
import com.dlsc.jfxcentral.PhotoCache;
import com.dlsc.jfxcentral.PhotoView;
import com.dlsc.jfxcentral.model.Library;
import com.dlsc.jfxcentral.model.Person;
import com.dlsc.jfxcentral.panels.SectionPane;
import com.dlsc.jfxcentral.util.Util;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import org.apache.commons.lang3.StringUtils;
import org.kordamp.ikonli.fontawesome5.FontAwesomeBrands;
import org.kordamp.ikonli.javafx.FontIcon;
import org.kordamp.ikonli.material.Material;

public class PersonView extends PageView {

    private HBox socialBox;
    private PhotoView photoView = new PhotoView();
    private Label nameLabel = new Label();
    private Label descriptionLabel = new Label();
    private ImageView championImageView = new ImageView();
    private ImageView rockstarImageView = new ImageView();

    public PersonView() {
        getStyleClass().add("person-view");

        createTitleBox();
        createLibraryBox();

        personProperty().addListener(it -> updateView());
        updateView();
    }

    private void createLibraryBox() {
        AdvancedListView<Library> listView = new AdvancedListView<>();
        listView.setPaging(true);
        listView.setVisibleRowCount(5);
        listView.setCellFactory(view -> new LibraryCell());

        SectionPane sectionPane = new SectionPane();
        sectionPane.setTitle("Libraries");
        sectionPane.getNodes().add(listView);

        personProperty().addListener(it -> {
            Person person = getPerson();
            if (person != null) {
                sectionPane.setSubtitle("Open source libraries developed by " + person.getName());
                listView.itemsProperty().bind(DataRepository.getInstance().getLibrariesByPerson(getPerson()));
            } else {
                sectionPane.setSubtitle("");
                listView.getItems().clear();
            }
        });

        getChildren().add(sectionPane);
    }

    private void createTitleBox() {
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
        titleBox.getStyleClass().add("hbox");

        SectionPane sectionPane = new SectionPane(titleBox);
        sectionPane.getStyleClass().add("title-section");
        getChildren().addAll(sectionPane);
    }

    private void updateView() {
        Person person = getPerson();
        if (person != null) {
            nameLabel.setText(person.getName());
            descriptionLabel.setText(person.getDescription());
            championImageView.setVisible(person.isChampion());
            rockstarImageView.setVisible(person.isRockstar());

            if (person.hasPhoto()) {
                photoView.photoProperty().bind(PhotoCache.getInstance().personImageProperty(person.getPhoto()));
                photoView.setVisible(true);
            } else {
                photoView.photoProperty().unbind();
                photoView.setVisible(false);
            }

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
                blog.setOnAction(evt ->Util.browse(""));
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
                website.setOnAction(evt -> Util.browse("mailto:"+ person.getEmail() + "?subject=JFXCentral%20Mail%20Contact"));
                website.setGraphic(new FontIcon(Material.MAIL));
                socialBox.getChildren().add(website);
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

    class LibraryCell extends ListCell<Library> {

        private Label titleLabel = new Label();
        private Label descriptionLabel = new Label();

        private ImageView logoImageView = new ImageView();

        public LibraryCell() {
            getStyleClass().add("library-cell");

            titleLabel.getStyleClass().add("title-label");
            titleLabel.setMaxWidth(Region.USE_PREF_SIZE);

            descriptionLabel.getStyleClass().add("description-label");
            descriptionLabel.setWrapText(true);
            descriptionLabel.setMaxWidth(Region.USE_PREF_SIZE);

            VBox vBox = new VBox(titleLabel, descriptionLabel);
            vBox.getStyleClass().add("vbox");
            HBox.setHgrow(vBox, Priority.ALWAYS);

            logoImageView.setFitHeight(64);
            logoImageView.setPreserveRatio(true);

            HBox hBox = new HBox(vBox, logoImageView);
            hBox.getStyleClass().add("hbox");

            setGraphic(hBox);
            setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
        }

        @Override
        protected void updateItem(Library item, boolean empty) {
            super.updateItem(item, empty);

            if (!empty && item != null) {
                String logoImageFile = item.getLogoImageFile();
                if (StringUtils.isNotEmpty(logoImageFile)) {
                    logoImageView.imageProperty().bind(PhotoCache.getInstance().libraryImageProperty(item.getLogoImageFile()));
                } else {
                    logoImageView.imageProperty().unbind();
                }
                logoImageView.setVisible(true);

                titleLabel.setText(item.getTitle());
                descriptionLabel.setText(item.getDescription());
            } else {
                logoImageView.imageProperty().unbind();
                logoImageView.setVisible(false);
            }
        }
    }
}