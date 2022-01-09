package com.dlsc.jfxcentral.views.master;

import com.dlsc.jfxcentral.util.DeveloperTool;
import com.dlsc.jfxcentral.views.RootPane;
import com.dlsc.jfxcentral.views.View;
import com.dlsc.jfxcentral.views.master.cells.MasterDeveloperToolCell;
import org.apache.commons.lang3.StringUtils;

import java.util.Comparator;

public class DeveloperToolMasterView extends MasterViewWithListView<DeveloperTool> {

    public DeveloperToolMasterView(RootPane rootPane) {
        super(rootPane, View.DEVELOPMENT);

        getStyleClass().add("developer-tools-master-view");

        listView.setCellFactory(view -> new MasterDeveloperToolCell());
        listView.setItems(createSortedAndFilteredList(rootPane.developerToolsProperty(),
                Comparator.comparing(x -> x.getName().toLowerCase()),
                tool -> StringUtils.isBlank(getFilterText()) || StringUtils.containsIgnoreCase(tool.getName(), getFilterText())));
    }
}