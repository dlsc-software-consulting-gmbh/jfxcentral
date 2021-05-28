package com.dlsc.jfxcentral.views;

import com.dlsc.jfxcentral.DataRepository;
import com.dlsc.jfxcentral.MarkdownView;
import com.dlsc.jfxcentral.RootPane;
import com.dlsc.jfxcentral.model.Tool;
import com.dlsc.jfxcentral.panels.SectionPane;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.layout.VBox;

public class ToolView extends PageView {

    private final VBox vBox = new VBox();

    public ToolView(RootPane rootPane) {
        super(rootPane);

        getStyleClass().add("tool-view");

        vBox.getStyleClass().add("vbox");

        SectionPane sectionPane = new SectionPane();
        sectionPane.titleProperty().bind(Bindings.createStringBinding(() -> getTool() != null ? getTool().getName() : "", toolProperty()));

        MarkdownView markdownView = new MarkdownView();
        sectionPane.getNodes().add(markdownView);

        toolProperty().addListener(it -> {
            Tool tool = getTool();
            if (tool != null) {
                markdownView.mdStringProperty().bind(DataRepository.getInstance().toolDescriptionProperty(getTool()));
                markdownView.setBaseURL(DataRepository.getInstance().getBaseUrl() + "tools/" + tool.getId());
            }
        });

        vBox.getChildren().add(sectionPane);

        setContent(vBox);
    }

    private final ObjectProperty<Tool> tool = new SimpleObjectProperty<>(this, "tool");

    public Tool getTool() {
        return tool.get();
    }

    public ObjectProperty<Tool> toolProperty() {
        return tool;
    }

    public void setTool(Tool tool) {
        this.tool.set(tool);
    }
}
