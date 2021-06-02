package com.dlsc.jfxcentral.views.page;

import com.dlsc.jfxcentral.data.model.Tool;
import com.dlsc.jfxcentral.views.RootPane;
import com.dlsc.jfxcentral.views.View;
import com.dlsc.jfxcentral.views.detail.DetailView;
import com.dlsc.jfxcentral.views.detail.ToolsDetailView;
import com.dlsc.jfxcentral.views.master.MasterView;
import com.dlsc.jfxcentral.views.master.ToolsMasterView;
import javafx.beans.binding.Bindings;

public class ToolsPage extends Page<Tool> {

    public ToolsPage(RootPane rootPane) {
        super(rootPane, View.TOOLS);

        titleProperty().bind(Bindings.createStringBinding(() -> getSelectedItem() != null ?
                "Tool - " + getSelectedItem().getName() :
                "Tools", selectedItemProperty()));

        descriptionProperty().bind(Bindings.createStringBinding(() -> getSelectedItem() != null ?
                "Detailed information on the JavaFX tool '" + getSelectedItem().getName() + "'" :
                "A list of tools that can be used by developers to create their JavaFX applications."));
    }

    @Override
    protected MasterView createMasterView() {
        ToolsMasterView view = new ToolsMasterView(getRootPane());
        selectedItemProperty().bindBidirectional(view.selectedItemProperty());
        return view;
    }

    @Override
    protected DetailView createDetailView() {
        ToolsDetailView view = new ToolsDetailView(getRootPane());
        selectedItemProperty().bindBidirectional(view.selectedItemProperty());
        return view;
    }
}
