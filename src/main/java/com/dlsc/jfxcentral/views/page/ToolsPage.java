package com.dlsc.jfxcentral.views.page;

import com.dlsc.jfxcentral.views.RootPane;
import com.dlsc.jfxcentral.views.View;
import com.dlsc.jfxcentral.views.master.MasterView;
import com.dlsc.jfxcentral.views.master.ToolsMasterView;
import com.dlsc.jfxcentral.model.Tool;
import com.dlsc.jfxcentral.views.detail.DetailView;
import com.dlsc.jfxcentral.views.detail.ToolsDetailView;

public class ToolsPage extends Page<Tool> {

    public ToolsPage(RootPane rootPane) {
        super(rootPane, View.TOOLS);
    }

    @Override
    protected MasterView createMasterView() {
        ToolsMasterView view = new ToolsMasterView(getRootPane());
        view.selectedItemProperty().bindBidirectional(selectedItemProperty());
        return view;
    }

    @Override
    protected DetailView createDetailView() {
        ToolsDetailView view = new ToolsDetailView(getRootPane());
        view.selectedItemProperty().bindBidirectional(selectedItemProperty());
        return view;
    }
}
