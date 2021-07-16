package com.dlsc.jfxcentral.views.detail.cells;

import com.dlsc.jfxcentral.data.DataRepository;
import com.dlsc.jfxcentral.data.ImageManager;
import com.dlsc.jfxcentral.data.model.Download;
import com.dlsc.jfxcentral.util.PageUtil;
import com.dlsc.jfxcentral.util.Util;
import com.dlsc.jfxcentral.views.RootPane;
import com.jpro.webapi.WebAPI;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import org.apache.commons.lang3.StringUtils;
import org.kordamp.ikonli.javafx.FontIcon;
import org.kordamp.ikonli.materialdesign.MaterialDesign;

public class DetailDownloadCell extends DetailCell<Download> {

    private final ResponsiveBox responsiveBox;
    private final RootPane rootPane;
    private final boolean primaryView;

    public DetailDownloadCell(RootPane rootPane, boolean primaryView) {
        this.rootPane = rootPane;
        this.primaryView = primaryView;
        
        getStyleClass().add("detail-download-cell");
        setPrefWidth(0);

        responsiveBox = new ResponsiveBox(rootPane.isMobile() ? ResponsiveBox.ImageLocation.BANNER : primaryView ? ResponsiveBox.ImageLocation.LARGE_ON_SIDE : ResponsiveBox.ImageLocation.SMALL_ON_SIDE);
        responsiveBox.visibleProperty().bind(itemProperty().isNotNull());

        setGraphic(responsiveBox);
        setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
    }

    private void downloadFile(Download.DownloadFile downloadFile) {
        if (StringUtils.isNotBlank(downloadFile.getUrl())) {
            Util.browse(this, downloadFile.getUrl(), false);
        } else {
            Util.browse(this, DataRepository.getInstance().getBaseUrl() + "downloads/" + getItem().getId() + "/" + downloadFile.getFileName(), false);
        }
    }

    @Override
    protected void updateItem(Download download, boolean empty) {
        super.updateItem(download, empty);

        responsiveBox.getExtraControls().clear();

        if (!empty && download != null) {
            responsiveBox.setTitle(download.getName());
            if (!primaryView) {
                Util.setLink(responsiveBox.getTitleLabel(), PageUtil.getLink(download), download.getName());
            }
            responsiveBox.descriptionProperty().bind(DataRepository.getInstance().downloadTextProperty(download));
            responsiveBox.imageProperty().bind(ImageManager.getInstance().downloadBannerImageProperty(download));

            if (StringUtils.isNotBlank(download.getHomepage())) {
                Button homepage = new Button("Homepage");
                homepage.setGraphic(new FontIcon(MaterialDesign.MDI_HOME));
                Util.setLink(homepage, download.getHomepage(), "Visit homepage");
                responsiveBox.getExtraControls().add(homepage);
            }

            if (!rootPane.isMobile()) {
                download.getFiles().forEach(file -> {
                    Button downloadButton = new Button(file.getName());
                    downloadButton.setGraphic(new FontIcon(MaterialDesign.MDI_DOWNLOAD));
                    if(WebAPI.isBrowser()) {
                        Util.setLink(downloadButton,file.getUrl(),file.getName());
                    } else {
                        downloadButton.setOnAction(evt -> downloadFile(file));
                    }
                    responsiveBox.getExtraControls().add(downloadButton);
                });
            }

            // Deactivated menu version for now, because the download is hard to implement.
            //if (!rootPane.isMobile()) {
            //    MenuButton menuButton = new MenuButton("Downloads");
            //    download.getFiles().forEach(file -> {
            //        MenuItem item = new MenuItem(file.getName());
            //        item.setGraphic(new FontIcon(MaterialDesign.MDI_DOWNLOAD));
            //        item.setOnAction(evt -> downloadFile(file));
            //        menuButton.getItems().add(item);
            //    });
            //}
        }
    }
}