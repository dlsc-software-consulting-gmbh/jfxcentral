package com.dlsc.jfxcentral.views.detail.cells;

import com.dlsc.jfxcentral.views.AdvancedListCell;
import javafx.scene.control.Label;
import org.kordamp.ikonli.javafx.FontIcon;
import org.kordamp.ikonli.materialdesign.MaterialDesign;

public abstract class DetailCell<T> extends AdvancedListCell<T> {

    protected DetailCell() {
        getStyleClass().add("detail-cell");
        setMaxWidth(Double.MAX_VALUE);
    }

    protected void addLinkIcon(Label label) {
        FontIcon icon = new FontIcon(MaterialDesign.MDI_OPEN_IN_NEW);
        icon.getStyleClass().add("open-link");

        Label l = new Label();
        l.getStyleClass().add("details-label");
        l.setGraphic(icon);

        label.setGraphicTextGap(10);
        label.setGraphic(l);
    }
}
