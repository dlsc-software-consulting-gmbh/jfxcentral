package com.dlsc.jfxcentral.views.master;

import com.dlsc.jfxcentral.data.DataRepository;
import com.dlsc.jfxcentral.data.model.Tip;
import com.dlsc.jfxcentral.views.RootPane;
import com.dlsc.jfxcentral.views.View;
import com.dlsc.jfxcentral.views.master.cells.MasterTipCell;
import org.apache.commons.lang3.StringUtils;

import java.util.Comparator;

public class TipsMasterView extends MasterViewWithListView<Tip> {

    public TipsMasterView(RootPane rootPane) {
        super(rootPane, View.TIPS);

        getStyleClass().add("tips-master-view");

        listView.setCellFactory(view -> new MasterTipCell());
        listView.setItems(createSortedAndFilteredList(DataRepository.getInstance().tipsProperty(),
                Comparator.comparing(Tip::getName),
                tip -> StringUtils.isBlank(getFilterText()) || StringUtils.containsIgnoreCase(tip.getName(), getFilterText())));
    }
}