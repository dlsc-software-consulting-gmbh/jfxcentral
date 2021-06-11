package com.dlsc.jfxcentral.views;

import com.dlsc.gemsfx.DialogPane;
import com.dlsc.jfxcentral.JFXCentralApp;
import com.dlsc.jfxcentral.util.PageUtil;
import com.dlsc.jfxcentral.util.Util;
import com.dlsc.jfxcentral.views.page.*;
import com.gluonhq.attach.display.DisplayService;
import com.jpro.webapi.WebAPI;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.css.PseudoClass;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;

import java.util.Collections;

public class RootPane extends StackPane {

    private final DialogPane dialogPane = new DialogPane();

    private Page<?> page;

    public RootPane() {
        getStyleClass().add("root-pane");

        ImageView compassImageView = new ImageView();
        compassImageView.getStyleClass().add("logo-image-view");

        DoubleBinding imageSizeBinding = Bindings.createDoubleBinding(() -> {
            if (isExpanded()) {
                return 128d;
            }
            return 80d;
        }, expandedProperty());

        compassImageView.fitWidthProperty().bind(imageSizeBinding);
        compassImageView.fitHeightProperty().bind(imageSizeBinding);
        compassImageView.setPreserveRatio(true);

        Util.setLink(compassImageView, PageUtil.getLink(View.HOME), "Home");

        StackPane compassWrapper = new StackPane(compassImageView);
        compassWrapper.getStyleClass().add("logo-image-wrapper");
        compassWrapper.maxWidthProperty().bind(compassImageView.fitWidthProperty());
        compassWrapper.maxHeightProperty().bind(compassImageView.fitHeightProperty());
        StackPane.setAlignment(compassWrapper, Pos.TOP_LEFT);

        BorderPane borderPane = new BorderPane();
        borderPane.setTop(new HeaderPane(this));
        getChildren().setAll(borderPane, dialogPane);

        dialogPane.getStylesheets().add(JFXCentralApp.class.getResource("styles.css").toExternalForm());

        DisplayService.create().ifPresentOrElse(service -> {
            if (service.isDesktop()) {
                setDisplay(Display.DESKTOP);
            } else if (service.isPhone()) {
                setDisplay(Display.PHONE);
            } else if (service.isTablet()) {
                setDisplay(Display.TABLET);
            }
        }, () -> {
            if (WebAPI.isBrowser()) {
                setDisplay(Display.WEB);
            } else {
                setDisplay(Display.DESKTOP);
            }
        });

        viewProperty().addListener(it -> {
            page = null;
            switch (getView()) {
                case HOME:
                    page = new HomePage(this);
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
                case REAL_WORLD:
                    page = new RealWorldAppsPage(this);
                    break;
                case DOWNLOADS:
                    page = new DownloadsPage(this);
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
            getChildren().setAll(borderPane, compassWrapper, dialogPane);
            borderPane.setCenter(page);
        });

        sceneProperty().addListener(it -> {
            Scene scene = getScene();
            if (scene != null && WebAPI.isBrowser()) {
                WebAPI webAPI = WebAPI.getWebAPI(scene);
                String language = webAPI.getLanguage();
                System.out.println("language: " + language);
                // determine user locale
            }
        });

        expandedProperty().addListener(it -> updateExpandedPseudoClass());
        updateExpandedPseudoClass();
    }

    private void updateExpandedPseudoClass() {
        pseudoClassStateChanged(PseudoClass.getPseudoClass("expanded"), isExpanded());
    }

    private final BooleanProperty expanded = new SimpleBooleanProperty(this, "expanded", true);

    public boolean isExpanded() {
        return expanded.get();
    }

    public BooleanProperty expandedProperty() {
        return expanded;
    }

    public void setExpanded(boolean expanded) {
        this.expanded.set(expanded);
    }

    public Page<?> getCurrentPage() {
        return page;
    }

    public DialogPane getDialogPane() {
        return dialogPane;
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
