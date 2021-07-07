package com.dlsc.jfxcentral.views.detail;

import com.dlsc.jfxcentral.data.DataRepository;
import com.dlsc.jfxcentral.data.ImageManager;
import com.dlsc.jfxcentral.data.model.Tool;
import com.dlsc.jfxcentral.panels.SectionPane;
import com.dlsc.jfxcentral.util.Util;
import com.dlsc.jfxcentral.views.MarkdownView;
import com.dlsc.jfxcentral.views.RootPane;
import com.dlsc.jfxcentral.views.View;
import javafx.beans.binding.Bindings;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import org.apache.commons.lang3.StringUtils;
import org.kordamp.ikonli.fontawesome5.FontAwesomeBrands;
import org.kordamp.ikonli.javafx.FontIcon;

public class ToolsDetailView extends ModelObjectDetailView<Tool> {

    public ToolsDetailView(RootPane rootPane) {
        super(rootPane, View.TOOLS);

        getStyleClass().add("tools-detail-view");

        createTitleBox();
        createCoordinatesBox();
        createReadMeBox();
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
        sectionPane.subtitleProperty().bind(Bindings.createStringBinding(() -> getSelectedItem() != null ? getSelectedItem().getSummary() : "", selectedItemProperty()));
        sectionPane.getStyleClass().add("title-section");
        sectionPane.setExtras(iconView);

        sectionPane.getNodes().add(hBox);

        selectedItemProperty().addListener(it -> {
            Tool tool = getSelectedItem();
            if (tool != null) {
                iconView.imageProperty().bind(ImageManager.getInstance().toolImageProperty(tool));
                descriptionMarkdownView.setBaseURL(DataRepository.getInstance().getBaseUrl() + "tools/" + tool.getId());
                descriptionMarkdownView.setMdString(tool.getDescription());

                buttonBox.getChildren().clear();

                if (StringUtils.isNotEmpty(tool.getHomepage())) {
                    Button twitter = new Button("Homepage");
                    twitter.getStyleClass().addAll("social-button", "homepage");
                    Util.setLink(twitter, tool.getHomepage(), tool.getName());
                    twitter.setGraphic(new FontIcon(FontAwesomeBrands.TWITTER));
                    buttonBox.getChildren().add(twitter);
                }

                if (StringUtils.isNotEmpty(tool.getRepository())) {
                    Button linkedIn = new Button("Repository");
                    linkedIn.getStyleClass().addAll("social-button", "repository");
                    Util.setLink(linkedIn, tool.getRepository(), tool.getName());
                    linkedIn.setGraphic(new FontIcon(FontAwesomeBrands.GITHUB));
                    buttonBox.getChildren().add(linkedIn);
                }
            }
        });

        content.getChildren().addAll(sectionPane);
    }

    private void createCoordinatesBox() {
        CoordinatesPane coordinatesPane = new CoordinatesPane();
        coordinatesPane.coordinatesProperty().bind(selectedItemProperty());
        content.getChildren().add(coordinatesPane);
    }

    private void createReadMeBox() {
        SectionPane sectionPane = new SectionPane();
        sectionPane.setTitle("Readme");

        MarkdownView markdownView = new MarkdownView();
        sectionPane.getNodes().add(markdownView);

        selectedItemProperty().addListener(it -> {
            Tool tool = getSelectedItem();
            if (tool != null) {
                markdownView.setBaseURL(DataRepository.getInstance().getBaseUrl() + "tools/" + tool.getId());
                markdownView.mdStringProperty().bind(DataRepository.getInstance().toolDescriptionProperty(getSelectedItem()));
            }
        });

        content.getChildren().add(sectionPane);
    }
}
