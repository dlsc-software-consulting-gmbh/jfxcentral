package com.dlsc.jfxcentral;

import com.dlsc.gemsfx.util.StageManager;
import com.dlsc.jfxcentral.data.DataRepository;
import com.dlsc.jfxcentral.data.model.ModelObject;
import com.dlsc.jfxcentral.panels.SectionPane;
import com.dlsc.jfxcentral.util.Detector;
import com.dlsc.jfxcentral.util.PageUtil;
import com.dlsc.jfxcentral.views.IntroView;
import com.dlsc.jfxcentral.views.ikonli.IkonliBrowser;
import com.dlsc.showcase.CssShowcaseView;
import com.dustinredmond.fxtrayicon.FXTrayIcon;
import com.gluonhq.attach.display.DisplayService;
import com.jpro.web.Util;
import com.jpro.web.sessionmanager.SessionManager;
import com.jpro.webapi.WebAPI;
import com.sun.nio.file.SensitivityWatchEventModifier;
import fr.brouillard.oss.cssfx.CSSFX;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.ProgressMonitor;
import org.eclipse.jgit.lib.TextProgressMonitor;
import org.eclipse.jgit.merge.ContentMergeStrategy;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.file.*;
import java.util.Comparator;
import java.util.function.Consumer;

public class JFXCentralApp extends Application {

    private static boolean repositoryInitialized;
    private WebApp app;

    @Override
    public void start(Stage stage) throws IOException, GitAPIException {
        app = new WebApp(stage);

        Scene scene;

        Parent root = app;
        if (!WebAPI.isBrowser()) {
             root = new IntroView(() -> {
                if (repositoryInitialized) {
                    showHomeOrLoadingView(app, stage);
                }
            });

            updateRepositoryInBackground(((IntroView) root).getAnimationView(), () -> buildTrayIcon(stage));
            CustomStage customStage = new CustomStage(stage, root);
            customStage.setCloseHandler(() -> System.exit(0));
            scene = new Scene(customStage);
        } else {
            if (!repositoryInitialized) {
                updateRepository(new TextProgressMonitor());
            }
            scene = new Scene(root);
        }

        scene.getStylesheets().add(JFXCentralApp.class.getResource("styles.css").toExternalForm());
        scene.getStylesheets().add(JFXCentralApp.class.getResource("performance.css").toExternalForm());
        scene.getStylesheets().add(JFXCentralApp.class.getResource("markdown.css").toExternalForm());

        if (WebAPI.isBrowser()) {
            WebAPI webAPI = WebAPI.getWebAPI(stage);

            boolean mobile = false;
            if (webAPI != null) {
                Rectangle2D browserSize = webAPI.getBrowserSize();
                mobile = browserSize.getWidth() < 800;
            }

            if (!mobile) {
                webAPI.darkMode().addListener(it -> {
                    updateDark(scene, webAPI.isDarkMode());
                });
                updateDark(scene, webAPI.isDarkMode());
            }
        } else {
            updateDark(scene, Detector.isDarkMode());
        }

        stage.initStyle(StageStyle.UNDECORATED);
        stage.setOnCloseRequest(evt -> System.exit(0));
        stage.setTitle("JFX-Central");
        stage.setScene(scene);
        stage.setWidth(CustomStage.MIN_STAGE_WIDTH);
        stage.setHeight(CustomStage.MIN_STAGE_HEIGHT);
        stage.getIcons().add(new javafx.scene.image.Image(JFXCentralApp.class.getResource("duke.png").toExternalForm()));

        if (!WebAPI.isBrowser()) {
            StageManager.install(stage, "main-window", CustomStage.MIN_STAGE_WIDTH, CustomStage.MIN_STAGE_HEIGHT);
        }

        stage.show();

        if (Boolean.getBoolean("cssfx")) {
            DisplayService.create().ifPresentOrElse(service -> {
                if (service.isDesktop()) {
                    CSSFX.start();
                }
            }, () -> CSSFX.start());
        }

        if (WebAPI.isBrowser()) {
            showHomeOrLoadingView(app, stage);
        }

        if (!WebAPI.isBrowser()) {
            darkModeProperty().addListener(it -> Platform.runLater(() -> updateDark(scene, isDarkMode())));
            watchForAppearanceChanged(scene);
        }
    }

    private void updateDark(Scene scene, boolean darkMode) {
        System.out.println("updating dark: " + darkMode);
        scene.setFill(Color.web("#4483A0"));
        scene.getStylesheets().remove(JFXCentralApp.class.getResource("dark.css").toExternalForm());
        scene.getStylesheets().remove(JFXCentralApp.class.getResource("markdown-dark.css").toExternalForm());
        if (darkMode) {
            scene.setFill(Color.rgb(60, 63, 65));
            scene.getStylesheets().add(JFXCentralApp.class.getResource("dark.css").toExternalForm());
            scene.getStylesheets().add(JFXCentralApp.class.getResource("markdown-dark.css").toExternalForm());
        }
    }

    private void buildTrayIcon(Stage stage) {
        FXTrayIcon icon = new FXTrayIcon(stage, JFXCentralApp.class.getResource("duke.png"));

        Menu libraries = new Menu("Libraries");
        Menu people = new Menu("People");
        Menu tools = new Menu("Tools");
        Menu blogs = new Menu("Blogs");
        Menu videos = new Menu("Videos");
        Menu tutorials = new Menu("Tutorials");
        Menu downloads = new Menu("Downloads");
        Menu companies = new Menu("Companies");
        Menu books = new Menu("Books");
        Menu realWorld = new Menu("Real World Apps");
        Menu tips = new Menu("Tips & Tricks");

        DataRepository.getInstance().getTools().stream().sorted(Comparator.comparing(ModelObject::getName)).forEach(mo -> createMenuItem(tools, mo));
        DataRepository.getInstance().getPeople().stream().sorted(Comparator.comparing(ModelObject::getName)).forEach(mo -> createMenuItem(people, mo));
        DataRepository.getInstance().getVideos().stream().sorted(Comparator.comparing(ModelObject::getName)).forEach(mo -> createMenuItem(videos, mo, modelObject -> {
            try {
                Desktop.getDesktop().browse(URI.create("https://www.youtube.com/watch?v=" + modelObject.getId()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }));
        DataRepository.getInstance().getTutorials().stream().sorted(Comparator.comparing(ModelObject::getName)).forEach(mo -> createMenuItem(tutorials, mo));
        DataRepository.getInstance().getCompanies().stream().sorted(Comparator.comparing(ModelObject::getName)).forEach(mo -> createMenuItem(companies, mo));
        DataRepository.getInstance().getDownloads().stream().sorted(Comparator.comparing(ModelObject::getName)).forEach(mo -> createMenuItem(downloads, mo));
        DataRepository.getInstance().getBlogs().stream().sorted(Comparator.comparing(ModelObject::getName)).forEach(mo -> createMenuItem(blogs, mo));
        DataRepository.getInstance().getLibraries().stream().sorted(Comparator.comparing(ModelObject::getName)).forEach(mo -> createMenuItem(libraries, mo));
        DataRepository.getInstance().getBooks().stream().sorted(Comparator.comparing(ModelObject::getName)).forEach(mo -> createMenuItem(books, mo));
        DataRepository.getInstance().getRealWorldApps().stream().sorted(Comparator.comparing(ModelObject::getName)).forEach(mo -> createMenuItem(realWorld, mo));
        DataRepository.getInstance().getTips().stream().sorted(Comparator.comparing(ModelObject::getName)).forEach(mo -> createMenuItem(tips, mo));

        MenuItem showApp = new MenuItem("Main Window");
        showApp.setOnAction(evt -> {
            stage.show();
            stage.setIconified(false);
            stage.toFront();
        });
        icon.addMenuItem(showApp);

        MenuItem openjfx = new MenuItem("Visit openjfx.io");
        openjfx.setOnAction(evt -> showURL("https://openjfx.io"));
        icon.addMenuItem(openjfx);

        icon.addSeparator();

        MenuItem iconBrowser = new MenuItem("Ikonli Browser");
        iconBrowser.setOnAction(evt -> showIkonliBrowser());
        icon.addMenuItem(iconBrowser);

//        MenuItem scenicView = new MenuItem("Scenic View");
//        scenicView.setOnAction(evt -> showScenicView());
//        icon.addMenuItem(scenicView);

        MenuItem showcase = new MenuItem("Showcase");
        showcase.setOnAction(evt -> showShowcase());
        icon.addMenuItem(showcase);

        MenuItem cssDocs = new MenuItem("CSS Documentation");
        cssDocs.setOnAction(evt -> showURL("https://openjfx.io/javadoc/17/javafx.graphics/javafx/scene/doc-files/cssref.html"));
        icon.addMenuItem(cssDocs);

        icon.addSeparator();

        MenuItem addInfoToJfxCentral = new MenuItem("Add Info to JFX-Central");
        addInfoToJfxCentral.setOnAction(evt -> showURL("https://github.com/dlemmermann/jfxcentral-data"));
        icon.addMenuItem(addInfoToJfxCentral);

        MenuItem reportIssue = new MenuItem("Report an Issue");
        reportIssue.setOnAction(evt -> showURL("https://github.com/dlemmermann/jfxcentral/issues"));
        icon.addMenuItem(reportIssue);

        icon.addSeparator();

        icon.addMenuItem(libraries);
        icon.addMenuItem(tools);
        icon.addMenuItem(books);
        icon.addMenuItem(people);
        icon.addMenuItem(blogs);
        icon.addMenuItem(videos);
        icon.addMenuItem(tutorials);
        icon.addMenuItem(companies);
        icon.addMenuItem(downloads);
        icon.addMenuItem(realWorld);
        icon.addMenuItem(tips);

        icon.addExitItem(true);

        icon.show();
    }

    private void showURL(String url) {
        try {
            Desktop.getDesktop().browse(URI.create(url));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Stage showcaseStage;

    private void showShowcase() {
        if (showcaseStage == null) {
            CssShowcaseView view = new CssShowcaseView();
            Scene scene = new Scene(view);

            showcaseStage = new Stage(StageStyle.DECORATED);
            showcaseStage.setTitle("ShowcaseFX");
            showcaseStage.setScene(scene);
            showcaseStage.setWidth(1000);
            showcaseStage.setHeight(800);

            StageManager.install(showcaseStage, "showcasefx", CustomStage.MIN_STAGE_WIDTH, CustomStage.MIN_STAGE_HEIGHT);
        }

        showcaseStage.setIconified(false);
        showcaseStage.show();
        showcaseStage.toFront();
    }

    private Stage ikonliStage;

    private void showIkonliBrowser() {
        if (ikonliStage == null) {
            SectionPane sectionPane = new SectionPane();
            sectionPane.setTitle("Ikonli Browser");
            sectionPane.setSubtitle("Explore all available icon fonts in Ikonli");
            sectionPane.getNodes().add(new IkonliBrowser());
            sectionPane.setPrefHeight(0);
            sectionPane.setMinHeight(0);

            Scene scene = new Scene(sectionPane);
            scene.getStylesheets().add(JFXCentralApp.class.getResource("styles.css").toExternalForm());

            ikonliStage = new Stage(StageStyle.DECORATED);

            ikonliStage.setTitle("Ikonli Browser");
            ikonliStage.setScene(scene);
            ikonliStage.setWidth(900);
            ikonliStage.setHeight(700);

            StageManager.install(ikonliStage, "ikonli-browser", CustomStage.MIN_STAGE_WIDTH, CustomStage.MIN_STAGE_HEIGHT);
        }

        ikonliStage.setIconified(false);
        ikonliStage.show();
        ikonliStage.toFront();
    }

    private void createMenuItem(Menu people, ModelObject mo) {
        createMenuItem(people, mo, m -> showModelObject(m));
    }

    private void createMenuItem(Menu people, ModelObject mo, Consumer<ModelObject> consumer) {
        MenuItem item = new MenuItem(mo.getName());
        item.setOnAction(evt -> consumer.accept(mo));
        people.getItems().add(item);
    }

    private void showModelObject(ModelObject mo) {
        Util.getSessionManager(app).gotoURL(PageUtil.getLink(mo));
        Stage stage = (Stage) app.getScene().getWindow();
        stage.show();
        stage.toFront();
    }

    public static boolean isRepositoryInitialized() {
        return repositoryInitialized;
    }

    public static void updateRepositoryInBackground(ProgressMonitor monitor, Runnable callback) {
        Thread thread = new Thread(() -> {
            try {
                updateRepository(monitor);
                callback.run();
            } catch (GitAPIException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        thread.setDaemon(true);
        thread.setName("Repository Update Thread");
        thread.start();
    }

    public static void updateRepository(ProgressMonitor monitor) throws GitAPIException, IOException {

        if(System.getProperty("jfxcentral.repo") == null) {
            System.out.println("updating repository, monitor = " + monitor);
            File repoDirectory = DataRepository.getInstance().getRepositoryDirectory();
            if (!repoDirectory.exists()) {
                Git.cloneRepository()
                        .setURI("https://github.com/dlemmermann/jfxcentral-data.git") //
                        .setBranch("live")
                        .setDirectory(repoDirectory)
                        .setProgressMonitor(monitor)
                        .call();
            } else {
                repoDirectory = new File(DataRepository.getInstance().getRepositoryDirectory(), "/.git");
                Git git = new Git(new FileRepositoryBuilder().create(repoDirectory));
                git.pull().setContentMergeStrategy(ContentMergeStrategy.THEIRS).call();
            }

            Git.shutdown();
        }

        // trigger the data loading inside the data repository if needed
        DataRepository.getInstance().loadData();

        monitor.endTask();

        repositoryInitialized = true;
    }

    private void showHomeOrLoadingView(WebApp app, Stage stage) {
//        if (DataRepository.getInstance().isLoaded()) {
        showHome(app, stage);
//        } else {
//            LoadingView loadingView = new LoadingView(() -> showHome(app, stage));
//
//            Scene scene = stage.getScene();
//            scene.setRoot(loadingView);
//        }
    }

    private void showHome(WebApp webApp, Stage stage) {
        webApp.start(SessionManager.getDefault(webApp, stage));

        if (!WebAPI.isBrowser()) {
            CustomStage customStage = new CustomStage(stage, webApp);
            customStage.setCloseHandler(() -> System.exit(0));
            Scene scene = stage.getScene();
            scene.setRoot(customStage);
            scene.getStylesheets().add(JFXCentralApp.class.getResource("desktop.css").toExternalForm());
        } else {
            stage.getScene().setRoot(webApp);
        }
    }

    private void watchForAppearanceChanged(Scene scene) {
        if (!Detector.getOperatingSystem().equals(Detector.OperatingSystem.MACOS)) {
            return;
        }

        final Path path = FileSystems.getDefault().getPath(System.getProperty("user.home"), "/Library/Preferences/");
        Thread thread = new Thread(() -> {
            try {
                WatchService watchService = FileSystems.getDefault().newWatchService();
                try {
                    path.register(watchService, new WatchEvent.Kind[]{StandardWatchEventKinds.ENTRY_MODIFY, StandardWatchEventKinds.ENTRY_CREATE, StandardWatchEventKinds.ENTRY_DELETE}, SensitivityWatchEventModifier.HIGH);
                    WatchKey key;
                    while ((key = watchService.take()) != null) {
                        for (WatchEvent<?> event : key.pollEvents()) {
                            final Path changed = (Path) event.context();
                            if (changed.endsWith(".GlobalPreferences.plist")) {
                                setDarkMode(Detector.isDarkMode());
                                //setAccentColor(Detector.getMacOSAccentColor());
                            }
                        }
                        key.reset();
                    }
                } finally {
                    watchService.close();
                }
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        });
        thread.setDaemon(true);
        thread.start();
    }

    private static final BooleanProperty darkMode = new SimpleBooleanProperty();

    public static boolean isDarkMode() {
        return darkMode.get();
    }

    public static BooleanProperty darkModeProperty() {
        return darkMode;
    }

    public static void setDarkMode(boolean dark) {
        darkMode.set(dark);
    }

    public static void main(String args[]) {
        System.setProperty("prism.lcdtext", "true");
        launch(args);
    }
}
