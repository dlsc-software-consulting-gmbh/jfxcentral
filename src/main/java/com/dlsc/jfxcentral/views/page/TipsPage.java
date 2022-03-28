package com.dlsc.jfxcentral.views.page;

import com.dlsc.jfxcentral.data.model.Tip;
import com.dlsc.jfxcentral.views.RootPane;
import com.dlsc.jfxcentral.views.View;
import com.dlsc.jfxcentral.views.detail.DetailView;
import com.dlsc.jfxcentral.views.detail.TipsDetailView;
import com.dlsc.jfxcentral.views.master.MasterView;
import com.dlsc.jfxcentral.views.master.TipsMasterView;
import javafx.beans.binding.Bindings;

public class TipsPage extends Page<Tip> {

    public TipsPage(RootPane rootPane) {
        super(rootPane, View.TIPS);

        titleProperty().bind(Bindings.createStringBinding(() -> getSelectedItem() != null ?
                "Tip - " + getSelectedItem().getName() + " - for JavaFX developers" :
                "Tips for JavaFX developers", selectedItemProperty()));

        descriptionProperty().bind(Bindings.createStringBinding(() -> getSelectedItem() != null ?
                "A tip for JavaFX titled '" + getSelectedItem().getName() + "'" :
                "Collection of tips for the JavaFX technology."));
    }

    @Override
    protected MasterView createMasterView() {
        TipsMasterView view = new TipsMasterView(getRootPane());
        selectedItemProperty().bindBidirectional(view.selectedItemProperty());
        return view;
    }

    @Override
    protected DetailView createDetailView() {
        TipsDetailView view = new TipsDetailView(getRootPane());
        selectedItemProperty().bindBidirectional(view.selectedItemProperty());
        return view;
    }
}
