package com.dlsc.jfxcentral.views.detail;

import com.dlsc.jfxcentral.data.DataRepository;
import com.dlsc.jfxcentral.views.MarkdownView;
import com.dlsc.jfxcentral.views.RootPane;
import com.dlsc.jfxcentral.panels.SectionPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class OpenJFXDetailView extends DetailView {

    private VBox content = new VBox();

    public OpenJFXDetailView(RootPane rootPane) {
        super(rootPane);

        createHeader();

        VBox.setVgrow(content, Priority.ALWAYS);
        setContent(content);
    }

    private void createHeader() {
        SectionPane sectionPane = new SectionPane();
        sectionPane.setTitle("OpenJFX Project");

        MarkdownView markdownView = new MarkdownView();
        markdownView.mdStringProperty().bind(DataRepository.getInstance().openJFXTextProperty());
        sectionPane.getNodes().add(markdownView);

        content.getChildren().add(sectionPane);
    }
}
