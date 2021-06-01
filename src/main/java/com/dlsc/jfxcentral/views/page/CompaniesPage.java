package com.dlsc.jfxcentral.views.page;

import com.dlsc.jfxcentral.views.RootPane;
import com.dlsc.jfxcentral.views.View;
import com.dlsc.jfxcentral.model.Company;
import com.dlsc.jfxcentral.views.detail.CompaniesDetailView;
import com.dlsc.jfxcentral.views.detail.DetailView;

public class CompaniesPage extends Page<Company> {

    public CompaniesPage(RootPane rootPane) {
        super(rootPane, View.COMPANIES);
    }

    @Override
    protected DetailView createDetailView() {
        CompaniesDetailView view = new CompaniesDetailView(getRootPane());
        view.selectedItemProperty().bindBidirectional(selectedItemProperty());
        return view;
    }
}
