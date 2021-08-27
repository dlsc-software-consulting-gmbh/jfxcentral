package com.dlsc.jfxcentral.views.mobile;

import com.dlsc.jfxcentral.data.model.ModelObject;
import com.dlsc.jfxcentral.views.IPage;
import com.dlsc.jfxcentral.views.RootPane;
import com.dlsc.jfxcentral.views.View;
import com.dlsc.jfxcentral.views.detail.DetailView;
import com.dlsc.jfxcentral.views.master.MasterView;
import com.dlsc.jfxcentral.views.page.DetailScrollPane;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.layout.BorderPane;

public class MobilePage<T extends ModelObject> extends BorderPane implements IPage<T> {

    private final DetailScrollPane detailScrollPane;
    private final RootPane rootPane;
    private final View view;

    private final MasterView<T> masterView;
    private final DetailView<T> detailView;

    public MobilePage(RootPane rootPane, View view) {
        this.rootPane = rootPane;
        this.view = view;

        getStyleClass().add(view.name().toLowerCase());

        detailScrollPane = new DetailScrollPane(rootPane);
        detailScrollPane.setContent(detailView = createDetailView());

        masterView = createMasterView();

        if (masterView != null) {
            showMaster();
        } else {
            showDetail();
        }

        selectedItemProperty().addListener(it -> {
            detailScrollPane.setVvalue(0);

            T selectedItem = getSelectedItem();
            if (selectedItem != null) {
                showDetail(selectedItem);
            } else {
                showMaster();
            }
        });
    }

    public DetailScrollPane getDetailScrollPane() {
        return detailScrollPane;
    }

    public void showMaster() {
        setCenter(masterView);
    }

    public void showDetail() {
        setCenter(detailScrollPane);
    }

    public void showDetail(T item) {
        detailView.showItem(item);
        showDetail();
    }

    @Override
    public void showItem(T item) {
        showDetail(item);
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
