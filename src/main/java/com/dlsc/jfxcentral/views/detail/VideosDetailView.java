package com.dlsc.jfxcentral.views.detail;

import com.dlsc.gemsfx.FilterView;
import com.dlsc.jfxcentral.data.DataRepository;
import com.dlsc.jfxcentral.data.model.Person;
import com.dlsc.jfxcentral.data.model.Video;
import com.dlsc.jfxcentral.panels.SectionPaneWithFilterView;
import com.dlsc.jfxcentral.util.EmptySelectionModel;
import com.dlsc.jfxcentral.views.AdvancedListView;
import com.dlsc.jfxcentral.views.RootPane;
import com.dlsc.jfxcentral.views.View;
import com.dlsc.jfxcentral.views.detail.cells.DetailVideoCell;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.WeakInvalidationListener;
import javafx.beans.binding.Bindings;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import org.apache.commons.lang3.StringUtils;

import java.util.*;

public class VideosDetailView extends DetailViewWithListView<Video> {

    private final FilterView.FilterGroup<Video> typeGroup = new FilterView.FilterGroup<>("Type");
    private final FilterView.FilterGroup<Video> eventGroup = new FilterView.FilterGroup<>("Event");
    private final FilterView.FilterGroup<Video> speakerGroup = new FilterView.FilterGroup<>("Speaker");
    private final FilterView.FilterGroup<Video> platformGroup = new FilterView.FilterGroup<>("Platform");
    private final FilterView.FilterGroup<Video> domainGroup = new FilterView.FilterGroup<>("Domain");

    private final InvalidationListener updateFilterListener = (Observable it) -> updateFilters();
    private final WeakInvalidationListener weakUpdateFilterListener = new WeakInvalidationListener(updateFilterListener);

    public VideosDetailView(RootPane rootPane) {
        super(rootPane, View.VIDEOS);

        getStyleClass().add("videos-detail-view");

        SectionPaneWithFilterView sectionPane = new SectionPaneWithFilterView();
        sectionPane.setTitle("Videos");
        sectionPane.setEnableAutoSubtitle(true);

        FilterView<Video> filterView = sectionPane.getFilterView();
        Bindings.bindContent(filterView.getItems(), DataRepository.getInstance().videosProperty());

        // show less filters, we have less space (width)
        if (rootPane.isMobile()) {
            filterView.getFilterGroups().setAll(typeGroup, speakerGroup, domainGroup);
        } else {
            filterView.getFilterGroups().setAll(typeGroup, eventGroup, speakerGroup, platformGroup, domainGroup);

            filterView.setTextFilterProvider(text -> video -> {
                if (StringUtils.containsAnyIgnoreCase(video.getTitle(), text)) {
                    return true;
                }

                if (StringUtils.containsAnyIgnoreCase(video.getDescription(), text)) {
                    return true;
                }
                return false;
            });
        }

        AdvancedListView<Video> listView = new AdvancedListView<>();
        listView.setPrefWidth(0);
        listView.setMinWidth(0);

        if (rootPane.isMobile()) {
            listView.setPaging(true);
            listView.setVisibleRowCount(5);
            listView.setShowItemCounter(false);
        }

        listView.getListView().setSelectionModel(new EmptySelectionModel<>());
        listView.setCellFactory(view -> new DetailVideoCell(getRootPane(), true));
        listView.itemsProperty().bind(filterView.filteredItemsProperty());
        listView.getSelectionModel().selectedItemProperty().addListener(it -> setSelectedItem(listView.getSelectionModel().getSelectedItem()));
        VBox.setVgrow(listView, Priority.ALWAYS);
        sectionPane.getNodes().add(listView);

        setContent(sectionPane);

        DataRepository.getInstance().videosProperty().addListener(weakUpdateFilterListener);

        updateFilters();
    }

    private void updateFilters() {
        eventGroup.getFilters().clear();
        typeGroup.getFilters().clear();
        domainGroup.getFilters().clear();
        speakerGroup.getFilters().clear();
        platformGroup.getFilters().clear();

        updateEventGroup();
        updateDomainGroup();
        updateTypeGroup();
        updatePlatformGroup();
        updateSpeakersGroup();
    }

    private void updateSpeakersGroup() {
        List<String> speakersList = new ArrayList<>();

        DataRepository.getInstance().getVideos().forEach(video -> {
            List<String> personIds = video.getPersonIds();
            if (personIds != null) {
                for (String id : personIds) {
                    if (!speakersList.contains(id.trim())) {
                        speakersList.add(id.trim());
                    }
                }
            }
        });

        List<FilterView.Filter<Video>> filters = new ArrayList<>();

        speakersList.forEach(item -> {
            Optional<Person> personById = DataRepository.getInstance().getPersonById(item);
            if (personById.isPresent()) {
                filters.add(new FilterView.Filter<>(personById.get().getName()) {
                    @Override
                    public boolean test(Video video) {
                        return video.getPersonIds().contains(item);
                    }
                });
            }
        });

        filters.sort(Comparator.comparing(FilterView.Filter::getName));
        speakerGroup.getFilters().setAll(filters);
    }

    private void updateEventGroup() {
        List<String> itemList = new ArrayList<>();
        DataRepository.getInstance().getVideos().forEach(video -> {
            String event = video.getEvent();
            if (StringUtils.isNotBlank(event)) {
                StringTokenizer st = new StringTokenizer(event, ",");
                while (st.hasMoreTokens()) {
                    String token = st.nextToken().trim();
                    if (!itemList.contains(token)) {
                        itemList.add(token);
                    }
                }
            }
        });

        Collections.sort(itemList);
        itemList.forEach(item -> eventGroup.getFilters().add(new FilterView.Filter<>(item) {
            @Override
            public boolean test(Video video) {
                return StringUtils.containsIgnoreCase(video.getEvent(), item);
            }
        }));
    }

    private void updateDomainGroup() {
        List<String> itemList = new ArrayList<>();
        DataRepository.getInstance().getVideos().forEach(video -> {
            String domain = video.getDomain();
            if (StringUtils.isNotBlank(domain)) {
                StringTokenizer st = new StringTokenizer(domain, ",");
                while (st.hasMoreTokens()) {
                    String token = st.nextToken().trim();
                    if (!itemList.contains(token)) {
                        itemList.add(token);
                    }
                }
            }
        });

        Collections.sort(itemList);
        itemList.forEach(item -> domainGroup.getFilters().add(new FilterView.Filter<>(item) {
            @Override
            public boolean test(Video video) {
                return StringUtils.containsIgnoreCase(video.getDomain(), item);
            }
        }));
    }

    private void updatePlatformGroup() {
        List<String> itemList = new ArrayList<>();
        DataRepository.getInstance().getVideos().forEach(video -> {
            String platform = video.getPlatform();
            if (StringUtils.isNotBlank(platform)) {
                StringTokenizer st = new StringTokenizer(platform, ",");
                while (st.hasMoreTokens()) {
                    String token = st.nextToken().trim();
                    if (!itemList.contains(token)) {
                        itemList.add(token);
                    }
                }
            }
        });

        Collections.sort(itemList);
        itemList.forEach(item -> platformGroup.getFilters().add(new FilterView.Filter<>(item) {
            @Override
            public boolean test(Video video) {
                return StringUtils.containsIgnoreCase(video.getPlatform(), item);
            }
        }));
    }

    private void updateTypeGroup() {
        List<String> itemList = new ArrayList<>();
        DataRepository.getInstance().getVideos().forEach(video -> {
            String type = video.getType();
            if (StringUtils.isNotBlank(type)) {
                StringTokenizer st = new StringTokenizer(type, ",");
                while (st.hasMoreTokens()) {
                    String token = st.nextToken().trim();
                    if (!itemList.contains(token)) {
                        itemList.add(token);
                    }
                }
            }
        });

        Collections.sort(itemList);
        itemList.forEach(item -> typeGroup.getFilters().add(new FilterView.Filter<>(item) {
            @Override
            public boolean test(Video video) {
                return StringUtils.containsIgnoreCase(video.getType(), item);
            }
        }));
    }
}