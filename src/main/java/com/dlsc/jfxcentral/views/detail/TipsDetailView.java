package com.dlsc.jfxcentral.views.detail;

import com.dlsc.jfxcentral.data.DataRepository;
import com.dlsc.jfxcentral.data.model.Tip;
import com.dlsc.jfxcentral.panels.SectionPane;
import com.dlsc.jfxcentral.views.MarkdownView;
import com.dlsc.jfxcentral.views.RootPane;
import com.dlsc.jfxcentral.views.View;
import javafx.beans.binding.Bindings;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;

public class TipsDetailView extends ModelObjectDetailView<Tip> {

    public TipsDetailView(RootPane rootPane) {
        super(rootPane, View.TIPS);

        getStyleClass().add("tips-detail-view");

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

        summaryMarkdownView.getStyleClass().add("description-label");
        HBox.setHgrow(summaryMarkdownView, Priority.ALWAYS);

        SectionPane sectionPane = new SectionPane();
        sectionPane.getStyleClass().add("title-section");
        sectionPane.setPrefWidth(0);
        sectionPane.setMinWidth(0);

        Label titleLabel = new Label();
        titleLabel.setWrapText(true);
        titleLabel.setMinHeight(Region.USE_PREF_SIZE);
        titleLabel.getStyleClass().add("title-label");

        Label summaryLabel = new Label();
        summaryLabel.setWrapText(true);
        summaryLabel.setMinHeight(Region.USE_PREF_SIZE);
        summaryLabel.getStyleClass().add("summary-label");

        titleLabel.textProperty().bind(Bindings.createStringBinding(() -> getSelectedItem() != null ? getSelectedItem().getName() : "", selectedItemProperty()));
        summaryLabel.textProperty().bind(Bindings.createStringBinding(() -> getSelectedItem() != null ? getSelectedItem().getSummary() : "", selectedItemProperty()));

        VBox vBox = new VBox(titleLabel, summaryLabel);
        vBox.getStyleClass().add("vbox");
        vBox.setMaxHeight(Region.USE_PREF_SIZE);
        StackPane.setAlignment(vBox, Pos.TOP_LEFT);

        ImageView imageView = new ImageView();
        imageView.fitWidthProperty().bind(Bindings.createDoubleBinding(() -> sectionPane.getWidth() - sectionPane.getInsets().getLeft() - sectionPane.getInsets().getRight(), sectionPane.widthProperty(), sectionPane.insetsProperty()));
        imageView.setPreserveRatio(true);

        StackPane stackPane = new StackPane(imageView, vBox);

        sectionPane.getNodes().add(stackPane);

        selectedItemProperty().addListener(it -> {
            Tip tip = getSelectedItem();
            if (tip != null) {
                summaryMarkdownView.setBaseURL(DataRepository.getInstance().getBaseUrl() + "tools/" + tip.getId());
                summaryMarkdownView.setMdString(tip.getDescription());
            }
        });

        content.getChildren().addAll(sectionPane);
    }
}
