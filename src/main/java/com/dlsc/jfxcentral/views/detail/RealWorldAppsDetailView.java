package com.dlsc.jfxcentral.views.detail;

import com.dlsc.jfxcentral.data.DataRepository;
import com.dlsc.jfxcentral.data.ImageManager;
import com.dlsc.jfxcentral.views.MarkdownView;
import com.dlsc.jfxcentral.views.RootPane;
import com.dlsc.jfxcentral.data.model.RealWorldApp;
import com.dlsc.jfxcentral.panels.SectionPane;
import com.dlsc.jfxcentral.views.View;
import javafx.beans.binding.Bindings;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

public class RealWorldAppsDetailView extends DetailView<RealWorldApp> {

    private final VBox vBox = new VBox();

    public RealWorldAppsDetailView(RootPane rootPane) {
        super(rootPane, View.REAL_WORLD);

        getStyleClass().add("real-world-detail-view");

        vBox.getStyleClass().add("vbox");

        SectionPane sectionPane = new SectionPane();
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

    protected boolean isUsingMasterView() {
        return true;
    }
}
