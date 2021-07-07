package com.dlsc.jfxcentral.views.master.cells;

import com.dlsc.jfxcentral.data.model.Tip;
import com.dlsc.jfxcentral.views.View;
import com.jpro.webapi.WebAPI;
import javafx.scene.layout.Region;

public class MasterTipCell extends MasterCell<Tip> {

    public MasterTipCell() {
        getStyleClass().add("master-tool-list-cell");

        setPrefWidth(0);

        setMinWidth(0);
        setWrapText(true);
        setMinHeight(Region.USE_PREF_SIZE);
    }

    @Override
    protected void updateItem(Tip tip, boolean empty) {
        super.updateItem(tip, empty);

        if (!empty && tip != null) {
            setText(tip.getName());

            if (WebAPI.isBrowser()) {
                setMouseTransparent(true);
            }

            setMasterCellLink(MasterTipCell.this, tip, tip.getName(), View.TIPS);
        } else {
            setText("");
        }
    }
}
