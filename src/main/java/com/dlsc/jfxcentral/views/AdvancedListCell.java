package com.dlsc.jfxcentral.views;

import com.dlsc.jfxcentral.data.model.ModelObject;
import com.dlsc.jfxcentral.util.Util;
import com.jpro.webapi.WebAPI;
import javafx.collections.ObservableList;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.ListCell;

import java.lang.reflect.Method;

public class AdvancedListCell<T> extends ListCell<T> {

    @Override
    public Orientation getContentBias() {
        return Orientation.HORIZONTAL;
    }

    public AdvancedListCell() {
        getStyleClass().add("advanced-list-cell");
        setMaxWidth(Double.MAX_VALUE);
        setPrefWidth(0);
    }

    protected void setLink(String url, String description) {
        try {
            ObservableList<Node> children2 = null;
            if (WebAPI.isBrowser()) {
                Method method = Parent.class.getDeclaredMethod("getChildren");
                method.setAccessible(true);
                children2 = (ObservableList<Node>) method.invoke(getParent());
            }
            Util.setLink(this, url, description, children2);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*
     * Super important. For some reason the default compute min height method for a list cell
     * will not work properly when the cell is used inside a VBox, which is what we do when we
     * use an AdvancedListView control.
     */
    @Override
    protected double computeMinHeight(double width) {
        double height = 0;
        for (Node child : getChildren()) {
            height += child.minHeight(width);
        }
        return height + getInsets().getTop() + getInsets().getBottom();
    }

    @Override
    protected double computePrefHeight(double width) {
        double height = 0;
        for (Node child : getChildren()) {
            height += child.prefHeight(width);
        }
        return height + getInsets().getTop() + getInsets().getBottom();
    }

    @Override
    protected double computeMaxHeight(double width) {
        double height = 0;
        for (Node child : getChildren()) {
            height += child.maxHeight(width);
        }
        return height + getInsets().getTop() + getInsets().getBottom();
    }

    public void setMasterCellLink(ListCell cell, ModelObject item, String description, View view) {
        try {
            ObservableList<Node> children2 = null;
            if (WebAPI.isBrowser()) {
                Method method = Parent.class.getDeclaredMethod("getChildren");
                method.setAccessible(true);
                children2 = (ObservableList<Node>) method.invoke(cell.getParent());
            }
            Util.setLink(cell, "/" + view.name().toLowerCase() + "/" + item.getId(), description, children2);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
