package com.dlsc.jfxcentral.views.master.cells;

import com.dlsc.jfxcentral.data.model.Tip;
import com.dlsc.jfxcentral.views.View;
import com.jpro.webapi.WebAPI;
import javafx.scene.control.ContentDisplay;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

public class MasterTipCell extends MasterCell<Tip> {

    Text label = new Text();

    public MasterTipCell() {
        getStyleClass().add("master-tip-list-cell");
        label.getStyleClass().add("headline");
        TextFlow flow = new TextFlow(label);
        flow.getStyleClass().add("textflow");
        setGraphic(flow);
        setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
    }

    @Override
    protected void updateItem(Tip tip, boolean empty) {
        super.updateItem(tip, empty);

        if (!empty && tip != null) {
            label.setText(tip.getName());

            if (WebAPI.isBrowser()) {
                setMouseTransparent(true);
            }

            setMasterCellLink(MasterTipCell.this, tip, tip.getName(), View.TIPS);
        } else {
            label.setText("");
        }
    }
}
