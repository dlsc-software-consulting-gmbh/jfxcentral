package com.dlsc.jfxcentral.views.master.cells;

import com.dlsc.jfxcentral.data.ImageManager;
import com.dlsc.jfxcentral.data.model.Library;
import com.dlsc.jfxcentral.panels.LicenseLabel;
import com.dlsc.jfxcentral.views.MarkdownView;
import com.dlsc.jfxcentral.views.View;
import com.jpro.webapi.WebAPI;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

public class MasterLibraryCell extends MasterCell<Library> {

    private final ImageView imageView = new ImageView();
    private final Label nameLabel = new Label();
    private final MarkdownView summaryLabel = new MarkdownView();
    private final LicenseLabel licenseLabel = new LicenseLabel();
    private final VBox vbox;

    public MasterLibraryCell() {
        getStyleClass().add("master-library-list-cell");

        imageView.setFitHeight(30);
        imageView.setFitWidth(30);
        imageView.setPreserveRatio(true);
        imageView.visibleProperty().bind(imageView.imageProperty().isNotNull());
        imageView.managedProperty().bind(imageView.imageProperty().isNotNull());

        nameLabel.getStyleClass().add("name-label");

        summaryLabel.getStyleClass().add("summary-label");

        vbox = new VBox();
        vbox.getStyleClass().add("vbox");
        vbox.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);

        nameLabel.setGraphic(licenseLabel);

        vbox.getChildren().addAll(nameLabel, summaryLabel, imageView);

        setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
        setGraphic(vbox);
    }

    @Override
    protected void updateItem(Library library, boolean empty) {
        super.updateItem(library, empty);

        if (!empty && library != null) {
            nameLabel.setText(library.getName());
            summaryLabel.setMdString(library.getSummary());
            imageView.imageProperty().bind(ImageManager.getInstance().libraryImageProperty(library));
            licenseLabel.setLicense(library.getLicense());
            licenseLabel.setVisible(true);

            if (WebAPI.isBrowser()) {
                setMouseTransparent(true);
            }

            setMasterCellLink(this, library, library.getSummary(), View.LIBRARIES);
        } else {
            nameLabel.setText("");
            summaryLabel.setMdString("");
            imageView.imageProperty().unbind();
            imageView.setImage(null);
            licenseLabel.setVisible(false);
        }
    }
}