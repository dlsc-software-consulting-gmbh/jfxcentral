package com.dlsc.jfxcentral.views.master;

import com.dlsc.jfxcentral.data.DataRepository;
import com.dlsc.jfxcentral.data.model.Person;
import com.dlsc.jfxcentral.views.RootPane;
import com.dlsc.jfxcentral.views.View;
import com.dlsc.jfxcentral.views.master.cells.MasterPersonCell;
import org.apache.commons.lang3.StringUtils;

import java.util.Comparator;

public class PeopleMasterView extends MasterViewWithListView<Person> {

    public PeopleMasterView(RootPane rootPane) {
        super(rootPane, View.PEOPLE);

        getStyleClass().add("people-master-view");

        listView.setCellFactory(view -> new MasterPersonCell());
        listView.setItems(createSortedAndFilteredList(DataRepository.getInstance().peopleProperty(),
                Comparator.comparing(x -> x.getName().toLowerCase()),
                person -> StringUtils.isBlank(getFilterText()) || StringUtils.containsIgnoreCase(person.getName(), getFilterText())));
    }
}