package com.dlsc.jfxcentral.views.detail;

import com.dlsc.jfxcentral.JFXCentralApp;
import com.dlsc.jfxcentral.panels.SectionPane;
import com.dlsc.jfxcentral.util.DeveloperTool;
import com.dlsc.jfxcentral.views.MarkdownView;
import com.dlsc.jfxcentral.views.RootPane;
import com.dlsc.jfxcentral.views.View;
import com.dlsc.jfxcentral.views.ikonli.IkonliBrowser;
import com.dlsc.showcase.CssShowcaseView;
import com.jpro.webapi.HTMLView;
import com.jpro.webapi.WebAPI;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;
import javafx.scene.web.WebView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.File;
import java.net.MalformedURLException;
import java.util.List;
import java.util.stream.Collectors;

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
                        break;
                    case IKONLI:
                        setContent(createIkonliView());
                        break;
                    case CSS_SHOWCASE:
                        setContent(createShowcaseView());
                        break;
                }
            }
        });
    }

    @Override
    public boolean isUsingScrollPane() {
        return false;
    }

    private SectionPane createIkonliView() {
        SectionPane sectionPane = new SectionPane();
        sectionPane.setTitle("Ikonli Browser");
        sectionPane.setSubtitle("Explore all available icon fonts in Ikonli");
        sectionPane.getNodes().add(new IkonliBrowser());
        sectionPane.setPrefHeight(0);
        sectionPane.setMinHeight(0);
        return sectionPane;
    }

    private SectionPane createCSSDocsView() {
        SectionPane sectionPane = new SectionPane();

        Node node;

        String url = "https://docs.oracle.com/javase/8/javafx/api/javafx/scene/doc-files/cssref.html";
        if (WebAPI.isBrowser()) {
            HTMLView htmlView = new HTMLView();
            String content = "<iframe src=\"" + url + "\" frameborder=\"0\" style=\"width: 100%; height: 100%; \"></iframe>";
            htmlView.setContent(content);
            node = htmlView;
            htmlView.parentProperty().addListener(it -> {
                Parent parent = htmlView.getParent();
                if (parent != null) {
                    htmlView.prefWidthProperty().bind((((Region) parent).widthProperty()));
                    htmlView.minWidthProperty().bind((((Region) parent).widthProperty()));
                    htmlView.maxWidthProperty().bind((((Region) parent).widthProperty()));
                    htmlView.prefHeightProperty().bind((((Region) parent).heightProperty()));
                    htmlView.minHeightProperty().bind((((Region) parent).heightProperty()));
                    htmlView.maxHeightProperty().bind((((Region) parent).heightProperty()));
                }
            });
        } else {
            WebView webView = new WebView();
            VBox.setVgrow(webView, Priority.ALWAYS);
            webView.getEngine().load(url);
            node = webView;
        }

        sectionPane.getNodes().add(node);

        return sectionPane;
    }

    private Node createShowcaseView() {
        Button button = new Button("Launch ShowcaseFX");
        button.setOnAction(e -> showShowcaseFX());

        ImageView imageView = new ImageView(JFXCentralApp.class.getResource("showcasefx.png").toExternalForm());
        imageView.setFitWidth(500);
        imageView.setPreserveRatio(true);
        imageView.setOnMouseClicked(evt -> showShowcaseFX());
        imageView.setCursor(Cursor.HAND);

        MarkdownView markdownView = new MarkdownView();
        markdownView.setMdString("ShowcaseFX is a tool that allows you to verify your CSS stylesheets easily. The tool lists all default controls and you can quickly see how your stylesheet's CSS rules are being applied. You can either drag and drop CSS files onto the view or use the file menu to load one or more. This application's project can be [found on GitHub](https://github.com/dlsc-software-consulting-gmbh/ShowcaseFX).");

        SectionPane sectionPane1 = new SectionPane();
        sectionPane1.setTitle("ShowcaseFX");
        sectionPane1.setSubtitle("Explore and debug JavaFX CSS stylesheets.");

        sectionPane1.getNodes().addAll(markdownView);

        Label label = new Label("ShowcaseFX must be launched in its own window, otherwise it\nwould inherit the styleshet of JFX-Central");
        label.setTextAlignment(TextAlignment.CENTER);
        label.getStyleClass().add("disclaimer");

        VBox vBox = new VBox(50, label, imageView, button);
        vBox.setAlignment(Pos.CENTER);
        VBox.setVgrow(vBox, Priority.ALWAYS);

        SectionPane sectionPane2 = new SectionPane();
        sectionPane2.getNodes().add(vBox);
        VBox.setVgrow(sectionPane2, Priority.ALWAYS);

        return new VBox(sectionPane1, sectionPane2);
    }

    private void showShowcaseFX() {
        CssShowcaseView view = new CssShowcaseView();

        MenuItem loadFile = new MenuItem("Load CSS Stylesheets ...");
        loadFile.setOnAction(evt -> loadFile(view));
        loadFile.setAccelerator(KeyCombination.keyCombination("shortcut+o"));

        MenuItem clearStylesheets = new MenuItem("Clear");
        clearStylesheets.setOnAction(evt -> view.getConfigurations().clear());
        clearStylesheets.setAccelerator(KeyCombination.keyCombination("shortcut+e"));

        MenuItem exit = new MenuItem("Quit");
        exit.setOnAction(evt -> Platform.exit());
        exit.setAccelerator(KeyCombination.keyCombination("shortcut+q"));

        Menu menu = new Menu("File");
        menu.getItems().addAll(loadFile, clearStylesheets, exit);

        MenuBar menuBar = new MenuBar(menu);
        menuBar.setUseSystemMenuBar(false);

        BorderPane borderPane = new BorderPane(view);
        borderPane.setTop(menuBar);

        Scene scene = new Scene(borderPane, 1024, 768);

        Stage stage = new Stage(StageStyle.DECORATED);
        stage.initOwner(getScene().getWindow());
        stage.setScene(scene);
        stage.setTitle("ShowcaseFX");
        stage.show();
    }

    private FileChooser fileChooser;

    private void loadFile(CssShowcaseView view) {
        if (fileChooser == null) {
            fileChooser = new FileChooser();
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSS Stylesheets", "*.css"));
            fileChooser.setSelectedExtensionFilter(fileChooser.getExtensionFilters().get(0));
        }

        List<File> files = fileChooser.showOpenMultipleDialog(view.getScene().getWindow());
        if (files != null) {
            String[] urls = files.stream().map(file -> {
                try {
                    return file.toURI().toURL().toExternalForm();
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
                return "";
            }).toArray(String[]::new);

            CssShowcaseView.CssConfiguration configuration = new CssShowcaseView.CssConfiguration( files.stream().map(file -> file.getName()).collect(Collectors.joining(", ")), urls);
            view.getConfigurations().add(configuration);
            view.setSelectedConfiguration(configuration);
        }
    }

    protected boolean isUsingMasterView() {
        return true;
    }

    @Override
    protected void createTitleBox() {
    }
}
