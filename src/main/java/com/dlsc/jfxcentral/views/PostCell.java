package com.dlsc.jfxcentral.views;

import com.dlsc.jfxcentral.model.Post;
import com.rometools.rome.feed.synd.SyndImage;
import javafx.scene.control.ListCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.apache.commons.lang3.StringUtils;

import java.net.MalformedURLException;
import java.net.URL;

public class PostCell extends ListCell<Post> {

    private ImageView imageView = new ImageView();

    public PostCell() {
        imageView.setFitHeight(24);
        imageView.setPreserveRatio(true);
        setGraphic(imageView);
    }

    @Override
    protected void updateItem(Post item, boolean empty) {
        super.updateItem(item, empty);

        if (!empty && item != null) {
            imageView.setImage(null);
            setText(item.getSyndFeed().getTitle() + " " + item.getSyndEntry().getTitle());

            SyndImage image = item.getSyndFeed().getImage();
            if (image != null) {
                if (StringUtils.isNotBlank(image.getUrl())) {
                    System.out.println(item.getSyndFeed().getTitle() + " -> " + image.getUrl());
                    try {
                        URL url = new URL(image.getUrl());
                        imageView.setImage(new Image(url.toExternalForm(), true));
                    } catch (MalformedURLException ex) {}
                }
            }
        } else {
            setText("");
            imageView.setImage(null);
        }
    }
}
