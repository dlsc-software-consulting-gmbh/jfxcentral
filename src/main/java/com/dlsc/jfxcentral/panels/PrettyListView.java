package com.dlsc.jfxcentral.panels;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollBar;

import java.util.Set;

public class PrettyListView<T> extends ListView<T> {

    private final ScrollBar vBar = new ScrollBar();
    private final ScrollBar hBar = new ScrollBar();

    public PrettyListView() {
        super();

        skinProperty().addListener(it -> {
            // first bind, then add new scrollbars, otherwise the new bars will be found
            bindScrollBars();
            getChildren().addAll(vBar, hBar);
        });

        getStyleClass().add("pretty-list-view");

        vBar.setManaged(false);
        vBar.setOrientation(Orientation.VERTICAL);
        vBar.getStyleClass().add("pretty-scroll-bar");
        vBar.visibleProperty().bind(vBar.visibleAmountProperty().isEqualTo(0).not().or(alwaysShowVBarProperty()));

        hBar.setManaged(false);
        hBar.setOrientation(Orientation.HORIZONTAL);
        hBar.getStyleClass().add("pretty-scroll-bar");
        hBar.visibleProperty().bind(hBar.visibleAmountProperty().isEqualTo(0).not().or(alwaysShowHBarProperty()));
    }

    public ScrollBar getVerticalBar() {
        return vBar;
    }

    public ScrollBar getHorizontalBar() {
        return hBar;
    }

    private final BooleanProperty alwaysShowVBar = new SimpleBooleanProperty(false);

    public boolean isAlwaysShowVBar() {
        return alwaysShowVBar.get();
    }

    public BooleanProperty alwaysShowVBarProperty() {
        return alwaysShowVBar;
    }

    public void setAlwaysShowVBar(boolean alwaysShowVBar) {
        this.alwaysShowVBar.set(alwaysShowVBar);
    }

    private final BooleanProperty alwaysShowHBar = new SimpleBooleanProperty(false);

    public boolean isAlwaysShowHBar() {
        return alwaysShowHBar.get();
    }

    public BooleanProperty alwaysShowHBarProperty() {
        return alwaysShowHBar;
    }

    public void setAlwaysShowHBar(boolean alwaysShowHBar) {
        this.alwaysShowHBar.set(alwaysShowHBar);
    }

    private void bindScrollBars() {
        Set<Node> nodes = lookupAll("VirtualScrollBar");
        for (Node node : nodes) {
            if (node instanceof ScrollBar) {
                ScrollBar bar = (ScrollBar) node;
                if (bar.getOrientation().equals(Orientation.VERTICAL)) {
                    bindScrollBars(vBar, bar);
                } else if (bar.getOrientation().equals(Orientation.HORIZONTAL)) {
                    bindScrollBars(hBar, bar);
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

    private final ObjectProperty<Insets> verticalScrollBarPadding = new SimpleObjectProperty<>(this, "verticalScrollBarPadding", new Insets(2, 5, 2, 0));

    public Insets getVerticalScrollBarPadding() {
        return verticalScrollBarPadding.get();
    }

    public ObjectProperty<Insets> verticalScrollBarPaddingProperty() {
        return verticalScrollBarPadding;
    }

    public void setVerticalScrollBarPadding(Insets verticalScrollBarPadding) {
        this.verticalScrollBarPadding.set(verticalScrollBarPadding);
    }

    private final ObjectProperty<Insets> horizontalScrollBarPadding = new SimpleObjectProperty<>(this, "horizontalScrollBarPadding", new Insets(0, 2, 5, 2));

    public Insets getHorizontalScrollBarPadding() {
        return horizontalScrollBarPadding.get();
    }

    public ObjectProperty<Insets> horizontalScrollBarPaddingProperty() {
        return horizontalScrollBarPadding;
    }

    public void setHorizontalScrollBarPadding(Insets horizontalScrollBarPadding) {
        this.horizontalScrollBarPadding.set(horizontalScrollBarPadding);
    }

    @Override
    protected void layoutChildren() {
        super.layoutChildren();

        Insets insets = getInsets();
        double w = getWidth();
        double h = getHeight();

        double prefWidth = vBar.prefWidth(-1);
        vBar.resizeRelocate(w - prefWidth - insets.getRight() - getVerticalScrollBarPadding().getRight(), insets.getTop() + getVerticalScrollBarPadding().getTop(), prefWidth, h - insets.getTop() - insets.getBottom() - getVerticalScrollBarPadding().getTop() - getVerticalScrollBarPadding().getBottom());

        double prefHeight = hBar.prefHeight(-1);
        hBar.resizeRelocate(insets.getLeft() + getHorizontalScrollBarPadding().getLeft(), h - prefHeight - insets.getBottom() - getHorizontalScrollBarPadding().getBottom(), w - insets.getLeft() - insets.getRight() - getHorizontalScrollBarPadding().getLeft() - getHorizontalScrollBarPadding().getRight(), prefHeight);
    }
}
