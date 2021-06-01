package com.dlsc.jfxcentral.views.page;

import com.dlsc.jfxcentral.views.RootPane;
import com.dlsc.jfxcentral.views.View;
import com.dlsc.jfxcentral.views.master.LibrariesMasterView;
import com.dlsc.jfxcentral.views.master.MasterView;
import com.dlsc.jfxcentral.model.Library;
import com.dlsc.jfxcentral.views.detail.DetailView;
import com.dlsc.jfxcentral.views.detail.LibrariesDetailView;

public class LibrariesPage extends Page<Library> {

    public LibrariesPage(RootPane rootPane) {
        super(rootPane, View.LIBRARIES);
    }

    @Override
    protected MasterView createMasterView() {
        LibrariesMasterView view = new LibrariesMasterView(getRootPane());
        view.selectedItemProperty().bindBidirectional(selectedItemProperty());
        return view;
    }

    @Override
    protected DetailView createDetailView() {
        LibrariesDetailView view = new LibrariesDetailView(getRootPane());
        view.selectedItemProperty().bindBidirectional(selectedItemProperty());
        return view;
    }
}
