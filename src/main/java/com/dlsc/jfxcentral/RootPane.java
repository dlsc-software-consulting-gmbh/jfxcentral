package com.dlsc.jfxcentral;

import com.dlsc.gemsfx.DialogPane;
import javafx.scene.layout.BorderPane;

public class RootPane extends ViewPane {

    private final RightPane rightPane = new RightPane();

    private final DialogPane dialogPane = new DialogPane();

    public RootPane() {
        getStyleClass().add("root-pane");

        SideBar sideBar = new SideBar(this);
        sideBar.viewProperty().bindBidirectional(viewProperty());

        BorderPane borderPane = new BorderPane();
        borderPane.setLeft(sideBar);
        borderPane.setCenter(rightPane);

        getChildren().addAll(borderPane, dialogPane);

        displayProperty().addListener(it -> System.out.println("display: " + getDisplay()));
    }

    public RightPane getRightPane() {
        return rightPane;
    }

    public DialogPane getDialogPane() {
        return dialogPane;
    }
}
