package com.dlsc.jfxcentral.views.master.cells;

import com.dlsc.jfxcentral.data.ImageManager;
import com.dlsc.jfxcentral.data.model.Tool;
import com.dlsc.jfxcentral.views.AdvancedListCell;
import com.dlsc.jfxcentral.views.MarkdownView;
import com.dlsc.jfxcentral.views.View;
import com.jpro.webapi.WebAPI;
import javafx.geometry.Pos;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

public class MasterToolCell extends AdvancedListCell<Tool> {

    private final Label nameLabel = new Label();
    private final ImageView imageView = new ImageView();
    private final MarkdownView markdownView = new MarkdownView();

    public MasterToolCell() {
        getStyleClass().add("master-tool-list-cell");

        setPrefWidth(0);

        imageView.visibleProperty().bind(imageView.imageProperty().isNotNull());
        imageView.managedProperty().bind(imageView.imageProperty().isNotNull());
        imageView.setFitHeight(40);
        imageView.setFitWidth(60);
        imageView.setPreserveRatio(true);

        nameLabel.getStyleClass().add("name-label");
        nameLabel.setMinWidth(0);
        nameLabel.setWrapText(true);
        nameLabel.setMinHeight(Region.USE_PREF_SIZE);

        VBox vbox = new VBox(nameLabel, markdownView);
        vbox.getStyleClass().add("vbox");
        HBox.setHgrow(vbox, Priority.ALWAYS);

        HBox hBox = new HBox(vbox, imageView);
        hBox.getStyleClass().add("hbox");
        hBox.setAlignment(Pos.TOP_LEFT);

        setGraphic(hBox);
        setContentDisplay(ContentDisplay.GRAPHIC_ONLY);

        vbox.visibleProperty().bind(emptyProperty().not());
    }

    @Override
    protected void updateItem(Tool tool, boolean empty) {
        super.updateItem(tool, empty);

        if (!empty && tool != null) {
            nameLabel.setText(tool.getName());
            imageView.imageProperty().bind(ImageManager.getInstance().toolImageProperty(tool));
            markdownView.setMdString(tool.getSummary());

            if (WebAPI.isBrowser()) {
                setMouseTransparent(true);
            }

            setMasterCellLink(MasterToolCell.this, tool, tool.getSummary(), View.TOOLS);
        }
    }
}
