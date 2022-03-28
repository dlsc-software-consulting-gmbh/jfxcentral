package com.dlsc.jfxcentral.views.page;

import com.dlsc.jfxcentral.util.DeveloperTool;
import com.dlsc.jfxcentral.views.RootPane;
import com.dlsc.jfxcentral.views.View;
import com.dlsc.jfxcentral.views.detail.DetailView;
import com.dlsc.jfxcentral.views.detail.DeveloperToolsDetailView;
import com.dlsc.jfxcentral.views.master.DeveloperToolMasterView;
import com.dlsc.jfxcentral.views.master.MasterView;
import javafx.beans.binding.Bindings;

public class DevToolsPage extends Page<DeveloperTool> {

    public DevToolsPage(RootPane rootPane) {
        super(rootPane, View.DEVELOPMENT);

        titleProperty().bind(Bindings.createStringBinding(() -> getSelectedItem() != null ?
                "Developer Tool - " + getSelectedItem().getName() + " - a tool for JavaFX developers" :
                "Developer Tools for JavaFX developers", selectedItemProperty()));

        descriptionProperty().bind(Bindings.createStringBinding(() -> getSelectedItem() != null ?
                "A developer tool for JavaFX titled '" + getSelectedItem().getName() + "'" :
                "Collection of tips for the JavaFX technology."));
    }

    @Override
    protected MasterView createMasterView() {
        DeveloperToolMasterView view = new DeveloperToolMasterView(getRootPane());
        selectedItemProperty().bindBidirectional(view.selectedItemProperty());
        return view;
    }

    @Override
    protected DetailView createDetailView() {
        DeveloperToolsDetailView view = new DeveloperToolsDetailView(getRootPane());
        selectedItemProperty().bindBidirectional(view.selectedItemProperty());
        return view;
    }
}
