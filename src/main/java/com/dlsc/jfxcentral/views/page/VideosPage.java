package com.dlsc.jfxcentral.views.page;

import com.dlsc.jfxcentral.data.model.Video;
import com.dlsc.jfxcentral.views.RootPane;
import com.dlsc.jfxcentral.views.View;
import com.dlsc.jfxcentral.views.detail.DetailView;
import com.dlsc.jfxcentral.views.detail.VideosDetailView;
import javafx.beans.binding.Bindings;

public class VideosPage extends Page<Video> {

    public VideosPage(RootPane rootPane) {
        super(rootPane, View.VIDEOS);

        titleProperty().bind(Bindings.createStringBinding(() -> getSelectedItem() != null ?
                "Video - " + getSelectedItem().getId() :
                "Videos", selectedItemProperty()));

        descriptionProperty().bind(Bindings.createStringBinding(() -> getSelectedItem() != null ?
                "Video covering JavaFX technology. Summary: '" + getSelectedItem().getDescription() + "'" :
                "A collection of videos targeting JavaFX and showcasing real world applications, tutorials, conference presentations."));
    }

    @Override
    protected DetailView createDetailView() {
        VideosDetailView view = new VideosDetailView(getRootPane());
        selectedItemProperty().bindBidirectional(view.selectedItemProperty());
        return view;
    }
}
