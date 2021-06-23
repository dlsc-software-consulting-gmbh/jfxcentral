package com.dlsc.jfxcentral.views.mobile.page;

import com.dlsc.jfxcentral.data.model.Library;
import com.dlsc.jfxcentral.views.RootPane;
import com.dlsc.jfxcentral.views.View;
import com.dlsc.jfxcentral.views.detail.DetailView;
import com.dlsc.jfxcentral.views.detail.LibrariesDetailView;
import com.dlsc.jfxcentral.views.master.MasterView;
import com.dlsc.jfxcentral.views.mobile.MobilePage;
import com.dlsc.jfxcentral.views.mobile.master.MobileLibrariesMasterView;
import javafx.beans.binding.Bindings;

public class MobileLibrariesPage extends MobilePage<Library> {

    public MobileLibrariesPage(RootPane rootPane) {
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
        return new MobileLibrariesMasterView(getRootPane());
    }

    @Override
    protected DetailView createDetailView() {
        return new LibrariesDetailView(getRootPane());
    }
}
