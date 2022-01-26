package com.dlsc.jfxcentral.views.master;

import com.dlsc.jfxcentral.data.model.ModelObject;
import com.dlsc.jfxcentral.panels.PrettyListView;
import com.dlsc.jfxcentral.views.AdvancedListView;
import com.dlsc.jfxcentral.views.RootPane;
import com.dlsc.jfxcentral.views.View;
import javafx.animation.FadeTransition;
import javafx.beans.Observable;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollBar;
import javafx.util.Duration;

public abstract class MasterViewWithListView<T extends ModelObject> extends MasterView<T> {

    protected final ListView<T> listView;

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

            ScrollBar scrollBar = prettyListView.getVerticalBar();
            if (scrollBar != null) {
                scrollBar.setOpacity(0);

                FadeTransition fadeTransition = new FadeTransition(Duration.millis(300), scrollBar);
                fadeTransition.setDelay(Duration.millis(500));
                fadeTransition.setFromValue(0);

                hoverProperty().addListener(it2 -> {
                    if (isHover()) {
                        fadeTransition.setFromValue(0);
                        fadeTransition.setToValue(1);
                        fadeTransition.play();
                    } else {
                        fadeTransition.setFromValue(1);
                        fadeTransition.setToValue(0);
                        fadeTransition.play();
                    }
                });
            }
        }

        bindListViewToSelectedItem();

        listView.itemsProperty().addListener((Observable it) -> performDefaultSelection());
        performDefaultSelection();
    }

    public void showItem(T item) {
        System.out.println("selected item in master view: " + item);
        setSelectedItem(item);
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
