package com.dlsc.jfxcentral.views.master;

import com.dlsc.jfxcentral.data.DataRepository;
import com.dlsc.jfxcentral.data.model.Blog;
import com.dlsc.jfxcentral.views.RootPane;
import com.dlsc.jfxcentral.views.View;
import com.dlsc.jfxcentral.views.master.cells.MasterBlogCell;
import org.apache.commons.lang3.StringUtils;

import java.util.Comparator;

public class BlogsMasterView extends MasterViewWithListView<Blog> {

    public BlogsMasterView(RootPane rootPane) {
        super(rootPane, View.BLOGS);

        getStyleClass().add("blogs-master-view");

        listView.setCellFactory(view -> new MasterBlogCell());
        listView.setItems(createSortedAndFilteredList(DataRepository.getInstance().blogsProperty(),
                Comparator.comparing(x -> x.getName().toLowerCase()),
                blog -> StringUtils.isBlank(getFilterText()) || StringUtils.containsIgnoreCase(blog.getName(), getFilterText())));
    }
}