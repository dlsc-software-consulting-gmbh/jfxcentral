package com.dlsc.jfxcentral.views.master.cells;

import com.dlsc.jfxcentral.data.ImageManager;
import com.dlsc.jfxcentral.data.model.Book;
import com.dlsc.jfxcentral.views.View;
import com.jpro.webapi.WebAPI;
import javafx.geometry.Pos;
import javafx.scene.control.ContentDisplay;
import javafx.scene.image.ImageView;

public class MasterBookCell extends MasterCell<Book> {

    private final ImageView coverImageView = new ImageView();

    public MasterBookCell() {
        getStyleClass().add("master-book-list-cell");

        coverImageView.setFitWidth(100);
        coverImageView.setPreserveRatio(true);

        setAlignment(Pos.CENTER);
        setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
        setGraphic(coverImageView);

        coverImageView.visibleProperty().bind(emptyProperty().not());
    }

    @Override
    protected void updateItem(Book book, boolean empty) {
        super.updateItem(book, empty);

        coverImageView.imageProperty().unbind();

        if (!empty && book != null) {
            coverImageView.imageProperty().bind(ImageManager.getInstance().bookCoverImageProperty(book));
            if (WebAPI.isBrowser()) {
                setMouseTransparent(true);
            }

            setMasterCellLink(this, book, book.getTitle(), View.BOOKS);
        }
    }
}
