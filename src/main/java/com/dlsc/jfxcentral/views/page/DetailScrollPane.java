package com.dlsc.jfxcentral.views.page;

import com.dlsc.jfxcentral.panels.PrettyScrollPane;
import com.dlsc.jfxcentral.views.RootPane;

public class DetailScrollPane extends PrettyScrollPane {

    public DetailScrollPane(RootPane rootPane) {
        getStyleClass().add("detail-scroll-pane");
        setShowScrollToTopButton(true);
        setShowShadow(rootPane.isMobile());
        setFitToHeight(true);
        setFitToWidth(true);
    }
}

