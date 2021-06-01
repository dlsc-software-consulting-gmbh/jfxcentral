package com.dlsc.jfxcentral.views.page;

import com.dlsc.jfxcentral.panels.PrettyScrollPane;

public class DetailPane extends PrettyScrollPane {

    public DetailPane() {
        getStyleClass().add("right-pane");
        setShowScrollToTopButton(true);
        setShowShadow(false);
        setFitToHeight(true);
        setFitToWidth(true);
    }
}

