package com.dlsc.jfxcentral.views.detail.cells;

import com.dlsc.jfxcentral.data.DataRepository;
import com.dlsc.jfxcentral.data.ImageManager;
import com.dlsc.jfxcentral.data.model.Company;
import com.dlsc.jfxcentral.util.PageUtil;
import com.dlsc.jfxcentral.util.Util;
import com.dlsc.jfxcentral.views.RootPane;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import org.kordamp.ikonli.javafx.FontIcon;
import org.kordamp.ikonli.materialdesign.MaterialDesign;

public class DetailCompanyCell extends DetailCell<Company> {

    private final Button homepageButton = new Button("Homepage");
    private final RootPane rootPane;
    private final ResponsiveBox responsiveBox;
    private final boolean primaryView;

    public DetailCompanyCell(boolean companyPage, RootPane rootPane, boolean primaryView) {
        this.rootPane = rootPane;
        this.primaryView = primaryView;

        getStyleClass().add("detail-company-cell");

        setPrefWidth(0);

        homepageButton.setGraphic(new FontIcon(MaterialDesign.MDI_HOME));

        responsiveBox = new ResponsiveBox((rootPane.isMobile() && !companyPage) ? ResponsiveBox.ImageLocation.HIDE : primaryView ? ResponsiveBox.ImageLocation.LARGE_ON_SIDE : ResponsiveBox.ImageLocation.SMALL_ON_SIDE);
        responsiveBox.getExtraControls().addAll(homepageButton);
        responsiveBox.visibleProperty().bind(itemProperty().isNotNull());
        responsiveBox.setLargeImageWidth(192);
        responsiveBox.setLargeImageHeight(64);

        addLinkIcon(responsiveBox.getTitleLabel());

        setGraphic(responsiveBox);
        setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
    }

    @Override
    protected void updateItem(Company company, boolean empty) {
        super.updateItem(company, empty);

        if (!empty && company != null) {
            responsiveBox.setTitle(company.getName());
            if (!primaryView) {
                Util.setLink(responsiveBox.getTitleLabel(), PageUtil.getLink(company), company.getName());
            }
            responsiveBox.descriptionProperty().bind(DataRepository.getInstance().companyDescriptionProperty(company));
            responsiveBox.imageProperty().bind(ImageManager.getInstance().companyImageProperty(company));

            Util.setLink(homepageButton, getItem().getHomepage(), getItem().getName());
        }
    }
}
