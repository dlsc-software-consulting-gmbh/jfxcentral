package com.dlsc.jfxcentral.views.master.cells;

import com.dlsc.jfxcentral.views.AdvancedListCell;

public class MasterCell<T> extends AdvancedListCell<T> {

    public MasterCell() {
        getStyleClass().add("master-cell");
        setMaxWidth(Double.MAX_VALUE);
    }
}
