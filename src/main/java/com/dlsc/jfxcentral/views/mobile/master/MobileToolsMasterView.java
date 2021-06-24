package com.dlsc.jfxcentral.views.mobile.master;

import com.dlsc.jfxcentral.data.DataRepository;
import com.dlsc.jfxcentral.data.model.Tool;
import com.dlsc.jfxcentral.panels.PrettyScrollPane;
import com.dlsc.jfxcentral.views.RootPane;
import com.dlsc.jfxcentral.views.View;
import com.dlsc.jfxcentral.views.mobile.MobileMasterViewWithAdvancedListView;
import com.dlsc.jfxcentral.views.mobile.master.cells.MobileMasterToolCell;
import org.apache.commons.lang3.StringUtils;

import java.util.Comparator;

public class MobileToolsMasterView extends MobileMasterViewWithAdvancedListView<Tool> {

    public MobileToolsMasterView(RootPane rootPane) {
        super(rootPane, View.TOOLS);

        getStyleClass().addAll("mobile-tools-master-view", "tools-master-view");

        listView.setPaging(true);
        listView.setVisibleRowCount(Integer.MAX_VALUE);
        listView.setCellFactory(view -> new MobileMasterToolCell());
        listView.setItems(createSortedAndFilteredList(DataRepository.getInstance().toolsProperty(),
                Comparator.comparing(Tool::getName),
                tool -> StringUtils.isBlank(getFilterText()) || StringUtils.containsIgnoreCase(tool.getName(), getFilterText()) || StringUtils.containsIgnoreCase(tool.getSummary(), getFilterText())));

        PrettyScrollPane scrollPane = new PrettyScrollPane(listView);
        scrollPane.setShowScrollToTopButton(true);
        scrollPane.setFitToWidth(true);
        setCenter(scrollPane);
    }
}