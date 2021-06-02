package com.dlsc.jfxcentral.views.page;

import com.dlsc.jfxcentral.data.model.Tutorial;
import com.dlsc.jfxcentral.views.RootPane;
import com.dlsc.jfxcentral.views.View;
import com.dlsc.jfxcentral.views.detail.DetailView;
import com.dlsc.jfxcentral.views.detail.TutorialsDetailView;
import com.dlsc.jfxcentral.views.master.MasterView;
import com.dlsc.jfxcentral.views.master.TutorialsMasterView;
import javafx.beans.binding.Bindings;

public class TutorialsPage extends Page<Tutorial> {

    public TutorialsPage(RootPane rootPane) {
        super(rootPane, View.TUTORIALS);

        titleProperty().bind(Bindings.createStringBinding(() -> getSelectedItem() != null ?
                "Tutorial - " + getSelectedItem().getId() :
                "Tutorials", selectedItemProperty()));

        descriptionProperty().bind(Bindings.createStringBinding(() -> getSelectedItem() != null ?
                "Detailed information on the JavaFX tutorial '" + getSelectedItem().getName() + "'" :
                "A list of tutorials that can be used by developers to create their JavaFX applications."));
    }

    @Override
    protected MasterView<Tutorial> createMasterView() {
        TutorialsMasterView view = new TutorialsMasterView(getRootPane());
        selectedItemProperty().bindBidirectional(view.selectedItemProperty());
        return view;
    }

    @Override
    protected DetailView<Tutorial> createDetailView() {
        TutorialsDetailView view = new TutorialsDetailView(getRootPane());
        selectedItemProperty().bindBidirectional(view.selectedItemProperty());
        return view;
    }
}
