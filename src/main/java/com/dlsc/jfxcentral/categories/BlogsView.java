package com.dlsc.jfxcentral.categories;

import com.dlsc.jfxcentral.DataRepository;
import com.dlsc.jfxcentral.ImageManager;
import com.dlsc.jfxcentral.RootPane;
import com.dlsc.jfxcentral.View;
import com.dlsc.jfxcentral.model.Blog;
import com.dlsc.jfxcentral.views.AdvancedListCell;
import com.dlsc.jfxcentral.views.BlogView;
import javafx.beans.Observable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import org.apache.commons.lang3.StringUtils;

import java.util.Comparator;

public class BlogsView extends CategoryView<Blog> {

    private BlogView blogView;

    private ListView<Blog> listView = new ListView<>();

    @Override
    public View getView() {
        return View.BLOGS;
    }

    public BlogsView(RootPane rootPane) {
        super(rootPane);

        getStyleClass().add("blogs-view");

        listView.setMinWidth(Region.USE_PREF_SIZE);
        listView.setCellFactory(view -> new BlogCell());
        listView.setItems(createSortedAndFilteredList(DataRepository.getInstance().blogsProperty(),
                Comparator.comparing(Blog::getTitle),
                blog -> StringUtils.isBlank(getFilterText()) || StringUtils.containsIgnoreCase(blog.getTitle(), getFilterText())));
        listView.getItems().addListener((Observable it) -> performDefaultSelection());

        VBox.setVgrow(listView, Priority.ALWAYS);

        listView.getSelectionModel().selectedItemProperty().addListener(it -> setItem(listView.getSelectionModel().getSelectedItem()));
        itemProperty().addListener(it -> listView.getSelectionModel().select(getItem()));

        Button button = new Button("Show all posts");
        button.setMaxWidth(Double.MAX_VALUE);
        button.setOnAction(evt -> listView.getSelectionModel().clearSelection());
        HBox.setHgrow(button, Priority.ALWAYS);

        HBox buttonWrapper = new HBox(button);
        buttonWrapper.getStyleClass().add("button-wrapper");

        VBox vBox = new VBox(10, buttonWrapper, listView);
        vBox.getStyleClass().add("vbox");

        setCenter(vBox);

        performDefaultSelection();
    }

    private void performDefaultSelection() {
        if (!listView.getItems().isEmpty()) {
            listView.getSelectionModel().select(0);
        } else {
            listView.getSelectionModel().clearSelection();
        }
    }

    @Override
    public Node getDetailPane() {
        if (blogView == null) {
            blogView = new BlogView(getRootPane());
            blogView.blogProperty().bind(listView.getSelectionModel().selectedItemProperty());
        }

        return blogView;
    }

    class BlogCell extends AdvancedListCell<Blog> {

        private ImageView imageView = new ImageView();
        private Label label = new Label();

        public BlogCell() {
            getStyleClass().add("blog-cell");

            imageView.setFitWidth(200);
            imageView.setFitHeight(200);
            imageView.setPreserveRatio(true);

            label.getStyleClass().add("title-label");

            VBox vbox = new VBox(imageView, label);
            vbox.setAlignment(Pos.TOP_CENTER);

            setGraphic(vbox);
            setContentDisplay(ContentDisplay.GRAPHIC_ONLY);

            vbox.visibleProperty().bind(emptyProperty().not());
        }

        @Override
        protected void updateItem(Blog blog, boolean empty) {
            super.updateItem(blog, empty);

            if (!empty && blog != null) {
                label.setText(blog.getTitle());
                imageView.imageProperty().bind(ImageManager.getInstance().blogPageImageProperty(blog));

                this.setMouseTransparent(true);
                setCellLink(getGraphic(), blog, this.getChildren());
            } else {
                label.setText("");
                imageView.imageProperty().unbind();
            }
        }
    }
}