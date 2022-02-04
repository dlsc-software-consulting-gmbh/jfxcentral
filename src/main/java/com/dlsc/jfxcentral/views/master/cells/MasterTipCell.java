package com.dlsc.jfxcentral.views.master.cells;

import com.dlsc.jfxcentral.data.model.Tip;
import com.dlsc.jfxcentral.views.MarkdownView;
import com.dlsc.jfxcentral.views.View;
import com.jpro.webapi.WebAPI;
import javafx.geometry.Pos;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.Locale;

public class MasterTipCell extends MasterCell<Tip> {

    private final Label nameLabel = new Label();
    private final Label dateLabel = new Label();
    private final MarkdownView markdownView = new MarkdownView();

    public MasterTipCell() {
        getStyleClass().add("master-tip-list-cell");

        setPrefWidth(0);

        nameLabel.getStyleClass().add("name-label");
        nameLabel.setMinWidth(0);
        nameLabel.setWrapText(true);
        nameLabel.setMinHeight(Region.USE_PREF_SIZE);

        dateLabel.getStyleClass().add("date-label");
        dateLabel.setMinWidth(Region.USE_PREF_SIZE);

        VBox vbox = new VBox(dateLabel, nameLabel, markdownView);
        vbox.getStyleClass().add("vbox");
        HBox.setHgrow(vbox, Priority.ALWAYS);

        HBox hBox = new HBox(vbox, dateLabel);
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
            LocalDate date = tip.getCreationOrUpdateDate();
            dateLabel.setText(date.getMonth().getDisplayName(TextStyle.SHORT, Locale.getDefault()) + "\n" + date.getDayOfMonth() + "\n" + date.getYear());
            dateLabel.setVisible(true);
            nameLabel.setText(tip.getName());
            markdownView.setMdString(tip.getSummary());

            if (WebAPI.isBrowser()) {
                setMouseTransparent(true);
            }

            setMasterCellLink(MasterTipCell.this, tip, tip.getName(), View.TIPS);
        } else {
            dateLabel.setText("");
            nameLabel.setText("");
            markdownView.setMdString("");
            dateLabel.setVisible(false);
        }
    }
}
