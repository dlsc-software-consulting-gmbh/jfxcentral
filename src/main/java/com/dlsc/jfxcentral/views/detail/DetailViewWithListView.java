package com.dlsc.jfxcentral.views.detail;

import com.dlsc.jfxcentral.data.model.ModelObject;
import com.dlsc.jfxcentral.views.AdvancedListView;
import com.dlsc.jfxcentral.views.RootPane;
import com.dlsc.jfxcentral.views.View;
import javafx.application.Platform;

public class DetailViewWithListView<T extends ModelObject> extends DetailView<T> {

    protected AdvancedListView<T> listView = new AdvancedListView<>();

    public DetailViewWithListView(RootPane rootPane, View view) {
        super(rootPane, view);

        selectedItemProperty().addListener(it -> {
            T selectedItem = getSelectedItem();
            listView.getSelectionModel().select(selectedItem);
        });
    }

    public void showItem(T item) {
        Platform.runLater(() -> {
            setSelectedItem(item);
            listView.getListView().scrollTo(item);
        });
    }
}
