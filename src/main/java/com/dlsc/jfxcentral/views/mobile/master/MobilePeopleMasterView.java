package com.dlsc.jfxcentral.views.mobile.master;

import com.dlsc.jfxcentral.data.DataRepository;
import com.dlsc.jfxcentral.data.model.Person;
import com.dlsc.jfxcentral.panels.PrettyScrollPane;
import com.dlsc.jfxcentral.views.RootPane;
import com.dlsc.jfxcentral.views.View;
import com.dlsc.jfxcentral.views.mobile.MobileMasterViewWithAdvancedListView;
import com.dlsc.jfxcentral.views.mobile.master.cells.MobileMasterPersonCell2;
import javafx.scene.layout.Region;
import org.apache.commons.lang3.StringUtils;

import java.util.Comparator;

public class MobilePeopleMasterView extends MobileMasterViewWithAdvancedListView<Person> {

    public MobilePeopleMasterView(RootPane rootPane) {
        super(rootPane, View.PEOPLE);

        getStyleClass().addAll("people-master-view", "mobile-people-master-view");

        listView.setPaging(true);
        listView.setVisibleRowCount(Integer.MAX_VALUE);
        listView.setMinWidth(Region.USE_PREF_SIZE);
        listView.setCellFactory(view -> new MobileMasterPersonCell2());
        listView.setItems(createSortedAndFilteredList(DataRepository.getInstance().peopleProperty(),
                Comparator.comparing(x -> x.getName().toLowerCase()),
                person -> StringUtils.isBlank(getFilterText()) || StringUtils.containsIgnoreCase(person.getName(), getFilterText())));

        PrettyScrollPane scrollPane = new PrettyScrollPane(listView);
        scrollPane.setMobile(true);
        scrollPane.setShowScrollToTopButton(true);
        scrollPane.setFitToWidth(true);
        setCenter(scrollPane);
    }
}