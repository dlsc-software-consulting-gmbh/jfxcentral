package com.dlsc.jfxcentral.views;

import com.dlsc.jfxcentral.DataRepository;
import com.dlsc.jfxcentral.MarkdownView;
import com.dlsc.jfxcentral.RootPane;
import com.dlsc.jfxcentral.panels.SectionPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class OpenJFXView extends PageView {

    private VBox content = new VBox();

    public OpenJFXView(RootPane rootPane) {
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
