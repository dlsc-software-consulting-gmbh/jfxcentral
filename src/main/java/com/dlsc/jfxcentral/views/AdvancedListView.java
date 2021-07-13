package com.dlsc.jfxcentral.views;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.binding.Bindings;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.util.Callback;
import org.kordamp.ikonli.javafx.FontIcon;
import org.kordamp.ikonli.material.Material;

public class AdvancedListView<T> extends StackPane {

    public static final String PAGE_BUTTON = "page-button";
    private final ListView<T> listView = new ListView<>();

    private final VBox box = new VBox();

    private final HBox paginationBox = new HBox();

    public AdvancedListView() {
        getStyleClass().add("advanced-list-view");

        paginationBox.getStyleClass().add("pagination-box");
        paginationBox.visibleProperty().bind(pagingProperty().and(pageCountProperty().greaterThan(1)));
        paginationBox.managedProperty().bind(pagingProperty().and(pageCountProperty().greaterThan(1)));

        Bindings.bindBidirectional(listView.itemsProperty(), itemsProperty());
        listView.cellFactoryProperty().bindBidirectional(cellFactoryProperty());

        box.getStyleClass().add("list-view-replacement");

        placeholder.addListener(it -> updateView());

        InvalidationListener updateListener = (Observable it) -> {

            box.setVisible(isPaging());
            listView.setVisible(!isPaging());

            box.setManaged(box.isVisible());
            listView.setManaged(listView.isVisible());

            Node placeholder = getPlaceholder();
            if (placeholder != null) {
                placeholder.setVisible(getItems() == null || getItems().isEmpty());
                placeholder.setManaged(placeholder.isVisible());
            }

            if (isPaging() || (getItems().size() <= getVisibleRowCount())) {
                updateItems();
            }
        };

        items.addListener(updateListener);
        visibleRowCount.addListener(updateListener);
        paging.addListener(updateListener);
        page.addListener(updateListener);
        cellFactory.addListener(updateListener);

        updateView();

        pageCount.bind(Bindings.createIntegerBinding(() -> (int) Math.ceil((double) getItems().size() / (double) getVisibleRowCount()), itemsProperty(), visibleRowCountProperty()));

        InvalidationListener buildPaginationControlsListener = it -> buildPaginationControls();
        maxPageIndicatorCountProperty().addListener(buildPaginationControlsListener);
        pageCountProperty().addListener(buildPaginationControlsListener);
        pageProperty().addListener(buildPaginationControlsListener);
        startPage.addListener(buildPaginationControlsListener);

        page.addListener(it -> {
            if (getPage() < startPage.get()) {
                startPage.set(Math.max(0, startPage.get() - getMaxPageIndicatorCount()));
            } else if (getPage() > startPage.get() + getMaxPageIndicatorCount() - 1) {
                startPage.set(startPage.get() + getMaxPageIndicatorCount());
            }
        });
    }

    private final BooleanProperty showItemCounter = new SimpleBooleanProperty(this, "showItemCounter", true);

    public boolean isShowItemCounter() {
        return showItemCounter.get();
    }

    public BooleanProperty showItemCounterProperty() {
        return showItemCounter;
    }

    public void setShowItemCounter(boolean showItemCounter) {
        this.showItemCounter.set(showItemCounter);
    }

    private final IntegerProperty startPage = new SimpleIntegerProperty();

    private void buildPaginationControls() {
        paginationBox.getChildren().clear();

        Label counterLabel = new Label();
        counterLabel.getStyleClass().add("counter-label");
        counterLabel.textProperty().bind(Bindings.createStringBinding(() -> "Items: " + getItems().size(), getItems()));
        counterLabel.visibleProperty().bind(showItemCounterProperty());
        counterLabel.managedProperty().bind(showItemCounterProperty());
        paginationBox.getChildren().add(counterLabel);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        paginationBox.getChildren().add(spacer);

        Button previousButton = new Button("Prev");
        previousButton.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
        previousButton.setGraphic(new FontIcon(Material.ARROW_LEFT));
        previousButton.getStyleClass().add("previous-button");
        previousButton.setOnAction(evt -> setPage(Math.max(0, getPage() - 1)));
        previousButton.setMinWidth(Region.USE_PREF_SIZE);
        previousButton.visibleProperty().bind(pageProperty().greaterThan(0));

        paginationBox.getChildren().add(previousButton);

        int i;
        for (i = startPage.get(); i < Math.min(getPageCount(), startPage.get() + getMaxPageIndicatorCount()); i++) {
            final int page = i;
            Button pageButton = new Button(Integer.toString(i + 1));
            pageButton.setOnKeyPressed(evt -> {
                if (evt.getCode().equals(KeyCode.RIGHT)) {
                    setPage(Math.min(getPage() + 1, getPageCount() - 1));
                } else if (evt.getCode().equals(KeyCode.LEFT)) {
                    setPage(Math.max(0, getPage() - 1));
                }
            });

            pageButton.getStyleClass().add(PAGE_BUTTON);
            pageButton.setOnAction(evt -> setPage(page));
            if (page == getPage()) {
                pageButton.getStyleClass().add("current");
            }
            paginationBox.getChildren().add(pageButton);
        }

        if (i < getPageCount() - 1) {
            Label spacerLabel = new Label("...");
            spacerLabel.setMinWidth(Region.USE_PREF_SIZE);
            spacerLabel.getStyleClass().add("spacer");
            paginationBox.getChildren().add(spacerLabel);

            Button lastPageButton = new Button(Integer.toString(getPageCount()));
            lastPageButton.getStyleClass().add(PAGE_BUTTON);
            lastPageButton.setOnAction(evt -> setPage(getPageCount() - 1));
            paginationBox.getChildren().add(lastPageButton);
        }

        Button nextButton = new Button("Next");
        nextButton.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
        nextButton.setGraphic(new FontIcon(Material.ARROW_RIGHT));
        nextButton.getStyleClass().add("next-button");
        nextButton.setOnAction(evt -> setPage(Math.min(getPageCount() - 1, getPage() + 1)));
        nextButton.setMinWidth(Region.USE_PREF_SIZE);
        nextButton.visibleProperty().bind(pageProperty().lessThan(getPageCount() - 1));
        nextButton.managedProperty().bind(pageProperty().lessThan(getPageCount() - 1));

        paginationBox.getChildren().add(nextButton);
    }

    private void updateView() {
        Node placeholder = getPlaceholder();
        if (placeholder != null) {
            if (!placeholder.getStyleClass().contains("placeholder")) {
                placeholder.getStyleClass().add("placeholder");
            }
            getChildren().setAll(box, listView, placeholder);
        } else {
            getChildren().setAll(box, listView);
        }
    }

    @Override
    protected double computePrefHeight(double width) {
        double height = getInsets().getTop() + getInsets().getBottom();

        if (box.isManaged()) {
            height += box.prefHeight(width);
        }

        if (listView.isManaged()) {
            height += listView.prefHeight(width);
        }

        if (!box.isManaged() && !listView.isManaged() && getPlaceholder() != null) {
            height += getPlaceholder().prefHeight(width);
        }

        return height;
    }

    private final ReadOnlyIntegerWrapper pageCount = new ReadOnlyIntegerWrapper(this, "pageCount", 0);

    public final int getPageCount() {
        return pageCount.get();
    }

    public final ReadOnlyIntegerWrapper pageCountProperty() {
        return pageCount;
    }

    public final void setPageCount(int pageCount) {
        this.pageCount.set(pageCount);
    }

    private final IntegerProperty maxPageIndicatorCount = new SimpleIntegerProperty(this, "maxPageIndicatorCount", 5);

    public int getMaxPageIndicatorCount() {
        return maxPageIndicatorCount.get();
    }

    public IntegerProperty maxPageIndicatorCountProperty() {
        return maxPageIndicatorCount;
    }

    public void setMaxPageIndicatorCount(int maxPageIndicatorCount) {
        this.maxPageIndicatorCount.set(maxPageIndicatorCount);
    }

    private final IntegerProperty page = new SimpleIntegerProperty(this, "page", 0);

    public int getPage() {
        return page.get();
    }

    public IntegerProperty pageProperty() {
        return page;
    }

    public void setPage(int page) {
        this.page.set(page);
    }

    private final BooleanProperty paging = new SimpleBooleanProperty(this, "paging", false);

    public boolean isPaging() {
        return paging.get();
    }

    public BooleanProperty pagingProperty() {
        return paging;
    }

    public void setPaging(boolean paging) {
        this.paging.set(paging);
    }

    public final ListView<T> getListView() {
        return listView;
    }

    public MultipleSelectionModel<T> getSelectionModel() {
        return getListView().getSelectionModel();
    }

    private final ObjectProperty<Node> placeholder = new SimpleObjectProperty<>(this, "placeholder", new Label("No items"));

    public final ObjectProperty<Node> placeholderProperty() {
        return placeholder;
    }

    public final void setPlaceholder(Node placeholder) {
        this.placeholder.set(placeholder);
    }

    public final Node getPlaceholder() {
        return placeholder.get();
    }

    private final IntegerProperty visibleRowCount = new SimpleIntegerProperty(this, "visibleRowCount", 5);

    public final int getVisibleRowCount() {
        return visibleRowCount.get();
    }

    public final IntegerProperty visibleRowCountProperty() {
        return visibleRowCount;
    }

    public final void setVisibleRowCount(int visibleRowCount) {
        this.visibleRowCount.set(visibleRowCount);
    }

    private final ListProperty<T> items = new SimpleListProperty<>(this, "items", FXCollections.observableArrayList());

    public ObservableList<T> getItems() {
        return items.get();
    }

    public ListProperty<T> itemsProperty() {
        return items;
    }

    public void setItems(ObservableList<T> items) {
        this.items.set(items);
        setPage(0);
    }

    private void updateItems() {
        box.getChildren().clear();

        Callback<ListView<T>, ListCell<T>> cellFactory = listView.getCellFactory();
        if (cellFactory != null) {

            int startIndex = getPage() * getVisibleRowCount();
            int endIndex = Math.min(startIndex + getVisibleRowCount(), getItems() != null ? getItems().size() : 0);

            if (isPaging() && getPageCount() > 1) {
                // endIndex = startIndex + getVisibleRowCount();
            }

            for (int index = startIndex; index < endIndex; index++) {

                ListCell<T> cell = cellFactory.call(listView);
                cell.getStyleClass().add("advanced-list-cell");
                cell.updateListView(listView);

                box.getChildren().add(cell);

                if (index < getItems().size()) {
                    cell.updateIndex(index);
                } else {
                    cell.updateIndex(-1);
                }

                cell.updateSelected(listView.getSelectionModel().isSelected(index));

                if (index == endIndex - 1) {
                    cell.getStyleClass().add("last");
                }
            }
        }
        box.getChildren().add(paginationBox);
    }

    private final ObjectProperty<Callback<ListView<T>, ListCell<T>>> cellFactory = new SimpleObjectProperty<>(this, "cellFactory", view -> new ListCell<>() {
        @Override
        protected void updateItem(T item, boolean empty) {
            super.updateItem(item, empty);

            if (!empty && item != null) {
                setText(item.toString());
            } else {
                setText("");
            }
        }
    });

    public final Callback<ListView<T>, ListCell<T>> getCellFactory() {
        return cellFactory.get();
    }

    public final ObjectProperty<Callback<ListView<T>, ListCell<T>>> cellFactoryProperty() {
        return cellFactory;
    }

    public final void setCellFactory(Callback<ListView<T>, ListCell<T>> cellFactory) {
        this.cellFactory.set(cellFactory);
    }

    public void refresh() {
        // update the replacement list view / the vbox
        if (getItems().size() <= getVisibleRowCount()) {
            updateItems();
        }

        // update the "real" list view
        getListView().refresh();
    }
}
