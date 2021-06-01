package com.dlsc.jfxcentral.views;

import com.dlsc.gemsfx.DialogPane;
import com.dlsc.jfxcentral.JFXCentralApp;
import com.dlsc.jfxcentral.views.page.*;
import com.jpro.webapi.WebAPI;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class RootPane extends StackPane {

    private final Map<Class<?>, Consumer<?>> openHandler = new HashMap<>();

    private final DialogPane dialogPane = new DialogPane();

    private Page<?> page;

    public RootPane() {
        getStyleClass().add("root-pane");

        dialogPane.getStylesheets().add(JFXCentralApp.class.getResource("styles.css").toExternalForm());

        viewProperty().addListener(it -> {
            page = null;
            switch (getView()) {
                case HOME:
                    page = new HomePage(this);
                    break;
                case NEWS:
                    page = new NewsPage(this);
                    break;
                case OPENJFX:
                    page = new OpenJFXPage(this);
                    break;
                case PEOPLE:
                    page = new PeoplePage(this);
                    break;
                case TUTORIALS:
                    page = new TutorialsPage(this);
                    break;
                case REALWORLD:
                    page = new RealWorldAppsPage(this);
                    break;
                case COMPANIES:
                    page = new CompaniesPage(this);
                    break;
                case TOOLS:
                    page = new ToolsPage(this);
                    break;
                case LIBRARIES:
                    page = new LibrariesPage(this);
                    break;
                case BLOGS:
                    page = new BlogsPage(this);
                    break;
                case BOOKS:
                    page = new BooksPage(this);
                    break;
                case VIDEOS:
                    page = new VideosPage(this);
                    break;
                default:
                    break;
            }
            getChildren().setAll(page, dialogPane);
        });

        if (WebAPI.isBrowser()) {
            setView(View.HOME);
        } else {
            getChildren().setAll(new IntroView(this));
        }
    }

    public Page<?> getCurrentPage() {
        return page;
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

    private ObjectProperty<View> view = new SimpleObjectProperty<>(this, "view");

    public View getView() {
        return view.get();
    }

    public ObjectProperty<View> viewProperty() {
        return view;
    }

    public void setView(View view) {
        this.view.set(view);
    }

    private ObjectProperty<Display> display = new SimpleObjectProperty<>(this, "display");

    public Display getDisplay() {
        return display.get();
    }

    public ObjectProperty<Display> displayProperty() {
        return display;
    }

    public void setDisplay(Display display) {
        this.display.set(display);
    }
}
