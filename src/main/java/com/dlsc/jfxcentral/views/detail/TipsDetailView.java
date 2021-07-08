package com.dlsc.jfxcentral.views.detail;

import com.dlsc.jfxcentral.JFXCentralApp;
import com.dlsc.jfxcentral.data.DataRepository;
import com.dlsc.jfxcentral.data.model.Tip;
import com.dlsc.jfxcentral.panels.SectionPane;
import com.dlsc.jfxcentral.views.MarkdownView;
import com.dlsc.jfxcentral.views.RootPane;
import com.dlsc.jfxcentral.views.View;
import javafx.beans.binding.Bindings;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class TipsDetailView extends ModelObjectDetailView<Tip> {

    public TipsDetailView(RootPane rootPane) {
        super(rootPane, View.TIPS);

        createTitleBox();
        createReadMeBox(
                tip -> DataRepository.getInstance().getBaseUrl() + "tips/" + tip.getId(),
                tip -> DataRepository.getInstance().tipDescriptionProperty(getSelectedItem()));
        createStandardBoxes();
    }

    protected boolean isUsingMasterView() {
        return true;
    }

    @Override
    protected void createTitleBox() {
        MarkdownView summaryMarkdownView = new MarkdownView();

        ImageView iconView = new ImageView();
        iconView.setFitWidth(128);
        iconView.setFitHeight(64);
        iconView.setPreserveRatio(true);

        summaryMarkdownView.getStyleClass().add("description-label");
        HBox.setHgrow(summaryMarkdownView, Priority.ALWAYS);

        FlowPane buttonBox = new FlowPane();
        buttonBox.getStyleClass().add("button-box");

        VBox vBox = new VBox(summaryMarkdownView, buttonBox);
        vBox.getStyleClass().add("vbox");
        vBox.setFillWidth(true);
        HBox.setHgrow(vBox, Priority.ALWAYS);

        SectionPane sectionPane = new SectionPane();
        sectionPane.titleProperty().bind(Bindings.createStringBinding(() -> getSelectedItem() != null ? getSelectedItem().getName() : "", selectedItemProperty()));
        sectionPane.subtitleProperty().bind(Bindings.createStringBinding(() -> getSelectedItem() != null ? getSelectedItem().getSummary() : "", selectedItemProperty()));
        sectionPane.getStyleClass().add("title-section");
        sectionPane.setExtras(iconView);
        sectionPane.setPrefWidth(0);
        sectionPane.setMinWidth(0);

        ImageView imageView = new ImageView(JFXCentralApp.class.getResource("tips.png").toExternalForm());
        imageView.fitWidthProperty().bind(Bindings.createDoubleBinding(() -> sectionPane.getWidth() - sectionPane.getInsets().getLeft() - sectionPane.getInsets().getRight(), sectionPane.widthProperty(), sectionPane.insetsProperty()));
        imageView.setPreserveRatio(true);

        sectionPane.getNodes().add(imageView);

        selectedItemProperty().addListener(it -> {
            Tip tip = getSelectedItem();
            if (tip != null) {
//                iconView.imageProperty().bind(ImageManager.getInstance().toolImageProperty(tip));
                summaryMarkdownView.setBaseURL(DataRepository.getInstance().getBaseUrl() + "tools/" + tip.getId());
                summaryMarkdownView.setMdString(tip.getDescription());
                buttonBox.getChildren().clear();
            }
        });

        content.getChildren().addAll(sectionPane);
    }
}
