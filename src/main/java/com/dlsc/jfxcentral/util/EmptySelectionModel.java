package com.dlsc.jfxcentral.util;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.MultipleSelectionModel;

public class EmptySelectionModel<T> extends MultipleSelectionModel<T> {

    private final ObservableList<Integer> integers = FXCollections.observableArrayList();

    @Override
    public ObservableList<Integer> getSelectedIndices() {
        return integers;
    }

    @Override
    public ObservableList<T> getSelectedItems() {
        return null;
    }

    @Override
    public void selectIndices(int index, int... indices) {
    }

    @Override
    public void selectAll() {

    }

    @Override
    public void clearAndSelect(int index) {

    }

    @Override
    public void select(int index) {

    }

    @Override
    public void select(T obj) {

    }

    @Override
    public void clearSelection(int index) {

    }

    @Override
    public void clearSelection() {

    }

    @Override
    public boolean isSelected(int index) {
        return false;
    }

    @Override
    public boolean isEmpty() {
        return true;
    }

    @Override
    public void selectPrevious() {

    }

    @Override
    public void selectNext() {

    }

    @Override
    public void selectFirst() {

    }

    @Override
    public void selectLast() {

    }
}
