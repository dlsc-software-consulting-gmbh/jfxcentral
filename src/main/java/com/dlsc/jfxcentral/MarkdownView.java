package com.dlsc.jfxcentral;

import com.sandec.mdfx.MDFXNode;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.Node;

import java.util.function.Consumer;

public class MarkdownView extends MDFXNode {

    public MarkdownView() {
    }

    private final ObjectProperty<Consumer<String>> hyperlinkCallback = new SimpleObjectProperty<>(this, "urlCallback");

    public Consumer<String> getHyperlinkCallback() {
        return hyperlinkCallback.get();
    }

    public ObjectProperty<Consumer<String>> hyperlinkCallbackProperty() {
        return hyperlinkCallback;
    }

    public void setHyperlinkCallback(Consumer<String> hyperlinkCallback) {
        this.hyperlinkCallback.set(hyperlinkCallback);
    }

    @Override
    public void setLink(Node node, String link, String description) {
        super.setLink(node, link, description);

        node.setOnMouseClicked(evt -> getHyperlinkCallback().accept(link));
    }
}
