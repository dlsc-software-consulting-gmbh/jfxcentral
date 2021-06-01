package com.dlsc.jfxcentral.views.page;

import com.dlsc.jfxcentral.views.RootPane;
import com.dlsc.jfxcentral.views.View;
import com.dlsc.jfxcentral.views.master.BlogsMasterView;
import com.dlsc.jfxcentral.views.master.MasterView;
import com.dlsc.jfxcentral.model.Blog;
import com.dlsc.jfxcentral.views.detail.BlogsDetailView;
import com.dlsc.jfxcentral.views.detail.DetailView;

public class BlogsPage extends Page<Blog> {

    public BlogsPage(RootPane rootPane) {
        super(rootPane, View.BLOGS);
    }

    @Override
    protected MasterView createMasterView() {
        BlogsMasterView view = new BlogsMasterView(getRootPane());
        view.selectedItemProperty().bindBidirectional(selectedItemProperty());
        return view;
    }

    @Override
    protected DetailView createDetailView() {
        BlogsDetailView view = new BlogsDetailView(getRootPane());
        view.selectedItemProperty().bindBidirectional(selectedItemProperty());
        return view;
    }
}
