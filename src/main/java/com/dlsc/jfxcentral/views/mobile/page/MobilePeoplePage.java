package com.dlsc.jfxcentral.views.mobile.page;

import com.dlsc.jfxcentral.data.model.Person;
import com.dlsc.jfxcentral.views.RootPane;
import com.dlsc.jfxcentral.views.View;
import com.dlsc.jfxcentral.views.detail.DetailView;
import com.dlsc.jfxcentral.views.detail.PeopleDetailView;
import com.dlsc.jfxcentral.views.master.MasterView;
import com.dlsc.jfxcentral.views.mobile.MobilePage;
import com.dlsc.jfxcentral.views.mobile.master.MobilePeopleMasterView;
import javafx.beans.binding.Bindings;

public class MobilePeoplePage extends MobilePage<Person> {

    public MobilePeoplePage(RootPane rootPane) {
        super(rootPane, View.PEOPLE);

        titleProperty().bind(Bindings.createStringBinding(() -> getSelectedItem() != null ?
                "Person - " + getSelectedItem().getName() + " - working in the JavaFX space" :
                "People working in the JavaFX space", selectedItemProperty()));

        descriptionProperty().bind(Bindings.createStringBinding(() -> getSelectedItem() != null ?
                "Detailed information on '" + getSelectedItem().getName() + "'. Summary" + getSelectedItem().getDescription() :
                "A curated list of influential people / developers in the JavaFX ecosystem."));
    }

    @Override
    protected MasterView<Person> createMasterView() {
        return new MobilePeopleMasterView(getRootPane());
    }

    @Override
    protected DetailView createDetailView() {
        return new PeopleDetailView(getRootPane());
    }
}
