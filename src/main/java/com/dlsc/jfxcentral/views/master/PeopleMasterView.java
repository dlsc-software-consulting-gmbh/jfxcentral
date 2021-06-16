package com.dlsc.jfxcentral.views.master;

import com.dlsc.jfxcentral.data.DataRepository;
import com.dlsc.jfxcentral.data.model.Person;
import com.dlsc.jfxcentral.views.RootPane;
import com.dlsc.jfxcentral.views.View;
import com.dlsc.jfxcentral.views.master.cells.MasterPersonCell;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Region;
import org.apache.commons.lang3.StringUtils;

import java.util.Comparator;

public class PeopleMasterView extends MasterViewWithListView<Person> {

    public PeopleMasterView(RootPane rootPane) {
        super(rootPane, View.PEOPLE);

        getStyleClass().add("people-master-view");

        listView.setMinWidth(Region.USE_PREF_SIZE);
        listView.setCellFactory(view -> new MasterPersonCell());
        listView.setItems(createSortedAndFilteredList(DataRepository.getInstance().peopleProperty(),
                Comparator.comparing(Person::getName),
                person -> StringUtils.isBlank(getFilterText()) || StringUtils.containsIgnoreCase(person.getName(), getFilterText())));

        CheckBox twitterOnly = new CheckBox("Include Twitter");

        BorderPane content = new BorderPane();
        content.setTop(twitterOnly);
        content.setCenter(listView);

        setCenter(listView);
    }
}