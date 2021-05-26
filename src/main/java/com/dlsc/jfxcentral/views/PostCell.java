package com.dlsc.jfxcentral.views;

import com.dlsc.jfxcentral.RootPane;
import com.dlsc.jfxcentral.model.Post;
import com.dlsc.jfxcentral.util.Util;
import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndImage;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import org.apache.commons.lang3.StringUtils;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Date;

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
