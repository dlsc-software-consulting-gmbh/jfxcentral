package com.dlsc.jfxcentral.views.detail;

import com.dlsc.jfxcentral.data.model.ModelObject;
import com.dlsc.jfxcentral.panels.PrettyListView;
import com.dlsc.jfxcentral.views.RootPane;
import javafx.application.Platform;

public class DetailViewWithListView<T extends ModelObject> extends DetailView<T> {

    protected PrettyListView<T> listView = new PrettyListView<>();

    public DetailViewWithListView(RootPane rootPane) {
        super(rootPane);

        selectedItemProperty().addListener(it -> {
            T selectedItem = getSelectedItem();
            listView.getSelectionModel().select(selectedItem);
        });
    }

    public void showItem(T item) {
        Platform.runLater(() -> {
            setSelectedItem(item);
            listView.scrollTo(item);
        });
    }
}
