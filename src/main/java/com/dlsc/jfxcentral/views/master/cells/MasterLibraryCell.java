package com.dlsc.jfxcentral.views.master.cells;

import com.dlsc.jfxcentral.data.ImageManager;
import com.dlsc.jfxcentral.data.model.Library;
import com.dlsc.jfxcentral.panels.LicenseLabel;
import com.dlsc.jfxcentral.views.View;
import com.jpro.webapi.WebAPI;
import javafx.geometry.VPos;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;

public class MasterLibraryCell extends MasterCell<Library> {

    private final ImageView imageView = new ImageView();
    private final Label nameLabel = new Label();
    private final LicenseLabel licenseLabel = new LicenseLabel();
    private final GridPane gridPane;

    public MasterLibraryCell() {
        getStyleClass().add("master-library-list-cell");

        imageView.setFitHeight(30);
        imageView.setFitWidth(30);
        imageView.setPreserveRatio(true);

        nameLabel.getStyleClass().add("name-label");

        gridPane = new GridPane();
        gridPane.getStyleClass().add("grid-pane");
        gridPane.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);

        gridPane.add(imageView, 1, 0);
        gridPane.add(nameLabel, 0, 0);
        gridPane.add(licenseLabel, 0, 1);

        GridPane.setRowSpan(imageView, 2);
        GridPane.setHgrow(nameLabel, Priority.ALWAYS);
        GridPane.setVgrow(nameLabel, Priority.ALWAYS);
        GridPane.setValignment(nameLabel, VPos.BOTTOM);
        GridPane.setValignment(licenseLabel, VPos.TOP);

        RowConstraints row1 = new RowConstraints();
        RowConstraints row2 = new RowConstraints();

        row1.setPercentHeight(50);
        row2.setPercentHeight(50);

        gridPane.getRowConstraints().setAll(row1, row2);

        setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
        setGraphic(gridPane);
    }

    @Override
    protected void updateItem(Library library, boolean empty) {
        super.updateItem(library, empty);

        if (!empty && library != null) {
            nameLabel.setText(library.getName());
            imageView.setVisible(true);
            imageView.imageProperty().bind(ImageManager.getInstance().libraryImageProperty(library));
            licenseLabel.setLicense(library.getLicense());
            licenseLabel.setVisible(true);

            if (WebAPI.isBrowser()) {
                setMouseTransparent(true);
            }

            setMasterCellLink(this, library, library.getSummary(), View.LIBRARIES);
        } else {
            nameLabel.setText("");
            imageView.imageProperty().unbind();
            imageView.setImage(null);
            licenseLabel.setVisible(false);
        }
    }
}