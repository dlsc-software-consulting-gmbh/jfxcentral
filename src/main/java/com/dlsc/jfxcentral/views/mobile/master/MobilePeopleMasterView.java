package com.dlsc.jfxcentral.views.mobile.master;

import com.dlsc.jfxcentral.data.DataRepository;
import com.dlsc.jfxcentral.data.model.Person;
import com.dlsc.jfxcentral.panels.PrettyScrollPane;
import com.dlsc.jfxcentral.views.RootPane;
import com.dlsc.jfxcentral.views.View;
import com.dlsc.jfxcentral.views.mobile.MobileMasterViewWithAdvancedListView;
import com.dlsc.jfxcentral.views.mobile.master.cells.MobileMasterPersonCell;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Region;
import org.apache.commons.lang3.StringUtils;

import java.util.Comparator;
import java.util.function.Consumer;

public class MobilePeopleMasterView extends MobileMasterViewWithAdvancedListView<Person> {

    public MobilePeopleMasterView(RootPane rootPane, Consumer<Person> onSelect) {
        super(rootPane, View.PEOPLE);

        getStyleClass().addAll("people-master-view", "mobile-people-master-view");

        listView.setPaging(true);
        listView.setVisibleRowCount(Integer.MAX_VALUE);
        listView.setMinWidth(Region.USE_PREF_SIZE);
        listView.setCellFactory(view -> new MobileMasterPersonCell(onSelect));
        listView.setItems(createSortedAndFilteredList(DataRepository.getInstance().peopleProperty(),
                Comparator.comparing(Person::getName),
                person -> StringUtils.isBlank(getFilterText()) || StringUtils.containsIgnoreCase(person.getName(), getFilterText())));

        CheckBox twitterOnly = new CheckBox("Include Twitter");

        BorderPane content = new BorderPane();
        //content.setTop(twitterOnly);

        PrettyScrollPane scrollPane = new PrettyScrollPane(listView);
        scrollPane.setShowScrollToTopButton(true);
        scrollPane.setFitToWidth(true);
        content.setCenter(scrollPane);

        setCenter(content);
    }
}