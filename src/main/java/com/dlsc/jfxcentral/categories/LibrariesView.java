package com.dlsc.jfxcentral.categories;

import com.dlsc.jfxcentral.DataRepository;
import com.dlsc.jfxcentral.ImageManager;
import com.dlsc.jfxcentral.RootPane;
import com.dlsc.jfxcentral.View;
import com.dlsc.jfxcentral.model.Library;
import com.dlsc.jfxcentral.panels.LicenseLabel;
import com.dlsc.jfxcentral.views.AdvancedListCell;
import com.dlsc.jfxcentral.views.LibraryView;
import javafx.beans.Observable;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.RowConstraints;
import org.apache.commons.lang3.StringUtils;

import java.util.Comparator;

public class LibrariesView extends CategoryView<Library> {

    private LibraryView libraryView;
    private ListView<Library> listView = new ListView<>();

    @Override
    public View getView() {
        return View.LIBRARIES;
    }

    public LibrariesView(RootPane rootPane) {
        super(rootPane);

        listView.setMinWidth(Region.USE_PREF_SIZE);
        listView.setCellFactory(view -> new SimpleLibraryCell());
        listView.setItems(createSortedAndFilteredList(DataRepository.getInstance().librariesProperty(),
                Comparator.comparing(Library::getTitle),
                library -> StringUtils.isBlank(getFilterText()) || StringUtils.containsIgnoreCase(library.getTitle(), getFilterText())));
        listView.getSelectionModel().selectedItemProperty().addListener(it -> setLibrary(listView.getSelectionModel().getSelectedItem()));
        listView.getItems().addListener((Observable it) -> performDefaultSelection());

        listView.getSelectionModel().selectedItemProperty().addListener(it -> setItem(listView.getSelectionModel().getSelectedItem()));
        itemProperty().addListener(it -> listView.getSelectionModel().select(getItem()));

        setCenter(listView);

        performDefaultSelection();
    }

    private void performDefaultSelection() {
        if (!listView.getItems().isEmpty()) {
            listView.getSelectionModel().select(0);
        } else {
            listView.getSelectionModel().clearSelection();
        }
    }

    @Override
    public Node getDetailPane() {
        if (libraryView == null) {
            libraryView = new LibraryView(getRootPane());
            libraryView.libraryProperty().bind(libraryProperty());
        }

        return libraryView;
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

    class SimpleLibraryCell extends AdvancedListCell<Library> {

        private final ImageView imageView = new ImageView();
        private final Label nameLabel = new Label();
        private final LicenseLabel licenseLabel = new LicenseLabel();
        private final GridPane gridPane;

        public SimpleLibraryCell() {
            getStyleClass().add("library-list-cell");

            imageView.setFitHeight(30);
            imageView.setFitWidth(30);
            imageView.setPreserveRatio(true);

            nameLabel.getStyleClass().add("name-label");

            gridPane = new GridPane();
            gridPane.getStyleClass().add("grid-pane");
            gridPane.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);

            gridPane.add(imageView, 1, 0);
            gridPane.add(nameLabel, 0, 0);
            gridPane.add(licenseLabel, 0, 1);

            GridPane.setRowSpan(imageView, 2);
            GridPane.setHgrow(nameLabel, Priority.ALWAYS);
            GridPane.setVgrow(nameLabel, Priority.ALWAYS);
            GridPane.setValignment(nameLabel, VPos.BOTTOM);
            GridPane.setValignment(licenseLabel, VPos.TOP);

            RowConstraints row1 = new RowConstraints();
            RowConstraints row2 = new RowConstraints();

            row1.setPercentHeight(50);
            row2.setPercentHeight(50);

            gridPane.getRowConstraints().setAll(row1, row2);

            setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
            setGraphic(gridPane);
        }

        @Override
        protected void updateItem(Library library, boolean empty) {
            super.updateItem(library, empty);

            if (!empty && library != null) {
                nameLabel.setText(library.getTitle());
                imageView.setVisible(true);
                imageView.imageProperty().bind(ImageManager.getInstance().libraryImageProperty(library));
                licenseLabel.setLicense(library.getLicense());
                licenseLabel.setVisible(true);

                this.setMouseTransparent(true);
                com.jpro.web.Util.setLink(gridPane, "/?page=/LIBRARIES/"+library.getId(), library.getTitle(), this.getChildren());
            } else {
                nameLabel.setText("");
                imageView.setVisible(false);
                licenseLabel.setVisible(false);
            }
        }
    }
}