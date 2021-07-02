package com.dlsc.jfxcentral.views.detail;

import com.dlsc.gemsfx.FilterView;
import com.dlsc.jfxcentral.data.DataRepository;
import com.dlsc.jfxcentral.data.model.Person;
import com.dlsc.jfxcentral.data.model.Tutorial;
import com.dlsc.jfxcentral.data.model.Tutorial.Format;
import com.dlsc.jfxcentral.panels.SectionPaneWithFilterView;
import com.dlsc.jfxcentral.util.EmptySelectionModel;
import com.dlsc.jfxcentral.views.AdvancedListView;
import com.dlsc.jfxcentral.views.RootPane;
import com.dlsc.jfxcentral.views.View;
import com.dlsc.jfxcentral.views.detail.cells.DetailTutorialCell;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.WeakInvalidationListener;
import javafx.beans.binding.Bindings;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class TutorialsDetailView extends DetailViewWithListView<Tutorial> {

    private final FilterView.FilterGroup<Tutorial> formatGroup = new FilterView.FilterGroup<>("Format");
    private final FilterView.FilterGroup<Tutorial> typeGroup = new FilterView.FilterGroup<>("Type");
    private final FilterView.FilterGroup<Tutorial> personGroup = new FilterView.FilterGroup<>("Person");

    private final InvalidationListener updateFilterListener = (Observable it) -> updateFilters();
    private final WeakInvalidationListener weakUpdateFilterListener = new WeakInvalidationListener(updateFilterListener);

    public TutorialsDetailView(RootPane rootPane) {
        super(rootPane, View.TUTORIALS);

        getStyleClass().add("tutorials-detail-view");

        SectionPaneWithFilterView sectionPane = new SectionPaneWithFilterView();
        sectionPane.setTitle("Tutorials");
        sectionPane.setEnableAutoSubtitle(true);

        FilterView<Tutorial> filterView = sectionPane.getFilterView();
        Bindings.bindContent(filterView.getItems(), DataRepository.getInstance().getTutorials());

        filterView.getFilterGroups().setAll(formatGroup, typeGroup, personGroup);
        filterView.setTextFilterProvider(text -> tutorial -> {
            if (StringUtils.containsAnyIgnoreCase(tutorial.getName(), text)) {
                return true;
            }

            if (StringUtils.containsAnyIgnoreCase(tutorial.getSummary(), text)) {
                return true;
            }

            if (StringUtils.containsAnyIgnoreCase(DataRepository.getInstance().tutorialTextProperty(tutorial).get(), text)) {
                return true;
            }

            return false;
        });

        listView = new AdvancedListView<>();
        listView.setCellFactory(view -> new DetailTutorialCell(getRootPane(), true));
        listView.getListView().setSelectionModel(new EmptySelectionModel<>());
        listView.itemsProperty().bind(filterView.filteredItemsProperty());
        listView.getSelectionModel().selectedItemProperty().addListener(it -> setSelectedItem(listView.getSelectionModel().getSelectedItem()));
        VBox.setVgrow(listView, Priority.ALWAYS);
        sectionPane.getNodes().add(listView);

        if (rootPane.isMobile()) {
            listView.setPaging(true);
            listView.setVisibleRowCount(5);
            listView.setShowItemCounter(false);
        }

        setContent(sectionPane);

        DataRepository.getInstance().tutorialsProperty().addListener(weakUpdateFilterListener);
        updateFilters();
    }

    private void updateFilters() {
        typeGroup.getFilters().clear();
        formatGroup.getFilters().clear();
        personGroup.getFilters().clear();

        updateFormatGroup();
        updatePersonGroup();
        updateTypeGroup();
    }

    private void updatePersonGroup() {
        List<String> speakersList = new ArrayList<>();

        DataRepository.getInstance().getPeople().forEach(person -> {
            List<String> tutorialIds = person.getTutorialIds();
            if (!tutorialIds.isEmpty()) {
                if (!speakersList.contains(person.getId().trim())) {
                    speakersList.add(person.getId().trim());
                }
            }
        });

        List<FilterView.Filter<Tutorial>> filters = new ArrayList<>();

        speakersList.forEach(item -> {
            Optional<Person> personById = DataRepository.getInstance().getPersonById(item);
            if (personById.isPresent()) {
                filters.add(new FilterView.Filter<>(personById.get().getName()) {
                    @Override
                    public boolean test(Tutorial tutorial) {
                        return DataRepository.getInstance().getTutorialsByModelObject(personById.get()).contains(tutorial);
                    }
                });
            }
        });

        filters.sort(Comparator.comparing(FilterView.Filter::getName));
        personGroup.getFilters().setAll(filters);
    }

    private void updateFormatGroup() {
        for (Format type : Format.values()) {
            formatGroup.getFilters().add(new FilterView.Filter<>(type.name()) {
                @Override
                public boolean test(Tutorial tutorial) {
                    return tutorial.getType() != null && tutorial.getType().equals(type);
                }
            });
        }
    }

    private void updateTypeGroup() {
        typeGroup.getFilters().add(new FilterView.Filter<>("Free") {
            @Override
            public boolean test(Tutorial tutorial) {
                return !tutorial.isCommercial();
            }
        });
        typeGroup.getFilters().add(new FilterView.Filter<>("Paid Course") {
            @Override
            public boolean test(Tutorial tutorial) {
                return tutorial.isCommercial();
            }
        });
    }
}