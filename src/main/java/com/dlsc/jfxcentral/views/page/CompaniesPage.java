package com.dlsc.jfxcentral.views.page;

import com.dlsc.jfxcentral.model.Company;
import com.dlsc.jfxcentral.views.RootPane;
import com.dlsc.jfxcentral.views.View;
import com.dlsc.jfxcentral.views.detail.CompaniesDetailView;
import com.dlsc.jfxcentral.views.detail.DetailView;
import javafx.beans.binding.Bindings;

public class CompaniesPage extends Page<Company> {

    public CompaniesPage(RootPane rootPane) {
        super(rootPane, View.COMPANIES);

        titleProperty().bind(Bindings.createStringBinding(() -> getSelectedItem() != null ?
                "Company - " + getSelectedItem().getName() :
                "Companies", selectedItemProperty()));

        descriptionProperty().bind(Bindings.createStringBinding(() -> getSelectedItem() != null ?
                "Information about the company '" + getSelectedItem().getName() + "'" :
                "Collection of companies working with and on JavaFX technology."));
    }

    @Override
    protected DetailView createDetailView() {
        CompaniesDetailView view = new CompaniesDetailView(getRootPane());
        selectedItemProperty().bindBidirectional(view.selectedItemProperty());
        return view;
    }
}
