package com.dlsc.jfxcentral;

import com.dlsc.jfxcentral.data.DataRepository;
import com.dlsc.jfxcentral.views.IntroView;
import com.dlsc.jfxcentral.views.LoadingView;
import com.gluonhq.attach.display.DisplayService;
import com.jpro.web.sessionmanager.SessionManager;
import com.jpro.webapi.WebAPI;
import fr.brouillard.oss.cssfx.CSSFX;
import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class JFXCentralApp extends Application {

    @Override
    public void start(Stage stage) {
        WebApp app = new WebApp(stage);

        Parent root = app;
        if (!WebAPI.isBrowser()) {
            root = new IntroView(() -> showHomeOrLoadingView(app, stage));
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
        System.setProperty("prism.lcdtext", "false");
        launch(args);
    }
}
