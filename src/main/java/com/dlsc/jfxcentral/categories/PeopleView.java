package com.dlsc.jfxcentral.categories;

import com.dlsc.jfxcentral.DataRepository;
import com.dlsc.jfxcentral.ImageManager;
import com.dlsc.jfxcentral.PhotoView;
import com.dlsc.jfxcentral.RootPane;
import com.dlsc.jfxcentral.model.Person;
import com.dlsc.jfxcentral.views.AdvancedListCell;
import com.dlsc.jfxcentral.views.PersonView;
import javafx.beans.Observable;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.*;

public class PeopleView extends CategoryView {

    private PersonView personView;
    private ListView<Person> listView = new ListView<>();

    public PeopleView(RootPane rootPane) {
        super(rootPane);

        getStyleClass().add("people-view");

        listView.setMinWidth(Region.USE_PREF_SIZE);
        listView.setCellFactory(view -> new PersonCell());
        listView.itemsProperty().bind(DataRepository.getInstance().peopleProperty());
        listView.getSelectionModel().selectedItemProperty().addListener(it -> setPerson(listView.getSelectionModel().getSelectedItem()));
        listView.getItems().addListener((Observable it) -> performDefaultSelection());

        personProperty().addListener(it -> listView.getSelectionModel().select(getPerson()));

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
        if (personView == null) {
            personView = new PersonView(getRootPane());
            personView.personProperty().bind(personProperty());
        }

        return personView;
    }

    private final ObjectProperty<Person> person = new SimpleObjectProperty<>(this, "person");

    public Person getPerson() {
        return person.get();
    }

    public ObjectProperty<Person> personProperty() {
        return person;
    }

    public void setPerson(Person person) {
        this.person.set(person);
    }

    class PersonCell extends AdvancedListCell<Person> {

        private final PhotoView photoView = new PhotoView();
        private final Label nameLabel = new Label();
        private final Label championLabel = new Label("CHAMPION");
        private final Label rockstarLabel = new Label("ROCKSTAR");
        private final javafx.scene.image.ImageView championImageView = new javafx.scene.image.ImageView();
        private final javafx.scene.image.ImageView rockstarImageView = new javafx.scene.image.ImageView();

        public PersonCell() {
            getStyleClass().add("person-list-cell");

            photoView.setEditable(false);
            photoView.setPlaceholder(new Label("test"));
            photoView.visibleProperty().bind(photoView.photoProperty().isNotNull());
            photoView.managedProperty().bind(photoView.photoProperty().isNotNull());

            nameLabel.getStyleClass().add("name-label");

            championLabel.getStyleClass().add("champion-label");
            championLabel.setGraphic(championImageView);
            championLabel.setContentDisplay(ContentDisplay.RIGHT);

            rockstarLabel.getStyleClass().add("rockstar-label");
            rockstarLabel.setGraphic(rockstarImageView);
            rockstarLabel.setContentDisplay(ContentDisplay.RIGHT);

            championImageView.getStyleClass().add("champion-image");
            championImageView.setPreserveRatio(true);
            championImageView.setFitHeight(12);

            rockstarImageView.getStyleClass().add("rockstar-image");
            rockstarImageView.setPreserveRatio(true);
            rockstarImageView.setFitHeight(12);

            HBox badgesBox = new HBox(championLabel, rockstarLabel);
            badgesBox.getStyleClass().add("badges");
            badgesBox.setAlignment(Pos.TOP_LEFT);

            GridPane gridPane = new GridPane();
            gridPane.getStyleClass().add("grid-pane");
            gridPane.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);

            gridPane.add(photoView, 1, 0);
            gridPane.add(nameLabel, 0, 0);
            gridPane.add(badgesBox, 0, 1);

            GridPane.setRowSpan(photoView, 2);
            GridPane.setHgrow(nameLabel, Priority.ALWAYS);
            GridPane.setHgrow(badgesBox, Priority.ALWAYS);
            GridPane.setVgrow(nameLabel, Priority.ALWAYS);
            GridPane.setVgrow(badgesBox, Priority.ALWAYS);
            GridPane.setValignment(nameLabel, VPos.BOTTOM);
            GridPane.setValignment(badgesBox, VPos.TOP);

            RowConstraints row1 = new RowConstraints();
            RowConstraints row2 = new RowConstraints();

            row1.setPercentHeight(50);
            row2.setPercentHeight(50);

            gridPane.getRowConstraints().setAll(row1, row2);

            setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
            setGraphic(gridPane);
        }

        @Override
        protected void updateItem(Person person, boolean empty) {
            super.updateItem(person, empty);

            if (!empty && person != null) {
                nameLabel.setText(person.getName());
                championLabel.setVisible(person.isChampion());
                championLabel.setManaged(person.isChampion());
                rockstarLabel.setVisible(person.isRockstar());
                rockstarLabel.setManaged(person.isRockstar());
                photoView.photoProperty().bind(ImageManager.getInstance().personImageProperty(person));
            } else {
                nameLabel.setText("");
                championLabel.setVisible(false);
                championLabel.setManaged(false);
                rockstarLabel.setVisible(false);
                rockstarLabel.setManaged(false);
                photoView.photoProperty().unbind();
            }
        }
    }
}