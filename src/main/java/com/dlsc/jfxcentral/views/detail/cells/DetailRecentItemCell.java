package com.dlsc.jfxcentral.views.detail.cells;

import com.dlsc.jfxcentral.data.model.*;
import com.dlsc.jfxcentral.views.AdvancedListCell;
import com.dlsc.jfxcentral.views.page.StandardIcons;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import org.apache.commons.lang3.StringUtils;
import org.kordamp.ikonli.Ikon;
import org.kordamp.ikonli.javafx.FontIcon;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

public class DetailRecentItemCell extends AdvancedListCell<ModelObject> {
    // TODO: web api locale lookup?
    private DateTimeFormatter formatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT);

    private FontIcon fontIcon = new FontIcon();
    private Label titleLabel = new Label();
    private Label subtitleLabel = new Label();
    private Label dateLabel = new Label();

    public DetailRecentItemCell() {
        getStyleClass().add("detail-recent-item-cell");

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
        dateLabel.setMinWidth(Region.USE_PREF_SIZE);

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
        return StandardIcons.getIcon(item);
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
        } else if (item instanceof Company) {
            return ((Company) item).getName();
        } else if (item instanceof RealWorldApp) {
            return ((RealWorldApp) item).getName();
        } else if (item instanceof Tool) {
            return ((RealWorldApp) item).getName();
        } else if (item instanceof Tutorial) {
            return ((Tutorial) item).getName();
        } else if (item instanceof Download) {
            return ((Download) item).getTitle();
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
            String description = ((Video) item).getDescription();
            if (StringUtils.isNotBlank(description)) {
                return description.substring(0, Math.min(100, description.length())).replace("\n", " ");
            }
            return "";
        } else if (item instanceof Blog) {
            return ((Blog) item).getSummary();
        } else if (item instanceof Library) {
            return ((Library) item).getSummary();
        } else if (item instanceof Company) {
            return ((Company) item).getHomepage();
        } else if (item instanceof RealWorldApp) {
            return ((RealWorldApp) item).getSummary();
        } else if (item instanceof Tool) {
            return ((RealWorldApp) item).getSummary();
        } else if (item instanceof Tutorial) {
            return ((Tutorial) item).getSummary();
        } else if (item instanceof Download) {
            return "Download";
        }

        return "";
    }
}
