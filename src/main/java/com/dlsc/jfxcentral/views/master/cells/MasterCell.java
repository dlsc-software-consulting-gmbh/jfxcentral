package com.dlsc.jfxcentral.views.master.cells;

import com.dlsc.jfxcentral.data.model.ModelObject;
import com.dlsc.jfxcentral.util.Util;
import com.dlsc.jfxcentral.views.AdvancedListCell;
import com.dlsc.jfxcentral.views.View;
import com.jpro.webapi.WebAPI;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.ListCell;

import java.lang.reflect.Method;

public class MasterCell<T> extends AdvancedListCell<T> {

    public MasterCell() {
        getStyleClass().add("master-cell");
        setMaxWidth(Double.MAX_VALUE);
    }

    public void setMasterCellLink(ListCell cell, ModelObject item, String description, View view) {
        try {
            ObservableList<Node> children2 = null;
            if (WebAPI.isBrowser()) {
                Method method = Parent.class.getDeclaredMethod("getChildren");
                method.setAccessible(true);
                children2 = (ObservableList<Node>) method.invoke(cell.getParent());
            }
            Util.setLink(cell, "/" + view.toString() + "/" + item.getId(), description, children2);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
