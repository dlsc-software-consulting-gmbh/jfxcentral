package com.dlsc.jfxcentral.categories;

import com.dlsc.jfxcentral.RootPane;
import com.dlsc.jfxcentral.View;
import com.dlsc.jfxcentral.model.ModelObject;
import com.dlsc.jfxcentral.util.Util;
import javafx.beans.binding.Bindings;
import javafx.beans.property.*;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.scene.Node;
import javafx.scene.control.ListCell;
import javafx.scene.layout.BorderPane;

import java.util.Comparator;
import java.util.Objects;
import java.util.function.Predicate;

public abstract class CategoryView<T extends ModelObject> extends BorderPane {

    private final RootPane rootPane;

    protected CategoryView(RootPane rootPane) {
        this.rootPane = Objects.requireNonNull(rootPane);

        CategoryHeader header = new CategoryHeader();
        header.filterTextProperty().bindBidirectional(filterTextProperty());
        setTop(header);
    }

    protected <T> FilteredList<T> createSortedAndFilteredList(ListProperty<T> booksProperty, Comparator<T> comparator, Predicate<T> predicate) {
        SortedList<T> sortedList = new SortedList<>(booksProperty);
        sortedList.setComparator(comparator);

        FilteredList<T> filteredList = new FilteredList<>(sortedList);
        filteredList.predicateProperty().bind(Bindings.createObjectBinding(() -> predicate, filterTextProperty()));

        return filteredList;
    }

    private StringProperty filterText = new SimpleStringProperty(this, "filterText");

    public String getFilterText() {
        return filterText.get();
    }

    public StringProperty filterTextProperty() {
        return filterText;
    }

    public void setFilterText(String filterText) {
        this.filterText.set(filterText);
    }

    public RootPane getRootPane() {
        return rootPane;
    }

    private Property<T> item = new SimpleObjectProperty<T>(null, "item");

    public T getItem() { return item.getValue(); }
    public void setItem(T x) { item.setValue(x); }
    public Property<T> itemProperty() { return item; }

    public abstract Node getDetailPane();

    abstract public View getView();

    public void setCellLink(Node cell, T item, ObservableList<Node> children) {
        Util.setLink(cell, "/?page=/" + getView().toString() + "/" + item.getId(), item.getId(), children);
    }
}
