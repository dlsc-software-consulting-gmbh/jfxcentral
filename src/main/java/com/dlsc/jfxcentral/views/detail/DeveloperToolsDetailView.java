package com.dlsc.jfxcentral.views.detail;

import com.dlsc.jfxcentral.util.DeveloperTool;
import com.dlsc.jfxcentral.views.RootPane;
import com.dlsc.jfxcentral.views.View;
import com.dlsc.jfxcentral.views.ikonli.IkonliBrowser;
import com.jpro.webapi.HTMLView;
import com.jpro.webapi.WebAPI;
import javafx.scene.Node;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebView;

public class DeveloperToolsDetailView extends ModelObjectDetailView<DeveloperTool> {

    public DeveloperToolsDetailView(RootPane rootPane) {
        super(rootPane, View.DEVELOPMENT);
        getStyleClass().add("developer-tools-detail-view");

        selectedItemProperty().addListener(it -> {
            DeveloperTool selectedItem = getSelectedItem();
            if (selectedItem != null) {
                switch (selectedItem.getTool()) {
                    case CSS_DOCS:
                        Node cssDocsView = createCSSDocsView();
                        setContent(cssDocsView);
                        BorderPane.clearConstraints(cssDocsView);
                        break;
                    case IKONLI:
                        setContent(createIkonliView());
                        break;
                }
            }
        });
    }

    private Node createIkonliView() {
        return new IkonliBrowser();
    }

    private Node createCSSDocsView() {
        String url = "https://docs.oracle.com/javase/8/javafx/api/javafx/scene/doc-files/cssref.html";
        if (WebAPI.isBrowser()) {
            HTMLView htmlView = new HTMLView();
            String content = "<iframe src=\"" + url +"\" frameborder=\"0\" style=\"width: 100%; height: 100%; \"></iframe>";
            htmlView.setContent(content);
            return htmlView;
        } else {
            WebView webView = new WebView();
            VBox.setVgrow(webView, Priority.ALWAYS);
            webView.getEngine().load(url);
            return webView;
        }
    }

    protected boolean isUsingMasterView() {
        return true;
    }

    @Override
    protected void createTitleBox() {
    }
}
