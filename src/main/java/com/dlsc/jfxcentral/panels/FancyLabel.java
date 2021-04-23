package com.dlsc.jfxcentral.panels;

import javafx.beans.binding.Bindings;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.layout.VBox;
import org.controlsfx.control.PopOver;

import java.util.function.Supplier;

public class FancyLabel extends Label {

    private final PopOverUtil popOverUtil = new PopOverUtil();

    private final Tooltip tooltip = new Tooltip();

    public FancyLabel() {
        init();
    }

    public FancyLabel(String text) {
        super(text);
        init();
    }

    public FancyLabel(String text, Node graphic) {
        super(text, graphic);
        init();
    }

    private PopOver popOver;

    private void init() {
        getStyleClass().add("fancy-label");

        Button copyButton = new Button();
        copyButton.setMaxWidth(Double.MAX_VALUE);
        copyButton.getStyleClass().addAll("white-button", "default-copy-button");
        copyButton.textProperty().bind(copyMenuItemTextProperty());
        getNodes().add(copyButton);

        setCursor(Cursor.HAND);

        tooltip.textProperty().bind(textProperty());
        tooltip.setWrapText(true);
        tooltip.setMaxWidth(400);

        setTooltip(tooltip);

        setOnMouseClicked(evt -> {
            if (popOver != null && popOver.isShowing()) {
                popOver.hide();
            }

            VBox box = new VBox();
            box.setFillWidth(true);
            box.getStyleClass().add("nodes-container");
            Bindings.bindContent(box.getChildren(), getNodes());

            popOver = new PopOver(box);
            popOver.setDetachable(false);
            popOver.setArrowLocation(popOverUtil.getArrowLocation(this));

            copyButton.setOnAction(e -> copy(getCopyContentSupplier()));

            popOver.show(this);
        });
    }

    private void copy(Supplier<String> copyContentSupplier) {
        ClipboardContent content = new ClipboardContent();
        String copyText = copyContentSupplier.get();
        content.putString(copyText);
        Clipboard.getSystemClipboard().setContent(content);
        popOver.hide();
    }

    public Button addCopyButton(String text, Supplier<String> copyContentSupplier) {
        Button button = new Button(text);
        button.getStyleClass().add("white-button");
        button.setOnAction(evt -> copy(copyContentSupplier));
        button.setMaxWidth(Double.MAX_VALUE);
        getNodes().add(button);
        return button;
    }

    private final ListProperty<Node> nodes = new SimpleListProperty<>(this, "nodes", FXCollections.observableArrayList());

    public ObservableList<Node> getNodes() {
        return nodes.get();
    }

    public ListProperty<Node> nodesProperty() {
        return nodes;
    }

    public void setNodes(ObservableList<Node> nodes) {
        this.nodes.set(nodes);
    }

    private final StringProperty copyMenuItemText = new SimpleStringProperty(this, "copyMenuItemText", "Copy");

    public final String getCopyMenuItemText() {
        return copyMenuItemText.get();
    }

    /**
     * The text used for the "copy" menu item.
     *
     * @return the copy menu item name
     */
    public final StringProperty copyMenuItemTextProperty() {
        return copyMenuItemText;
    }

    public final void setCopyMenuItemText(String copyMenuItemText) {
        this.copyMenuItemText.set(copyMenuItemText);
    }

    private final ObjectProperty<Supplier<String>> copyContentSupplier = new SimpleObjectProperty<>(this, "copyContentProvider", () -> getText());

    public final Supplier<String> getCopyContentSupplier() {
        return copyContentSupplier.get();
    }

    /**
     * Stores the supplier used for filling the clipboard when the user copies the label.
     * Sometimes applications want to copy only a part of the label, e.g. an ID shown by
     * the label like this: "Customer account #12345678". The copy content supplier can then
     * decide to just return the ID "12345678" instead of the whole string. The default
     * supplier however returns the whole string.
     *
     * @return the supplier for the clipboard content when the user copies the label
     */
    public final ObjectProperty<Supplier<String>> copyContentSupplierProperty() {
        return copyContentSupplier;
    }

    public final void setCopyContentSupplier(Supplier<String> copyContentSupplier) {
        this.copyContentSupplier.set(copyContentSupplier);
    }
}
