package com.dlsc.jfxcentral.views.detail.cells;

import com.dlsc.jfxcentral.data.model.Tip;
import com.dlsc.jfxcentral.views.AdvancedListCell;
import com.dlsc.jfxcentral.views.MarkdownView;
import com.dlsc.jfxcentral.views.RootPane;
import javafx.geometry.Pos;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;

public class DetailTipCell extends AdvancedListCell<Tip> {

    private Label titleLabel = new Label();
    private MarkdownView descriptionMarkdownView = new MarkdownView();

    private ImageView logoImageView = new ImageView();
    private HBox buttonBox = new HBox();

    public DetailTipCell(RootPane rootPane) {
        getStyleClass().add("detail-tool-cell");

        titleLabel.getStyleClass().addAll("header2", "title-label");
        titleLabel.setWrapText(true);
        titleLabel.setMinHeight(Region.USE_PREF_SIZE);
        titleLabel.setAlignment(Pos.TOP_LEFT);
        titleLabel.setContentDisplay(ContentDisplay.RIGHT);

        VBox vBox = new VBox(titleLabel, descriptionMarkdownView);
        vBox.getStyleClass().add("vbox");
        vBox.setAlignment(Pos.TOP_LEFT);

        HBox.setHgrow(vBox, Priority.ALWAYS);

        logoImageView.setFitWidth(48);
        logoImageView.setPreserveRatio(true);

        StackPane logoWrapper = new StackPane(logoImageView);
        logoWrapper.setMinWidth(48);
        logoWrapper.setMaxWidth(48);
        StackPane.setAlignment(logoImageView, Pos.TOP_LEFT);

        HBox hBox = new HBox(vBox, logoWrapper);
        hBox.setAlignment(Pos.TOP_LEFT);
        hBox.getStyleClass().add("hbox");

        VBox outerBox = new VBox(hBox, buttonBox);
        outerBox.visibleProperty().bind(itemProperty().isNotNull());
        outerBox.getStyleClass().add("outer-box");

        setGraphic(outerBox);
        setContentDisplay(ContentDisplay.GRAPHIC_ONLY);

        outerBox.visibleProperty().bind(emptyProperty().not());
    }

    @Override
    protected void updateItem(Tip item, boolean empty) {
        super.updateItem(item, empty);

        if (!empty && item != null) {
//            logoImageView.imageProperty().bind(ImageManager.getInstance().toolImageProperty(item));
            logoImageView.setVisible(true);
            titleLabel.setText(item.getName());
            descriptionMarkdownView.setMdString(item.getSummary());
        }
    }
}
