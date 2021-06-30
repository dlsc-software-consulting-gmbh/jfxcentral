package com.dlsc.jfxcentral.views.page;

import com.dlsc.jfxcentral.data.model.Blog;
import com.dlsc.jfxcentral.views.RootPane;
import com.dlsc.jfxcentral.views.View;
import com.dlsc.jfxcentral.views.detail.BlogsDetailView;
import com.dlsc.jfxcentral.views.detail.DetailView;
import com.dlsc.jfxcentral.views.master.BlogsMasterView;
import com.dlsc.jfxcentral.views.master.MasterView;
import javafx.beans.binding.Bindings;

public class BlogsPage extends Page<Blog> {

    public BlogsPage(RootPane rootPane) {
        super(rootPane, View.BLOGS);

        titleProperty().bind(Bindings.createStringBinding(() -> getSelectedItem() != null ?
                "Blog - " + getSelectedItem().getName() :
                "Blogs", selectedItemProperty()));

        descriptionProperty().bind(Bindings.createStringBinding(() -> getSelectedItem() != null ?
                "Latest JavaFX posts from the blog '" + getSelectedItem().getName() + "'" :
                "Collection of blogs covering JavaFX technology."));
    }

    @Override
    protected MasterView createMasterView() {
        BlogsMasterView view = new BlogsMasterView(getRootPane());
        selectedItemProperty().bindBidirectional(view.selectedItemProperty());
        return view;
    }

    @Override
    protected DetailView createDetailView() {
        BlogsDetailView view = new BlogsDetailView(getRootPane());
        selectedItemProperty().bindBidirectional(view.selectedItemProperty());
        return view;
    }
}
