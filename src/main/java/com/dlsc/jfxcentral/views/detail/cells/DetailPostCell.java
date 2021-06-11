package com.dlsc.jfxcentral.views.detail.cells;

import com.dlsc.jfxcentral.data.ImageManager;
import com.dlsc.jfxcentral.data.model.Blog;
import com.dlsc.jfxcentral.data.model.Post;
import com.rometools.rome.feed.synd.SyndEntry;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
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

    private Label titleLabel = new Label();
    private Label blogLabel = new Label();
    private Label ageLabel = new Label();

    private ImageView imageView = new ImageView();

    public DetailPostCell() {
        getStyleClass().add("post-cell");

        titleLabel.getStyleClass().add("title-label");
        titleLabel.setMaxWidth(Double.MAX_VALUE);

        blogLabel.getStyleClass().add("blog-label");
        blogLabel.visibleProperty().bind(blogProperty().isNull());
        blogLabel.managedProperty().bind(blogProperty().isNull());

        imageView.setFitHeight(32);
        imageView.setFitWidth(32);
        imageView.setPreserveRatio(true);

        ageLabel.getStyleClass().add("age-label");
        ageLabel.setMinWidth(Region.USE_PREF_SIZE);

        HBox hbox = new HBox(imageView, blogLabel, titleLabel, ageLabel);
        HBox.setHgrow(titleLabel, Priority.ALWAYS);
        hbox.getStyleClass().add("hbox");
        hbox.setMinWidth(0);
        hbox.setPrefWidth(0);

        setGraphic(hbox);
        setContentDisplay(ContentDisplay.GRAPHIC_ONLY);

        hbox.visibleProperty().bind(itemProperty().isNotNull());
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
            blogLabel.setText(item.getSyndFeed().getTitle());
            ageLabel.setText(getAge(item));
            imageView.imageProperty().bind(ImageManager.getInstance().blogIconImageProperty(item.getBlog()));
            setLink(item.getSyndEntry().getLink(), item.getSyndEntry().getTitle());
        } else {
            imageView.imageProperty().unbind();
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
