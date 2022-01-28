package com.dlsc.jfxcentral.views;

import com.dlsc.jfxcentral.data.model.*;
import com.dlsc.jfxcentral.util.PageUtil;
import com.dlsc.jfxcentral.views.autocomplete.SearchResult;
import com.dlsc.jfxcentral.views.page.StandardIcons;
import com.jpro.web.Util;
import com.jpro.webapi.WebAPI;
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

    private final RootPane rootPane;
    private FontIcon fontIcon = new FontIcon();
    private Label titleLabel = new Label();
    private Label subtitleLabel = new Label();

    public ModelObjectSearchResultCell(RootPane rootPane) {
        this.rootPane = rootPane;

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

        hBox.visibleProperty().bind(itemProperty().isNotNull());
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

            if (WebAPI.isBrowser()) {
                setLink(PageUtil.getLink((ModelObject) searchResult.getValue()), "Search Result");
            } else {

                // work around ... setLink() should work after a fix from Florian
                setOnMouseClicked(evt -> {
                    getScene().getWindow().hide();
                    Util.getSessionManager(rootPane).gotoURL(PageUtil.getLink(item));
                });
            }
        }
    }

    private Ikon createIkonCode(ModelObject item) {
        return StandardIcons.getIcon(item);
    }

    private String createTitle(ModelObject item) {
        return getTypePrefix(item.getClass()) + ": " + item.getName();
    }

    private String getTypePrefix(Class<? extends ModelObject> clazz) {
        if (clazz.equals(Tip.class)) {
            return "Tip";
        } else if (clazz.equals(RealWorldApp.class)) {
            return "Real World App";
        } else if (clazz.equals(Person.class)) {
            return "Person";
        } else if (clazz.equals(Company.class)) {
            return "Company";
        } else if (clazz.equals(Blog.class)) {
            return "Blog";
        } else if (clazz.equals(Video.class)) {
            return "Video";
        } else if (clazz.equals(Book.class)) {
            return "Book";
        } else if (clazz.equals(Tool.class)) {
            return "Tool";
        } else if (clazz.equals(Library.class)) {
            return "Library";
        } else if (clazz.equals(Tutorial.class)) {
            return "Tutorial";
        } else if (clazz.equals(Download.class)) {
            return "Download";
        } else if (clazz.equals(News.class)) {
            return "News";
        } else if (clazz.equals(Post.class)) {
            return "Post";
        } else {
            return "Item";
        }
    }

    private String createSubTitle(ModelObject item) {
        if (item instanceof Book) {
            return ((Book) item).getSubtitle();
        } else if (item instanceof Person) {
            return ((Person) item).getWebsite();
        } else if (item instanceof News) {
            return ((News) item).getSubtitle();
        } else if (item instanceof Video) {
            String description = item.getDescription();
            if (StringUtils.isNotBlank(description)) {
                return description.substring(0, Math.min(100, description.length())).replace("\n", " ");
            }
            return "";
        } else if (item instanceof Download) {
            return "Download";
        } else {
            return item.getSummary();
        }
    }
}
