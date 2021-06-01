package com.dlsc.jfxcentral.views.master;

import com.dlsc.jfxcentral.model.ModelObject;
import com.dlsc.jfxcentral.util.Util;
import com.dlsc.jfxcentral.views.RootPane;
import com.dlsc.jfxcentral.views.View;
import javafx.beans.binding.Bindings;
import javafx.beans.property.*;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.scene.Node;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;

import java.util.Comparator;
import java.util.Objects;
import java.util.function.Predicate;

public abstract class MasterView<T extends ModelObject> extends BorderPane {

    private final RootPane rootPane;

    private final View view;

    protected MasterView(RootPane rootPane, View view) {
        this.rootPane = Objects.requireNonNull(rootPane);
        this.view = Objects.requireNonNull(view);

        getStyleClass().add("master-view");

        MasterViewHeader header = new MasterViewHeader();
        header.filterTextProperty().bindBidirectional(filterTextProperty());
        setTop(header);
    }

    protected void performDefaultSelection(ListView listView) {
        if (!listView.getItems().isEmpty()) {
            listView.getSelectionModel().select(0);
        } else {
            listView.getSelectionModel().clearSelection();
        }
    }

    protected void bindListViewToSelectedItem(ListView<T> listView) {
        listView.getSelectionModel().selectedItemProperty().addListener(it -> setSelectedItem(listView.getSelectionModel().getSelectedItem()));
        selectedItemProperty().addListener(it -> listView.getSelectionModel().select(getSelectedItem()));
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

    private ObjectProperty<T> selectedItem = new SimpleObjectProperty<>(this, "selectedItem");

    public T getSelectedItem() {
        return selectedItem.getValue();
    }

    public void setSelectedItem(T selectedItem) {
        this.selectedItem.set(selectedItem);
    }

    public Property<T> selectedItemProperty() {
        return selectedItem;
    }

    public void setCellLink(Node cell, T item, ObservableList<Node> children) {
        Util.setLink(cell, "/?page=/" + view.toString() + "/" + item.getId(), item.getId(), children);
    }
}
