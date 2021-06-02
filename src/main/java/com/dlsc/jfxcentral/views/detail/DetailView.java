package com.dlsc.jfxcentral.views.detail;

import com.dlsc.gemsfx.DialogPane;
import com.dlsc.jfxcentral.data.model.ModelObject;
import com.dlsc.jfxcentral.views.RootPane;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

public class DetailView<T extends ModelObject> extends BorderPane {

    private final RootPane rootPane;

    protected DetailView(RootPane rootPane) {
        this.rootPane = rootPane;
        getStyleClass().add("page");
        setMinHeight(Region.USE_PREF_SIZE);

        contentProperty().addListener(it -> {
            Node content = getContent();
            if (content != null) {
                BorderPane.setMargin(content, new Insets(20));
                if (content instanceof VBox) {
                    ((VBox) content).setSpacing(20);
                }
            }
        });

        centerProperty().bind(contentProperty());
    }

    private final ObjectProperty<T> selectedItem = new SimpleObjectProperty<>(this, "selectedItem");

    public T getSelectedItem() {
        return selectedItem.get();
    }

    public ObjectProperty<T> selectedItemProperty() {
        return selectedItem;
    }

    public void setSelectedItem(T selectedItem) {
        this.selectedItem.set(selectedItem);
    }

    private final ObjectProperty<Node> content = new SimpleObjectProperty<>();

    public Node getContent() {
        return content.get();
    }

    public ObjectProperty<Node> contentProperty() {
        return content;
    }

    public void setContent(Node content) {
        this.content.set(content);
    }

    public RootPane getRootPane() {
        return rootPane;
    }

    public DialogPane getDialogPane() {
        return rootPane.getDialogPane();
    }
}
