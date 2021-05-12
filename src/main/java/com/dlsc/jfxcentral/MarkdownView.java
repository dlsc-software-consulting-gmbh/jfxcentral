package com.dlsc.jfxcentral;

import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.apache.commons.lang3.StringUtils;

import java.util.function.Consumer;

public class MarkdownView extends com.sandec.mdfx.MarkdownView {

    private String baseURL = "";

    public MarkdownView() {
        getStylesheets().add(JFXCentralApp.class.getResource("styles.css").toExternalForm());
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
            Node node = super.generateImage(url);
            configureImage(node);
            return node;
        }

        Node node = super.generateImage(baseURL + "/" + url);
        configureImage(node);
        return node;
    }

    private void configureImage(Node node) {
        if (node instanceof ImageView) {
            ImageView imageView = (ImageView) node;
            imageView.fitWidthProperty().bind(Bindings.createDoubleBinding(() -> computeFitWidth(imageView), widthProperty(), imageView.getImage().progressProperty()));
            imageView.setPreserveRatio(true);
        }
    }

    private double computeFitWidth(ImageView imageView) {
        Image image = imageView.getImage();
        if (image.getWidth() < getWidth()) {
            return image.getWidth();
        }

        return getWidth();
    }
}
