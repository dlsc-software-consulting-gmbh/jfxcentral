package com.dlsc.jfxcentral;

import com.dlsc.jfxcentral.panels.PrettyScrollPane;

public class RightPane extends PrettyScrollPane {

    public RightPane() {
        getStyleClass().add("right-pane");
        setShowScrollToTopButton(true);
        setShowShadow(false);
        setFitToHeight(true);
        setFitToWidth(true);
    }
}

