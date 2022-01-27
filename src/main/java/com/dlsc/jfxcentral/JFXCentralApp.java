package com.dlsc.jfxcentral;

import com.dlsc.jfxcentral.data.DataRepository;
import com.dlsc.jfxcentral.views.IntroView;
import com.gluonhq.attach.display.DisplayService;
import com.jpro.web.sessionmanager.SessionManager;
import com.jpro.webapi.WebAPI;
import fr.brouillard.oss.cssfx.CSSFX;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.ProgressMonitor;
import org.eclipse.jgit.lib.TextProgressMonitor;
import org.eclipse.jgit.merge.ContentMergeStrategy;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;

import java.io.File;
import java.io.IOException;

public class JFXCentralApp extends Application {

    public static final String REPOSITORY = System.getProperty("jfxcentral.repo",System.getProperty("user.home") + "/" + ".jfxcentralrepo");

    private static boolean repositoryInitialized;

    @Override
    public void start(Stage stage) throws IOException, GitAPIException {
        DataRepository.BASE_URL = getRepoDirectory().toURI().toURL().toExternalForm() + "/";

        WebApp app = new WebApp(stage);

        Parent root = app;
        if (!WebAPI.isBrowser()) {
            root = new IntroView(() -> {
                if (repositoryInitialized) {
                    showHomeOrLoadingView(app, stage);
                }
            });
            updateRepositoryInBackground(((IntroView) root).getAnimationView(), () -> {});
        } else {
            if (!repositoryInitialized) {
                updateRepository(new TextProgressMonitor());
            }
        }

        CustomStage customStage = new CustomStage(stage, root);
        customStage.setCloseHandler(() -> Platform.exit());

        Scene scene = new Scene(customStage, 1250, 1200);
        scene.setFill(Color.rgb(68, 131, 160));

        scene.getStylesheets().add(JFXCentralApp.class.getResource("markdown.css").toExternalForm());

        stage.initStyle(StageStyle.UNDECORATED);
        stage.setOnCloseRequest(evt -> System.exit(0));
        stage.centerOnScreen();
        stage.setTitle("JFX-Central");
        stage.setScene(scene);
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
        customStage.setCloseHandler(() -> Platform.exit());
        stage.getScene().setRoot(customStage);
    }

    public static void main(String args[]) {
//        System.setProperty("prism.lcdtext", "true");
        launch(args);
    }
}
