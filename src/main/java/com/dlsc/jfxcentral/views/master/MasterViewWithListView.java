package com.dlsc.jfxcentral.views.master;

import com.dlsc.jfxcentral.data.model.ModelObject;
import com.dlsc.jfxcentral.views.RootPane;
import com.dlsc.jfxcentral.views.View;
import javafx.application.Platform;
import javafx.beans.Observable;
import javafx.scene.control.ListView;

public abstract class MasterViewWithListView<T extends ModelObject> extends MasterView<T> {

    protected final ListView<T> listView = new ListView<>();

    public MasterViewWithListView(RootPane rootPane, View view) {
        super(rootPane, view);

        bindListViewToSelectedItem();

        listView.itemsProperty().addListener((Observable it) -> performDefaultSelection());
        performDefaultSelection();
    }

    public void showItem(T item) {
        setSelectedItem(item);
        Platform.runLater(() -> listView.scrollTo(item));
    }

    protected void performDefaultSelection() {
        if (!listView.getItems().isEmpty()) {
            listView.getSelectionModel().select(0);
        } else {
            listView.getSelectionModel().clearSelection();
        }
    }

    protected void bindListViewToSelectedItem() {
        listView.getSelectionModel().selectedItemProperty().addListener(it -> {
            setSelectedItem(listView.getSelectionModel().getSelectedItem());
        });

        selectedItemProperty().addListener(it -> {
            T selectedItem = getSelectedItem();
            listView.getSelectionModel().select(selectedItem);
        });
    }
}
