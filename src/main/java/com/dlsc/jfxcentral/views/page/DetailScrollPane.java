package com.dlsc.jfxcentral.views.page;

import com.dlsc.jfxcentral.panels.PrettyScrollPane;
import com.dlsc.jfxcentral.views.RootPane;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.StackPane;

public class DetailScrollPane extends StackPane {

    private PrettyScrollPane scrollPane;

    public DetailScrollPane(RootPane rootPane) {
        getStyleClass().add("detail-scroll-pane");

        scrollPane = new PrettyScrollPane();
        scrollPane.setShowScrollToTopButton(true);
        scrollPane.showShadowProperty().bind(rootPane.disableEffectsProperty().not());
        scrollPane.setFitToHeight(true);
        scrollPane.setFitToWidth(true);
        scrollPane.setMobile(rootPane.isMobile());
        getChildren().add(scrollPane);

        scrollPane.getVerticalScrollBar().setOpacity(0);
        scrollPane.getVerticalScrollBar().valueProperty().addListener(it -> showScrollBar(true));
        hoverProperty().addListener(it -> showScrollBar(isHover()));

    }

    private void showScrollBar(boolean show) {
        scrollPane.getVerticalScrollBar().setOpacity(show ? 1 : 0);
    }

    public void setContent(Node content) {
        if (scrollPane != null) {
            scrollPane.setContent(content);
        } else {
            getChildren().setAll(content);
        }
    }

    public PrettyScrollPane getScrollPane() {
        return scrollPane;
    }

    public void setVvalue(int value) {
        if (scrollPane != null) {
            scrollPane.setVvalue(value);
        }
    }

    public void setVbarPolicy(ScrollPane.ScrollBarPolicy policy) {
        if (scrollPane != null) {
            scrollPane.setVbarPolicy(policy);
        }
    }
}

