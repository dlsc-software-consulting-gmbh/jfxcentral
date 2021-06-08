package com.dlsc.jfxcentral.views.page;

import com.dlsc.jfxcentral.data.model.ModelObject;
import com.dlsc.jfxcentral.panels.PrettyScrollPane;
import com.dlsc.jfxcentral.views.RootPane;
import com.dlsc.jfxcentral.views.View;
import com.dlsc.jfxcentral.views.detail.DetailView;
import com.dlsc.jfxcentral.views.master.MasterView;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;

public class Page<T extends ModelObject> extends BorderPane {

    private final DetailScrollPane detailPane;
    private final TopMenu topMenu;
    private final RootPane rootPane;
    private final View view;

    private final MasterView<T> masterView;
    private final DetailView<T> detailView;

    public Page(RootPane rootPane, View view) {
        this.rootPane = rootPane;
        this.view = view;

        getStyleClass().add(view.name().toLowerCase());

        detailPane = new DetailScrollPane();
        detailPane.setContent(detailView = createDetailView());

        topMenu = new TopMenu(this);

        setCenter(detailPane);

        HBox leftSide = new HBox(wrap(topMenu));
        masterView = createMasterView();

        if (masterView != null) {
            leftSide.getChildren().add(masterView);
        }

        setLeft(leftSide);
    }

    public void showItem(T item) {
        if (masterView != null) {
            masterView.showItem(item);
        }

        detailView.showItem(item);
    }

    private PrettyScrollPane wrap(TopMenu topMenu) {
        PrettyScrollPane scrollPane = new PrettyScrollPane(topMenu);
        scrollPane.setShowScrollToTopButton(false);
        scrollPane.setShowShadow(false);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);
        return scrollPane;
    }

    private final StringProperty title = new SimpleStringProperty(this, "title");

    public String getTitle() {
        return title.get();
    }

    public StringProperty titleProperty() {
        return title;
    }

    public void setTitle(String title) {
        this.title.set(title);
    }

    private final StringProperty description = new SimpleStringProperty(this, "description");

    public String getDescription() {
        return description.get();
    }

    public StringProperty descriptionProperty() {
        return description;
    }

    public void setDescription(String description) {
        this.description.set(description);
    }

    private final ObjectProperty<T> selectedItem = new SimpleObjectProperty<>(this, "item");

    public T getSelectedItem() {
        return selectedItem.get();
    }

    public ObjectProperty<T> selectedItemProperty() {
        return selectedItem;
    }

    public void setSelectedItem(T selectedItem) {
        this.selectedItem.set(selectedItem);
    }

    protected DetailView createDetailView() {
        return null;
    }

    protected MasterView createMasterView() {
        return null;
    }

    public View getView() {
        return view;
    }

    public RootPane getRootPane() {
        return rootPane;
    }
}
