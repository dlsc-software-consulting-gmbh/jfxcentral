package com.dlsc.jfxcentral;

import com.sandec.mdfx.MDFXNode;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.Node;
import javafx.scene.image.ImageView;
import org.apache.commons.lang3.StringUtils;

import java.util.function.Consumer;

public class MarkdownView extends MDFXNode {

    private String baseURL = "";

    public MarkdownView() {
    }

    public String getBaseURL() {
        return baseURL;
    }

    public void setBaseURL(String baseURL) {
        this.baseURL = baseURL;
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

        node.setOnMouseClicked(evt -> getHyperlinkCallback().accept(StringUtils.deleteWhitespace(link)));
    }

    @Override
    public Node generateImage(String url) {
        if (url.startsWith("http")) {
            return super.generateImage(url);
        }
        System.out.println(baseURL + "/" + url);
        Node node = super.generateImage(baseURL + "/" + url);
        if (node instanceof ImageView) {
            ImageView imageView = (ImageView) node;
            imageView.fitWidthProperty().bind(widthProperty().multiply(.9));
            imageView.setPreserveRatio(true);
        }

        return node;
    }
}
