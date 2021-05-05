package com.dlsc.jfxcentral.views;

import javafx.scene.Node;
import javafx.scene.control.ListCell;

public class AdvancedListCell<T> extends ListCell<T> {

    public AdvancedListCell() {
        getStyleClass().add("advanced-list-cell");
        setMaxWidth(Double.MAX_VALUE);
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
            height += child.prefHeight(getWidth());
        }
        return height + getInsets().getTop() + getInsets().getBottom();
    }
}
