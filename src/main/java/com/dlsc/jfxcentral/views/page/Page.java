package com.dlsc.jfxcentral.views.page;

import com.dlsc.jfxcentral.views.Display;
import com.dlsc.jfxcentral.views.RootPane;
import com.dlsc.jfxcentral.views.View;
import com.dlsc.jfxcentral.views.detail.DetailView;
import com.dlsc.jfxcentral.views.master.MasterView;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.Node;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;

public class Page<T> extends BorderPane {

    private final HeaderPane headerPane;
    private final DetailScrollPane detailPane;
    private final TopMenu topMenu;
    private final RootPane rootPane;
    private final View view;

    public Page(RootPane rootPane, View view) {
        this.rootPane = rootPane;
        this.view = view;

        getStyleClass().add(view.name().toLowerCase());

        headerPane = new HeaderPane();
        detailPane = new DetailScrollPane();
        detailPane.setContent(createDetailView());

        topMenu = new TopMenu(this);

        setTop(headerPane);
        setCenter(detailPane);

        HBox leftSide = new HBox(topMenu);
        Node masterNode = createMasterView();

        if (masterNode != null) {
            leftSide.getChildren().add(masterNode);
        }

        setLeft(leftSide);
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

    private ObjectProperty<Display> display = new SimpleObjectProperty<>(this, "display");

    public Display getDisplay() {
        return display.get();
    }

    public ObjectProperty<Display> displayProperty() {
        return display;
    }

    public void setDisplay(Display display) {
        this.display.set(display);
    }
}
