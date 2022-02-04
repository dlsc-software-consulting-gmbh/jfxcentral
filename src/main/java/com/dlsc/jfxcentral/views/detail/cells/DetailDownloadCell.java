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
import javafx.scene.layout.VBox;
import org.apache.commons.lang3.StringUtils;
import org.kordamp.ikonli.javafx.FontIcon;
import org.kordamp.ikonli.materialdesign.MaterialDesign;

public class DetailDownloadCell extends DetailCell<Download> {

    private final ResponsiveBox responsiveBox;
    private final RootPane rootPane;
    private final boolean primaryView;
    private final Button homepage;

    public DetailDownloadCell(RootPane rootPane, boolean primaryView) {
        this.rootPane = rootPane;
        this.primaryView = primaryView;

        getStyleClass().add("detail-download-cell");
        setPrefWidth(0);

        homepage = new Button("Homepage");
        homepage.setGraphic(new FontIcon(MaterialDesign.MDI_HOME));

        responsiveBox = new ResponsiveBox(rootPane.isMobile() ? ResponsiveBox.ImageLocation.BANNER : primaryView ? ResponsiveBox.ImageLocation.LARGE_ON_SIDE : ResponsiveBox.ImageLocation.SMALL_ON_SIDE);
        responsiveBox.visibleProperty().bind(itemProperty().isNotNull());
        responsiveBox.getExtraControls().addAll(homepage);

        if (!rootPane.isMobile()) {
            Button downloadButton = new Button("Downloads");
            downloadButton.setGraphic(new FontIcon(MaterialDesign.MDI_DOWNLOAD));
            downloadButton.setOnAction(evt -> showDownloads(getItem()));
            responsiveBox.getExtraControls().addAll(downloadButton);
        }

        addLinkIcon(responsiveBox.getTitleLabel());

        setGraphic(responsiveBox);
        setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
    }

    private void showDownloads(Download item) {
        VBox box = new VBox();
        box.getStyleClass().add("download-box");

        item.getFiles().forEach(file -> {
            Button downloadButton = new Button(file.getName());
            downloadButton.getStyleClass().add("download-button-big");
            downloadButton.setGraphic(new FontIcon(MaterialDesign.MDI_DOWNLOAD));
            if (WebAPI.isBrowser()) {
                Util.setLink(downloadButton, file.getUrl(), file.getName());
            } else {
                downloadButton.setOnAction(evt -> downloadFile(file));
            }
            box.getChildren().add(downloadButton);
        });

        rootPane.getOverlayPane().setContent(box);
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

        if (!empty && download != null) {
            responsiveBox.setTitle(download.getName());
            if (!primaryView) {
                Util.setLink(responsiveBox.getTitleLabel(), PageUtil.getLink(download), download.getName());
            }

            responsiveBox.descriptionProperty().bind(DataRepository.getInstance().downloadTextProperty(download));
            responsiveBox.imageProperty().bind(ImageManager.getInstance().downloadBannerImageProperty(download));

            if (StringUtils.isNotBlank(download.getHomepage())) {
                Util.setLink(homepage, download.getHomepage(), "Visit homepage");
            }
        }
    }
}