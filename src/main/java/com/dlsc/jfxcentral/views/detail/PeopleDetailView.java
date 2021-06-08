package com.dlsc.jfxcentral.views.detail;

import com.dlsc.jfxcentral.data.DataRepository;
import com.dlsc.jfxcentral.data.ImageManager;
import com.dlsc.jfxcentral.data.model.*;
import com.dlsc.jfxcentral.panels.SectionPane;
import com.dlsc.jfxcentral.util.PageUtil;
import com.dlsc.jfxcentral.util.Util;
import com.dlsc.jfxcentral.views.*;
import com.jpro.webapi.WebAPI;
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
import org.kordamp.ikonli.materialdesign.MaterialDesign;

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
        createBlogsBox();
        createBooksBox();
        createLibraryBox();
        createVideoBox();

        content.getChildren().forEach(node -> VBox.setVgrow(node, Priority.NEVER));

        setContent(content);

        selectedItemProperty().addListener(it -> updateView());
        updateView();
    }

    private void createBlogsBox() {
        AdvancedListView<Blog> listView = new AdvancedListView<>();
        listView.setPaging(true);
        listView.setVisibleRowCount(1000);
        listView.setCellFactory(view -> new PersonBlogCell());

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
        listView.setVisibleRowCount(1000);
        listView.setCellFactory(view -> new PersonBookCell());

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

    private void createVideoBox() {
        AdvancedListView<Video> listView = new AdvancedListView<>();
        listView.setPaging(true);
        listView.setVisibleRowCount(3);
        listView.setCellFactory(view -> {
            VideosDetailView.VideoCell videoCell = new VideosDetailView.VideoCell(getRootPane(), false);
            videoCell.setCoverImageWidth(160);
            return videoCell;
        });

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

    private void createLibraryBox() {
        AdvancedListView<Library> listView = new AdvancedListView<>();
        listView.setPaging(true);
        listView.setVisibleRowCount(3);
        listView.setCellFactory(view -> new LibraryCell(getRootPane()));

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

    class PersonBlogCell extends AdvancedListCell<Blog> {

        private final Button detailsButton;
        private final Button visitButton;

        private Label titleLabel = new Label();
        private Label descriptionLabel = new Label();

        private javafx.scene.image.ImageView pageImageView = new javafx.scene.image.ImageView();
        private HBox buttonBox = new HBox();

        public PersonBlogCell() {
            getStyleClass().add("blog-cell");

            titleLabel.getStyleClass().addAll("header3", "title-label");
            titleLabel.setMaxWidth(Double.MAX_VALUE);
            titleLabel.setWrapText(true);
            titleLabel.setMinHeight(Region.USE_PREF_SIZE);

            descriptionLabel.getStyleClass().add("description-label");
            descriptionLabel.setMaxWidth(Double.MAX_VALUE);
            descriptionLabel.setWrapText(true);
            descriptionLabel.setMinHeight(Region.USE_PREF_SIZE);

            buttonBox.getStyleClass().add("button-box");

            detailsButton = new Button("Details");
            detailsButton.getStyleClass().addAll("library-button", "details");
            detailsButton.setGraphic(new FontIcon(MaterialDesign.MDI_MORE));

            buttonBox.getChildren().add(detailsButton);

            visitButton = new Button("Visit");
            visitButton.getStyleClass().addAll("library-button", "visit");
            visitButton.setGraphic(new FontIcon(MaterialDesign.MDI_WEB));
            buttonBox.getChildren().add(visitButton);

            titleLabel.setContentDisplay(ContentDisplay.RIGHT);
            titleLabel.setGraphicTextGap(20);

            VBox vBox = new VBox(titleLabel, descriptionLabel, buttonBox);
            vBox.getStyleClass().add("vbox");
            HBox.setHgrow(vBox, Priority.ALWAYS);

            pageImageView.setFitWidth(100);
            pageImageView.setPreserveRatio(true);

            HBox hBox = new HBox(vBox, pageImageView);
            hBox.getStyleClass().add("hbox");

            setGraphic(hBox);
            setContentDisplay(ContentDisplay.GRAPHIC_ONLY);

            if (WebAPI.isBrowser()) {
                setMouseTransparent(true);
            }
        }

        @Override
        protected void updateItem(Blog item, boolean empty) {
            super.updateItem(item, empty);

            if (!empty && item != null) {
                pageImageView.imageProperty().bind(ImageManager.getInstance().blogPageImageProperty(item));
                pageImageView.setVisible(true);

                titleLabel.setText(item.getTitle());
                descriptionLabel.setText(StringUtils.abbreviate(item.getSummary(), 250));

                visitButton.setVisible(StringUtils.isNotEmpty(item.getUrl()));
                visitButton.setManaged(StringUtils.isNotEmpty(item.getUrl()));

                buttonBox.setVisible(visitButton.isVisible() || detailsButton.isVisible());
                buttonBox.setManaged(buttonBox.isVisible());

                Util.setLink(detailsButton, PageUtil.getLink(item), item.getSummary());
                Util.setLink(visitButton, item.getUrl(), item.getSummary());
            } else {
                pageImageView.imageProperty().unbind();
                pageImageView.setVisible(false);
                buttonBox.setVisible(false);
                buttonBox.setManaged(false);
            }
        }
    }

    class PersonBookCell extends AdvancedListCell<Book> {

        private final Button detailsButton;
        private final Button homepageButton;
        private final Button amazonButton;

        private Label titleLabel = new Label();
        private Label subtitleLabel = new Label();
        private Label authorsLabel = new Label();
        private MarkdownView descriptionLabel = new MarkdownView();

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

            buttonBox.getStyleClass().add("button-box");

            detailsButton = new Button("Details");
            detailsButton.getStyleClass().addAll("library-button", "details");
            detailsButton.setGraphic(new FontIcon(MaterialDesign.MDI_MORE));
            buttonBox.getChildren().add(detailsButton);

            homepageButton = new Button("Homepage");
            homepageButton.getStyleClass().addAll("library-button", "homepage");
            homepageButton.setGraphic(new FontIcon(MaterialDesign.MDI_WEB));
            buttonBox.getChildren().add(homepageButton);

            amazonButton = new Button("Amazon");
            amazonButton.getStyleClass().addAll("library-button", "amazon");
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
                coverImageView.imageProperty().bind(ImageManager.getInstance().bookCoverImageProperty(item));
                coverImageView.setVisible(true);

                titleLabel.setText(item.getTitle());
                subtitleLabel.setText(item.getSubtitle());
                authorsLabel.setText(item.getAuthors());

                descriptionLabel.mdStringProperty().bind(DataRepository.getInstance().bookTextProperty(item));

                homepageButton.setVisible(StringUtils.isNotEmpty(item.getUrl()));
                homepageButton.setManaged(StringUtils.isNotEmpty(item.getUrl()));

                amazonButton.setVisible(StringUtils.isNotEmpty(item.getAmazonASIN()));
                amazonButton.setManaged(StringUtils.isNotEmpty(item.getAmazonASIN()));

                buttonBox.setVisible(homepageButton.isVisible() || amazonButton.isVisible());
                buttonBox.setManaged(buttonBox.isVisible());

                Util.setLink(homepageButton, item.getUrl(), item.getTitle());
                Util.setLink(detailsButton, PageUtil.getLink(item), item.getTitle());
                Util.setLink(amazonButton, "http://www.amazon.com/dp/" + item.getAmazonASIN(), item.getTitle());

            } else {
                coverImageView.imageProperty().unbind();
                coverImageView.setVisible(false);
                buttonBox.setVisible(false);
                buttonBox.setManaged(false);
            }
        }
    }
}
