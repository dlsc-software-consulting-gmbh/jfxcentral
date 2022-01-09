package com.dlsc.jfxcentral.views.master.cells;

import com.dlsc.jfxcentral.util.DeveloperTool;
import com.dlsc.jfxcentral.views.View;
import com.jpro.webapi.WebAPI;
import javafx.scene.control.ContentDisplay;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

public class MasterDeveloperToolCell extends MasterCell<DeveloperTool> {

    Text label = new Text();

    public MasterDeveloperToolCell() {
        getStyleClass().add("master-developer-list-cell");
        label.getStyleClass().add("headline");
        TextFlow flow = new TextFlow(label);
        flow.getStyleClass().add("textflow");
        setGraphic(flow);
        setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
    }

    @Override
    protected void updateItem(DeveloperTool tool, boolean empty) {
        super.updateItem(tool, empty);

        if (!empty && tool != null) {
            label.setText(tool.getName());

            if (WebAPI.isBrowser()) {
                setMouseTransparent(true);
            }

            setMasterCellLink(MasterDeveloperToolCell.this, tool, tool.getName(), View.DEVELOPMENT);
        } else {
            label.setText("");
        }
    }
}
