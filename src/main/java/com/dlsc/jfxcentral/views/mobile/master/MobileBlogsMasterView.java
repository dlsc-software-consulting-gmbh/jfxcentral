package com.dlsc.jfxcentral.views.mobile.master;

import com.dlsc.jfxcentral.data.DataRepository;
import com.dlsc.jfxcentral.data.model.Blog;
import com.dlsc.jfxcentral.panels.PrettyScrollPane;
import com.dlsc.jfxcentral.views.RootPane;
import com.dlsc.jfxcentral.views.View;
import com.dlsc.jfxcentral.views.mobile.MobileMasterViewWithAdvancedListView;
import com.dlsc.jfxcentral.views.mobile.master.cells.MobileMasterBlogCell;
import javafx.scene.layout.Region;
import org.apache.commons.lang3.StringUtils;

import java.util.Comparator;

public class MobileBlogsMasterView extends MobileMasterViewWithAdvancedListView<Blog> {

    public MobileBlogsMasterView(RootPane rootPane) {
        super(rootPane, View.BLOGS);

        getStyleClass().addAll("mobile-blogs-master-view", "blogs-master-view");

        listView.setPaging(true);
        listView.setVisibleRowCount(Integer.MAX_VALUE);
        listView.setMinWidth(Region.USE_PREF_SIZE);
        listView.setCellFactory(view -> new MobileMasterBlogCell());
        listView.setItems(createSortedAndFilteredList(DataRepository.getInstance().blogsProperty(),
                Comparator.comparing(Blog::getTitle),
                blog -> StringUtils.isBlank(getFilterText()) || StringUtils.containsIgnoreCase(blog.getTitle(), getFilterText())));

        PrettyScrollPane scrollPane = new PrettyScrollPane(listView);
        scrollPane.setShowScrollToTopButton(true);
        scrollPane.setFitToWidth(true);
        setCenter(scrollPane);
    }
}