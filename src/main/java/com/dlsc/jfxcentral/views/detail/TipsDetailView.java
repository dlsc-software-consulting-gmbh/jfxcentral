package com.dlsc.jfxcentral.views.detail;

import com.dlsc.jfxcentral.data.DataRepository;
import com.dlsc.jfxcentral.data.model.Person;
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

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class TipsDetailView extends ModelObjectDetailView<Tip> {

    private MarkdownView summaryMarkdownView = new MarkdownView();

    public TipsDetailView(RootPane rootPane) {
        super(rootPane, View.TIPS);

        getStyleClass().add("tips-detail-view");

//        createTitleBox();
        SectionPane readMeBox = createReadMeBox(null,
                tip -> DataRepository.getInstance().getBaseUrl() + "tips/" + tip.getId(),
                tip -> DataRepository.getInstance().tipDescriptionProperty(getSelectedItem()));
        readMeBox.titleProperty().bind(Bindings.createStringBinding(() -> getSelectedItem() != null ? getSelectedItem().getName() : "", selectedItemProperty()));
        readMeBox.subtitleProperty().bind(Bindings.createStringBinding(() -> getSelectedItem() != null ? getSelectedItem().getSummary() : "", selectedItemProperty()));
        createStandardBoxes();
    }

    protected boolean isUsingMasterView() {
        return true;
    }

    @Override
    protected void createTitleBox() {
        // summary markdown is not used, yet
        summaryMarkdownView.getStyleClass().add("description-label");
        HBox.setHgrow(summaryMarkdownView, Priority.ALWAYS);

        if (getRootPane().isMobile()) {
            createTitleBoxMobile();
        } else {
            createTitleBoxDesktop();
        }

        selectedItemProperty().addListener(it -> {
            Tip tip = getSelectedItem();
            if (tip != null) {
                summaryMarkdownView.setBaseURL(DataRepository.getInstance().getBaseUrl() + "tools/" + tip.getId());
                summaryMarkdownView.setMdString(tip.getDescription());
            }
        });
    }

    private void createTitleBoxMobile() {
        Label titleLabel = new Label();
        titleLabel.setWrapText(true);
        titleLabel.setMinHeight(Region.USE_PREF_SIZE);
        titleLabel.getStyleClass().add("title-label");

        Label summaryLabel = new Label();
        summaryLabel.setWrapText(true);
        summaryLabel.setMinHeight(Region.USE_PREF_SIZE);
        summaryLabel.getStyleClass().add("summary-label");

        Label authorLabel = new Label();
        authorLabel.setWrapText(true);
        authorLabel.setMinHeight(Region.USE_PREF_SIZE);
        authorLabel.getStyleClass().add("author-label");

        titleLabel.textProperty().bind(Bindings.createStringBinding(() -> getSelectedItem() != null ? getSelectedItem().getName() : "", selectedItemProperty()));
        summaryLabel.textProperty().bind(Bindings.createStringBinding(() -> getSelectedItem() != null ? getSelectedItem().getSummary() : "", selectedItemProperty()));
        authorLabel.textProperty().bind(Bindings.createStringBinding(() -> {
            Tip tip = getSelectedItem();
            if (tip != null) {
                List<String> personIds = tip.getPersonIds();
                if (personIds != null) {
                    String text = personIds.stream().map(id -> {
                        Optional<Person> personById = DataRepository.getInstance().getPersonById(id);
                        if (personById.isPresent()) {
                            return personById.get().getName();
                        }
                        return id;
                    }).collect(Collectors.joining(" - "));

                    return "Written by " + text;
                }
            }
            return "";
        }, selectedItemProperty()));

        VBox vBox = new VBox();
        vBox.setPrefWidth(0);
        vBox.setMinWidth(0);

        ImageView imageView = new ImageView();
        imageView.fitWidthProperty().bind(Bindings.createDoubleBinding(() -> vBox.getWidth(), vBox.widthProperty()));
        imageView.setPreserveRatio(true);

        vBox.getChildren().setAll(imageView, titleLabel, summaryLabel, authorLabel);
        vBox.getStyleClass().add("header-box");
        vBox.setMaxHeight(Region.USE_PREF_SIZE);
        StackPane.setAlignment(vBox, Pos.TOP_LEFT);

        content.getChildren().addAll(vBox);
    }

    private void createTitleBoxDesktop() {

        summaryMarkdownView.getStyleClass().add("description-label");
        HBox.setHgrow(summaryMarkdownView, Priority.ALWAYS);

        SectionPane sectionPane = new SectionPane();
        sectionPane.getStyleClass().add("title-section");
        sectionPane.setPrefWidth(0);
        sectionPane.setMinWidth(0);
        sectionPane.titleProperty().bind(Bindings.createStringBinding(() -> getSelectedItem() != null ? getSelectedItem().getName() : "", selectedItemProperty()));
        sectionPane.subtitleProperty().bind(Bindings.createStringBinding(() -> getSelectedItem() != null ? getSelectedItem().getSummary() : "", selectedItemProperty()));

        ImageView imageView = new ImageView();
        imageView.fitWidthProperty().bind(Bindings.createDoubleBinding(() -> sectionPane.getWidth() - sectionPane.getInsets().getLeft() - sectionPane.getInsets().getRight(), sectionPane.widthProperty(), sectionPane.insetsProperty()));
        imageView.setPreserveRatio(true);

        StackPane stackPane = new StackPane(imageView);

        sectionPane.getNodes().add(stackPane);

        content.getChildren().addAll(sectionPane);
    }
}
