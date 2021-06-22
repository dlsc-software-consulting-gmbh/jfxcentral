package com.dlsc.jfxcentral.views.detail;

import com.dlsc.jfxcentral.data.DataRepository;
import com.dlsc.jfxcentral.data.model.Company;
import com.dlsc.jfxcentral.panels.SectionPane;
import com.dlsc.jfxcentral.util.EmptySelectionModel;
import com.dlsc.jfxcentral.views.RootPane;
import com.dlsc.jfxcentral.views.detail.cells.DetailCompanyCell;
import javafx.collections.transformation.SortedList;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import java.util.Comparator;

public class CompaniesDetailView extends DetailViewWithListView<Company> {

    public CompaniesDetailView(RootPane rootPane) {
        super(rootPane);

        getStyleClass().add("companies-detail-view");

        SectionPane sectionPane = new SectionPane();
        sectionPane.setTitle("Companies");

        SortedList<Company> sortedList = new SortedList<>(DataRepository.getInstance().companiesProperty());
        sortedList.setComparator(Comparator.comparing(Company::getName));

        listView.setCellFactory(view -> new DetailCompanyCell(rootPane, true));
        listView.setItems(sortedList);
        listView.setSelectionModel(new EmptySelectionModel<>());
//        listView.getSelectionModel().selectedItemProperty().addListener(it -> setSelectedItem(listView.getSelectionModel().getSelectedItem()));

        VBox.setVgrow(listView, Priority.ALWAYS);
        sectionPane.getNodes().add(listView);

        setContent(sectionPane);
    }
}