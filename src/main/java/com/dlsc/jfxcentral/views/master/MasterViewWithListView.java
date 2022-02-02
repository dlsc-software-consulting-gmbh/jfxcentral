package com.dlsc.jfxcentral.views.master;

import com.dlsc.jfxcentral.data.model.ModelObject;
import com.dlsc.jfxcentral.panels.PrettyListView;
import com.dlsc.jfxcentral.views.AdvancedListView;
import com.dlsc.jfxcentral.views.RootPane;
import com.dlsc.jfxcentral.views.View;
import javafx.application.Platform;
import javafx.beans.Observable;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollBar;

public abstract class MasterViewWithListView<T extends ModelObject> extends MasterView<T> {

    protected final ListView<T> listView;
    private final ScrollBar scrollBar;
//    private FadeTransition fadeTransition;


    public MasterViewWithListView(RootPane rootPane, View view) {
        super(rootPane, view);

        if (false) { //rootPane.getDisplay().equals(Display.WEB)) {
            AdvancedListView<T> advancedListView = new AdvancedListView<>();
            advancedListView.setPaging(true);
            advancedListView.setVisibleRowCount(20);
            setCenter(advancedListView);
            listView = advancedListView.getListView();
        } else {

            PrettyListView prettyListView = new PrettyListView<>();
            setCenter(prettyListView);
            listView = prettyListView;

            scrollBar = prettyListView.getVerticalBar();
            if (scrollBar != null) {
                scrollBar.setOpacity(0);

//                fadeTransition = new FadeTransition(Duration.millis(300), scrollBar);
//                fadeTransition.setDelay(Duration.millis(100));
//                fadeTransition.setFromValue(0);

                scrollBar.valueProperty().addListener(it -> showScrollBar(true));
                hoverProperty().addListener(it -> showScrollBar(isHover()));
            }
        }

        bindListViewToSelectedItem();

        listView.itemsProperty().addListener((Observable it) -> performDefaultSelection());
        performDefaultSelection();
    }

    private void showScrollBar(boolean show) {
        scrollBar.setOpacity(show ? 1 : 0);
    }

    public void showItem(T item) {
        if (item == null) {
            Platform.runLater(() -> performDefaultSelection());
        } else {
            setSelectedItem(item);
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
        listView.getSelectionModel().selectedItemProperty().addListener(it -> {
            setSelectedItem(listView.getSelectionModel().getSelectedItem());
        });

        selectedItemProperty().addListener(it -> {
            T selectedItem = getSelectedItem();
            listView.getSelectionModel().select(selectedItem);
        });
    }
}
