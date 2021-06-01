package com.dlsc.jfxcentral.views.page;

import com.dlsc.jfxcentral.panels.PrettyScrollPane;

public class DetailScrollPane extends PrettyScrollPane {

    public DetailScrollPane() {
        getStyleClass().add("detail-scroll-pane");
        setShowScrollToTopButton(true);
        setShowShadow(false);
        setFitToHeight(true);
        setFitToWidth(true);
    }
}

