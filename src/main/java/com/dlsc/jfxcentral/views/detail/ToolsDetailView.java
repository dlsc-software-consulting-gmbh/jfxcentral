package com.dlsc.jfxcentral.views.detail;

import com.dlsc.jfxcentral.data.DataRepository;
import com.dlsc.jfxcentral.views.MarkdownView;
import com.dlsc.jfxcentral.views.RootPane;
import com.dlsc.jfxcentral.model.Tool;
import com.dlsc.jfxcentral.panels.SectionPane;
import javafx.beans.binding.Bindings;
import javafx.scene.layout.VBox;

public class ToolsDetailView extends DetailView<Tool> {

    private final VBox vBox = new VBox();

    public ToolsDetailView(RootPane rootPane) {
        super(rootPane);

        getStyleClass().add("tool-view");

        vBox.getStyleClass().add("vbox");

        SectionPane sectionPane = new SectionPane();
        sectionPane.titleProperty().bind(Bindings.createStringBinding(() -> getSelectedItem() != null ? getSelectedItem().getName() : "", selectedItemProperty()));

        MarkdownView markdownView = new MarkdownView();
        sectionPane.getNodes().add(markdownView);

        selectedItemProperty().addListener(it -> {
            Tool tool = getSelectedItem();
            if (tool != null) {
                markdownView.mdStringProperty().bind(DataRepository.getInstance().toolDescriptionProperty(getSelectedItem()));
                markdownView.setBaseURL(DataRepository.getInstance().getBaseUrl() + "tools/" + tool.getId());
            }
        });

        vBox.getChildren().add(sectionPane);

        setContent(vBox);
    }
}
