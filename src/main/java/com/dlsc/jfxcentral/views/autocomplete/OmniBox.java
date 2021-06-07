package com.dlsc.jfxcentral.views.autocomplete;

import com.dlsc.jfxcentral.views.AdvancedListView;
import javafx.application.Platform;
import javafx.beans.Observable;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.MapChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.collections.transformation.FilteredList;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.concurrent.Worker.State;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * The {@link OmniBox} is a popup control that can be used to perform searches
 * on one or more {@link OmniBoxService} instances. The box is associated with
 * an {@link OmniBoxTextField} and becomes visible when search results arrive.
 *
 * @author Dirk Lemmermann
 * @see OmniBoxTextField
 */
public class OmniBox extends PopupControl {

    private static final Logger LOG = Logger.getLogger(OmniBox.class.getName());

//    private static final double FIXED_CELL_SIZE = 40;

    // The service used for performing the actual search in the background
    private final OmniSearchService service = new OmniSearchService();

    // The ListView control used to display the results
    private final SearchResultListView listView;

    // Every box has an owning control.
    private final OmniBoxOwner owner;

    /**
     * Construcs a new {@link OmniBox}.
     *
     * @param owner the owner control of this box
     */
    public OmniBox(OmniBoxOwner owner) {
        this.owner = Objects.requireNonNull(owner);

        listView = new SearchResultListView();
      //  listView.setFixedCellSize(FIXED_CELL_SIZE);

        ObservableList<SearchResult<?>> items = FXCollections.observableArrayList();
        FilteredList<SearchResult<?>> filteredItems = new FilteredList<>(items);
        filteredItems.predicateProperty().bind(resultFilterProperty());
        listView.setItems(filteredItems);

        setAutoFix(false);
        setAutoHide(true);
        setHideOnEscape(true);
        setOnHiding(evt -> cancelSearch());

        searchText.addListener(it -> {
            if (isAutoSearch()) {
                cancelSearch();
                startSearch(false);
                listView.setPage(0);
            }
        });

        findAll.addListener(it -> {
            if (isFindAll()) {
                cancelSearch();
                startSearch(false);
            }
        });

        MapChangeListener<? super OmniBoxService<?>, ? super List<SearchResult<?>>> l = change -> {
            if (change.wasAdded()) {
                LOG.fine(change.getKey().getServiceName() + " -> " + change.getValueAdded().size() + " search results");
            } else if (change.wasRemoved()) {
                LOG.fine(change.getKey() + " has been removed");
            }

            List<SearchResult<?>> allResults = new ArrayList<>();
            getSearchResults().forEach((key, value) -> allResults.addAll(value));

            Platform.runLater(() -> {
                SearchResult<?> selectedItem = listView.getSelectionModel().getSelectedItem();
                items.setAll(allResults);
                listView.getSelectionModel().select(selectedItem);
                updateSearchResultCount();
            });
        };

        searchResults.addListener(l);

        listView.setOnKeyPressed(t -> {
            if (t.getCode() == KeyCode.ENTER || t.getCode() == KeyCode.SPACE || t.getCode() == KeyCode.ESCAPE) {
                hide();
            }

            if (t.getCode() == KeyCode.ENTER || t.getCode() == KeyCode.SPACE) {
                applySelection();
            }
        });

        listView.setOnMouseClicked(evt -> {
            if (evt.getButton().equals(MouseButton.PRIMARY) && evt.getClickCount() == 1) {
                applySelection();
            }
        });

        listView.getItems().addListener((Observable it) -> {
            if (listView.getItems().isEmpty()) {
                getOwner().hideOmniBox();
            } else {
                getOwner().showOmniBox();
            }
        });

        setSkin(createDefaultSkin());
    }

    private final ObjectProperty<Predicate<SearchResult<?>>> resultFilter = new SimpleObjectProperty<>(this, "resultFilter", item -> true);

    public Predicate<SearchResult<?>> getResultFilter() {
        return resultFilter.get();
    }

    public ObjectProperty<Predicate<SearchResult<?>>> resultFilterProperty() {
        return resultFilter;
    }

    public void setResultFilter(Predicate<SearchResult<?>> resultFilter) {
        this.resultFilter.set(resultFilter);
    }

    private final BooleanProperty findAll = new SimpleBooleanProperty(this, "findAll", false);

    public boolean isFindAll() {
        return findAll.get();
    }

    public BooleanProperty findAllProperty() {
        return findAll;
    }

    public void setFindAll(boolean findAll) {
        this.findAll.set(findAll);
    }

    public void clear() {
        selectedSearchResult.set(null);
    }

    public void setManualSearchResult(SearchResult result) {
        selectedSearchResult.set(result);
    }

    /**
     * Returns the owning control of this box.
     *
     * @return the owner
     */
    public final OmniBoxOwner getOwner() {
        return owner;
    }

    /*
     * Sets the currently selected list item as the selected search result.
     */
    private void applySelection() {
        cancelSearch();
        SearchResult<?> selectedItem = listView.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            lastSearchText = selectedItem.getText();
        } else {
            lastSearchText = null;
        }
        selectedSearchResult.set(selectedItem);
        hide();
    }

    private void updateSearchResultCount() {
        int count = 0;
        for (OmniBoxService<?> service : searchResults.keySet()) {
            List<SearchResult<?>> results = searchResults.get(service);
            if (results != null && !results.isEmpty()) {
                count += results.size();
            }
        }

        searchResultCount.set(count);
    }

    private final BooleanProperty autoSearch = new SimpleBooleanProperty(this, "autoSearch", true);

    /**
     * A property used to control whether the search services will be called
     * automatically when the {@link #searchTextProperty()} changes. The default
     * is "true". If set to "false" the application needs to call
     * {@link #startSearch(boolean)} explicitly.
     *
     * @return the auto search property
     */
    public final BooleanProperty autoSearchProperty() {
        return autoSearch;
    }

    /**
     * Sets the value of {@link #autoSearchProperty()}.
     *
     * @param auto if true the services will be called after a delay when the
     *             {@link #searchTextProperty()} changes
     */
    public final void setAutoSearch(boolean auto) {
        autoSearch.set(auto);
    }

    /**
     * Returns the value of {@link #autoSearchProperty()}.
     *
     * @return true if auto search is enabled, false otherwise
     */
    public final boolean isAutoSearch() {
        return autoSearch.get();
    }

    private final ReadOnlyIntegerWrapper searchResultCount = new ReadOnlyIntegerWrapper(this, "searchResultCount");

    /**
     * A property used to store the total number of search results found so far.
     *
     * @return the search result count
     */
    public final ReadOnlyIntegerProperty searchResultCountProperty() {
        return searchResultCount.getReadOnlyProperty();
    }

    /**
     * Returns the value of {@link #searchResultCountProperty()}.
     *
     * @return the search result count
     */
    public final int getSearchResultCount() {
        return searchResultCount.get();
    }

    // selected search result support

    private final ReadOnlyObjectWrapper<SearchResult<?>> selectedSearchResult = new ReadOnlyObjectWrapper<>(this, "selectedSearchResult");

    /**
     * Returns the property used to store the user selected search result.
     *
     * @return the selected search result
     */
    public final ReadOnlyObjectProperty<SearchResult<?>> selectedSearchResultProperty() {
        return selectedSearchResult.getReadOnlyProperty();
    }

    /**
     * Returns the value of {@link #selectedSearchResultProperty()}.
     *
     * @return the selected search result
     */
    public final SearchResult<?> getSelectedSearchResult() {
        return selectedSearchResult.get();
    }


    // remember the last text used for the search. Only perform new search if
    // new text is "really" different, e.g. ignore trailing spaces
    private String lastSearchText;

    /**
     * Perform a new search but only if the new search text is really different
     * than the one used for the last search, or if the "force" parameter is
     * true.
     *
     * @param force determines if a search is forced to run, even when the search
     *              text has not changed (e.g. when user hits ENTER).
     * @see #searchTextProperty()
     */
    public final void startSearch(boolean force) {
        String text = getSearchText();
        if (isFindAll() || (text != null && (force || lastSearchText == null || !text.trim().equals(lastSearchText)))) {
            cancelSearch();
            lastSearchText = getSearchText().trim();
            service.restart();
        }
    }

    /**
     * Cancel the currently running search. Calling this method will ask the
     * background service to cancel its currently runnning task. It will also
     * ask each individual service to do so, too.
     */
    public final void cancelSearch() {
        searchRunning.set(false);
        service.cancel();
    }

    /**
     * Determines if the last search has been cancelled or not.
     *
     * @return true if the {@link #cancelSearch()} method has been called
     * before, false after a call to {@link #startSearch(boolean)}
     */
    public final boolean isCancelled() {
        return service.getState().equals(State.CANCELLED);
    }

    private final IntegerProperty visibleRowCount = new SimpleIntegerProperty(this, "visibleRowCount", 10);

    /**
     * A property used to control the maximum number of visible rows in the list
     * view.
     *
     * @return visible row count property
     */
    public final IntegerProperty visibleRowCountProperty() {
        return visibleRowCount;
    }

    /**
     * Sets the value of {@link #visibleRowCountProperty()}.
     *
     * @param count the maximum number of rows visible at the same time
     */
    public final void setVisibleRowCount(int count) {
        visibleRowCount.set(count);
    }

    /**
     * Returns the value of {@link #visibleRowCountProperty()}.
     *
     * @return the maximum number of rows visible at the same time
     */
    public final int getVisibleRowCount() {
        return visibleRowCount.get();
    }

    private final ReadOnlyBooleanWrapper searchRunning = new ReadOnlyBooleanWrapper(this, "searchRunning", false);

    /**
     * A property used to signal whether a search is currently in progress or
     * not.
     *
     * @return the search running property
     */
    public final ReadOnlyBooleanProperty searchRunningProperty() {
        return searchRunning.getReadOnlyProperty();
    }

    /**
     * Returns the value of {@link #searchRunningProperty()}.
     *
     * @return true if a search is currently in progress
     */
    public final boolean isSearchRunning() {
        return searchRunning.get();
    }

    @Override
    protected Skin<?> createDefaultSkin() {
        return new OmniBoxSkin(this);
    }

    private final ObjectProperty<Duration> searchDelay = new SimpleObjectProperty<>(this, "searchDelay", Duration.ofMillis(500)) {
        @Override
        public void set(Duration newValue) {
            if (newValue == null) {
                throw new IllegalArgumentException("search delay can not be null");
            }

            super.set(newValue);
        }

    };

    /**
     * A property used to set the initial delay before running a new search. The
     * delay is needed so that a new search is not executed after every
     * character pressed by the user but only when the user stops typing for a
     * moment.
     *
     * @return the initial search delay
     */
    public final ObjectProperty<Duration> searchDelayProperty() {
        return searchDelay;
    }

    /**
     * Returns the value of {@link #searchDelayProperty()}.
     *
     * @return the initial delay before running a new search
     */
    public final Duration getSearchDelay() {
        return searchDelay.get();
    }

    /**
     * Sets the value of {@link #searchDelayProperty()}.
     *
     * @param duration the initial delay before running a new search
     */
    public final void setSearchDelay(Duration duration) {
        searchDelay.set(duration);
    }

    /**
     * Returns the {@link ListView} instance used by the
     * {@link OmniBox} to present the search results.
     *
     * @return the list view
     */
    public final AdvancedListView<SearchResult<?>> getListView() {
        return listView;
    }

    private final ObservableMap<OmniBoxService<?>, List<SearchResult<?>>> searchResults = FXCollections.observableHashMap();

    /**
     * Stores the search results returned by the search services.
     *
     * @return the search results
     */
    public final ObservableMap<OmniBoxService<?>, List<SearchResult<?>>> getSearchResults() {
        return searchResults;
    }

    private final ObservableList<OmniBoxService<?>> omniBoxServices = FXCollections.observableArrayList();

    /**
     * Stores all instances of {@link OmniBoxService} that will be used to
     * perform a search.
     *
     * @return the search services
     */
    public final ObservableList<OmniBoxService<?>> getOmniBoxServices() {
        return omniBoxServices;
    }

    // Initialize with empty string
    private final StringProperty searchText = new SimpleStringProperty(this, "searchText", "");

    /**
     * The search text. Whenever the value of this property changes a new search
     * will be performed.
     *
     * @return the search text
     */
    public final StringProperty searchTextProperty() {
        return searchText;
    }

    /**
     * Returns the value of {@link #searchTextProperty()}.
     *
     * @return the search text
     */
    public final String getSearchText() {
        return searchTextProperty().get();
    }

    /**
     * Sets the value of {@link #searchTextProperty()}.
     *
     * @param searchText the new search text
     */
    public final void setSearchText(String searchText) {
        searchTextProperty().set(searchText);
    }

    /**
     * A service implementation used for creating new instances of
     * {@link OmniSearchTask}.
     */
    class OmniSearchService extends Service<Void> {

        @Override
        protected Task<Void> createTask() {
            String searchText = getSearchText();
            LOG.fine("creating search task with text " + searchText + ", findAll = " + isFindAll());
            return new OmniSearchTask(searchText, isFindAll());
        }
    }

    /**
     * A task used for running the searches against all instances of
     * {@link OmniSearchService} that are currently registered with the
     * {@link OmniBox}.
     */
    class OmniSearchTask extends Task<Void> implements SearchContext {

        private final String searchText;

        private final boolean findAll;

        /**
         * Constructs a new instance.
         *
         * @param searchText the text used for the search
         */
        public OmniSearchTask(String searchText, boolean findAll) {
            this.searchText = Objects.requireNonNull(searchText);
            this.findAll = findAll;
        }

        @Override
        public boolean isFindAll() {
            return findAll;
        }

        @Override
        protected Void call() throws Exception {
            if (!searchText.trim().isEmpty() || findAll) {
                Thread.sleep(getSearchDelay().toMillis());

                Platform.runLater(() -> {
                    setFindAll(false);
                    searchRunning.set(true);
                });

                LOG.fine("running task after sleeping for delay = " + getSearchDelay().toMillis());
                if (!isCancelled()) {
                    updateProgress(50, 100);
                    getOmniBoxServices().parallelStream().peek(service -> LOG.fine("peeking at " + service.getServiceName())).peek(service -> performSearch(service)).collect(Collectors.toList());
                }
            } else {
                if (!getSearchResults().isEmpty()) {
                    LOG.fine("search text is empty, clearing results");
                    getOmniBoxServices().forEach(service -> getSearchResults().remove(service));
                }
            }

            Platform.runLater(() -> searchRunning.set(false));

            return null;
        }

        @SuppressWarnings("rawtypes")
        private void performSearch(OmniBoxService service) {
            if (!isCancelled()) {
                LOG.fine("invoking search on search service " + service.getServiceName());
                @SuppressWarnings("unchecked")
                List<SearchResult<?>> results = service.search(this, searchText);
                getSearchResults().put(service, results);
            }
        }
    }

    /**
     * A specialized list view for displaying search results.
     */
    public class SearchResultListView extends AdvancedListView<SearchResult<?>> {

        private static final String DEFAULT_STYLE_CLASS = "search-result-list-view";

        public SearchResultListView() {
            getStyleClass().setAll(DEFAULT_STYLE_CLASS);
            setCellFactory(view -> new SearchResultCell());
            setFocusTraversable(false);
            setPaging(false);
            setVisibleRowCount(8);
        }
    }

    /**
     * A specialized {@link ListCell} used for displaying a single {@link SearchResult}.
     */
    public static class SearchResultCell extends ListCell<SearchResult<?>> {

        private static final String DEFAULT_STYLE_CLASS = "search-result-cell";

        private final Label label = new Label();
        private final HBox box = new HBox();
        private final HBox badgesBox = new HBox();

        public SearchResultCell() {
            getStyleClass().add(DEFAULT_STYLE_CLASS);

            HBox.setHgrow(label, Priority.ALWAYS);
            HBox.setHgrow(badgesBox, Priority.NEVER);

            label.setMaxWidth(500);
            label.setWrapText(true);
            label.setAlignment(Pos.TOP_LEFT);

            box.setFillHeight(false);
            box.getChildren().addAll(label, badgesBox);
            box.setAlignment(Pos.TOP_LEFT);

            badgesBox.setFillHeight(true);

            setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
            setGraphic(box);

            box.setCursor(Cursor.DEFAULT);
        }

        @Override
        protected void updateItem(SearchResult<?> item, boolean empty) {
            super.updateItem(item, empty);

            if (item == null || empty) {
                label.setGraphic(null);
                label.setText(null);
                badgesBox.getChildren().clear();
            } else {
                label.setText(item.getText());

                Node icon = item.getIcon();
                if (icon != null) {
                    label.setGraphic(icon);
                }

                badgesBox.getChildren().clear();

                Node[] badges = item.getBadges();
                if (badges != null && badges.length > 0) {
                    for (Node badgeIcon : badges) {
                        badgesBox.getChildren().add(badgeIcon);
                    }
                }
            }
        }
    }
}
