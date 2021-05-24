package com.dlsc.jfxcentral.views;

import com.dlsc.jfxcentral.DataRepository;
import com.dlsc.jfxcentral.ImageManager;
import com.dlsc.jfxcentral.RootPane;
import com.dlsc.jfxcentral.model.Blog;
import javafx.geometry.Pos;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;
import org.controlsfx.control.GridCell;
import org.controlsfx.control.GridView;

public class BlogsView extends PageView {

    public BlogsView(RootPane rootPane) {
        super(rootPane);

        GridView<Blog> gridView = new GridView<>();
        gridView.setItems(DataRepository.getInstance().blogsProperty());
        gridView.setCellFactory(view -> new BlogCell());
        gridView.setCellHeight(400);
        gridView.setCellWidth(300);
        gridView.setHorizontalCellSpacing(0);
        gridView.setVerticalCellSpacing(20);

        VBox vBox = new VBox(gridView);
        vBox.setFillWidth(true);
        vBox.setAlignment(Pos.TOP_CENTER);

        setContent(vBox);
    }

    private class BlogCell extends GridCell<Blog> {

        private ImageView imageView = new ImageView();
        private Label label = new Label();

        public BlogCell() {
            getStyleClass().add("blog-cell");

            imageView.setFitWidth(300);
            imageView.setFitHeight(300);
            imageView.setPreserveRatio(true);

            label.getStyleClass().add("summary-label");
            label.setWrapText(true);
            label.setMinHeight(Region.USE_PREF_SIZE);
            label.setAlignment(Pos.CENTER);
            label.setTextAlignment(TextAlignment.CENTER);

            VBox vBox = new VBox(imageView, label);

            setGraphic(vBox);
            setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
        }

        @Override
        protected void updateItem(Blog blog, boolean empty) {
            super.updateItem(blog, empty);

            if (!empty && blog != null) {
                label.setText(blog.getSummary());
                imageView.imageProperty().bind(ImageManager.getInstance().blogPageImageProperty(blog));
            } else {
                label.setText("");
                imageView.imageProperty().unbind();
            }
        }
    }
}
