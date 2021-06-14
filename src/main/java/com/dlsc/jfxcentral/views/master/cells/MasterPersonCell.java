package com.dlsc.jfxcentral.views.master.cells;

import com.dlsc.jfxcentral.data.ImageManager;
import com.dlsc.jfxcentral.data.model.Person;
import com.dlsc.jfxcentral.views.AdvancedListCell;
import com.dlsc.jfxcentral.views.PhotoView;
import com.dlsc.jfxcentral.views.View;
import com.jpro.webapi.WebAPI;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;

public class MasterPersonCell extends AdvancedListCell<Person> {

    private final PhotoView photoView = new PhotoView();
    private final Label nameLabel = new Label();
    private final Label championLabel = new Label("CHAMPION");
    private final Label rockstarLabel = new Label("ROCKSTAR");
    private final javafx.scene.image.ImageView championImageView = new javafx.scene.image.ImageView();
    private final javafx.scene.image.ImageView rockstarImageView = new javafx.scene.image.ImageView();
    private final GridPane gridPane;

    public MasterPersonCell() {
        getStyleClass().add("master-person-list-cell");

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

            setMasterCellLink(MasterPersonCell.this, person, person.getDescription(), View.PEOPLE);
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
