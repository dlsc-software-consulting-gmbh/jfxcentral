package com.dlsc.jfxcentral.views.master;

import com.dlsc.jfxcentral.data.model.ModelObject;
import com.dlsc.jfxcentral.views.RootPane;
import com.dlsc.jfxcentral.views.View;
import javafx.animation.FadeTransition;
import javafx.application.Platform;
import javafx.beans.Observable;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollBar;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;

import java.util.Set;

public abstract class MasterViewWithListView<T extends ModelObject> extends MasterView<T> {

    protected final ListView<T> listView = new ListView<>();
    private final ScrollBar scrollBar;

    public MasterViewWithListView(RootPane rootPane, View view) {
        super(rootPane, view);

        bindListViewToSelectedItem();

        listView.itemsProperty().addListener((Observable it) -> performDefaultSelection());
        performDefaultSelection();

        scrollBar = new ScrollBar();
        scrollBar.setOrientation(Orientation.VERTICAL);
        scrollBar.getStyleClass().add("left-scroll-bar");
        AnchorPane anchorPane = new AnchorPane(listView, scrollBar);

        AnchorPane.setTopAnchor(scrollBar, 5d);
        AnchorPane.setBottomAnchor(scrollBar, 5d);
        AnchorPane.setLeftAnchor(scrollBar, 5d);

        AnchorPane.setTopAnchor(listView, 0d);
        AnchorPane.setBottomAnchor(listView, 0d);
        AnchorPane.setLeftAnchor(listView, 0d);
        AnchorPane.setRightAnchor(listView, 0d);

        setCenter(anchorPane);

        listView.skinProperty().addListener(it -> bindScrollBars());

        scrollBar.setOpacity(0);

        FadeTransition fadeTransition = new FadeTransition(Duration.millis(300), scrollBar);
        fadeTransition.setFromValue(0);

        anchorPane.hoverProperty().addListener(it -> {
            if (anchorPane.isHover()) {
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

    private void bindScrollBars() {
        Set<Node> nodes = lookupAll("VirtualScrollBar");
        for (Node node : nodes) {
            if (node instanceof ScrollBar) {
                ScrollBar bar = (ScrollBar) node;
                if (bar.getOrientation().equals(Orientation.VERTICAL)) {
                    bindScrollBars(scrollBar, bar);
                }
            }
        }
    }

    private void bindScrollBars(ScrollBar scrollBarA, ScrollBar scrollBarB) {
        scrollBarA.valueProperty().bindBidirectional(scrollBarB.valueProperty());
        scrollBarA.minProperty().bindBidirectional(scrollBarB.minProperty());
        scrollBarA.maxProperty().bindBidirectional(scrollBarB.maxProperty());
        scrollBarA.visibleAmountProperty().bindBidirectional(scrollBarB.visibleAmountProperty());
        scrollBarA.unitIncrementProperty().bindBidirectional(scrollBarB.unitIncrementProperty());
        scrollBarA.blockIncrementProperty().bindBidirectional(scrollBarB.blockIncrementProperty());
    }
}
