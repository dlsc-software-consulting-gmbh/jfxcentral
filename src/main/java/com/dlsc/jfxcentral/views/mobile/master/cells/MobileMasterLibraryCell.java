package com.dlsc.jfxcentral.views.mobile.master.cells;

import com.dlsc.jfxcentral.data.ImageManager;
import com.dlsc.jfxcentral.data.model.Library;
import com.dlsc.jfxcentral.views.MarkdownView;
import com.dlsc.jfxcentral.views.mobile.MobileAdvancedListCell;
import javafx.geometry.Pos;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

public class MobileMasterLibraryCell extends MobileAdvancedListCell<Library> {

    private ImageView imageView = new ImageView();
    private Label label = new Label();
    private MarkdownView markdownView = new MarkdownView();

    public MobileMasterLibraryCell() {
        getStyleClass().add("mobile-master-library-cell");

        setPrefWidth(0);
        setMinWidth(0);

        imageView.setFitWidth(50);
        imageView.setPreserveRatio(true);

        label.getStyleClass().add("title-label");
        label.setWrapText(true);
        label.setMinHeight(Region.USE_PREF_SIZE);

        VBox vbox = new VBox(label, markdownView);
        vbox.getStyleClass().add("vbox");
        HBox.setHgrow(vbox, Priority.ALWAYS);

        HBox hBox = new HBox(vbox, imageView);
        hBox.getStyleClass().add("hbox");
        hBox.setAlignment(Pos.TOP_LEFT);

        setGraphic(hBox);
        setContentDisplay(ContentDisplay.GRAPHIC_ONLY);

        vbox.visibleProperty().bind(emptyProperty().not());
    }

    @Override
    protected void updateItem(Library library, boolean empty) {
        super.updateItem(library, empty);

        if (!empty && library != null) {
            label.setText(library.getName());
            imageView.imageProperty().bind(ImageManager.getInstance().libraryImageProperty(library));
            markdownView.setMdString(library.getSummary());
        } else {
            imageView.imageProperty().unbind();
        }
    }
}
