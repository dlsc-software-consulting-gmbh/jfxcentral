package com.dlsc.jfxcentral.views.detail;

import com.dlsc.jfxcentral.data.DataRepository;
import com.dlsc.jfxcentral.data.ImageManager;
import com.dlsc.jfxcentral.views.MarkdownView;
import com.dlsc.jfxcentral.views.RootPane;
import com.dlsc.jfxcentral.model.RealWorldApp;
import com.dlsc.jfxcentral.panels.SectionPane;
import javafx.beans.binding.Bindings;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

public class RealWorldAppsDetailView extends DetailView<RealWorldApp> {

    private final VBox vBox = new VBox();

    public RealWorldAppsDetailView(RootPane rootPane) {
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
        markdownView.setOnImageClick(image -> rootPane.showImage(getSelectedItem().getName(), image));
        sectionPane.getNodes().addAll(imageView, markdownView);

        selectedItemProperty().addListener(it -> {
            RealWorldApp app = getSelectedItem();
            if (app != null) {
                imageView.imageProperty().bind(ImageManager.getInstance().realWorldAppLargeImageProperty(app));
                markdownView.setBaseURL(DataRepository.getInstance().getBaseUrl() + "realworld/" + app.getId());
                markdownView.mdStringProperty().bind(DataRepository.getInstance().realWorldAppDescriptionProperty(getSelectedItem()));
            } else {
                imageView.imageProperty().unbind();
                markdownView.mdStringProperty().unbind();
            }
        });

        vBox.getChildren().add(sectionPane);

        setContent(vBox);
    }
}