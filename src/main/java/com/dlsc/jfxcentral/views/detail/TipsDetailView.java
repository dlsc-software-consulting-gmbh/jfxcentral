package com.dlsc.jfxcentral.views.detail;

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
        MarkdownView descriptionMarkdownView = new MarkdownView();

        ImageView iconView = new ImageView();
        iconView.setFitWidth(128);
        iconView.setFitHeight(64);
        iconView.setPreserveRatio(true);

        descriptionMarkdownView.getStyleClass().add("description-label");
        HBox.setHgrow(descriptionMarkdownView, Priority.ALWAYS);

        FlowPane buttonBox = new FlowPane();
        buttonBox.getStyleClass().add("button-box");

        VBox vBox = new VBox(descriptionMarkdownView, buttonBox);
        vBox.getStyleClass().add("vbox");
        vBox.setFillWidth(true);
        HBox.setHgrow(vBox, Priority.ALWAYS);

        HBox hBox = new HBox(vBox);
        hBox.getStyleClass().add("hbox");

        SectionPane sectionPane = new SectionPane();
        sectionPane.titleProperty().bind(Bindings.createStringBinding(() -> getSelectedItem() != null ? getSelectedItem().getName() : "", selectedItemProperty()));
        sectionPane.subtitleProperty().bind(Bindings.createStringBinding(() -> getSelectedItem() != null ? getSelectedItem().getName() : "", selectedItemProperty()));
        sectionPane.getStyleClass().add("title-section");
        sectionPane.setExtras(iconView);

        sectionPane.getNodes().add(hBox);

        selectedItemProperty().addListener(it -> {
            Tip tip = getSelectedItem();
            if (tip != null) {
//                iconView.imageProperty().bind(ImageManager.getInstance().toolImageProperty(tip));
                descriptionMarkdownView.setBaseURL(DataRepository.getInstance().getBaseUrl() + "tools/" + tip.getId());
//                descriptionMarkdownView.setMdString(tip.getDescription());
                buttonBox.getChildren().clear();
            }
        });

        content.getChildren().addAll(sectionPane);
    }
}
