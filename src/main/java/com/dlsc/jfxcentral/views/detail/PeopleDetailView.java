package com.dlsc.jfxcentral.views.detail;

import com.dlsc.jfxcentral.data.DataRepository;
import com.dlsc.jfxcentral.data.ImageManager;
import com.dlsc.jfxcentral.data.model.*;
import com.dlsc.jfxcentral.panels.SectionPane;
import com.dlsc.jfxcentral.util.EmptySelectionModel;
import com.dlsc.jfxcentral.util.Util;
import com.dlsc.jfxcentral.views.AdvancedListView;
import com.dlsc.jfxcentral.views.RootPane;
import com.dlsc.jfxcentral.views.View;
import com.dlsc.jfxcentral.views.detail.cells.*;
import com.dlsc.jfxcentral.views.detail.cells.ResponsiveBox.ImageLocation;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import org.apache.commons.lang3.StringUtils;
import org.kordamp.ikonli.fontawesome5.FontAwesomeBrands;
import org.kordamp.ikonli.javafx.FontIcon;
import org.kordamp.ikonli.material.Material;

public class PeopleDetailView extends DetailView<Person> {

    private VBox content = new VBox();
    private ImageView championImageView = new ImageView();
    private ImageView rockstarImageView = new ImageView();
    private ResponsiveBoxWithPhotoView responsiveBox;

    public PeopleDetailView(RootPane rootPane) {
        super(rootPane, View.PEOPLE);

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

    protected boolean isUsingMasterView() {
        return true;
    }

    private void createBlogsBox() {
        AdvancedListView<Blog> listView = new AdvancedListView<>();
        listView.setPaging(true);
        listView.getListView().setSelectionModel(new EmptySelectionModel<>());
        listView.setVisibleRowCount(3);
        listView.setCellFactory(view -> new DetailBlogCell(getRootPane(), false));

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
                Bindings.bindContent(listView.getItems(), DataRepository.getInstance().getBlogsByModelObject(getSelectedItem()));
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
        listView.getListView().setSelectionModel(new EmptySelectionModel<>());
        listView.setPaging(true);
        listView.setVisibleRowCount(3);
        listView.setCellFactory(view -> new DetailBookCell(getRootPane(), false));

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
                Bindings.bindContent(listView.getItems(), DataRepository.getInstance().getBooksByModelObject(getSelectedItem()));
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
        listView.getListView().setSelectionModel(new EmptySelectionModel<>());
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
                Bindings.bindContent(listView.getItems(), DataRepository.getInstance().getTutorialsByModelObject(getSelectedItem()));
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
        listView.getListView().setSelectionModel(new EmptySelectionModel<>());
        listView.setPaging(true);
        listView.setVisibleRowCount(3);
        listView.setCellFactory(view -> new DetailDownloadCell(getRootPane(), false));

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
                Bindings.bindContent(listView.getItems(), DataRepository.getInstance().getDownloadsByModelObject(getSelectedItem()));
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
        listView.getListView().setSelectionModel(new EmptySelectionModel<>());
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
                Bindings.bindContent(listView.getItems(), DataRepository.getInstance().getVideosByModelObject(getSelectedItem()));
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
        listView.getListView().setSelectionModel(new EmptySelectionModel<>());
        listView.setPaging(true);
        listView.setVisibleRowCount(3);
        listView.setCellFactory(view -> new DetailLibraryCell(getRootPane(), false));

        SectionPane sectionPane = new SectionPane();
        sectionPane.setTitle("Libraries");
        sectionPane.getNodes().add(listView);

        selectedItemProperty().addListener(it -> {
            Person person = getSelectedItem();
            if (person != null) {
                sectionPane.setSubtitle("Libraries developed by " + person.getName());
                Bindings.bindContent(listView.getItems(), DataRepository.getInstance().getLibrariesByModelObject(getSelectedItem()));
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
        listView.getListView().setSelectionModel(new EmptySelectionModel<>());
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
                Bindings.bindContent(listView.getItems(), DataRepository.getInstance().getToolsByModelObject(getSelectedItem()));
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

    private void updateView() {
        Person person = getSelectedItem();
        if (person != null) {

            championImageView.setVisible(person.isChampion());
            rockstarImageView.setVisible(person.isRockstar());

            responsiveBox.setTitle(person.getName());
            responsiveBox.descriptionProperty().bind(DataRepository.getInstance().personDescriptionProperty(person));
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
                Button blog = new Button("Blog");
                blog.getStyleClass().addAll("social-button", "blog");
                Util.setLink(blog, "", "");
                blog.setGraphic(new FontIcon(FontAwesomeBrands.BLOGGER));
                responsiveBox.getButtons().add(blog);
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
