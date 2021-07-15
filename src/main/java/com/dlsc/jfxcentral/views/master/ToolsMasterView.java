package com.dlsc.jfxcentral.views.master;

import com.dlsc.jfxcentral.data.DataRepository;
import com.dlsc.jfxcentral.data.model.Tool;
import com.dlsc.jfxcentral.views.RootPane;
import com.dlsc.jfxcentral.views.View;
import com.dlsc.jfxcentral.views.master.cells.MasterToolCell;
import org.apache.commons.lang3.StringUtils;

import java.util.Comparator;

public class ToolsMasterView extends MasterViewWithListView<Tool> {

    public ToolsMasterView(RootPane rootPane) {
        super(rootPane, View.TOOLS);

        getStyleClass().add("tools-master-view");

        listView.setCellFactory(view -> new MasterToolCell());
        listView.setItems(createSortedAndFilteredList(DataRepository.getInstance().toolsProperty(),
                Comparator.comparing(x -> x.getName().toLowerCase()),
                tool -> StringUtils.isBlank(getFilterText()) || StringUtils.containsIgnoreCase(tool.getName(), getFilterText()) || StringUtils.containsIgnoreCase(tool.getSummary(), getFilterText())));
    }
}