package com.dlsc.jfxcentral.views;

import com.dlsc.gemsfx.DialogPane;
import com.dlsc.jfxcentral.*;
import com.dlsc.jfxcentral.model.Blog;
import com.dlsc.jfxcentral.model.Company;
import com.dlsc.jfxcentral.model.Person;
import com.dlsc.jfxcentral.model.Post;
import com.dlsc.jfxcentral.panels.SectionPane;
import com.dlsc.jfxcentral.util.Util;
import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndImage;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import org.apache.commons.lang3.StringUtils;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public class BlogView extends PageView {

    private VBox content = new VBox();

    public BlogView(RootPane rootPane) {
        super(rootPane);

        getStyleClass().add("blog-view");

        VBox.setVgrow(content, Priority.ALWAYS);

        createTitleBox();
        createPostsBox();

        setContent(content);
    }

    private void createPostsBox() {
        SectionPane sectionPane = new SectionPane();
        sectionPane.setTitle("Posts");
        sectionPane.subtitleProperty().bind(Bindings.createStringBinding(() -> getBlog() != null ? "List of current posts on " + getBlog().getTitle() : "", blogProperty()));

        FilteredList<Post> filteredList = new FilteredList(DataRepository.getInstance().postsProperty());
        filteredList.predicateProperty().bind(Bindings.createObjectBinding(() -> post -> getBlog() == null || post.getBlog().equals(getBlog()), blogProperty()));

        SortedList<Post> sortedPosts = new SortedList<>(filteredList);
        sortedPosts.setComparator(Comparator.comparing(Post::getDate).reversed());
        AdvancedListView<Post> listView = new AdvancedListView<>();
        listView.setItems(filteredList);

        listView.setCellFactory(view -> new PostCell(getRootPane()));
        VBox.setVgrow(listView, Priority.ALWAYS);

        sectionPane.getNodes().add(listView);
        VBox.setVgrow(sectionPane, Priority.ALWAYS);

        content.getChildren().add(sectionPane);
    }

    private void createTitleBox() {
        PhotoView photoView = new PhotoView();
        photoView.setEditable(false);
        blog.addListener(it -> {
            Blog blog = getBlog();
            if (blog != null) {
                String companyId = blog.getCompanyId();
                if (StringUtils.isNotBlank(companyId)) {
                    Optional<Company> companyById = DataRepository.getInstance().getCompanyById(companyId);
                    if (companyById.isPresent()) {
                        photoView.photoProperty().bind(ImageManager.getInstance().companyImageProperty(companyById.get()));
                    }
                } else {
                    List<String> personIds = blog.getPersonIds();
                    if (!personIds.isEmpty()) {
                        Optional<Person> personById = DataRepository.getInstance().getPersonById(personIds.get(0));
                        if (personById.isPresent()) {
                            photoView.photoProperty().bind(ImageManager.getInstance().personImageProperty(personById.get()));
                        }
                    }
                }
            }
        });

        Label nameLabel = new Label();
        nameLabel.getStyleClass().addAll("header1", "name-label");
        nameLabel.setMaxWidth(Double.MAX_VALUE);
        nameLabel.textProperty().bind(Bindings.createStringBinding(() -> getBlog() != null ? getBlog().getTitle() : "", blogProperty()));
        HBox.setHgrow(nameLabel, Priority.ALWAYS);

        Label descriptionLabel = new Label();
        descriptionLabel.setWrapText(true);
        descriptionLabel.setMinHeight(Region.USE_PREF_SIZE);
        descriptionLabel.getStyleClass().add("description-label");
        descriptionLabel.textProperty().bind(Bindings.createStringBinding(() -> getBlog() != null ? getBlog().getSummary() : "", blogProperty()));
        HBox.setHgrow(descriptionLabel, Priority.ALWAYS);

        HBox linksBox = new HBox();
        linksBox.getStyleClass().add("social-box");

        VBox vBox = new VBox(nameLabel, descriptionLabel, linksBox);
        vBox.getStyleClass().add("vbox");
        vBox.setFillWidth(true);
        HBox.setHgrow(vBox, Priority.ALWAYS);

        HBox titleBox = new HBox(vBox, photoView);
        titleBox.getStyleClass().add("hbox");

        SectionPane sectionPane = new SectionPane(titleBox);
        sectionPane.getStyleClass().add("title-section");

        sectionPane.visibleProperty().bind(blogProperty().isNotNull());
        sectionPane.managedProperty().bind(blogProperty().isNotNull());

        content.getChildren().addAll(sectionPane);
    }

    private final ObjectProperty<Blog> blog = new SimpleObjectProperty<>(this, "blog");

    public Blog getBlog() {
        return blog.get();
    }

    public ObjectProperty<Blog> blogProperty() {
        return blog;
    }

    public void setBlog(Blog blog) {
        this.blog.set(blog);
    }

    private void showBlogDetails(Blog blog) {
        ImageView largeImageView = new ImageView();
        largeImageView.setFitWidth(800);
        largeImageView.setPreserveRatio(true);
        largeImageView.imageProperty().bind(ImageManager.getInstance().blogPageLargeImageProperty(blog));
        getRootPane().getDialogPane().showNode(DialogPane.Type.BLANK, "Title", largeImageView);
    }

    public class PostCell extends ListCell<Post> {

        private final RootPane rootPane;
        private Label titleLabel = new Label();
        private Label blogLabel = new Label();
        private Label ageLabel = new Label();

        private ImageView imageView = new ImageView();

        public PostCell(RootPane rootPane) {
            this.rootPane = rootPane;
            getStyleClass().add("post-cell");

            titleLabel.getStyleClass().add("title-label");
            titleLabel.setMaxWidth(Double.MAX_VALUE);

            blogLabel.getStyleClass().add("blog-label");
            blogLabel.visibleProperty().bind(blogProperty().isNull());
            blogLabel.managedProperty().bind(blogProperty().isNull());

            imageView.setFitHeight(24);
            imageView.setPreserveRatio(true);

            ageLabel.getStyleClass().add("age-label");
            ageLabel.setMinWidth(Region.USE_PREF_SIZE);

//        VBox vBox = new VBox(titleLabel, subtitleLabel);
//        vBox.getStyleClass().add("vbox");
//        HBox.setHgrow(vBox, Priority.ALWAYS);


            HBox hbox = new HBox(blogLabel, titleLabel, ageLabel);
            HBox.setHgrow(titleLabel, Priority.ALWAYS);
            hbox.getStyleClass().add("hbox");
            hbox.setMinWidth(0);
            hbox.setPrefWidth(0);

            setGraphic(hbox);
            setContentDisplay(ContentDisplay.GRAPHIC_ONLY);

            setOnMouseClicked(evt -> {
                if (evt.getClickCount() == 2 && evt.getButton() == MouseButton.PRIMARY) {
                    if (evt.isShiftDown()) {
                        Util.browse(getItem().getSyndEntry().getLink());
                    } else {
                        showItem();
                    }
                }
            });
        }

        private void showItem() {
            Util.browse(getItem().getSyndEntry().getLink());
        }

        @Override
        protected void updateItem(Post item, boolean empty) {
            super.updateItem(item, empty);

            if (!empty && item != null) {
                imageView.setImage(null);
                titleLabel.setText(item.getSyndEntry().getTitle());
                blogLabel.setText(item.getSyndFeed().getTitle());
                ageLabel.setText(getAge(item));

                SyndImage image = item.getSyndFeed().getImage();
                if (image != null) {
                    if (StringUtils.isNotBlank(image.getUrl())) {
                        try {
                            URL url = new URL(image.getUrl());
                            imageView.setImage(new Image(url.toExternalForm(), true));
                        } catch (MalformedURLException ex) {
                        }
                    }
                }
            } else {
                imageView.setImage(null);
                titleLabel.setText("");
                blogLabel.setText("");
                ageLabel.setText("");
            }
        }

        private String getAge(Post item) {
            SyndEntry syndEntry = item.getSyndEntry();
            Date date = syndEntry.getUpdatedDate();
            if (date == null) {
                date = syndEntry.getPublishedDate();
            }

            ZonedDateTime zonedDateTime = ZonedDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
            Duration between = Duration.between(zonedDateTime, ZonedDateTime.now());

            long days = between.toDays();
            if (days <= 0) {
                return between.toHours() + " hours";
            } else if (days > 0 && days < 100) {
                return days + (days > 1 ? " days" : "day");
            }

            return DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT).format(zonedDateTime);
        }
    }
}
