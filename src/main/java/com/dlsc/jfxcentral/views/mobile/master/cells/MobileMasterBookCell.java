package com.dlsc.jfxcentral.views.mobile.master.cells;

import com.dlsc.jfxcentral.data.ImageManager;
import com.dlsc.jfxcentral.data.model.Book;
import com.dlsc.jfxcentral.views.View;
import com.dlsc.jfxcentral.views.mobile.MobileAdvancedListCell;
import com.jpro.webapi.WebAPI;
import javafx.geometry.Pos;
import javafx.scene.control.ContentDisplay;
import javafx.scene.image.ImageView;

public class MobileMasterBookCell extends MobileAdvancedListCell<Book> {

    private final ImageView coverImageView = new ImageView();

    public MobileMasterBookCell() {
        getStyleClass().add("mobile-master-book-list-cell");

        setPrefWidth(0);

        coverImageView.fitWidthProperty().bind(widthProperty().multiply(.75));
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
