package com.dlsc.jfxcentral.views.master.cells;

import com.dlsc.jfxcentral.JFXCentralApp;
import com.dlsc.jfxcentral.data.model.Tip;
import com.dlsc.jfxcentral.views.MarkdownView;
import com.dlsc.jfxcentral.views.View;
import com.jpro.webapi.WebAPI;
import javafx.geometry.Pos;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

public class MasterTipCell extends MasterCell<Tip> {

    private final Label nameLabel = new Label();
    private final ImageView imageView = new ImageView();
    private final MarkdownView markdownView = new MarkdownView();
    private final Image image = new Image(JFXCentralApp.class.getResource("dlsc-logo.png").toExternalForm());

    public MasterTipCell() {
        getStyleClass().add("master-tip-list-cell");

        setPrefWidth(0);

        imageView.visibleProperty().bind(imageView.imageProperty().isNotNull());
        imageView.managedProperty().bind(imageView.imageProperty().isNotNull());
        imageView.setImage(image);
        imageView.setFitHeight(40);
        imageView.setFitWidth(40);
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
    protected void updateItem(Tip tip, boolean empty) {
        super.updateItem(tip, empty);

        if (!empty && tip != null) {
            nameLabel.setText(tip.getName());
            markdownView.setMdString(tip.getSummary());
            imageView.setImage(image);

            if (WebAPI.isBrowser()) {
                setMouseTransparent(true);
            }

            setMasterCellLink(MasterTipCell.this, tip, tip.getName(), View.TIPS);
        } else {
            nameLabel.setText("");
            imageView.setImage(null);
            markdownView.setMdString("");
        }
    }
}
