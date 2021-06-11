package com.dlsc.jfxcentral.views.detail.cells;

import com.dlsc.jfxcentral.views.AdvancedListCell;

public abstract class DetailCell<T> extends AdvancedListCell<T> {

    protected DetailCell() {
        getStyleClass().add("detail-cell");
        setMaxWidth(Double.MAX_VALUE);
    }
}
