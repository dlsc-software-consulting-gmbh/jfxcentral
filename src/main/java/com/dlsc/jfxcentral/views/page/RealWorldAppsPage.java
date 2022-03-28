package com.dlsc.jfxcentral.views.page;

import com.dlsc.jfxcentral.data.model.RealWorldApp;
import com.dlsc.jfxcentral.views.RootPane;
import com.dlsc.jfxcentral.views.View;
import com.dlsc.jfxcentral.views.detail.DetailView;
import com.dlsc.jfxcentral.views.detail.RealWorldAppsDetailView;
import com.dlsc.jfxcentral.views.master.MasterView;
import com.dlsc.jfxcentral.views.master.RealWorldAppsMasterView;
import javafx.beans.binding.Bindings;

public class RealWorldAppsPage extends Page<RealWorldApp> {

    public RealWorldAppsPage(RootPane rootPane) {
        super(rootPane, View.REAL_WORLD);

        titleProperty().bind(Bindings.createStringBinding(() -> getSelectedItem() != null ?
                "Real World Application - " + getSelectedItem().getName() + " - written in JavaFX" :
                "Real World Applications written in JavaFX", selectedItemProperty()));

        descriptionProperty().bind(Bindings.createStringBinding(() -> getSelectedItem() != null ?
                "Summary information on JavaFX real world application '" + getSelectedItem().getName() + "' developed by " + getSelectedItem().getCompany() + ". Summry: " + getSelectedItem().getSummary():
                "A list of real world applications that were developed with JavaFX."));
    }

    @Override
    protected MasterView createMasterView() {
        RealWorldAppsMasterView view = new RealWorldAppsMasterView(getRootPane());
        selectedItemProperty().bindBidirectional(view.selectedItemProperty());
        return view;
    }

    @Override
    protected DetailView createDetailView() {
        RealWorldAppsDetailView view = new RealWorldAppsDetailView(getRootPane());
        selectedItemProperty().bindBidirectional(view.selectedItemProperty());
        return view;
    }
}
