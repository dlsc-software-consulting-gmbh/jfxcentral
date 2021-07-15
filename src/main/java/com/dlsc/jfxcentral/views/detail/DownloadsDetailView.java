package com.dlsc.jfxcentral.views.detail;

import com.dlsc.gemsfx.FilterView;
import com.dlsc.jfxcentral.data.DataRepository;
import com.dlsc.jfxcentral.data.model.Company;
import com.dlsc.jfxcentral.data.model.Download;
import com.dlsc.jfxcentral.data.model.Download.DownloadType;
import com.dlsc.jfxcentral.data.model.Download.FileType;
import com.dlsc.jfxcentral.data.model.Person;
import com.dlsc.jfxcentral.panels.SectionPaneWithFilterView;
import com.dlsc.jfxcentral.util.EmptySelectionModel;
import com.dlsc.jfxcentral.views.RootPane;
import com.dlsc.jfxcentral.views.View;
import com.dlsc.jfxcentral.views.detail.cells.DetailDownloadCell;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.WeakInvalidationListener;
import javafx.beans.binding.Bindings;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class DownloadsDetailView extends DetailViewWithListView<Download> {

    private final FilterView.FilterGroup<Download> downloadTypeGroup = new FilterView.FilterGroup<>("Type");
    private final FilterView.FilterGroup<Download> fileTypeGroup = new FilterView.FilterGroup<>("File Type");
    private final FilterView.FilterGroup<Download> personGroup = new FilterView.FilterGroup<>("Person");
    private final FilterView.FilterGroup<Download> companyGroup = new FilterView.FilterGroup<>("Company");

    private final InvalidationListener updateFilterListener = (Observable it) -> updateFilters();
    private final WeakInvalidationListener weakUpdateFilterListener = new WeakInvalidationListener(updateFilterListener);

    public DownloadsDetailView(RootPane rootPane) {
        super(rootPane, View.DOWNLOADS);

        getStyleClass().add("downloads-detail-view");

        SectionPaneWithFilterView sectionPane = new SectionPaneWithFilterView();
        sectionPane.setTitle("Downloads");
        sectionPane.setEnableAutoSubtitle(true);

        FilterView<Download> filterView = sectionPane.getFilterView();
        Bindings.bindContent(filterView.getItems(), DataRepository.getInstance().getDownloads());

        if (rootPane.isMobile()) {
            filterView.getFilterGroups().setAll(downloadTypeGroup, personGroup, companyGroup);
        } else {
            filterView.getFilterGroups().setAll(downloadTypeGroup, fileTypeGroup, personGroup, companyGroup);
            filterView.setTextFilterProvider(text -> video -> {
                if (video.getName().toLowerCase().contains(text)) {
                    return true;
                }
                return false;
            });
        }

        listView.getListView().setSelectionModel(new EmptySelectionModel<>());
        listView.setCellFactory(view -> new DetailDownloadCell(getRootPane(), true));
        listView.itemsProperty().bind(filterView.filteredItemsProperty());
        listView.getSelectionModel().selectedItemProperty().addListener(it -> setSelectedItem(listView.getSelectionModel().getSelectedItem()));

        if (true) { //rootPane.getDisplay().equals(Display.WEB)) {
            listView.setPaging(true);
            listView.setVisibleRowCount(5);
            listView.setShowItemCounter(false);
        }

        VBox.setVgrow(listView, Priority.ALWAYS);
        sectionPane.getNodes().add(listView);

        setContent(sectionPane);

        DataRepository.getInstance().downloadsProperty().addListener(weakUpdateFilterListener);
        updateFilters();
    }

    private void updateFilters() {
        fileTypeGroup.getFilters().clear();
        downloadTypeGroup.getFilters().clear();
        personGroup.getFilters().clear();
        companyGroup.getFilters().clear();

        updateDownloadTypeGroup();
        updatePersonGroup();
        updateCompanyGroup();
        updateFileTypeGroup();
    }

    private void updatePersonGroup() {
        List<String> personList = new ArrayList<>();

        DataRepository.getInstance().getDownloads().forEach(download -> {
            List<String> personIds = download.getPersonIds();
            if (personIds != null) {
                for (String id : personIds) {
                    if (!personList.contains(id.trim())) {
                        personList.add(id.trim());
                    }
                }
            }
        });

        List<FilterView.Filter<Download>> filters = new ArrayList<>();

        personList.forEach(item -> {
            Optional<Person> personById = DataRepository.getInstance().getPersonById(item);
            if (personById.isPresent()) {
                filters.add(new FilterView.Filter<>(personById.get().getName()) {
                    @Override
                    public boolean test(Download download) {
                        return download.getPersonIds().contains(item);
                    }
                });
            }
        });

        filters.sort(Comparator.comparing(FilterView.Filter::getName));
        personGroup.getFilters().setAll(filters);
    }

    private void updateCompanyGroup() {
        List<String> companyList = new ArrayList<>();

        DataRepository.getInstance().getDownloads().forEach(download -> {
            List<String> personIds = download.getPersonIds();
            if (personIds != null) {
                for (String id : personIds) {
                    if (!companyList.contains(id.trim())) {
                        companyList.add(id.trim());
                    }
                }
            }
        });

        List<FilterView.Filter<Download>> filters = new ArrayList<>();

        companyList.forEach(item -> {
            Optional<Company> companyById = DataRepository.getInstance().getCompanyById(item);
            if (companyById.isPresent()) {
                filters.add(new FilterView.Filter<>(companyById.get().getName()) {
                    @Override
                    public boolean test(Download download) {
                        return download.getCompanyIds().contains(item);
                    }
                });
            }
        });

        filters.sort(Comparator.comparing(FilterView.Filter::getName));
        companyGroup.getFilters().setAll(filters);
    }

    private void updateFileTypeGroup() {
        for (FileType fileType : FileType.values()) {
            fileTypeGroup.getFilters().add(new FilterView.Filter<>(fileType.toString()) {
                @Override
                public boolean test(Download download) {
                    return download.getFiles().stream().anyMatch(file -> file.getFileType().equals(fileType));
                }
            });
        }
    }

    private void updateDownloadTypeGroup() {
        for (DownloadType downloadType : DownloadType.values()) {
            downloadTypeGroup.getFilters().add(new FilterView.Filter<>(downloadType.toString()) {
                @Override
                public boolean test(Download download) {
                    return download.getDownloadType().equals(downloadType);
                }
            });
        }
    }
}
