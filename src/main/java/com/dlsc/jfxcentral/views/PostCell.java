package com.dlsc.jfxcentral.views;

import com.dlsc.jfxcentral.model.Post;
import javafx.scene.control.ListCell;

public class PostCell extends ListCell<Post> {

    public PostCell() {
    }

    @Override
    protected void updateItem(Post item, boolean empty) {
        super.updateItem(item, empty);

        if (!empty && item != null) {
            setText(item.getSyndEntry().getTitle());
        } else {
            setText("");
        }
    }
}
