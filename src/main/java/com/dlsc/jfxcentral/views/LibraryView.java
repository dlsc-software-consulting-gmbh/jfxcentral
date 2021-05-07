package com.dlsc.jfxcentral.views;

import com.dlsc.jfxcentral.RootPane;
import com.dlsc.jfxcentral.model.Library;
import com.dlsc.jfxcentral.panels.SectionPaneWithFilterView;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

public class LibraryView extends PageView {

    public LibraryView(RootPane rootPane) {
        super(rootPane);

        SectionPaneWithFilterView sectionPane = new SectionPaneWithFilterView();
        sectionPane.setTitle("Libraries");
        sectionPane.setEnableAutoSubtitle(true);

        setContent(sectionPane);
    }

    private final ObjectProperty<Library> Library = new SimpleObjectProperty<>(this, "Library");

    public Library getLibrary() {
        return Library.get();
    }

    public ObjectProperty<Library> libraryProperty() {
        return Library;
    }

    public void setLibrary(Library Library) {
        this.Library.set(Library);
    }
}