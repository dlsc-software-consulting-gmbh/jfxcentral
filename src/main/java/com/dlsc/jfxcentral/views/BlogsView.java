package com.dlsc.jfxcentral.views;

import com.dlsc.gemsfx.DialogPane;
import com.dlsc.jfxcentral.AdvancedListView;
import com.dlsc.jfxcentral.DataRepository;
import com.dlsc.jfxcentral.ImageManager;
import com.dlsc.jfxcentral.RootPane;
import com.dlsc.jfxcentral.model.Blog;
import com.dlsc.jfxcentral.model.Post;
import com.dlsc.jfxcentral.panels.PrettyScrollPane;
import com.dlsc.jfxcentral.panels.SectionPaneWithTabs;
import com.dlsc.jfxcentral.panels.Tab;
import com.dlsc.jfxcentral.util.Util;
import javafx.beans.Observable;
import javafx.collections.transformation.SortedList;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;

import java.util.Comparator;

public class BlogsView extends PageView {

    private final TilePane gridPane = new TilePane();

    public BlogsView(RootPane rootPane) {
        super(rootPane);

        getStyleClass().add("blogs-view");

        SectionPaneWithTabs sectionPaneWithTabs = new SectionPaneWithTabs();

        gridPane.getStyleClass().add("grid-pane");
        gridPane.setPrefColumns(3);
        gridPane.setTileAlignment(Pos.CENTER);
        gridPane.setAlignment(Pos.TOP_CENTER);

        SortedList<Post> sortedPosts = new SortedList<>(DataRepository.getInstance().postsProperty());
        sortedPosts.setComparator(Comparator.comparing(Post::getDate).reversed());
        AdvancedListView<Post> postsListView = new AdvancedListView<>();
        postsListView.setItems(sortedPosts);

        postsListView.setCellFactory(view -> new PostCell(rootPane));
        VBox.setVgrow(postsListView, Priority.ALWAYS);

        PrettyScrollPane prettyScrollPane = new PrettyScrollPane(gridPane);
        prettyScrollPane.setShowScrollToTopButton(false);
        prettyScrollPane.setShowScrollToTopButton(true);

        Tab blogsTab = new Tab("Blogs");
        blogsTab.setContent(prettyScrollPane);

        Tab postsTab = new Tab("Posts");
        postsTab.setContent(postsListView);

        sectionPaneWithTabs.getTabs().setAll(postsTab, blogsTab);

        setContent(sectionPaneWithTabs);

        DataRepository.getInstance().getBlogs().addListener((Observable it) -> updateView());
        updateView();
    }

    private void updateView() {
        gridPane.getChildren().clear();

        for (Blog blog : DataRepository.getInstance().getBlogs()) {
            BlogCell blogCell = new BlogCell(blog);
            gridPane.getChildren().add(blogCell);
        }
    }

    private class BlogCell extends VBox {

        private ImageView imageView = new ImageView();
        private Label label = new Label();

        public BlogCell(Blog blog) {
            getStyleClass().add("blog-cell");

            imageView.setFitWidth(300);
            imageView.setFitHeight(300);
            imageView.setPreserveRatio(true);

            label.getStyleClass().add("summary-label");
            label.setWrapText(true);
            label.setMinHeight(Region.USE_PREF_SIZE);
            label.setAlignment(Pos.CENTER);
            label.setTextAlignment(TextAlignment.CENTER);
            label.setPrefWidth(300);

            Region spacer = new Region();
            VBox.setVgrow(spacer, Priority.ALWAYS);

            label.setText(blog.getSummary());
            imageView.imageProperty().bind(ImageManager.getInstance().blogPageImageProperty(blog));

            Button visitButton = new Button("Visit Blog");
            visitButton.setOnAction(evt -> Util.browse(blog.getUrl()));

            getChildren().setAll(imageView, label, spacer, visitButton);

            imageView.setOnMouseClicked(evt -> showBlogDetails(blog));
            label.setOnMouseClicked(evt -> showBlogDetails(blog));
        }

        private void showBlogDetails(Blog blog) {
            ImageView largeImageView = new ImageView();
            largeImageView.setFitWidth(800);
            largeImageView.setPreserveRatio(true);
            largeImageView.imageProperty().bind(ImageManager.getInstance().blogPageLargeImageProperty(blog));
            getRootPane().getDialogPane().showNode(DialogPane.Type.BLANK, "Title", largeImageView);
        }
    }
}
