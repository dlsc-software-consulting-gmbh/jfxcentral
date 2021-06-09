package com.dlsc.jfxcentral.views.master;

import com.dlsc.jfxcentral.data.DataRepository;
import com.dlsc.jfxcentral.data.ImageManager;
import com.dlsc.jfxcentral.data.model.Person;
import com.dlsc.jfxcentral.views.AdvancedListCell;
import com.dlsc.jfxcentral.views.PhotoView;
import com.dlsc.jfxcentral.views.RootPane;
import com.dlsc.jfxcentral.views.View;
import com.jpro.webapi.WebAPI;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import org.apache.commons.lang3.StringUtils;

import java.util.Comparator;

public class PeopleMasterView extends MasterViewWithListView<Person> {

    public PeopleMasterView(RootPane rootPane) {
        super(rootPane, View.PEOPLE);

        getStyleClass().add("people-master-view");

        listView.setMinWidth(Region.USE_PREF_SIZE);
        listView.setCellFactory(view -> new PersonCell());
        listView.setItems(createSortedAndFilteredList(DataRepository.getInstance().peopleProperty(),
                Comparator.comparing(Person::getName),
                person -> StringUtils.isBlank(getFilterText()) || StringUtils.containsIgnoreCase(person.getName(), getFilterText())));

        setCenter(listView);
    }

    class PersonCell extends AdvancedListCell<Person> {

        private final PhotoView photoView = new PhotoView();
        private final Label nameLabel = new Label();
        private final Label championLabel = new Label("CHAMPION");
        private final Label rockstarLabel = new Label("ROCKSTAR");
        private final javafx.scene.image.ImageView championImageView = new javafx.scene.image.ImageView();
        private final javafx.scene.image.ImageView rockstarImageView = new javafx.scene.image.ImageView();
        private final GridPane gridPane;

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

            gridPane = new GridPane();
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

            gridPane.visibleProperty().bind(emptyProperty().not());
            gridPane.managedProperty().bind(emptyProperty().not());
        }

        @Override
        protected void updateItem(Person person, boolean empty) {
            super.updateItem(person, empty);
            if(person != null) {
                System.out.println("updateItem: " + person.getId());
            }

            if (!empty && person != null) {
                nameLabel.setText(person.getName());
                championLabel.setVisible(person.isChampion());
                championLabel.setManaged(person.isChampion());
                rockstarLabel.setVisible(person.isRockstar());
                rockstarLabel.setManaged(person.isRockstar());
                photoView.photoProperty().bind(ImageManager.getInstance().personImageProperty(person));

                if (WebAPI.isBrowser()) {
                    setMouseTransparent(true);
                }

                setCellLink(PersonCell.this, person, person.getDescription());
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