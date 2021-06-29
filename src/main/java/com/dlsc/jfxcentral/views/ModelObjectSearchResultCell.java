package com.dlsc.jfxcentral.views;

import com.dlsc.jfxcentral.data.model.*;
import com.dlsc.jfxcentral.util.PageUtil;
import com.dlsc.jfxcentral.views.autocomplete.OmniBoxSearchField;
import com.dlsc.jfxcentral.views.autocomplete.SearchResult;
import com.dlsc.jfxcentral.views.page.StandardIcons;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import org.apache.commons.lang3.StringUtils;
import org.kordamp.ikonli.Ikon;
import org.kordamp.ikonli.javafx.FontIcon;

public class ModelObjectSearchResultCell extends AdvancedListCell<SearchResult<?>> {

    private FontIcon fontIcon = new FontIcon();
    private Label titleLabel = new Label();
    private Label subtitleLabel = new Label();

    public ModelObjectSearchResultCell(OmniBoxSearchField searchField, RootPane rootPane) {
        getStyleClass().add("search-result-list-cell");

        VBox vBox = new VBox(titleLabel, subtitleLabel);
        vBox.getStyleClass().add("vbox");
        HBox.setHgrow(vBox, Priority.ALWAYS);

        StackPane iconWrapper = new StackPane(fontIcon);
        iconWrapper.getStyleClass().add("icon-wrapper");

        HBox hBox = new HBox(iconWrapper, vBox);
        hBox.getStyleClass().add("hbox");

        titleLabel.getStyleClass().add("title-label");
        subtitleLabel.getStyleClass().add("subtitle-label");

        setPrefWidth(0);

//        setOnMouseClicked(evt -> searchField.hideOmniBox());

        setGraphic(hBox);
        setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
    }

    @Override
    protected void updateItem(SearchResult<?> searchResult, boolean empty) {
        super.updateItem(searchResult, empty);

        if (!empty && searchResult != null) {
            ModelObject item = (ModelObject) searchResult.getValue();

            titleLabel.setText(createTitle(item));
            subtitleLabel.setText(createSubTitle(item));
            fontIcon.setIconCode(createIkonCode(item));

            setLink(PageUtil.getLink((ModelObject) searchResult.getValue()), "Search Result");
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
            return ((Tool) item).getName();
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
            return ((Tool) item).getSummary();
        } else if (item instanceof Tutorial) {
            return ((Tutorial) item).getSummary();
        } else if (item instanceof Download) {
            return "Download";
        }

        return "";
    }
}
