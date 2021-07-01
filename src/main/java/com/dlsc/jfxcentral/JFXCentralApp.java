package com.dlsc.jfxcentral;

import com.dlsc.jfxcentral.data.DataRepository;
import com.dlsc.jfxcentral.views.IntroView;
import com.dlsc.jfxcentral.views.LoadingView;
import com.gluonhq.attach.display.DisplayService;
import com.jpro.web.sessionmanager.SessionManager;
import com.jpro.webapi.WebAPI;
import fr.brouillard.oss.cssfx.CSSFX;
import javafx.application.Application;
import javafx.beans.Observable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.ProgressMonitor;
import org.eclipse.jgit.lib.TextProgressMonitor;
import org.eclipse.jgit.merge.ContentMergeStrategy;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;

import java.io.File;
import java.io.IOException;

public class JFXCentralApp extends Application {

    private static final String REPOSITORY = "jfxcentralrepo";

    @Override
    public void start(Stage stage) throws GitAPIException, IOException {
        DataRepository.BASE_URL = getRepoDirectory().toURI().toURL().toExternalForm() + "/";

        DataRepository.getInstance().getPosts().addListener((Observable obs) -> System.out.println(">>>> posts: " + DataRepository.getInstance().getPosts().size()));

        WebApp app = new WebApp(stage);

        Parent root = app;
        if (!WebAPI.isBrowser()) {
            root = new IntroView(() -> showHomeOrLoadingView(app, stage));
            updateRepositoryInBackground(((IntroView) root).getAnimationView());
        } else {
            updateRepositoryInBackground(new TextProgressMonitor());
        }

        Scene scene = new Scene(root, 1250, 1200);
        scene.setFill(Color.rgb(68, 131, 160));

        scene.getStylesheets().add(JFXCentralApp.class.getResource("styles.css").toExternalForm());
        scene.getStylesheets().add(JFXCentralApp.class.getResource("markdown.css").toExternalForm());

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

    private void updateRepositoryInBackground(ProgressMonitor monitor) {
        Thread thread = new Thread(() -> {
            try {
                updateRepository(monitor);
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
            System.out.println("cloning repo");
            Git.cloneRepository()
                    .setURI("https://github.com/dlemmermann/jfxcentral-data.git")
                    .setBranch("live")
                    .setDirectory(repoDirectory)
                    .setProgressMonitor(monitor)
                    .call();
        } else {
            System.out.println("updating repo");
            repoDirectory = new File(System.getProperty("user.home") + "/" + REPOSITORY + "/.git");
            Git git = new Git(new FileRepositoryBuilder().create(repoDirectory));
            git.pull().setContentMergeStrategy(ContentMergeStrategy.THEIRS).call();
            System.out.println("done updating");
        }
    }

    private static File getRepoDirectory() {
        return new File(System.getProperty("user.home") + "/" + REPOSITORY);
    }

    private void showHomeOrLoadingView(WebApp app, Stage stage) {
        if (DataRepository.getInstance().isLoaded()) {
            showHome(app, stage);
        } else {
            LoadingView loadingView = new LoadingView(() -> showHome(app, stage));

            Scene scene = stage.getScene();
            scene.setRoot(loadingView);
        }
    }

    private void showHome(WebApp webApp, Stage stage) {
        webApp.start(SessionManager.getDefault(webApp, stage));
        stage.getScene().setRoot(webApp);
        stage.getScene().setFill(Color.rgb(224, 229, 234)); // reduce flickering
    }

    public static void main(String args[]) {
//        System.setProperty("prism.lcdtext", "true");
        launch(args);
    }
}
