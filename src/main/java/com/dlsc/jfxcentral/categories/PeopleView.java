package com.dlsc.jfxcentral.categories;

import com.dlsc.gemsfx.PhotoView;
import com.dlsc.jfxcentral.RootPane;
import com.dlsc.jfxcentral.model.Person;

import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.RowConstraints;

public class PeopleView extends CategoryView {

    public PeopleView(RootPane rootPane) {
        super(rootPane);

        ListView<Person> listView = new ListView<>();
        listView.setMinWidth(Region.USE_PREF_SIZE);
        listView.setCellFactory(view -> new PersonCell());
        listView.itemsProperty().bind(rootPane.peopleProperty());

        getChildren().add(listView);
    }

    class PersonCell extends ListCell<Person> {

        private final PhotoView photoView = new PhotoView();
        private final Label nameLabel = new Label();
        private final ImageView championImageView = new ImageView();
        private final ImageView rockstarImageView = new ImageView();

        public PersonCell() {
            getStyleClass().add("person-list-cell");

            photoView.setEditable(false);

            nameLabel.getStyleClass().add("name-label");

            championImageView.getStyleClass().add("champion-image");
            championImageView.setPreserveRatio(true);
            championImageView.setFitHeight(16);

            rockstarImageView.getStyleClass().add("rockstar-image");
            rockstarImageView.setPreserveRatio(true);
            rockstarImageView.setFitHeight(16);

            HBox badgesBox = new HBox(championImageView, rockstarImageView);
            badgesBox.getStyleClass().add("badges");
            badgesBox.setAlignment(Pos.TOP_LEFT);

            GridPane gridPane = new GridPane();
            gridPane.getStyleClass().add("grid-pane");
            gridPane.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);

            gridPane.add(photoView, 0, 0);
            gridPane.add(nameLabel, 1, 0);
            gridPane.add(badgesBox, 1, 1);

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
                championImageView.setVisible(person.isChampion());
                championImageView.setManaged(person.isChampion());
                rockstarImageView.setVisible(person.isRockstar());
                rockstarImageView.setManaged(person.isRockstar());
                photoView.setVisible(true);
                photoView.setPhoto(person.getPhoto());
            } else {
                nameLabel.setText("");
                championImageView.setVisible(false);
                championImageView.setManaged(false);
                rockstarImageView.setVisible(false);
                rockstarImageView.setManaged(false);
                photoView.setVisible(false);
            }
        }
    }
}