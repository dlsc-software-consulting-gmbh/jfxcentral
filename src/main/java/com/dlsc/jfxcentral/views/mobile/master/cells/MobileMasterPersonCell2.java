package com.dlsc.jfxcentral.views.mobile.master.cells;

import com.dlsc.jfxcentral.data.DataRepository;
import com.dlsc.jfxcentral.data.ImageManager;
import com.dlsc.jfxcentral.data.model.Person;
import com.dlsc.jfxcentral.views.MarkdownView;
import com.dlsc.jfxcentral.views.PhotoView;
import com.dlsc.jfxcentral.views.mobile.MobileAdvancedListCell;
import javafx.geometry.Pos;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

public class MobileMasterPersonCell2 extends MobileAdvancedListCell<Person> {

    private PhotoView photoView = new PhotoView();
    private Label label = new Label();
    private MarkdownView markdownView = new MarkdownView();

    public MobileMasterPersonCell2() {
        getStyleClass().add("mobile-master-person-cell");

        photoView.setEditable(false);

        setPrefWidth(0);
        setMinWidth(0);

        label.getStyleClass().add("title-label");
        label.setWrapText(true);
        label.setMinHeight(Region.USE_PREF_SIZE);

        VBox vbox = new VBox(label, markdownView);
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
        } else {
            photoView.photoProperty().unbind();
            markdownView.mdStringProperty().unbind();
        }
    }
}
