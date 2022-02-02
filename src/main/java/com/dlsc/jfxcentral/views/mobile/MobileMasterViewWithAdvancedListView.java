package com.dlsc.jfxcentral.views.mobile;

import com.dlsc.jfxcentral.data.model.ModelObject;
import com.dlsc.jfxcentral.util.EmptySelectionModel;
import com.dlsc.jfxcentral.views.AdvancedListView;
import com.dlsc.jfxcentral.views.RootPane;
import com.dlsc.jfxcentral.views.View;
import com.dlsc.jfxcentral.views.master.MasterView;
import javafx.application.Platform;
import javafx.beans.Observable;

public abstract class MobileMasterViewWithAdvancedListView<T extends ModelObject> extends MasterView<T> {

    protected final AdvancedListView<T> listView = new AdvancedListView<>();

    public MobileMasterViewWithAdvancedListView(RootPane rootPane, View view) {
        super(rootPane, view);

        bindListViewToSelectedItem();

        listView.getListView().setSelectionModel(new EmptySelectionModel<>());
        listView.itemsProperty().addListener((Observable it) -> performDefaultSelection());
        performDefaultSelection();
    }

    public void showItem(T item) {
        if (item == null) {
            Platform.runLater(() -> performDefaultSelection());
        } else {
            setSelectedItem(item);
            Platform.runLater(() -> listView.getListView().scrollTo(item));
        }
    }

    protected void performDefaultSelection() {
        if (!listView.getItems().isEmpty()) {
            listView.getSelectionModel().select(0);
        } else {
            listView.getSelectionModel().clearSelection();
        }
    }

    protected void bindListViewToSelectedItem() {
        listView.getSelectionModel().selectedItemProperty().addListener(it -> setSelectedItem(listView.getSelectionModel().getSelectedItem()));

        selectedItemProperty().addListener(it -> {
            T selectedItem = getSelectedItem();
            listView.getSelectionModel().select(selectedItem);
        });
    }
}
