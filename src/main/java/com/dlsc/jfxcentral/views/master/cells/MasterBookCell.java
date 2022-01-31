package com.dlsc.jfxcentral.views.master.cells;

import com.dlsc.jfxcentral.data.ImageManager;
import com.dlsc.jfxcentral.data.model.Book;
import com.dlsc.jfxcentral.views.MarkdownView;
import com.dlsc.jfxcentral.views.View;
import com.jpro.webapi.WebAPI;
import javafx.geometry.Pos;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

public class MasterBookCell extends MasterCell<Book> {

    private final Label nameLabel = new Label();
    private final ImageView imageView = new ImageView();
    private final MarkdownView markdownView = new MarkdownView();

    public MasterBookCell() {
        getStyleClass().add("master-book-list-cell");

        setPrefWidth(0);

        imageView.visibleProperty().bind(imageView.imageProperty().isNotNull());
        imageView.managedProperty().bind(imageView.imageProperty().isNotNull());
        imageView.setFitHeight(60);
        imageView.setFitWidth(60);
        imageView.setPreserveRatio(true);

        nameLabel.getStyleClass().add("name-label");
        nameLabel.setMinWidth(0);
        nameLabel.setWrapText(true);
        nameLabel.setMinHeight(Region.USE_PREF_SIZE);

        VBox vbox = new VBox(nameLabel, markdownView);
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
    protected void updateItem(Book book, boolean empty) {
        super.updateItem(book, empty);

        if (!empty && book != null) {
            nameLabel.setText(book.getName());
            imageView.imageProperty().bind(ImageManager.getInstance().bookCoverImageProperty(book));
            markdownView.setMdString(book.getSubtitle());

            if (WebAPI.isBrowser()) {
                setMouseTransparent(true);
            }

            setMasterCellLink(MasterBookCell.this, book, book.getName() + " - " + book.getSubtitle(), View.BOOKS);
        } else {
            nameLabel.setText("");
            imageView.imageProperty().unbind();
            imageView.setImage(null);
            markdownView.setMdString("");
        }
    }
}
