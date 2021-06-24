package com.dlsc.jfxcentral.views.mobile.master.cells;

import com.dlsc.jfxcentral.data.ImageManager;
import com.dlsc.jfxcentral.data.model.Tool;
import com.dlsc.jfxcentral.views.mobile.MobileAdvancedListCell;
import javafx.geometry.VPos;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;

public class MobileMasterToolCell extends MobileAdvancedListCell<Tool> {

    private final Label nameLabel = new Label();
    private final Label summaryLabel = new Label();
    private final ImageView imageView = new ImageView();

    public MobileMasterToolCell() {
        getStyleClass().add("mobile-master-tool-list-cell");

        setPrefWidth(0);
        setMaxWidth(Double.MAX_VALUE);

        imageView.visibleProperty().bind(imageView.imageProperty().isNotNull());
        imageView.managedProperty().bind(imageView.imageProperty().isNotNull());
        imageView.setFitHeight(100);
        imageView.setFitWidth(100);
        imageView.setPreserveRatio(true);

        nameLabel.getStyleClass().add("name-label");
        nameLabel.setMaxWidth(Double.MAX_VALUE);
        nameLabel.setMinWidth(Region.USE_PREF_SIZE);

        summaryLabel.setWrapText(true);
        summaryLabel.setMaxWidth(Double.MAX_VALUE);
        summaryLabel.setMinHeight(Region.USE_PREF_SIZE);

        GridPane gridPane = new GridPane();
        gridPane.getStyleClass().add("grid-pane");

        gridPane.add(nameLabel, 0, 0);
        gridPane.add(summaryLabel, 0, 1);
        gridPane.add(imageView, 1, 0);

        GridPane.setRowSpan(imageView, 2);

        GridPane.setHgrow(nameLabel, Priority.ALWAYS);
        GridPane.setHgrow(summaryLabel, Priority.ALWAYS);

        GridPane.setFillWidth(nameLabel, true);
        GridPane.setFillWidth(summaryLabel, true);

        GridPane.setValignment(nameLabel, VPos.TOP);
        GridPane.setValignment(summaryLabel, VPos.TOP);
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
            summaryLabel.setText(tool.getSummary());
        }
    }
}
