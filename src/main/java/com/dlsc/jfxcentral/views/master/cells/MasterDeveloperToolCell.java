package com.dlsc.jfxcentral.views.master.cells;

import com.dlsc.jfxcentral.JFXCentralApp;
import com.dlsc.jfxcentral.util.DeveloperTool;
import com.dlsc.jfxcentral.views.MarkdownView;
import com.dlsc.jfxcentral.views.View;
import com.jpro.webapi.WebAPI;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;

public class MasterDeveloperToolCell extends MasterCell<DeveloperTool> {

    private final Label nameLabel = new Label();
    private final ImageView imageView = new ImageView();
    private final MarkdownView summaryMarkdownView = new MarkdownView();
    private final GridPane gridPane;

    public MasterDeveloperToolCell() {
        getStyleClass().add("master-app-list-cell");
        getStyleClass().add("master-developer-list-cell");

        setPrefWidth(0);

        imageView.visibleProperty().bind(imageView.imageProperty().isNotNull());
        imageView.managedProperty().bind(imageView.imageProperty().isNotNull());
        imageView.setFitWidth(200);
        imageView.setFitHeight(120);

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

//        gridPane.visibleProperty().bind(emptyProperty().not());
    }

    @Override
    protected void updateItem(DeveloperTool tool, boolean empty) {
        super.updateItem(tool, empty);

        if (!empty && tool != null) {
            nameLabel.setText(tool.getName());
            imageView.setImage(getImage(tool));
            summaryMarkdownView.setMdString(tool.getSummary());

            if (WebAPI.isBrowser()) {
                setMouseTransparent(true);
            }

            setMasterCellLink(this, tool, tool.getName(), View.DEVELOPMENT);
        } else {
            nameLabel.setText("");
            imageView.setImage(null);
            summaryMarkdownView.setMdString("");
        }
    }

    private Image getImage(DeveloperTool tool) {
        switch (tool.getTool()) {
            case CSS_DOCS:
                return new Image(JFXCentralApp.class.getResource("devtools/cssdocs.jpg").toExternalForm());
            case IKONLI:
                return new Image(JFXCentralApp.class.getResource("devtools/iconbrowser.png").toExternalForm());
            case CSS_SHOWCASE:
                return new Image(JFXCentralApp.class.getResource("devtools/showcasefx.jpg").toExternalForm());
        }

        return null;
    }
}
