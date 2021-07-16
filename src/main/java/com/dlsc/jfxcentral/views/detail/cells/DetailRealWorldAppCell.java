package com.dlsc.jfxcentral.views.detail.cells;

import com.dlsc.jfxcentral.data.ImageManager;
import com.dlsc.jfxcentral.data.model.RealWorldApp;
import com.dlsc.jfxcentral.util.PageUtil;
import com.dlsc.jfxcentral.util.Util;
import com.dlsc.jfxcentral.views.RootPane;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import org.kordamp.ikonli.javafx.FontIcon;
import org.kordamp.ikonli.materialdesign.MaterialDesign;

public class DetailRealWorldAppCell extends DetailCell<RealWorldApp> {

    private final Button homepageButton = new Button("Homepage");
    private final RootPane rootPane;
    private final ResponsiveBox responsiveBox;

    public DetailRealWorldAppCell(RootPane rootPane, boolean largeImage) {
        this.rootPane = rootPane;

        getStyleClass().add("detail-real-world-app-cell");

        setPrefWidth(0);

        homepageButton.setGraphic(new FontIcon(MaterialDesign.MDI_HOME));

        responsiveBox = new ResponsiveBox(rootPane.isMobile() ? ResponsiveBox.ImageLocation.BANNER : largeImage ? ResponsiveBox.ImageLocation.LARGE_ON_SIDE : ResponsiveBox.ImageLocation.SMALL_ON_SIDE);
        responsiveBox.getExtraControls().addAll(homepageButton);
        responsiveBox.visibleProperty().bind(itemProperty().isNotNull());
        responsiveBox.setLargeImageWidth(200);
        responsiveBox.setLargeImageHeight(100);

        setGraphic(responsiveBox);
        setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
    }

    @Override
    protected void updateItem(RealWorldApp app, boolean empty) {
        super.updateItem(app, empty);

        if (!empty && app != null) {
            responsiveBox.setTitle(app.getName());
            Util.setLink(responsiveBox.getTitleLabel(), PageUtil.getLink(app), app.getName());
            responsiveBox.setDescription(app.getSummary());
            responsiveBox.imageProperty().bind(ImageManager.getInstance().realWorldAppImageProperty(app));

            Util.setLink(homepageButton, getItem().getUrl(), getItem().getName());
        }
    }
}
