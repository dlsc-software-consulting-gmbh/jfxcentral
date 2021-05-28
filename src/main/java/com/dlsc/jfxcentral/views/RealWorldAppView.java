package com.dlsc.jfxcentral.views;

import com.dlsc.jfxcentral.DataRepository;
import com.dlsc.jfxcentral.ImageManager;
import com.dlsc.jfxcentral.MarkdownView;
import com.dlsc.jfxcentral.RootPane;
import com.dlsc.jfxcentral.model.RealWorldApp;
import com.dlsc.jfxcentral.panels.SectionPane;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

public class RealWorldAppView extends PageView {

    private final VBox vBox = new VBox();

    public RealWorldAppView(RootPane rootPane) {
        super(rootPane);

        getStyleClass().add("real-world-app-view");

        vBox.getStyleClass().add("vbox");

        SectionPane sectionPane = new SectionPane();
        // title not needed as we always have a header banner
//        sectionPane.titleProperty().bind(Bindings.createStringBinding(() -> getApp() != null ? getApp().getName() : "", appProperty()));
        sectionPane.setPrefWidth(0);
        sectionPane.setMinWidth(0);

        ImageView imageView = new ImageView();
        imageView.fitWidthProperty().bind(Bindings.createDoubleBinding(() -> sectionPane.getWidth() - sectionPane.getInsets().getLeft() - sectionPane.getInsets().getRight(), sectionPane.widthProperty(), sectionPane.insetsProperty()));
        imageView.setPreserveRatio(true);

        MarkdownView markdownView = new MarkdownView();
        sectionPane.getNodes().addAll(imageView, markdownView);

        appProperty().addListener(it -> {
            RealWorldApp app = getApp();
            if (app != null) {
                imageView.imageProperty().bind(ImageManager.getInstance().realWorldAppLargeImageProperty(app));
                markdownView.mdStringProperty().bind(DataRepository.getInstance().realWorldAppDescriptionProperty(getApp()));
                markdownView.setBaseURL(DataRepository.getInstance().getBaseUrl() + "realworld/" + app.getId());
            } else {
                imageView.imageProperty().unbind();
                markdownView.mdStringProperty().unbind();
            }
        });

        vBox.getChildren().add(sectionPane);

        setContent(vBox);
    }

    private final ObjectProperty<RealWorldApp> app = new SimpleObjectProperty<>(this, "app");

    public RealWorldApp getApp() {
        return app.get();
    }

    public ObjectProperty<RealWorldApp> appProperty() {
        return app;
    }

    public void setApp(RealWorldApp app) {
        this.app.set(app);
    }
}
