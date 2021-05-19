package com.dlsc.jfxcentral;

import com.dlsc.gemsfx.DialogPane;
import javafx.scene.layout.BorderPane;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class RootPane extends ViewPane {

    private final Map<Class<?>, Consumer<?>> openHandler = new HashMap<>();

    private final HeaderPane headerPane = new HeaderPane();
    private final RightPane rightPane = new RightPane();

    private final DialogPane dialogPane = new DialogPane();

    public RootPane() {
        getStyleClass().add("root-pane");

        dialogPane.getStylesheets().add(JFXCentralApp.class.getResource("styles.css").toExternalForm());

        SideBar sideBar = new SideBar(this);
        sideBar.viewProperty().bindBidirectional(viewProperty());

        BorderPane borderPane = new BorderPane();
        borderPane.setTop(headerPane);
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

    public <T> void registerOpenHandler(Class<T> clazz, Consumer<T> handler) {
        openHandler.put(clazz, handler);
    }

    public void open(Object object) {
        if (!open(object, object.getClass())) {
            System.err.println("No handler found to open the item of type " + object.getClass().getSimpleName());
        }
    }

    private boolean open(Object object, Class clazz) {
        Consumer handler = openHandler.get(clazz);
        if (handler != null) {
            handler.accept(object);
            return true;
        } else {
            clazz = clazz.getSuperclass();
            if (clazz != null) {
                return open(object, clazz);
            }
        }

        return false;
    }
}
