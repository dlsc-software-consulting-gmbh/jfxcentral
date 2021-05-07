package com.dlsc.jfxcentral.views;

import com.dlsc.jfxcentral.*;
import com.dlsc.jfxcentral.model.Book;
import com.dlsc.jfxcentral.model.Library;
import com.dlsc.jfxcentral.model.Person;
import com.dlsc.jfxcentral.model.Video;
import com.dlsc.jfxcentral.panels.SectionPane;
import com.dlsc.jfxcentral.util.Util;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
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
import org.kordamp.ikonli.materialdesign.MaterialDesign;

public class PersonView extends PageView {

    private HBox socialBox;
    private PhotoView photoView = new PhotoView();
    private Label nameLabel = new Label();
    private Label descriptionLabel = new Label();
    private javafx.scene.image.ImageView championImageView = new javafx.scene.image.ImageView();
    private javafx.scene.image.ImageView rockstarImageView = new javafx.scene.image.ImageView();

    private VBox content = new VBox();

    public PersonView(RootPane rootPane) {
        super(rootPane);

        getStyleClass().add("person-view");

        createTitleBox();
        createBooksBox();
        createLibraryBox();
        createVideoBox();

        setContent(content);

        personProperty().addListener(it -> updateView());
        updateView();
    }

    private void createBooksBox() {
        AdvancedListView<Book> listView = new AdvancedListView<>();
        listView.setPaging(true);
        listView.setVisibleRowCount(1000);
        listView.setCellFactory(view -> new PersonBookCell());

        SectionPane sectionPane = new SectionPane();
        sectionPane.setTitle("Books");
        sectionPane.subtitleProperty().bind(Bindings.createStringBinding(() -> "Written or co-authored by " + getPerson().getName(), person));
        sectionPane.getNodes().add(listView);

        personProperty().addListener(it -> {
            Person person = getPerson();
            if (person != null) {
                listView.itemsProperty().bind(DataRepository.getInstance().getBooksByPerson(getPerson()));
            } else {
                listView.getItems().clear();
            }
        });

        sectionPane.visibleProperty().bind(listView.itemsProperty().emptyProperty().not());
        sectionPane.managedProperty().bind(listView.itemsProperty().emptyProperty().not());

        content.getChildren().add(sectionPane);
    }

    private void createVideoBox() {
        AdvancedListView<Video> listView = new AdvancedListView<>();
        listView.setPaging(true);
        listView.setVisibleRowCount(3);
        listView.setCellFactory(view -> {
            VideosView.VideoCell videoCell = new VideosView.VideoCell(getRootPane(), false);
            videoCell.setCoverImageWidth(160);
            return videoCell;
        });

        SectionPane sectionPane = new SectionPane();
        sectionPane.setTitle("Videos");
        sectionPane.getNodes().add(listView);

        personProperty().addListener(it -> {
            Person person = getPerson();
            if (person != null) {
                sectionPane.setSubtitle("Sessions presented by " + person.getName());
                listView.itemsProperty().bind(DataRepository.getInstance().getVideosByPerson(getPerson()));
            } else {
                sectionPane.setSubtitle("");
                listView.getItems().clear();
            }
        });

        sectionPane.visibleProperty().bind(listView.itemsProperty().emptyProperty().not());
        sectionPane.managedProperty().bind(listView.itemsProperty().emptyProperty().not());

        content.getChildren().add(sectionPane);
    }

    private void createLibraryBox() {
        AdvancedListView<Library> listView = new AdvancedListView<>();
        listView.setPaging(true);
        listView.setVisibleRowCount(3);
        listView.setCellFactory(view -> new LibraryCell(getRootPane()));

        SectionPane sectionPane = new SectionPane();
        sectionPane.setTitle("Libraries");
        sectionPane.getNodes().add(listView);

        personProperty().addListener(it -> {
            Person person = getPerson();
            if (person != null) {
                sectionPane.setSubtitle("Libraries developed by " + person.getName());
                listView.itemsProperty().bind(DataRepository.getInstance().getLibrariesByPerson(getPerson()));
            } else {
                sectionPane.setSubtitle("");
                listView.getItems().clear();
            }
        });

        sectionPane.visibleProperty().bind(listView.itemsProperty().emptyProperty().not());
        sectionPane.managedProperty().bind(listView.itemsProperty().emptyProperty().not());

        content.getChildren().add(sectionPane);
    }

    private void createTitleBox() {
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

        HBox titleBox = new HBox(vBox, photoView);
        titleBox.getStyleClass().add("hbox");

        SectionPane sectionPane = new SectionPane(titleBox);
        sectionPane.getStyleClass().add("title-section");
        content.getChildren().addAll(sectionPane);
    }

    private void updateView() {
        Person person = getPerson();
        if (person != null) {
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

    class PersonBookCell extends AdvancedListCell<Book> {

        private final Button detailsButton;
        private final Button homepageButton;
        private final Button amazonButton;

        private Label titleLabel = new Label();
        private Label subtitleLabel = new Label();
        private Label authorsLabel = new Label();
        private Label descriptionLabel = new Label();

        private javafx.scene.image.ImageView coverImageView = new javafx.scene.image.ImageView();
        private HBox buttonBox = new HBox();

        public PersonBookCell() {
            getStyleClass().add("book-cell");

            titleLabel.getStyleClass().addAll("header3", "title-label");
            titleLabel.setMaxWidth(Double.MAX_VALUE);
            titleLabel.setWrapText(true);
            titleLabel.setMinHeight(Region.USE_PREF_SIZE);

            subtitleLabel.getStyleClass().add("subtitle-label");
            subtitleLabel.setMaxWidth(Double.MAX_VALUE);
            subtitleLabel.setWrapText(true);
            subtitleLabel.setMinHeight(Region.USE_PREF_SIZE);

            authorsLabel.getStyleClass().add("authors-label");
            authorsLabel.setMaxWidth(Double.MAX_VALUE);
            authorsLabel.setWrapText(true);
            authorsLabel.setMinHeight(Region.USE_PREF_SIZE);

            descriptionLabel.getStyleClass().add("description-label");
            descriptionLabel.setMaxWidth(Double.MAX_VALUE);
            descriptionLabel.setWrapText(true);
            descriptionLabel.setMinHeight(Region.USE_PREF_SIZE);

            buttonBox.getStyleClass().add("button-box");

            detailsButton = new Button("Details");
            detailsButton.getStyleClass().addAll("library-button", "details");
            detailsButton.setGraphic(new FontIcon(MaterialDesign.MDI_MORE));
            detailsButton.setOnAction(evt -> getRootPane().open(getItem()));
            buttonBox.getChildren().add(detailsButton);

            homepageButton = new Button("Homepage");
            homepageButton.getStyleClass().addAll("library-button", "homepage");
            homepageButton.setOnAction(evt -> Util.browse(getItem().getUrl()));
            homepageButton.setGraphic(new FontIcon(MaterialDesign.MDI_WEB));
            buttonBox.getChildren().add(homepageButton);

            amazonButton = new Button("Amazon");
            amazonButton.getStyleClass().addAll("library-button", "amazon");
            amazonButton.setOnAction(evt -> Util.browse(getItem().getAmazon()));
            amazonButton.setGraphic(new FontIcon(FontAwesomeBrands.AMAZON));
            buttonBox.getChildren().add(amazonButton);

            titleLabel.setContentDisplay(ContentDisplay.RIGHT);
            titleLabel.setGraphicTextGap(20);

            VBox vBox = new VBox(titleLabel, subtitleLabel, authorsLabel, descriptionLabel, buttonBox);
            vBox.getStyleClass().add("vbox");
            HBox.setHgrow(vBox, Priority.ALWAYS);

            coverImageView.setFitWidth(100);
            coverImageView.setPreserveRatio(true);

            HBox hBox = new HBox(vBox, coverImageView);
            hBox.getStyleClass().add("hbox");

            setGraphic(hBox);
            setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
        }

        @Override
        protected void updateItem(Book item, boolean empty) {
            super.updateItem(item, empty);

            if (!empty && item != null) {
                String coverImage = item.getImage();
                if (StringUtils.isNotEmpty(coverImage)) {
                    coverImageView.imageProperty().bind(ImageManager.getInstance().bookCoverImageProperty(item));
                } else {
                    coverImageView.imageProperty().unbind();
                }
                coverImageView.setVisible(true);

                titleLabel.setText(item.getTitle());
                subtitleLabel.setText(item.getSubtitle());
                authorsLabel.setText(item.getAuthors());

                descriptionLabel.setText(StringUtils.abbreviate(item.getDescription(), 250));

                homepageButton.setVisible(StringUtils.isNotEmpty(item.getUrl()));
                homepageButton.setManaged(StringUtils.isNotEmpty(item.getUrl()));

                amazonButton.setVisible(StringUtils.isNotEmpty(item.getAmazon()));
                amazonButton.setManaged(StringUtils.isNotEmpty(item.getAmazon()));

                buttonBox.setVisible(homepageButton.isVisible() || amazonButton.isVisible());
                buttonBox.setManaged(buttonBox.isVisible());
            } else {
                coverImageView.imageProperty().unbind();
                coverImageView.setVisible(false);
                buttonBox.setVisible(false);
                buttonBox.setManaged(false);
            }
        }
    }
}
