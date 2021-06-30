package com.dlsc.jfxcentral.views.mobile.master.cells;

import com.dlsc.jfxcentral.data.ImageManager;
import com.dlsc.jfxcentral.data.model.Blog;
import com.dlsc.jfxcentral.views.MarkdownView;
import com.dlsc.jfxcentral.views.mobile.MobileAdvancedListCell;
import javafx.geometry.Pos;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

public class MobileMasterBlogCell extends MobileAdvancedListCell<Blog> {

        private ImageView imageView = new ImageView();
        private Label label = new Label();
        private MarkdownView markdownView = new MarkdownView();

        public MobileMasterBlogCell() {
            getStyleClass().add("mobile-master-blog-cell");

            setPrefWidth(0);
            setMinWidth(0);

            imageView.setFitWidth(100);
            imageView.setFitHeight(100);
            imageView.setPreserveRatio(true);

            label.getStyleClass().add("title-label");
            label.setWrapText(true);
            label.setMinHeight(Region.USE_PREF_SIZE);

            VBox vbox = new VBox(label, markdownView);
            vbox.getStyleClass().add("vbox");
            HBox.setHgrow(vbox, Priority.ALWAYS);

            HBox hBox = new HBox(vbox, imageView);
            hBox.getStyleClass().add("hbox");
            hBox.setAlignment(Pos.TOP_LEFT);

            setGraphic(hBox);
            setContentDisplay(ContentDisplay.GRAPHIC_ONLY);

            vbox.visibleProperty().bind(emptyProperty().not());
        }

        @Override
        protected void updateItem(Blog blog, boolean empty) {
            super.updateItem(blog, empty);

            if (!empty && blog != null) {
                label.setText(blog.getName());
                imageView.imageProperty().bind(ImageManager.getInstance().blogPageImageProperty(blog));
                markdownView.setMdString(blog.getSummary());
            } else {
                label.setText("");
                imageView.imageProperty().unbind();
            }
        }
    }
