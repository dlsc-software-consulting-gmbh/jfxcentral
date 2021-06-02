package com.dlsc.jfxcentral.views.page;

import com.dlsc.jfxcentral.data.model.News;
import com.dlsc.jfxcentral.views.RootPane;
import com.dlsc.jfxcentral.views.View;
import com.dlsc.jfxcentral.views.detail.DetailView;
import com.dlsc.jfxcentral.views.detail.NewsDetailView;
import javafx.beans.binding.Bindings;

public class NewsPage extends Page<News> {

    public NewsPage(RootPane rootPane) {
        super(rootPane, View.NEWS);

        titleProperty().bind(Bindings.createStringBinding(() -> getSelectedItem() != null ?
                "News - " + getSelectedItem().getTitle() :
                "News", selectedItemProperty()));

        descriptionProperty().bind(Bindings.createStringBinding(() -> getSelectedItem() != null ?
                "News '" + getSelectedItem().getTitle() + "'" :
                "News related to JavaFX technology. Announcements, releases, interesting links."));
    }

    @Override
    protected DetailView createDetailView() {
        NewsDetailView view = new NewsDetailView(getRootPane());
        selectedItemProperty().bindBidirectional(view.selectedItemProperty());
        return view;
    }
}
