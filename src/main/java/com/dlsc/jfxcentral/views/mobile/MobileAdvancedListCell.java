package com.dlsc.jfxcentral.views.mobile;

import com.dlsc.jfxcentral.data.model.ModelObject;
import com.dlsc.jfxcentral.views.AdvancedListCell;

public abstract class MobileAdvancedListCell<T extends ModelObject> extends AdvancedListCell<T> {

    protected MobileAdvancedListCell() {
        getStyleClass().add("mobile-cell");
    }
}
