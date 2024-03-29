package com.dlsc.jfxcentral.views.master.cells;

import com.dlsc.jfxcentral.data.ImageManager;
import com.dlsc.jfxcentral.data.model.RealWorldApp;
import com.dlsc.jfxcentral.views.MarkdownView;
import com.dlsc.jfxcentral.views.View;
import com.jpro.webapi.WebAPI;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;

public class MasterRealWorldAppCell extends MasterCell<RealWorldApp> {

    private final Label nameLabel = new Label();
    private final ImageView imageView = new ImageView();
    private final MarkdownView summaryMarkdownView = new MarkdownView();
    private final GridPane gridPane;

    public MasterRealWorldAppCell() {
        getStyleClass().add("master-app-list-cell");

        setPrefWidth(0);

        imageView.visibleProperty().bind(imageView.imageProperty().isNotNull());
        imageView.managedProperty().bind(imageView.imageProperty().isNotNull());
        imageView.setFitWidth(200);
        imageView.setPreserveRatio(true);

        nameLabel.getStyleClass().add("name-label");
        nameLabel.setMinWidth(0);
        nameLabel.setWrapText(true);
        nameLabel.setMinHeight(Region.USE_PREF_SIZE);

        gridPane = new GridPane();
        gridPane.getStyleClass().add("grid-pane");
        gridPane.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);

        gridPane.add(nameLabel, 0, 0);
        gridPane.add(summaryMarkdownView, 0, 1);
        gridPane.add(imageView, 0, 2);

        setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
        setGraphic(gridPane);

        gridPane.visibleProperty().bind(emptyProperty().not());
    }

    @Override
    protected void updateItem(RealWorldApp app, boolean empty) {
        super.updateItem(app, empty);

        if (!empty && app != null) {
            nameLabel.setText(app.getName());
            imageView.imageProperty().bind(ImageManager.getInstance().realWorldAppImageProperty(app));
            summaryMarkdownView.setMdString(app.getSummary());

            if (WebAPI.isBrowser()) {
                setMouseTransparent(true);
            }

            setMasterCellLink(this, app, app.getName(), View.REAL_WORLD);
        } else {
            nameLabel.setText("");
            imageView.imageProperty().unbind();
            imageView.setImage(null);
            summaryMarkdownView.setMdString("");
        }
    }
}
