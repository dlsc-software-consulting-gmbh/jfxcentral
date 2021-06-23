package com.dlsc.jfxcentral.views.mobile.page;

import com.dlsc.jfxcentral.data.model.Video;
import com.dlsc.jfxcentral.views.RootPane;
import com.dlsc.jfxcentral.views.View;
import com.dlsc.jfxcentral.views.detail.DetailView;
import com.dlsc.jfxcentral.views.detail.VideosDetailView;
import com.dlsc.jfxcentral.views.mobile.MobilePage;
import javafx.beans.binding.Bindings;
import javafx.scene.control.ScrollPane;

public class MobileVideosPage extends MobilePage<Video> {

    public MobileVideosPage(RootPane rootPane) {
        super(rootPane, View.VIDEOS);

        getDetailScrollPane().setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

        titleProperty().bind(Bindings.createStringBinding(() -> getSelectedItem() != null ?
                "Video - " + getSelectedItem().getId() :
                "Videos", selectedItemProperty()));

        descriptionProperty().bind(Bindings.createStringBinding(() -> getSelectedItem() != null ?
                "Video covering JavaFX technology. Summary: '" + getSelectedItem().getDescription() + "'" :
                "A collection of videos targeting JavaFX and showcasing real world applications, tutorials, conference presentations."));
    }
    
    @Override
    protected DetailView<Video> createDetailView() {
        return new VideosDetailView(getRootPane());
    }
}
