package com.dlsc.jfxcentral.views;

import com.dlsc.jfxcentral.model.*;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import org.kordamp.ikonli.Ikon;
import org.kordamp.ikonli.fontawesome5.FontAwesomeBrands;
import org.kordamp.ikonli.javafx.FontIcon;
import org.kordamp.ikonli.material.Material;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

public class RecentItemCell extends AdvancedListCell<ModelObject> {
    // TODO: web api locale lookup?
    private DateTimeFormatter formatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT);

    private FontIcon fontIcon = new FontIcon();
    private Label titleLabel = new Label();
    private Label subtitleLabel = new Label();
    private Label dateLabel = new Label();

    public RecentItemCell() {
        getStyleClass().add("recent-item-cell");

        VBox vBox = new VBox(titleLabel, subtitleLabel);
        vBox.getStyleClass().add("vbox");
        HBox.setHgrow(vBox, Priority.ALWAYS);

        StackPane iconWrapper = new StackPane(fontIcon);
        iconWrapper.getStyleClass().add("icon-wrapper");

        HBox hBox = new HBox(iconWrapper, vBox, dateLabel);
        hBox.getStyleClass().add("hbox");

        titleLabel.getStyleClass().add("title-label");
        subtitleLabel.getStyleClass().add("subtitle-label");
        dateLabel.getStyleClass().add("date-label");

        setGraphic(hBox);
        setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
    }

    @Override
    protected void updateItem(ModelObject item, boolean empty) {
        super.updateItem(item, empty);

        if (!empty && item != null) {
            titleLabel.setText(createTitle(item));
            subtitleLabel.setText(createSubTitle(item));
            fontIcon.setIconCode(createIkonCode(item));

            LocalDate updatedDate = item.getModifiedOn();
            LocalDate createdDate = item.getCreatedOn();

            if (updatedDate != null && createdDate != null) {
                if (updatedDate.isAfter(createdDate)) {
                    dateLabel.setText("Updated on: " + formatter.format(updatedDate));
                } else {
                    dateLabel.setText("Created on: " + formatter.format(createdDate));
                }
            } else if (updatedDate != null) {
                dateLabel.setText("Updated on: " + formatter.format(updatedDate));
            } else if (createdDate != null) {
                dateLabel.setText("Created on: " + formatter.format(createdDate));
            } else {
                dateLabel.setText("");
            }
        }
    }

    private Ikon createIkonCode(ModelObject item) {
        // TODO: icons for domain objects should be declared centrally
        if (item instanceof Book) {
            return FontAwesomeBrands.AMAZON;
        } else if (item instanceof Person) {
            return Material.PERSON;
        } else if (item instanceof News) {
            return Material.NOTES;
        } else if (item instanceof Video) {
            return FontAwesomeBrands.YOUTUBE;
        } else if (item instanceof Blog) {
            return FontAwesomeBrands.BLOGGER;
        } else if (item instanceof Library) {
            return FontAwesomeBrands.GITHUB;
        }

        return null;
    }

    private String createTitle(ModelObject item) {
        if (item instanceof Book) {
            return ((Book) item).getTitle();
        } else if (item instanceof Person) {
            return ((Person) item).getName();
        } else if (item instanceof News) {
            return ((News) item).getTitle();
        } else if (item instanceof Video) {
            return ((Video) item).getTitle();
        } else if (item instanceof Blog) {
            return ((Blog) item).getTitle();
        } else if (item instanceof Library) {
            return ((Library) item).getTitle();
        }

        return "";
    }

    private String createSubTitle(ModelObject item) {
        if (item instanceof Book) {
            return ((Book) item).getSubtitle();
        } else if (item instanceof Person) {
            return ((Person) item).getWebsite();
        } else if (item instanceof News) {
            return ((News) item).getSubtitle();
        } else if (item instanceof Video) {
            return ((Video) item).getSummary();
        } else if (item instanceof Blog) {
            return ((Blog) item).getSummary();
        } else if (item instanceof Library) {
            return ((Library) item).getSummary();
        }

        return "";
    }
}
