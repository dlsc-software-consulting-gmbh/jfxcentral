package com.dlsc.jfxcentral.views.master;

import com.dlsc.jfxcentral.data.DataRepository;
import com.dlsc.jfxcentral.data.model.Library;
import com.dlsc.jfxcentral.views.RootPane;
import com.dlsc.jfxcentral.views.View;
import com.dlsc.jfxcentral.views.master.cells.MasterLibraryCell;
import org.apache.commons.lang3.StringUtils;

import java.util.Comparator;

public class LibrariesMasterView extends MasterViewWithListView<Library> {

    public LibrariesMasterView(RootPane rootPane) {
        super(rootPane, View.LIBRARIES);

        getStyleClass().add("libraries-master-view");

        listView.setCellFactory(view -> new MasterLibraryCell());
        listView.setItems(createSortedAndFilteredList(DataRepository.getInstance().librariesProperty(),
                Comparator.comparing(x -> x.getName().toLowerCase()),
                library -> StringUtils.isBlank(getFilterText()) || StringUtils.containsIgnoreCase(library.getName(), getFilterText())));
    }
}