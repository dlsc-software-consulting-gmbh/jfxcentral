package com.dlsc.jfxcentral.views.mobile;

import com.dlsc.jfxcentral.data.model.ModelObject;
import com.dlsc.jfxcentral.util.PageUtil;
import com.dlsc.jfxcentral.util.Util;
import com.dlsc.jfxcentral.views.AdvancedListCell;
import com.jpro.webapi.WebAPI;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.Parent;

import java.lang.reflect.Method;

public abstract class MobileAdvancedListCell<T extends ModelObject> extends AdvancedListCell<T> {

    protected MobileAdvancedListCell() {
        getStyleClass().add("mobile-cell");



        itemProperty().addListener((p,o,item) -> {
            if(item != null) {
                try {
                    ObservableList<Node> children2 = null;
                    if (WebAPI.isBrowser()) {
                        Method method = Parent.class.getDeclaredMethod("getChildren");
                        method.setAccessible(true);
                        children2 = (ObservableList<Node>) method.invoke(this.getParent());
                    }
                    Util.setLink(this, PageUtil.getLink(item), "description", children2);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });


        //setOnMouseClicked(evt -> {
        //    if (evt.isStillSincePress()) {
        //        Util.gotoPage(this, PageUtil.getLink(getItem()));
        //    }
        //});
    }
}
