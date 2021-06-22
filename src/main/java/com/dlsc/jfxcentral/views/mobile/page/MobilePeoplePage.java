package com.dlsc.jfxcentral.views.mobile.page;

import com.dlsc.jfxcentral.data.model.Person;
import com.dlsc.jfxcentral.views.RootPane;
import com.dlsc.jfxcentral.views.View;
import com.dlsc.jfxcentral.views.detail.DetailView;
import com.dlsc.jfxcentral.views.detail.PeopleDetailView;
import com.dlsc.jfxcentral.views.master.MasterView;
import com.dlsc.jfxcentral.views.mobile.MobilePage;
import com.dlsc.jfxcentral.views.mobile.master.MobilePeopleMasterView;

public class MobilePeoplePage extends MobilePage<Person> {

    public MobilePeoplePage(RootPane rootPane) {
        super(rootPane, View.PEOPLE);
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
