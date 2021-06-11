package com.dlsc.jfxcentral.views.master;

import com.dlsc.jfxcentral.data.DataRepository;
import com.dlsc.jfxcentral.data.model.RealWorldApp;
import com.dlsc.jfxcentral.views.RootPane;
import com.dlsc.jfxcentral.views.View;
import com.dlsc.jfxcentral.views.master.cell.MasterRealWorldAppCell;
import javafx.scene.layout.Region;
import org.apache.commons.lang3.StringUtils;

import java.util.Comparator;

public class RealWorldAppsMasterView extends MasterViewWithListView<RealWorldApp> {

    public RealWorldAppsMasterView(RootPane rootPane) {
        super(rootPane, View.REAL_WORLD);

        getStyleClass().add("real-world-master-view");

        listView.setMinWidth(Region.USE_PREF_SIZE);
        listView.setCellFactory(view -> new MasterRealWorldAppCell());
        listView.setItems(createSortedAndFilteredList(DataRepository.getInstance().realWorldAppsProperty(),
                Comparator.comparing(RealWorldApp::getName),
                app -> StringUtils.isBlank(getFilterText()) ||
                        StringUtils.containsIgnoreCase(app.getName(), getFilterText()) ||
                        StringUtils.containsIgnoreCase(app.getSummary(), getFilterText()) ||
                        StringUtils.containsIgnoreCase(app.getCompany(), getFilterText())));

        setCenter(listView);
    }
}