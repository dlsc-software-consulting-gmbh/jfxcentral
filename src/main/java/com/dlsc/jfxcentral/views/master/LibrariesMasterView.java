package com.dlsc.jfxcentral.views.master;

import com.dlsc.jfxcentral.data.DataRepository;
import com.dlsc.jfxcentral.data.model.Library;
import com.dlsc.jfxcentral.views.RootPane;
import com.dlsc.jfxcentral.views.View;
import com.dlsc.jfxcentral.views.master.cells.MasterLibraryCell;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.layout.Region;
import org.apache.commons.lang3.StringUtils;

import java.util.Comparator;

public class LibrariesMasterView extends MasterViewWithListView<Library> {

    public LibrariesMasterView(RootPane rootPane) {
        super(rootPane, View.LIBRARIES);

        getStyleClass().add("libraries-master-view");

        listView.setMinWidth(Region.USE_PREF_SIZE);
        listView.setCellFactory(view -> new MasterLibraryCell());
        listView.setItems(createSortedAndFilteredList(DataRepository.getInstance().librariesProperty(),
                Comparator.comparing(Library::getName),
                library -> StringUtils.isBlank(getFilterText()) || StringUtils.containsIgnoreCase(library.getName(), getFilterText())));
        listView.getSelectionModel().selectedItemProperty().addListener(it -> setLibrary(listView.getSelectionModel().getSelectedItem()));

        setCenter(listView);
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