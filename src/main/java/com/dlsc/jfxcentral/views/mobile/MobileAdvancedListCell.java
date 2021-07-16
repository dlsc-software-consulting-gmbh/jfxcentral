package com.dlsc.jfxcentral.views.mobile;

import com.dlsc.jfxcentral.data.model.ModelObject;
import com.dlsc.jfxcentral.util.PageUtil;
import com.dlsc.jfxcentral.views.AdvancedListCell;
import com.jpro.web.Util;

public abstract class MobileAdvancedListCell<T extends ModelObject> extends AdvancedListCell<T> {

    protected MobileAdvancedListCell() {
        getStyleClass().add("mobile-cell");

        setOnMouseClicked(evt -> {
            if (evt.isStillSincePress()) {
                Util.gotoPage(this, PageUtil.getLink(getItem()));
            }
        });
    }
}
