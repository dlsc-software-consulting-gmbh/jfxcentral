package com.dlsc.jfxcentral.views.mobile.page;

import com.dlsc.jfxcentral.data.model.Company;
import com.dlsc.jfxcentral.views.RootPane;
import com.dlsc.jfxcentral.views.View;
import com.dlsc.jfxcentral.views.detail.CompaniesDetailView;
import com.dlsc.jfxcentral.views.detail.DetailView;
import com.dlsc.jfxcentral.views.mobile.MobilePage;
import javafx.beans.binding.Bindings;

public class MobileCompaniesPage extends MobilePage<Company> {

    public MobileCompaniesPage(RootPane rootPane) {
        super(rootPane, View.COMPANIES);

        titleProperty().bind(Bindings.createStringBinding(() -> getSelectedItem() != null ?
                "Company - " + getSelectedItem().getName() + " - working in the JavaFX space" :
                "Companies working in the JavaFX space", selectedItemProperty()));

        descriptionProperty().bind(Bindings.createStringBinding(() -> getSelectedItem() != null ?
                "Information about the company '" + getSelectedItem().getName() + "'" :
                "Collection of companies working with and on JavaFX technology."));
    }
    
    @Override
    protected DetailView<Company> createDetailView() {
        return new CompaniesDetailView(getRootPane());
    }
}
