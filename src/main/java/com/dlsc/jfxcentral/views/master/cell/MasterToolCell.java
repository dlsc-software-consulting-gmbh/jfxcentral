package com.dlsc.jfxcentral.views.master.cell;

import com.dlsc.jfxcentral.data.ImageManager;
import com.dlsc.jfxcentral.data.model.Tool;
import com.dlsc.jfxcentral.views.AdvancedListCell;
import com.dlsc.jfxcentral.views.MarkdownView;
import com.dlsc.jfxcentral.views.View;
import com.jpro.webapi.WebAPI;
import javafx.geometry.VPos;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;

public class MasterToolCell extends AdvancedListCell<Tool> {

    private final Label nameLabel = new Label();
    private final ImageView imageView = new ImageView();
    private final MarkdownView markdownView = new MarkdownView();
    private final GridPane gridPane;

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

        gridPane = new GridPane();
        gridPane.getStyleClass().add("grid-pane");
        gridPane.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);

        gridPane.add(nameLabel, 0, 0);
        gridPane.add(markdownView, 0, 1);
        gridPane.add(imageView, 1, 0);

        GridPane.setRowSpan(imageView, 2);

        GridPane.setHgrow(nameLabel, Priority.ALWAYS);
        GridPane.setHgrow(markdownView, Priority.ALWAYS);

        GridPane.setValignment(nameLabel, VPos.TOP);
        GridPane.setValignment(markdownView, VPos.TOP);
        GridPane.setValignment(imageView, VPos.TOP);

        setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
        setGraphic(gridPane);

        gridPane.visibleProperty().bind(emptyProperty().not());
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
