package com.dlsc.jfxcentral.views.detail.cells;

import com.dlsc.jfxcentral.data.DataRepository;
import com.dlsc.jfxcentral.data.ImageManager;
import com.dlsc.jfxcentral.data.model.Download;
import com.dlsc.jfxcentral.util.Util;
import com.dlsc.jfxcentral.views.RootPane;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import org.apache.commons.lang3.StringUtils;
import org.kordamp.ikonli.javafx.FontIcon;
import org.kordamp.ikonli.materialdesign.MaterialDesign;

public class DetailDownloadCell extends DetailCell<Download> {

    private final ResponsiveBox responsiveBox;
    private final RootPane rootPane;

    public DetailDownloadCell(RootPane rootPane, boolean largeImage) {
        this.rootPane = rootPane;

        getStyleClass().add("detail-download-cell");
        setPrefWidth(0);

        responsiveBox = new ResponsiveBox(rootPane.isMobile() ? ResponsiveBox.ImageLocation.BANNER : largeImage ? ResponsiveBox.ImageLocation.LARGE_ON_SIDE : ResponsiveBox.ImageLocation.SMALL_ON_SIDE);
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
            responsiveBox.setTitle(download.getTitle());
            responsiveBox.descriptionProperty().bind(DataRepository.getInstance().downloadTextProperty(download));
            responsiveBox.imageProperty().bind(ImageManager.getInstance().downloadBannerImageProperty(download));

            if (!rootPane.isMobile()) {
                MenuButton menuButton = new MenuButton("Downloads");
                download.getFiles().forEach(file -> {
                    MenuItem item = new MenuItem(file.getName());
                    item.setGraphic(new FontIcon(MaterialDesign.MDI_DOWNLOAD));
                    item.setOnAction(evt -> downloadFile(file));
                    menuButton.getItems().add(item);
                });
                responsiveBox.getExtraControls().add(menuButton);
            }
        }
    }
}