package com.dlsc.jfxcentral.views.page;

import com.dlsc.jfxcentral.views.RootPane;
import com.dlsc.jfxcentral.views.View;
import com.dlsc.jfxcentral.views.master.MasterView;
import com.dlsc.jfxcentral.views.master.PeopleMasterView;
import com.dlsc.jfxcentral.model.Person;
import com.dlsc.jfxcentral.views.detail.DetailView;
import com.dlsc.jfxcentral.views.detail.PeopleDetailView;

public class PeoplePage extends Page<Person> {

    public PeoplePage(RootPane rootPane) {
        super(rootPane, View.PEOPLE);
    }

    @Override
    protected MasterView createMasterView() {
        PeopleMasterView view = new PeopleMasterView(getRootPane());
        view.selectedItemProperty().bindBidirectional(selectedItemProperty());
        return view;
    }

    @Override
    protected DetailView createDetailView() {
        PeopleDetailView view = new PeopleDetailView(getRootPane());
        view.selectedItemProperty().bindBidirectional(selectedItemProperty());
        return view;
    }
}
