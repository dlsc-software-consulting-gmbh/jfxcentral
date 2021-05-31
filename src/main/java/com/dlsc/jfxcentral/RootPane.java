package com.dlsc.jfxcentral;

import com.dlsc.gemsfx.DialogPane;
import com.jpro.webapi.WebAPI;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class RootPane extends ViewPane {

    private final Map<Class<?>, Consumer<?>> openHandler = new HashMap<>();

    private final HeaderPane headerPane = new HeaderPane();
    private final RightPane rightPane = new RightPane();

    private final DialogPane dialogPane = new DialogPane();
    private final SideBar sideBar;

    public RootPane() {
        getStyleClass().add("root-pane");

        sceneProperty().addListener(it -> {
            Scene scene = getScene();
            if (scene != null) {
                if (WebAPI.isBrowser()) {
                    WebAPI webAPI = WebAPI.getWebAPI(scene);
                    String language = webAPI.getLanguage();
                    System.out.println("language: " + language);
                }
            }
        });

        dialogPane.getStylesheets().add(JFXCentralApp.class.getResource("styles.css").toExternalForm());

        sideBar = new SideBar(this);
        sideBar.viewProperty().bindBidirectional(viewProperty());

        BorderPane borderPane = new BorderPane();
        borderPane.setTop(headerPane);
        borderPane.setLeft(sideBar);
        borderPane.setCenter(rightPane);

        getChildren().addAll(borderPane, dialogPane);

        displayProperty().addListener(it -> System.out.println("display: " + getDisplay()));
    }

    public SideBar getSideBar() {
        return sideBar;
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

    public void showImage(String title, Image image) {
        ImageView imageView = new ImageView(image);
        imageView.setPreserveRatio(true);

        StackPane stackPane = new StackPane(imageView);
        stackPane.setPrefSize(image.getWidth(), image.getHeight()); // important
        stackPane.setMinSize(0, 0); // important

        imageView.fitWidthProperty().bind(stackPane.widthProperty().multiply(.8));

        getDialogPane().showNode(DialogPane.Type.BLANK, title, stackPane, false, Collections.emptyList());

    }
}
