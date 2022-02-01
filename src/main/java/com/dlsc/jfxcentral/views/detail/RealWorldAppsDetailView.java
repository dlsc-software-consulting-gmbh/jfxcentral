package com.dlsc.jfxcentral.views.detail;

import com.dlsc.jfxcentral.data.DataRepository;
import com.dlsc.jfxcentral.data.ImageManager;
import com.dlsc.jfxcentral.data.model.RealWorldApp;
import com.dlsc.jfxcentral.panels.SectionPane;
import com.dlsc.jfxcentral.views.MarkdownView;
import com.dlsc.jfxcentral.views.RootPane;
import com.dlsc.jfxcentral.views.View;
import com.jpro.webapi.WebAPI;
import javafx.beans.binding.Bindings;
import javafx.scene.image.ImageView;

public class RealWorldAppsDetailView extends ModelObjectDetailView<RealWorldApp> {

    public RealWorldAppsDetailView(RootPane rootPane) {
        super(rootPane, View.REAL_WORLD);

        getStyleClass().add("real-world-detail-view");

        if (WebAPI.isBrowser()) {
            createTitleBox();
        }
        createReadMeSection();
        createStandardBoxes();
    }

    @Override
    protected void createTitleBox() {
        SectionPane sectionPane = new SectionPane();
        sectionPane.getStyleClass().add("title-box");
        sectionPane.setPrefWidth(0);
        sectionPane.setMinWidth(0);

        ImageView imageView = new ImageView();
        imageView.setPreserveRatio(true);

        selectedItemProperty().addListener(it -> {
            RealWorldApp app = getSelectedItem();
            if (app != null) {
                imageView.imageProperty().bind(ImageManager.getInstance().realWorldAppLargeImageProperty(app));
            } else {
                imageView.imageProperty().unbind();
            }
        });

        if (getRootPane().isMobile()) {
            imageView.fitWidthProperty().bind(Bindings.createDoubleBinding(() -> content.getWidth(), content.widthProperty()));
            content.getChildren().add(imageView);
        } else {
            imageView.fitWidthProperty().bind(Bindings.createDoubleBinding(() -> sectionPane.getWidth() - sectionPane.getInsets().getLeft() - sectionPane.getInsets().getRight(), sectionPane.widthProperty(), sectionPane.insetsProperty()));
            sectionPane.getNodes().add(imageView);
            content.getChildren().add(sectionPane);
        }
    }

    private void createReadMeSection() {
        SectionPane sectionPane = new SectionPane();
        sectionPane.setPrefWidth(0);
        sectionPane.setMinWidth(0);
        sectionPane.setTitle("Description");
        sectionPane.setSubtitle("Information about the application and screenshots");

        MarkdownView markdownView = new MarkdownView();
        markdownView.setOnImageClick(image -> {
            getRootPane().showImage(getSelectedItem().getName(), image);
        });
        sectionPane.getNodes().add(markdownView);

        selectedItemProperty().addListener(it -> {
            RealWorldApp app = getSelectedItem();
            if (app != null) {
                sectionPane.setTitle(app.getName());
                sectionPane.setSubtitle(app.getSummary());
                markdownView.setBaseURL(DataRepository.getInstance().getBaseUrl() + "realworld/" + app.getId());
                markdownView.mdStringProperty().bind(DataRepository.getInstance().realWorldAppDescriptionProperty(getSelectedItem()));
            } else {
                sectionPane.setTitle("");
                sectionPane.setSubtitle("");
                markdownView.mdStringProperty().unbind();
            }
        });

        content.getChildren().add(sectionPane);
    }

    protected boolean isUsingMasterView() {
        return true;
    }
}
