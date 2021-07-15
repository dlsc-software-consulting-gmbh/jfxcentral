package com.dlsc.jfxcentral.views.mobile.master;

import com.dlsc.jfxcentral.data.DataRepository;
import com.dlsc.jfxcentral.data.model.RealWorldApp;
import com.dlsc.jfxcentral.panels.PrettyScrollPane;
import com.dlsc.jfxcentral.views.RootPane;
import com.dlsc.jfxcentral.views.View;
import com.dlsc.jfxcentral.views.mobile.MobileMasterViewWithAdvancedListView;
import com.dlsc.jfxcentral.views.mobile.master.cells.MobileMasterRealWorldAppCell;
import javafx.scene.layout.Region;
import org.apache.commons.lang3.StringUtils;

import java.util.Comparator;

public class MobileRealWorldAppsMasterView extends MobileMasterViewWithAdvancedListView<RealWorldApp> {

    public MobileRealWorldAppsMasterView(RootPane rootPane) {
        super(rootPane, View.REAL_WORLD);

        getStyleClass().addAll("mobile", "real-world-master-view");

        listView.setPaging(true);
        listView.setVisibleRowCount(Integer.MAX_VALUE);
        listView.setMinWidth(Region.USE_PREF_SIZE);
        listView.setCellFactory(view -> new MobileMasterRealWorldAppCell());
        listView.setItems(createSortedAndFilteredList(DataRepository.getInstance().realWorldAppsProperty(),
                Comparator.comparing(x -> x.getName().toLowerCase()),
                app -> StringUtils.isBlank(getFilterText()) ||
                        StringUtils.containsIgnoreCase(app.getName(), getFilterText()) ||
                        StringUtils.containsIgnoreCase(app.getSummary(), getFilterText()) ||
                        StringUtils.containsIgnoreCase(app.getCompany(), getFilterText())));

        PrettyScrollPane scrollPane = new PrettyScrollPane(listView);
        scrollPane.setMobile(true);
        scrollPane.setShowScrollToTopButton(true);
        scrollPane.setFitToWidth(true);
        setCenter(scrollPane);
    }
}