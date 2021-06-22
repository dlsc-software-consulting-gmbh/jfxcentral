package com.dlsc.jfxcentral.views.detail;

import com.dlsc.jfxcentral.data.DataRepository;
import com.dlsc.jfxcentral.data.ImageManager;
import com.dlsc.jfxcentral.data.model.Download;
import com.dlsc.jfxcentral.data.model.Tool;
import com.dlsc.jfxcentral.data.model.Video;
import com.dlsc.jfxcentral.panels.SectionPane;
import com.dlsc.jfxcentral.util.Util;
import com.dlsc.jfxcentral.views.AdvancedListView;
import com.dlsc.jfxcentral.views.MarkdownView;
import com.dlsc.jfxcentral.views.RootPane;
import com.dlsc.jfxcentral.views.detail.cells.DetailDownloadCell;
import com.dlsc.jfxcentral.views.detail.cells.DetailVideoCell;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import org.apache.commons.lang3.StringUtils;
import org.kordamp.ikonli.fontawesome5.FontAwesomeBrands;
import org.kordamp.ikonli.javafx.FontIcon;

public class ToolsDetailView extends DetailView<Tool> {

    private final VBox content = new VBox();

    public ToolsDetailView(RootPane rootPane) {
        super(rootPane);

        getStyleClass().add("tools-detail-view");

        content.getStyleClass().add("vbox");

        createTitleBox();
        createCoordinatesBox();
        createDownloadsBox();
        createVideoBox();
        createReadMeBox();

        setContent(content);
    }

    private void createTitleBox() {
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

    private void createVideoBox() {
        AdvancedListView<Video> listView = new AdvancedListView<>();
        listView.setPaging(true);
        listView.setVisibleRowCount(3);
        listView.setCellFactory(view -> new DetailVideoCell(getRootPane(), false));

        SectionPane sectionPane = new SectionPane();
        sectionPane.setTitle("Videos");
        sectionPane.getNodes().add(listView);

        selectedItemProperty().addListener(it -> {
            Tool tool = getSelectedItem();
            if (tool != null) {
                sectionPane.setSubtitle("Videos relevant for tool " + tool.getName());
                listView.setItems(DataRepository.getInstance().getVideosByModelObject(tool));
            } else {
                sectionPane.setSubtitle("");
                listView.setItems(FXCollections.observableArrayList());
            }
        });

        sectionPane.visibleProperty().bind(listView.itemsProperty().emptyProperty().not());
        sectionPane.managedProperty().bind(listView.itemsProperty().emptyProperty().not());

        content.getChildren().add(sectionPane);
    }

    private void createDownloadsBox() {
        AdvancedListView<Download> listView = new AdvancedListView<>();
        listView.setPaging(true);
        listView.setVisibleRowCount(3);
        listView.setCellFactory(view -> new DetailDownloadCell(getRootPane(), false));

        SectionPane sectionPane = new SectionPane();
        sectionPane.setTitle("Downloads");
        sectionPane.getNodes().add(listView);

        selectedItemProperty().addListener(it -> {
            Tool tool = getSelectedItem();
            if (tool != null) {
                sectionPane.setSubtitle("Downloads related to tool " + tool.getName());
                listView.setItems(DataRepository.getInstance().getDownloadsByModelObject(tool));
            } else {
                sectionPane.setSubtitle("");
                listView.setItems(FXCollections.observableArrayList());
            }
        });

        sectionPane.visibleProperty().bind(listView.itemsProperty().emptyProperty().not());
        sectionPane.managedProperty().bind(listView.itemsProperty().emptyProperty().not());

        content.getChildren().add(sectionPane);
    }
}
