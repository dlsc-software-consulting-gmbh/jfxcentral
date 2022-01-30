package com.dlsc.jfxcentral;

import com.dlsc.jfxcentral.data.DataRepository;
import com.dlsc.jfxcentral.data.model.ModelObject;
import com.dlsc.jfxcentral.panels.SectionPane;
import com.dlsc.jfxcentral.util.PageUtil;
import com.dlsc.jfxcentral.util.StageSession;
import com.dlsc.jfxcentral.views.IntroView;
import com.dlsc.jfxcentral.views.ikonli.IkonliBrowser;
import com.dlsc.showcase.CssShowcaseView;
import com.dustinredmond.fxtrayicon.FXTrayIcon;
import com.gluonhq.attach.display.DisplayService;
import com.jpro.web.Util;
import com.jpro.web.sessionmanager.SessionManager;
import com.jpro.webapi.WebAPI;
import fr.brouillard.oss.cssfx.CSSFX;
import javafx.application.Application;
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
import java.util.Comparator;
import java.util.function.Consumer;
import java.util.prefs.Preferences;

public class JFXCentralApp extends Application {

    public static final String REPOSITORY = System.getProperty("jfxcentral.repo", System.getProperty("user.home") + "/" + ".jfxcentralrepo");

    private static boolean repositoryInitialized;
    private WebApp app;

    @Override
    public void start(Stage stage) throws IOException, GitAPIException {
        DataRepository.BASE_URL = getRepoDirectory().toURI().toURL().toExternalForm() + "/";

        app = new WebApp(stage);

        Parent root = app;
        if (!WebAPI.isBrowser()) {
            root = new IntroView(() -> {
                if (repositoryInitialized) {
                    showHomeOrLoadingView(app, stage);
                }
            });
            updateRepositoryInBackground(((IntroView) root).getAnimationView(), () -> buildTrayIcon(stage));
        } else {
            if (!repositoryInitialized) {
                updateRepository(new TextProgressMonitor());
            }
        }

        CustomStage customStage = new CustomStage(stage, root);
        customStage.setCloseHandler(() -> System.exit(0));

        Scene scene = new Scene(customStage, 1250, 800);
        scene.setFill(Color.rgb(68, 131, 160));

        scene.getStylesheets().add(JFXCentralApp.class.getResource("styles.css").toExternalForm());
        scene.getStylesheets().add(JFXCentralApp.class.getResource("markdown.css").toExternalForm());

        stage.initStyle(StageStyle.UNDECORATED);
        stage.setOnCloseRequest(evt -> System.exit(0));
        stage.centerOnScreen();
        stage.setTitle("JFX-Central");
        stage.setScene(scene);

        new StageSession(stage, Preferences.userNodeForPackage(JFXCentralApp.class));

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

        MenuItem openjfx = new MenuItem("Visit openjfx.io");
        openjfx.setOnAction(evt -> showURL("https://openjfx.io"));
        icon.addMenuItem(openjfx);

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

        // ------
        icon.addSeparator();

        MenuItem addInfoToJfxCentral = new MenuItem("Add Info to JFX-Central");
        addInfoToJfxCentral.setOnAction(evt -> showURL("https://github.com/dlemmermann/jfxcentral-data"));
        icon.addMenuItem(addInfoToJfxCentral);

        MenuItem reportIssue = new MenuItem("Report an Issue");
        reportIssue.setOnAction(evt -> showURL("https://github.com/dlemmermann/jfxcentral-data/issues"));
        icon.addMenuItem(reportIssue);

        // ------
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

        // ------
        icon.addSeparator();

        icon.addExitItem(true);

        // ------
        icon.addSeparator();

        icon.show();
    }

    private void showURL(String url) {
        try {
            Desktop.getDesktop().browse(URI.create(url));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showShowcase() {
        CssShowcaseView view = new CssShowcaseView();
        Scene scene = new Scene(view);

        Stage stage = new Stage(StageStyle.DECORATED);
        stage.setTitle("ShowcaseFX");
        stage.setScene(scene);
        stage.setWidth(1000);
        stage.setHeight(800);
        stage.show();
    }

    private void showIkonliBrowser() {
        SectionPane sectionPane = new SectionPane();
        sectionPane.setTitle("Ikonli Browser");
        sectionPane.setSubtitle("Explore all available icon fonts in Ikonli");
        sectionPane.getNodes().add(new IkonliBrowser());
        sectionPane.setPrefHeight(0);
        sectionPane.setMinHeight(0);

        Scene scene = new Scene(sectionPane);
        scene.getStylesheets().add(JFXCentralApp.class.getResource("styles.css").toExternalForm());

        Stage stage = new Stage(StageStyle.DECORATED);
        stage.setTitle("Ikonli Browser");
        stage.setScene(scene);
        stage.setWidth(900);
        stage.setHeight(700);
        stage.show();
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
        System.out.println("updating repository, monitor = " + monitor);
        File repoDirectory = getRepoDirectory();
        if (!repoDirectory.exists()) {
            Git.cloneRepository()
                    .setURI("https://github.com/dlemmermann/jfxcentral-data.git")
                    .setBranch("live")
                    .setDirectory(repoDirectory)
                    .setProgressMonitor(monitor)
                    .call();
        } else {
            repoDirectory = new File(REPOSITORY + "/.git");
            Git git = new Git(new FileRepositoryBuilder().create(repoDirectory));
            git.pull().setContentMergeStrategy(ContentMergeStrategy.THEIRS).call();
        }

        Git.shutdown();

        monitor.endTask();

        // trigger the data loading inside the data repository if needed
        DataRepository.getInstance();

        repositoryInitialized = true;
    }

    private static File getRepoDirectory() {
        return new File(REPOSITORY);
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

        CustomStage customStage = new CustomStage(stage, webApp);
        customStage.setCloseHandler(() -> System.exit(0));
        Scene scene = stage.getScene();
        scene.setRoot(customStage);

        if (!WebAPI.isBrowser()) {
            scene.getStylesheets().add(JFXCentralApp.class.getResource("desktop.css").toExternalForm());
        }
    }

    public static void main(String args[]) {
        System.setProperty("prism.lcdtext", "true");
        launch(args);
    }
}
