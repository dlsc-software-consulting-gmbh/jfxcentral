package com.dlsc.jfxcentral.views.mobile.page;

import com.dlsc.jfxcentral.data.model.Tutorial;
import com.dlsc.jfxcentral.views.RootPane;
import com.dlsc.jfxcentral.views.View;
import com.dlsc.jfxcentral.views.detail.DetailView;
import com.dlsc.jfxcentral.views.detail.TutorialsDetailView;
import com.dlsc.jfxcentral.views.mobile.MobilePage;
import javafx.beans.binding.Bindings;

public class MobileTutorialsPage extends MobilePage<Tutorial> {

    public MobileTutorialsPage(RootPane rootPane) {
        super(rootPane, View.TUTORIALS);

        titleProperty().bind(Bindings.createStringBinding(() -> getSelectedItem() != null ?
                "Tutorial - " + getSelectedItem().getId() :
                "Tutorials", selectedItemProperty()));

        descriptionProperty().bind(Bindings.createStringBinding(() -> getSelectedItem() != null ?
                "Detailed information on the JavaFX tutorial '" + getSelectedItem().getName() + "'" :
                "A list of tutorials that can be used by developers to create their JavaFX applications."));
    }
    
    @Override
    protected DetailView<Tutorial> createDetailView() {
        return new TutorialsDetailView(getRootPane());
    }
}
