package com.dlsc.jfxcentral.views;

import com.dlsc.gemsfx.DialogPane;
import com.dlsc.jfxcentral.AdvancedListView;
import com.dlsc.jfxcentral.DataRepository;
import com.dlsc.jfxcentral.ImageManager;
import com.dlsc.jfxcentral.RootPane;
import com.dlsc.jfxcentral.model.Blog;
import com.dlsc.jfxcentral.model.Post;
import com.dlsc.jfxcentral.panels.SectionPane;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import java.util.Comparator;

public class BlogView extends PageView {

    public BlogView(RootPane rootPane) {
        super(rootPane);

        getStyleClass().add("blog-view");

        SectionPane sectionPane = new SectionPane();
        sectionPane.setTitle("Posts");
        sectionPane.subtitleProperty().bind(Bindings.createStringBinding(() -> getBlog() != null ? "List of current posts on " + getBlog().getTitle() : "", blogProperty()));

        FilteredList<Post> filteredList = new FilteredList(DataRepository.getInstance().postsProperty());
        filteredList.predicateProperty().bind(Bindings.createObjectBinding(() -> post -> getBlog() == null || post.getBlog().equals(getBlog()), blogProperty()));

        SortedList<Post> sortedPosts = new SortedList<>(filteredList);
        sortedPosts.setComparator(Comparator.comparing(Post::getDate).reversed());
        AdvancedListView<Post> listView = new AdvancedListView<>();
        listView.setItems(filteredList);

        listView.setCellFactory(view -> new PostCell(rootPane));
        VBox.setVgrow(listView, Priority.ALWAYS);

        sectionPane.getNodes().add(listView);

        setContent(sectionPane);
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
}
