package com.dlsc.jfxcentral.views.mobile.master;

import com.dlsc.jfxcentral.data.DataRepository;
import com.dlsc.jfxcentral.data.model.Library;
import com.dlsc.jfxcentral.panels.PrettyScrollPane;
import com.dlsc.jfxcentral.views.RootPane;
import com.dlsc.jfxcentral.views.View;
import com.dlsc.jfxcentral.views.mobile.MobileMasterViewWithAdvancedListView;
import com.dlsc.jfxcentral.views.mobile.master.cells.MobileMasterLibraryCell;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.layout.Region;
import org.apache.commons.lang3.StringUtils;

import java.util.Comparator;

public class MobileLibrariesMasterView extends MobileMasterViewWithAdvancedListView<Library> {

    public MobileLibrariesMasterView(RootPane rootPane) {
        super(rootPane, View.LIBRARIES);

        getStyleClass().addAll("libraries-master-view", "mobile-libraries-master-view");

        listView.setMinWidth(Region.USE_PREF_SIZE);
        listView.setPaging(true);
        listView.setVisibleRowCount(Integer.MAX_VALUE);
        listView.setCellFactory(view -> new MobileMasterLibraryCell());
        listView.setItems(createSortedAndFilteredList(DataRepository.getInstance().librariesProperty(),
                Comparator.comparing(Library::getTitle),
                library -> StringUtils.isBlank(getFilterText()) || StringUtils.containsIgnoreCase(library.getTitle(), getFilterText())));
        listView.getSelectionModel().selectedItemProperty().addListener(it -> setLibrary(listView.getSelectionModel().getSelectedItem()));

        PrettyScrollPane scrollPane = new PrettyScrollPane(listView);
        scrollPane.setShowScrollToTopButton(true);
        scrollPane.setFitToWidth(true);
        setCenter(scrollPane);
    }

    private final ObjectProperty<Library> library = new SimpleObjectProperty<>(this, "library");

    public Library getLibrary() {
        return library.get();
    }

    public ObjectProperty<Library> libraryProperty() {
        return library;
    }

    public void setLibrary(Library library) {
        this.library.set(library);
    }
}