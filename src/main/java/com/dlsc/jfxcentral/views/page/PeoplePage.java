package com.dlsc.jfxcentral.views.page;

import com.dlsc.jfxcentral.views.RootPane;
import com.dlsc.jfxcentral.views.View;
import com.dlsc.jfxcentral.views.master.MasterView;
import com.dlsc.jfxcentral.views.master.PeopleMasterView;
import com.dlsc.jfxcentral.model.Person;
import com.dlsc.jfxcentral.views.detail.DetailView;
import com.dlsc.jfxcentral.views.detail.PeopleDetailView;
import javafx.beans.binding.Bindings;

public class PeoplePage extends Page<Person> {

    public PeoplePage(RootPane rootPane) {
        super(rootPane, View.PEOPLE);

        titleProperty().bind(Bindings.createStringBinding(() -> getSelectedItem() != null ?
                "Person - " + getSelectedItem().getName() :
                "Person", selectedItemProperty()));

        descriptionProperty().bind(Bindings.createStringBinding(() -> getSelectedItem() != null ?
                "Detailed information on '" + getSelectedItem().getName() + "'. Summary" + getSelectedItem().getDescription() :
                "A curated list of influential people / developers in the JavaFX ecosystem."));
    }

    @Override
    protected MasterView createMasterView() {
        PeopleMasterView view = new PeopleMasterView(getRootPane());
        selectedItemProperty().bindBidirectional(view.selectedItemProperty());
        return view;
    }

    @Override
    protected DetailView createDetailView() {
        PeopleDetailView view = new PeopleDetailView(getRootPane());
        selectedItemProperty().bindBidirectional(view.selectedItemProperty());
        return view;
    }
}
