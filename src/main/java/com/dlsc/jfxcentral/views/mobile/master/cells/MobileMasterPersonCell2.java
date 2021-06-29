package com.dlsc.jfxcentral.views.mobile.master.cells;

import com.dlsc.jfxcentral.data.DataRepository;
import com.dlsc.jfxcentral.data.ImageManager;
import com.dlsc.jfxcentral.data.model.Person;
import com.dlsc.jfxcentral.views.MarkdownView;
import com.dlsc.jfxcentral.views.PhotoView;
import com.dlsc.jfxcentral.views.mobile.MobileAdvancedListCell;
import javafx.beans.binding.Bindings;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

public class MobileMasterPersonCell2 extends MobileAdvancedListCell<Person> {

    private final Label championLabel = new Label("CHAMPION");
    private final Label rockstarLabel = new Label("ROCKSTAR");
    private PhotoView photoView = new PhotoView();
    private Label label = new Label();
    private MarkdownView markdownView = new MarkdownView();
    private final ImageView championImageView = new ImageView();
    private final ImageView rockstarImageView = new ImageView();

    public MobileMasterPersonCell2() {
        getStyleClass().add("mobile-master-person-cell");

        photoView.setEditable(false);

        setPrefWidth(0);
        setMinWidth(0);

        championLabel.getStyleClass().add("champion-label");
        championLabel.setGraphic(championImageView);
        championLabel.setContentDisplay(ContentDisplay.RIGHT);

        rockstarLabel.getStyleClass().add("rockstar-label");
        rockstarLabel.setGraphic(rockstarImageView);
        rockstarLabel.setContentDisplay(ContentDisplay.RIGHT);

        championImageView.getStyleClass().add("champion-image");
        championImageView.setPreserveRatio(true);
        championImageView.setFitHeight(16);

        rockstarImageView.getStyleClass().add("rockstar-image");
        rockstarImageView.setPreserveRatio(true);
        rockstarImageView.setFitHeight(16);

        HBox badgesBox = new HBox(championLabel, rockstarLabel);
        badgesBox.getStyleClass().add("badges");
        badgesBox.setAlignment(Pos.TOP_LEFT);
        badgesBox.visibleProperty().bind(Bindings.createBooleanBinding(() -> championLabel.isVisible() || rockstarLabel.isVisible(), championLabel.visibleProperty(), rockstarLabel.visibleProperty()));
        badgesBox.managedProperty().bind(Bindings.createBooleanBinding(() -> championLabel.isVisible() || rockstarLabel.isVisible(), championLabel.visibleProperty(), rockstarLabel.visibleProperty()));
        VBox.setMargin(badgesBox, new Insets(10, 0, 0, 0));

        label.getStyleClass().add("title-label");
        label.setWrapText(true);
        label.setMinHeight(Region.USE_PREF_SIZE);

        VBox vbox = new VBox(label, markdownView, badgesBox);
        vbox.getStyleClass().add("vbox");
        HBox.setHgrow(vbox, Priority.ALWAYS);

        HBox hBox = new HBox(vbox, photoView);
        hBox.getStyleClass().add("hbox");
        hBox.setAlignment(Pos.TOP_LEFT);

        setGraphic(hBox);
        setContentDisplay(ContentDisplay.GRAPHIC_ONLY);

        vbox.visibleProperty().bind(emptyProperty().not());
    }

    @Override
    protected void updateItem(Person person, boolean empty) {
        super.updateItem(person, empty);

        if (!empty && person != null) {
            label.setText(person.getName());
            photoView.photoProperty().bind(ImageManager.getInstance().personImageProperty(person));
            markdownView.mdStringProperty().bind(DataRepository.getInstance().personDescriptionProperty(person));
            championLabel.setVisible(person.isChampion());
            championLabel.setManaged(person.isChampion());
            rockstarLabel.setVisible(person.isRockstar());
            rockstarLabel.setManaged(person.isRockstar());
        } else {
            championLabel.setVisible(false);
            championLabel.setManaged(false);
            rockstarLabel.setVisible(false);
            rockstarLabel.setManaged(false);
            photoView.photoProperty().unbind();
            markdownView.mdStringProperty().unbind();
        }
    }
}
