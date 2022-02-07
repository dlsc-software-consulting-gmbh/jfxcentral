package com.dlsc.jfxcentral.views.detail.cells;

import com.dlsc.jfxcentral.data.ImageManager;
import com.dlsc.jfxcentral.data.model.Blog;
import com.dlsc.jfxcentral.data.model.Post;
import com.dlsc.jfxcentral.util.Util;
import com.dlsc.jfxcentral.views.RootPane;
import com.jpro.webapi.WebAPI;
import com.rometools.rome.feed.synd.SyndEntry;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;

import java.time.Duration;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Date;

public class DetailPostCell extends DetailCell<Post> {

    private final RootPane rootPane;
    private final DateTimeFormatter dateTimeFormatter;
    private Label titleLabel = new Label();
    private Label ageLabel = new Label();

    private ImageView imageView = new ImageView();

    public DetailPostCell(RootPane rootPane) {
        this.rootPane = rootPane;

        dateTimeFormatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM).withLocale(rootPane.getLocale());

        getStyleClass().add("detail-post-cell");

        setPrefWidth(0);

        titleLabel.getStyleClass().add("title-label");
        titleLabel.setMaxWidth(Double.MAX_VALUE);
        titleLabel.setWrapText(true);
        titleLabel.setMinHeight(Region.USE_PREF_SIZE);

        imageView.setFitHeight(32);
        imageView.setFitWidth(32);
        imageView.setPreserveRatio(true);

        ageLabel.getStyleClass().add("age-label");
        ageLabel.setMinWidth(Region.USE_PREF_SIZE);

        HBox hbox = new HBox(imageView, titleLabel, ageLabel);
        hbox.getStyleClass().add("hbox");
        HBox.setHgrow(titleLabel, Priority.ALWAYS);

        setGraphic(hbox);
        setContentDisplay(ContentDisplay.GRAPHIC_ONLY);

        hbox.visibleProperty().bind(itemProperty().isNotNull());

        if (rootPane.isMobile()) {
            addEventFilter(MouseEvent.MOUSE_CLICKED, evt -> {
                if (evt.isStillSincePress()) {
                    WebAPI.getWebAPI(getScene()).openURL(getItem().getSyndEntry().getLink());
                }
            });
        } else {
            setOnMouseClicked(evt -> Util.browse(this, getItem().getSyndEntry().getLink()));
        }
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

    @Override
    protected void updateItem(Post item, boolean empty) {
        super.updateItem(item, empty);

        if (!empty && item != null) {
            titleLabel.setText(item.getSyndEntry().getTitle());
            ageLabel.setText(getAge(item));
            imageView.imageProperty().bind(ImageManager.getInstance().blogIconImageProperty(item.getBlog()));

            if (!rootPane.isMobile()) {
               setLink(item.getSyndEntry().getLink(), item.getSyndEntry().getTitle());
            }
        } else {
            imageView.imageProperty().unbind();
            titleLabel.setText("");
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

        return dateTimeFormatter.format(zonedDateTime);
    }
}
