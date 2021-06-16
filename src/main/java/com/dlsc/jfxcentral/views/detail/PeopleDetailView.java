package com.dlsc.jfxcentral.views.detail;

import com.dlsc.jfxcentral.data.DataRepository;
import com.dlsc.jfxcentral.data.ImageManager;
import com.dlsc.jfxcentral.data.model.*;
import com.dlsc.jfxcentral.panels.SectionPane;
import com.dlsc.jfxcentral.util.Util;
import com.dlsc.jfxcentral.views.AdvancedListView;
import com.dlsc.jfxcentral.views.MarkdownView;
import com.dlsc.jfxcentral.views.PhotoView;
import com.dlsc.jfxcentral.views.RootPane;
import com.dlsc.jfxcentral.views.detail.cells.*;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import org.apache.commons.lang3.StringUtils;
import org.kordamp.ikonli.fontawesome5.FontAwesomeBrands;
import org.kordamp.ikonli.javafx.FontIcon;
import org.kordamp.ikonli.material.Material;

// TODO:dl too many listeners on selected item
public class PeopleDetailView extends DetailView<Person> {

    private FlowPane linksBox;
    private PhotoView photoView = new PhotoView();
    private Label nameLabel = new Label();
    private MarkdownView descriptionMarkdownView = new MarkdownView();
    private ImageView championImageView = new ImageView();
    private ImageView rockstarImageView = new ImageView();
    private VBox content = new VBox();

    public PeopleDetailView(RootPane rootPane) {
        super(rootPane);

        getStyleClass().add("people-detail-view");

        createTitleBox();
        createLibrariesBox();
        createToolsBox();
        createBlogsBox();
        createTutorialsBox();
        createDownloadsBox();
        createVideoBox();
        createBooksBox();

        content.getChildren().forEach(node -> VBox.setVgrow(node, Priority.NEVER));

        setContent(content);

        selectedItemProperty().addListener(it -> updateView());
        updateView();
    }

    private void createBlogsBox() {
        AdvancedListView<Blog> listView = new AdvancedListView<>();
        listView.setPaging(true);
        listView.setVisibleRowCount(3);
        listView.setCellFactory(view -> new DetailBlogCell());

        SectionPane sectionPane = new SectionPane();
        sectionPane.titleProperty().bind(Bindings.createStringBinding(() -> listView.getItems().size() > 1 ? "Blogs" : "Blog", listView.itemsProperty()));
        sectionPane.subtitleProperty().bind(Bindings.createStringBinding(() -> {
            Person person = getSelectedItem();
            if (person != null) {
                return "Published or co-authored by " + person.getName();
            }
            return "";
        }, selectedItemProperty()));

        sectionPane.getNodes().add(listView);

        selectedItemProperty().addListener(it -> {
            Person person = getSelectedItem();
            if (person != null) {
                listView.setItems(DataRepository.getInstance().getBlogsByPerson(getSelectedItem()));
            } else {
                listView.setItems(FXCollections.observableArrayList());
            }
        });

        sectionPane.visibleProperty().bind(listView.itemsProperty().emptyProperty().not());
        sectionPane.managedProperty().bind(listView.itemsProperty().emptyProperty().not());

        content.getChildren().add(sectionPane);
    }

    private void createBooksBox() {
        AdvancedListView<Book> listView = new AdvancedListView<>();
        listView.setPaging(true);
        listView.setVisibleRowCount(3);
        listView.setCellFactory(view -> new DetailBookCell(false));

        SectionPane sectionPane = new SectionPane();
        sectionPane.setTitle("Books");
        sectionPane.subtitleProperty().bind(Bindings.createStringBinding(() -> {
            Person person = getSelectedItem();
            if (person != null) {
                return "Written or co-authored by " + person.getName();
            }
            return "";
        }, selectedItemProperty()));

        sectionPane.getNodes().add(listView);

        selectedItemProperty().addListener(it -> {
            Person person = getSelectedItem();
            if (person != null) {
                listView.setItems(DataRepository.getInstance().getBooksByPerson(getSelectedItem()));
            } else {
                listView.setItems(FXCollections.observableArrayList());
            }
        });

        sectionPane.visibleProperty().bind(listView.itemsProperty().emptyProperty().not());
        sectionPane.managedProperty().bind(listView.itemsProperty().emptyProperty().not());

        content.getChildren().add(sectionPane);
    }

    private void createTutorialsBox() {
        AdvancedListView<Tutorial> listView = new AdvancedListView<>();
        listView.setPaging(true);
        listView.setVisibleRowCount(3);
        listView.setCellFactory(view -> new DetailTutorialCell(getRootPane(), false));

        SectionPane sectionPane = new SectionPane();
        sectionPane.setTitle("Tutorials");
        sectionPane.subtitleProperty().bind(Bindings.createStringBinding(() -> {
            Person person = getSelectedItem();
            if (person != null) {
                return "Published by " + person.getName();
            }
            return "";
        }, selectedItemProperty()));

        sectionPane.getNodes().add(listView);

        selectedItemProperty().addListener(it -> {
            Person person = getSelectedItem();
            if (person != null) {
                listView.setItems(DataRepository.getInstance().getTutorialsByPerson(getSelectedItem()));
            } else {
                listView.setItems(FXCollections.observableArrayList());
            }
        });

        sectionPane.visibleProperty().bind(listView.itemsProperty().emptyProperty().not());
        sectionPane.managedProperty().bind(listView.itemsProperty().emptyProperty().not());

        content.getChildren().add(sectionPane);
    }

    private void createDownloadsBox() {
        AdvancedListView<Download> listView = new AdvancedListView<>();
        listView.setPaging(true);
        listView.setVisibleRowCount(3);
        listView.setCellFactory(view -> new DetailDownloadCell(false));

        SectionPane sectionPane = new SectionPane();
        sectionPane.setTitle("Downloads");
        sectionPane.subtitleProperty().bind(Bindings.createStringBinding(() -> {
            Person person = getSelectedItem();
            if (person != null) {
                return "Published by " + person.getName();
            }
            return "";
        }, selectedItemProperty()));

        sectionPane.getNodes().add(listView);

        selectedItemProperty().addListener(it -> {
            Person person = getSelectedItem();
            if (person != null) {
                listView.setItems(DataRepository.getInstance().getDownloadsByPerson(getSelectedItem()));
            } else {
                listView.setItems(FXCollections.observableArrayList());
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
        listView.setCellFactory(view -> new DetailVideoCell(getRootPane(), false));

        SectionPane sectionPane = new SectionPane();
        sectionPane.setTitle("Videos");
        sectionPane.getNodes().add(listView);

        selectedItemProperty().addListener(it -> {
            Person person = getSelectedItem();
            if (person != null) {
                sectionPane.setSubtitle("Sessions presented by " + person.getName());
                listView.setItems(DataRepository.getInstance().getVideosByPerson(getSelectedItem()));
            } else {
                sectionPane.setSubtitle("");
                listView.setItems(FXCollections.observableArrayList());
            }
        });

        sectionPane.visibleProperty().bind(listView.itemsProperty().emptyProperty().not());
        sectionPane.managedProperty().bind(listView.itemsProperty().emptyProperty().not());

        content.getChildren().add(sectionPane);
    }

    private void createLibrariesBox() {
        AdvancedListView<Library> listView = new AdvancedListView<>();
        listView.setPaging(true);
        listView.setVisibleRowCount(3);
        listView.setCellFactory(view -> new DetailLibraryCell(getRootPane()));

        SectionPane sectionPane = new SectionPane();
        sectionPane.setTitle("Libraries");
        sectionPane.getNodes().add(listView);

        selectedItemProperty().addListener(it -> {
            Person person = getSelectedItem();
            if (person != null) {
                sectionPane.setSubtitle("Libraries developed by " + person.getName());
                listView.setItems(DataRepository.getInstance().getLibrariesByPerson(getSelectedItem()));
            } else {
                sectionPane.setSubtitle("");
                listView.setItems(FXCollections.observableArrayList());
            }
        });

        sectionPane.visibleProperty().bind(listView.itemsProperty().emptyProperty().not());
        sectionPane.managedProperty().bind(listView.itemsProperty().emptyProperty().not());

        content.getChildren().add(sectionPane);
    }

    private void createToolsBox() {
        AdvancedListView<Tool> listView = new AdvancedListView<>();
        listView.setPaging(true);
        listView.setVisibleRowCount(3);
        listView.setCellFactory(view -> new DetailToolCell(getRootPane()));

        SectionPane sectionPane = new SectionPane();
        sectionPane.setTitle("Tools");
        sectionPane.getNodes().add(listView);

        selectedItemProperty().addListener(it -> {
            Person person = getSelectedItem();
            if (person != null) {
                sectionPane.setSubtitle("Tools developed by " + person.getName());
                listView.setItems(DataRepository.getInstance().getToolsByPerson(getSelectedItem()));
            } else {
                sectionPane.setSubtitle("");
                listView.setItems(FXCollections.observableArrayList());
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

        descriptionMarkdownView.setMinHeight(Region.USE_PREF_SIZE);
        descriptionMarkdownView.getStyleClass().add("description-label");
        HBox.setHgrow(descriptionMarkdownView, Priority.ALWAYS);

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

        linksBox = new FlowPane();
        linksBox.getStyleClass().add("links-box");

        VBox vBox = new VBox(nameLabel, descriptionMarkdownView, badgesBox, linksBox);
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
        Person person = getSelectedItem();
        if (person != null) {

            nameLabel.setText(person.getName());
            descriptionMarkdownView.mdStringProperty().bind(DataRepository.getInstance().personDescriptionProperty(person));
            championImageView.setVisible(person.isChampion());
            rockstarImageView.setVisible(person.isRockstar());
            photoView.photoProperty().bind(ImageManager.getInstance().personImageProperty(person));
            linksBox.getChildren().clear();

            if (StringUtils.isNotEmpty(person.getTwitter())) {
                Button twitter = new Button("Twitter");
                twitter.getStyleClass().addAll("social-button", "twitter");
                Util.setLink(twitter, "https://twitter.com/" + person.getTwitter(), person.getName());
                twitter.setGraphic(new FontIcon(FontAwesomeBrands.TWITTER));
                linksBox.getChildren().add(twitter);
            }

            if (StringUtils.isNotEmpty(person.getLinkedIn())) {
                Button linkedIn = new Button("LinkedIn");
                linkedIn.getStyleClass().addAll("social-button", "linkedin");
                Util.setLink(linkedIn, "https://www.linkedin.com/in/" + person.getLinkedIn(), person.getName());
                linkedIn.setGraphic(new FontIcon(FontAwesomeBrands.LINKEDIN));
                linksBox.getChildren().add(linkedIn);
            }

            if (StringUtils.isNotEmpty(person.getBlogId())) {
                Button blog = new Button("Blog");
                blog.getStyleClass().addAll("social-button", "blog");
                Util.setLink(blog, "", "");
                blog.setGraphic(new FontIcon(FontAwesomeBrands.BLOGGER));
                linksBox.getChildren().add(blog);
            }

            if (StringUtils.isNotEmpty(person.getWebsite())) {
                Button website = new Button("Website");
                website.getStyleClass().addAll("social-button", "website");
                Util.setLink(website, person.getWebsite(), person.getName());
                website.setGraphic(new FontIcon(FontAwesomeBrands.SAFARI));
                linksBox.getChildren().add(website);
            }

            if (StringUtils.isNotEmpty(person.getEmail())) {
                Button website = new Button("Mail");
                website.getStyleClass().addAll("social-button", "mail");
                Util.setLink(website, "mailto:" + person.getEmail() + "?subject=JFXCentral%20Mail%20Contact", person.getName());
                website.setGraphic(new FontIcon(Material.MAIL));
                linksBox.getChildren().add(website);
            }

            if (StringUtils.isNotEmpty(person.getGitHub())) {
                Button github = new Button("GitHub");
                github.getStyleClass().addAll("social-button", "github");
                Util.setLink(github, "https://github.com/" + person.getGitHub(), person.getName());
                github.setGraphic(new FontIcon(FontAwesomeBrands.GITHUB));
                linksBox.getChildren().add(github);
            }
        }
    }
}
