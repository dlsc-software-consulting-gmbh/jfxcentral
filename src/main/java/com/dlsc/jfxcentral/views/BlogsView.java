package com.dlsc.jfxcentral.views;

import com.dlsc.jfxcentral.DataRepository;
import com.dlsc.jfxcentral.ImageManager;
import com.dlsc.jfxcentral.RootPane;
import com.dlsc.jfxcentral.model.Blog;
import com.dlsc.jfxcentral.util.Util;
import javafx.beans.Observable;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;

public class BlogsView extends PageView {

    private final GridPane gridPane = new GridPane();

    public BlogsView(RootPane rootPane) {
        super(rootPane);

        getStyleClass().add("blogs-view");

        gridPane.getStyleClass().add("grid-pane");

        setContent(gridPane);

        DataRepository.getInstance().getBlogs().addListener((Observable it) -> updateView());
        updateView();
    }

    private void updateView() {
        gridPane.getChildren().clear();

        int column = 0;
        int row = 0;

        for (Blog blog : DataRepository.getInstance().getBlogs()) {

            BlogCell blogCell = new BlogCell(blog);
            gridPane.add(blogCell, column, row);

            column++;
            if (column == 3) {
                column = 0;
                row++;
            }
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

            getChildren().setAll(imageView, label);

            label.setText(blog.getSummary());
            imageView.imageProperty().bind(ImageManager.getInstance().blogPageImageProperty(blog));

            setOnMouseClicked(evt -> Util.browse(blog.getUrl()));
        }
    }
}
