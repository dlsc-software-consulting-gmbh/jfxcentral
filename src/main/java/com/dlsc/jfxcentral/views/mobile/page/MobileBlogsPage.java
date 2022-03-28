package com.dlsc.jfxcentral.views.mobile.page;

import com.dlsc.jfxcentral.data.model.Blog;
import com.dlsc.jfxcentral.views.RootPane;
import com.dlsc.jfxcentral.views.View;
import com.dlsc.jfxcentral.views.detail.BlogsDetailView;
import com.dlsc.jfxcentral.views.detail.DetailView;
import com.dlsc.jfxcentral.views.master.MasterView;
import com.dlsc.jfxcentral.views.mobile.MobilePage;
import com.dlsc.jfxcentral.views.mobile.master.MobileBlogsMasterView;
import javafx.beans.binding.Bindings;

public class MobileBlogsPage extends MobilePage<Blog> {

    public MobileBlogsPage(RootPane rootPane) {
        super(rootPane, View.BLOGS);

        titleProperty().bind(Bindings.createStringBinding(() -> getSelectedItem() != null ?
                "Blog - " + getSelectedItem().getName() + " - for JavaFX developers" :
                "Blogs for JavaFX developers", selectedItemProperty()));

        descriptionProperty().bind(Bindings.createStringBinding(() -> getSelectedItem() != null ?
                "Latest JavaFX posts from the blog '" + getSelectedItem().getName() + "'" :
                "Collection of blogs covering JavaFX technology."));
    }

    @Override
    protected MasterView createMasterView() {
        return new MobileBlogsMasterView(getRootPane());
    }

    @Override
    protected DetailView createDetailView() {
        return new BlogsDetailView(getRootPane());
    }
}
