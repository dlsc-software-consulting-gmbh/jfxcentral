package com.dlsc.jfxcentral.views.detail.cells;

import com.dlsc.jfxcentral.data.DataRepository;
import com.dlsc.jfxcentral.data.ImageManager;
import com.dlsc.jfxcentral.data.model.Book;
import com.dlsc.jfxcentral.util.PageUtil;
import com.dlsc.jfxcentral.util.Util;
import com.dlsc.jfxcentral.views.RootPane;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import org.apache.commons.lang3.StringUtils;
import org.kordamp.ikonli.fontawesome5.FontAwesomeBrands;
import org.kordamp.ikonli.javafx.FontIcon;
import org.kordamp.ikonli.materialdesign.MaterialDesign;

public class DetailBookCell extends DetailCell<Book> {

    private final Button detailsButton;
    private final Button homepageButton;
    private final Button amazonButton;
    private final ResponsiveBox responsiveBox;


    public DetailBookCell(RootPane rootPane, boolean largeImage) {
        getStyleClass().add("detail-book-cell");

        detailsButton = new Button("Details");
        detailsButton.getStyleClass().addAll("library-button", "details");
        detailsButton.setGraphic(new FontIcon(MaterialDesign.MDI_MORE));

        homepageButton = new Button("Homepage");
        homepageButton.getStyleClass().addAll("library-button", "homepage");
        homepageButton.setGraphic(new FontIcon(MaterialDesign.MDI_WEB));

        amazonButton = new Button("Amazon");
        amazonButton.getStyleClass().addAll("library-button", "amazon");
        amazonButton.setGraphic(new FontIcon(FontAwesomeBrands.AMAZON));

        responsiveBox = new ResponsiveBox(rootPane.isMobile() ? ResponsiveBox.ImageLocation.BANNER : largeImage ? ResponsiveBox.ImageLocation.LARGE_ON_SIDE : ResponsiveBox.ImageLocation.SMALL_ON_SIDE);
        responsiveBox.getExtraControls().addAll(detailsButton, homepageButton, amazonButton);
        responsiveBox.visibleProperty().bind(itemProperty().isNotNull());

        setGraphic(responsiveBox);
        setContentDisplay(ContentDisplay.GRAPHIC_ONLY);

        setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
    }

    @Override
    protected void updateItem(Book item, boolean empty) {
        super.updateItem(item, empty);

        if (!empty && item != null) {
            responsiveBox.imageProperty().bind(ImageManager.getInstance().bookCoverImageProperty(item));
            responsiveBox.setTitle(item.getName());
            responsiveBox.setSubtitle(item.getSubtitle());
            responsiveBox.descriptionProperty().bind(DataRepository.getInstance().bookTextProperty(item));

            homepageButton.setVisible(StringUtils.isNotEmpty(item.getUrl()));
            homepageButton.setManaged(StringUtils.isNotEmpty(item.getUrl()));

            amazonButton.setVisible(StringUtils.isNotEmpty(item.getAmazonASIN()));
            amazonButton.setManaged(StringUtils.isNotEmpty(item.getAmazonASIN()));

            Util.setLink(homepageButton, item.getUrl(), item.getName());
            Util.setLink(detailsButton, PageUtil.getLink(item), item.getName());
            Util.setLink(amazonButton, "http://www.amazon.com/dp/" + item.getAmazonASIN(), item.getName());
        }
    }
}
