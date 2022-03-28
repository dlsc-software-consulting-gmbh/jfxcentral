package com.dlsc.jfxcentral.views.mobile.page;

import com.dlsc.jfxcentral.data.model.Tip;
import com.dlsc.jfxcentral.views.RootPane;
import com.dlsc.jfxcentral.views.View;
import com.dlsc.jfxcentral.views.detail.DetailView;
import com.dlsc.jfxcentral.views.detail.TipsDetailView;
import com.dlsc.jfxcentral.views.master.MasterView;
import com.dlsc.jfxcentral.views.mobile.MobilePage;
import com.dlsc.jfxcentral.views.mobile.master.MobileTipsMasterView;
import javafx.beans.binding.Bindings;

public class MobileTipsPage extends MobilePage<Tip> {

    public MobileTipsPage(RootPane rootPane) {
        super(rootPane, View.TIPS);

        titleProperty().bind(Bindings.createStringBinding(() -> getSelectedItem() != null ?
                "Tip - " + getSelectedItem().getName() + " - for JavaFX":
                "Tips & Tricks for JavaFX", selectedItemProperty()));

        setDescription("Tips & Tricks for JavaFX developers");
    }

    @Override
    protected MasterView<Tip> createMasterView() {
        return new MobileTipsMasterView(getRootPane());
    }

    @Override
    protected DetailView createDetailView() {
        return new TipsDetailView(getRootPane());
    }
}
