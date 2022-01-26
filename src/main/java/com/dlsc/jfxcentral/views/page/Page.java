package com.dlsc.jfxcentral.views.page;

import com.dlsc.jfxcentral.data.model.ModelObject;
import com.dlsc.jfxcentral.views.IPage;
import com.dlsc.jfxcentral.views.RootPane;
import com.dlsc.jfxcentral.views.View;
import com.dlsc.jfxcentral.views.detail.DetailView;
import com.dlsc.jfxcentral.views.master.MasterView;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;

public class Page<T extends ModelObject> extends BorderPane implements IPage<T> {

    private final TopMenu topMenu;
    private final RootPane rootPane;
    private final View view;

    private final MasterView<T> masterView;
    private final DetailView<T> detailView;

    public Page(RootPane rootPane, View view) {
        this.rootPane = rootPane;
        this.view = view;

        setPrefWidth(0);

        getStyleClass().add(view.name().toLowerCase());

        detailView = createDetailView();
        if (detailView.isUsingScrollPane()) {
            DetailScrollPane detailPane = new DetailScrollPane(rootPane);
            detailPane.setContent(detailView);
            setCenter(detailPane);
            selectedItemProperty().addListener(it -> detailPane.setVvalue(0));
        } else {
            setCenter(detailView);
        }

        topMenu = new TopMenu(this);

        HBox leftSide = new HBox(topMenu);
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
