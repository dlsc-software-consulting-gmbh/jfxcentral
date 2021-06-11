package com.dlsc.jfxcentral.views.master.cells;

import com.dlsc.jfxcentral.data.ImageManager;
import com.dlsc.jfxcentral.data.model.Blog;
import com.dlsc.jfxcentral.views.View;
import com.jpro.webapi.WebAPI;
import javafx.geometry.Pos;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

public class MasterBlogCell extends MasterCell<Blog> {

        private ImageView imageView = new ImageView();
        private Label label = new Label();

        public MasterBlogCell() {
            getStyleClass().add("master-blog-cell");

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

                if (WebAPI.isBrowser()) {
                    setMouseTransparent(true);
                }
                setMasterCellLink(this, blog, blog.getSummary(), View.BLOGS);
            } else {
                label.setText("");
                imageView.imageProperty().unbind();
            }
        }
    }
