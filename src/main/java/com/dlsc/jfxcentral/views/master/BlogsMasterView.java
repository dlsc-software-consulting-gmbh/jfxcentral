package com.dlsc.jfxcentral.views.master;

import com.dlsc.jfxcentral.data.DataRepository;
import com.dlsc.jfxcentral.data.model.Blog;
import com.dlsc.jfxcentral.views.RootPane;
import com.dlsc.jfxcentral.views.View;
import com.dlsc.jfxcentral.views.master.cell.MasterBlogCell;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import org.apache.commons.lang3.StringUtils;

import java.util.Comparator;

public class BlogsMasterView extends MasterViewWithListView<Blog> {

    public BlogsMasterView(RootPane rootPane) {
        super(rootPane, View.BLOGS);

        getStyleClass().add("blogs-master-view");

        listView.setMinWidth(Region.USE_PREF_SIZE);
        listView.setCellFactory(view -> new MasterBlogCell());
        listView.setItems(createSortedAndFilteredList(DataRepository.getInstance().blogsProperty(),
                Comparator.comparing(Blog::getTitle),
                blog -> StringUtils.isBlank(getFilterText()) || StringUtils.containsIgnoreCase(blog.getTitle(), getFilterText())));

        VBox.setVgrow(listView, Priority.ALWAYS);

        Button button = new Button("Show all posts");
        button.setMaxWidth(Double.MAX_VALUE);
        button.setOnAction(evt -> listView.getSelectionModel().clearSelection());
        HBox.setHgrow(button, Priority.ALWAYS);

        HBox buttonWrapper = new HBox(button);
        buttonWrapper.getStyleClass().add("button-wrapper");

        VBox vBox = new VBox(10, buttonWrapper, listView);
        vBox.getStyleClass().add("vbox");

        setCenter(vBox);
    }
}