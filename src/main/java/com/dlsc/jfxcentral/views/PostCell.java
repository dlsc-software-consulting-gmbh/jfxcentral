package com.dlsc.jfxcentral.views;

import com.dlsc.jfxcentral.RootPane;
import com.dlsc.jfxcentral.model.Post;
import com.dlsc.jfxcentral.util.Util;
import com.rometools.rome.feed.synd.SyndImage;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import org.apache.commons.lang3.StringUtils;

import java.net.MalformedURLException;
import java.net.URL;

public class PostCell extends ListCell<Post> {

    private final RootPane rootPane;
    private Label titleLabel = new Label();
    private Label subtitleLabel = new Label();

    private ImageView imageView = new ImageView();

    public PostCell(RootPane rootPane) {
        this.rootPane = rootPane;
        getStyleClass().add("post-cell");

        titleLabel.getStyleClass().add("title-label");
        subtitleLabel.getStyleClass().add("subtitle-label");

        imageView.setFitHeight(24);
        imageView.setPreserveRatio(true);

        VBox vBox = new VBox(titleLabel, subtitleLabel);
        vBox.getStyleClass().add("vbox");
        HBox.setHgrow(vBox, Priority.ALWAYS);

        HBox hbox = new HBox(vBox, imageView);
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
            subtitleLabel.setText(item.getSyndFeed().getTitle());

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
            setText("");
            imageView.setImage(null);
        }
    }
}
