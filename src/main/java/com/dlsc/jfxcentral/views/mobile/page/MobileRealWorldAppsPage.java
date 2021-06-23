package com.dlsc.jfxcentral.views.mobile.page;

import com.dlsc.jfxcentral.data.model.RealWorldApp;
import com.dlsc.jfxcentral.views.RootPane;
import com.dlsc.jfxcentral.views.View;
import com.dlsc.jfxcentral.views.detail.DetailView;
import com.dlsc.jfxcentral.views.detail.RealWorldAppsDetailView;
import com.dlsc.jfxcentral.views.master.MasterView;
import com.dlsc.jfxcentral.views.mobile.MobilePage;
import com.dlsc.jfxcentral.views.mobile.master.MobileRealWorldAppsMasterView;
import javafx.beans.binding.Bindings;

public class MobileRealWorldAppsPage extends MobilePage<RealWorldApp> {

    public MobileRealWorldAppsPage(RootPane rootPane) {
        super(rootPane, View.REAL_WORLD);

        titleProperty().bind(Bindings.createStringBinding(() -> getSelectedItem() != null ?
                "Real World Application - " + getSelectedItem().getName() :
                "Real World Applications", selectedItemProperty()));

        descriptionProperty().bind(Bindings.createStringBinding(() -> getSelectedItem() != null ?
                "Summary information on JavaFX real world application '" + getSelectedItem().getName() + "' developed by " + getSelectedItem().getCompany() + ". Summry: " + getSelectedItem().getSummary() :
                "A list of real world applications that were developed with JavaFX."));
    }

    @Override
    protected MasterView createMasterView() {
        return new MobileRealWorldAppsMasterView(getRootPane());
    }

    @Override
    protected DetailView createDetailView() {
        return new RealWorldAppsDetailView(getRootPane());
    }
}
