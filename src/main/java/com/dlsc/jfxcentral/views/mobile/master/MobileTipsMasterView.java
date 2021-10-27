package com.dlsc.jfxcentral.views.mobile.master;

import com.dlsc.jfxcentral.data.DataRepository;
import com.dlsc.jfxcentral.data.model.Tip;
import com.dlsc.jfxcentral.panels.PrettyScrollPane;
import com.dlsc.jfxcentral.views.RootPane;
import com.dlsc.jfxcentral.views.View;
import com.dlsc.jfxcentral.views.mobile.MobileMasterViewWithAdvancedListView;
import com.dlsc.jfxcentral.views.mobile.master.cells.MobileMasterTipsCell;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.layout.Region;
import org.apache.commons.lang3.StringUtils;

import java.util.Comparator;

public class MobileTipsMasterView extends MobileMasterViewWithAdvancedListView<Tip> {

    public MobileTipsMasterView(RootPane rootPane) {
        super(rootPane, View.TIPS);

        getStyleClass().addAll("tips-master-view", "mobile-tips-master-view");

        listView.setMinWidth(Region.USE_PREF_SIZE);
        listView.setPaging(true);
        listView.setVisibleRowCount(Integer.MAX_VALUE);
        listView.setCellFactory(view -> new MobileMasterTipsCell());
        listView.setItems(createSortedAndFilteredList(DataRepository.getInstance().tipsProperty(),
                Comparator.comparing(x -> x.getName().toLowerCase()),
                library -> StringUtils.isBlank(getFilterText()) || StringUtils.containsIgnoreCase(library.getName(), getFilterText())));
        listView.getSelectionModel().selectedItemProperty().addListener(it -> setTip(listView.getSelectionModel().getSelectedItem()));

        PrettyScrollPane scrollPane = new PrettyScrollPane(listView);
        scrollPane.setMobile(true);
        scrollPane.setShowScrollToTopButton(true);
        scrollPane.setFitToWidth(true);
        setCenter(scrollPane);
    }

    private final ObjectProperty<Tip> tip = new SimpleObjectProperty<>(this, "tip");

    public Tip getTip() {
        return tip.get();
    }

    public ObjectProperty<Tip> tipProperty() {
        return tip;
    }

    public void setTip(Tip tip) {
        this.tip.set(tip);
    }
}