package com.dlsc.jfxcentral.views.page;

import com.dlsc.jfxcentral.data.model.Library;
import com.dlsc.jfxcentral.views.RootPane;
import com.dlsc.jfxcentral.views.View;
import com.dlsc.jfxcentral.views.detail.DetailView;
import com.dlsc.jfxcentral.views.detail.LibrariesDetailView;
import com.dlsc.jfxcentral.views.master.LibrariesMasterView;
import com.dlsc.jfxcentral.views.master.MasterView;
import javafx.beans.binding.Bindings;

public class LibrariesPage extends Page<Library> {

    public LibrariesPage(RootPane rootPane) {
        super(rootPane, View.LIBRARIES);

        titleProperty().bind(Bindings.createStringBinding(() -> getSelectedItem() != null ?
                "Library - " + getSelectedItem().getTitle() :
                "Libraries", selectedItemProperty()));

        descriptionProperty().bind(Bindings.createStringBinding(() -> getSelectedItem() != null ?
                "Information about the library '" + getSelectedItem().getTitle() + "'" :
                "Collection of libraries that can be used for developing JavaFX applications."));
    }

    @Override
    protected MasterView createMasterView() {
        LibrariesMasterView view = new LibrariesMasterView(getRootPane());
        selectedItemProperty().bindBidirectional(view.selectedItemProperty());
        return view;
    }

    @Override
    protected DetailView createDetailView() {
        LibrariesDetailView view = new LibrariesDetailView(getRootPane());
        selectedItemProperty().bindBidirectional(view.selectedItemProperty());
        return view;
    }
}
