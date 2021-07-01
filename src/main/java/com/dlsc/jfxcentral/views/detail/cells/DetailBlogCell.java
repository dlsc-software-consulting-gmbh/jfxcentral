package com.dlsc.jfxcentral.views.detail.cells;

import com.dlsc.jfxcentral.data.ImageManager;
import com.dlsc.jfxcentral.data.model.Blog;
import com.dlsc.jfxcentral.util.PageUtil;
import com.dlsc.jfxcentral.util.Util;
import com.dlsc.jfxcentral.views.RootPane;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import org.apache.commons.lang3.StringUtils;
import org.kordamp.ikonli.javafx.FontIcon;
import org.kordamp.ikonli.materialdesign.MaterialDesign;

public class DetailBlogCell extends DetailCell<Blog> {

    private final Button detailsButton;
    private final Button visitButton;
    private final ResponsiveBox responsiveBox;

    public DetailBlogCell(RootPane rootPane, boolean largeImage) {
        getStyleClass().add("detail-blog-cell");

        detailsButton = new Button("Details");
        detailsButton.getStyleClass().addAll("library-button", "details");
        detailsButton.setGraphic(new FontIcon(MaterialDesign.MDI_MORE));

        visitButton = new Button("Visit");
        visitButton.getStyleClass().addAll("library-button", "visit");
        visitButton.setGraphic(new FontIcon(MaterialDesign.MDI_WEB));

        responsiveBox = new ResponsiveBox(rootPane.isMobile() ? ResponsiveBox.ImageLocation.BANNER : largeImage ? ResponsiveBox.ImageLocation.LARGE_ON_SIDE : ResponsiveBox.ImageLocation.SMALL_ON_SIDE);
        responsiveBox.getExtraControls().addAll(detailsButton, visitButton);
        responsiveBox.visibleProperty().bind(itemProperty().isNotNull());

        setGraphic(responsiveBox);
        setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
    }

    @Override
    protected void updateItem(Blog item, boolean empty) {
        super.updateItem(item, empty);

        if (!empty && item != null) {
            responsiveBox.setTitle(item.getName());
            responsiveBox.setDescription(StringUtils.abbreviate(item.getSummary(), 250));
            responsiveBox.imageProperty().bind(ImageManager.getInstance().blogPageImageProperty(item));

            visitButton.setVisible(StringUtils.isNotEmpty(item.getUrl()));
            visitButton.setManaged(StringUtils.isNotEmpty(item.getUrl()));

            Util.setLink(detailsButton, PageUtil.getLink(item), item.getSummary());
            Util.setLink(visitButton, item.getUrl(), item.getSummary());
        }
    }
}
