package com.dlsc.jfxcentral.views.mobile.master.cells;

import com.dlsc.jfxcentral.JFXCentralApp;
import com.dlsc.jfxcentral.data.model.Tip;
import com.dlsc.jfxcentral.views.MarkdownView;
import com.dlsc.jfxcentral.views.View;
import com.dlsc.jfxcentral.views.mobile.MobileAdvancedListCell;
import javafx.geometry.Pos;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

public class MobileMasterTipsCell extends MobileAdvancedListCell<Tip> {

    private ImageView imageView = new ImageView();
    private Label label = new Label();
    private MarkdownView markdownView = new MarkdownView();

    public MobileMasterTipsCell() {
        getStyleClass().add("mobile-master-tips-cell");

        setPrefWidth(0);
        setMinWidth(0);

        imageView.setFitWidth(50);
        imageView.setPreserveRatio(true);
        imageView.setImage(new Image(JFXCentralApp .class.getResource("tip-round.jpg").toExternalForm()));

        label.getStyleClass().add("title-label");
        label.setWrapText(true);
        label.setMinHeight(Region.USE_PREF_SIZE);

        VBox vbox = new VBox(label, markdownView);
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
            label.setText(tip.getName());
            markdownView.setMdString(tip.getSummary());
            setMasterCellLink(MobileMasterTipsCell.this, tip, tip.getSummary(), View.TIPS);
        } else {
            label.setText("");
            markdownView.setMdString("");
        }
    }
}
