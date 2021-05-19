package com.dlsc.jfxcentral.views;

import com.dlsc.jfxcentral.MarkdownView;
import com.dlsc.jfxcentral.RootPane;
import com.dlsc.jfxcentral.panels.SectionPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class HomeView extends PageView {

    private final VBox content = new VBox();

    public HomeView(RootPane rootPane) {
        super(rootPane);
        getStyleClass().add("home-view");

        createNewSection();
        createContactInfo();

        setContent(content);
    }

    private void createNewSection() {
        SectionPane sectionPane = new SectionPane();
        sectionPane.setTitle("Latest Additions");
        sectionPane.setSubtitle("");
        VBox.setVgrow(sectionPane, Priority.ALWAYS);
        content.getChildren().add(sectionPane);
    }

    private void createContactInfo() {
        MarkdownView markdownView = new MarkdownView();
        markdownView.setMdString("## Contact\n\nDLSC Software & Consulting GmbH\n\nAsylweg 28, 8134 Adliswil, Switzerland\n\nMobile: +41-79-800-23-20\n\nMail: dlemmermann@gmail.com");
        markdownView.getStyleClass().add("contact-markdown-view");

        SectionPane sectionPane = new SectionPane(markdownView);
        sectionPane.setTitle(null);
        sectionPane.setSubtitle(null);
        content.getChildren().add(sectionPane);
    }
}