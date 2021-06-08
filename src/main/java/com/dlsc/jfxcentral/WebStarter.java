package com.dlsc.jfxcentral;

import com.dlsc.jfxcentral.data.DataRepository;
import com.dlsc.jfxcentral.data.ImageManager;
import com.dlsc.jfxcentral.views.IntroView;
import com.gluonhq.attach.display.DisplayService;
import com.jpro.web.sessionmanager.SessionManager;
import com.jpro.webapi.WebAPI;
import fr.brouillard.oss.cssfx.CSSFX;
import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class WebStarter extends Application {

    /* workaround to load data before starting the application */
    static {
        DataRepository.getInstance();
        ImageManager.getInstance();
    }

    @Override
    public void start(Stage stage) {
        WebApp app = new WebApp(stage);

        Parent root =  app;
        if(!WebAPI.isBrowser()) {
            root = new IntroView(app);
        }

        Scene scene = new Scene(root, 1200, 1200);
        scene.getStylesheets().add(JFXCentralApp.class.getResource("styles.css").toExternalForm());

        stage.setScene(scene);
        stage.show();

        DisplayService.create().ifPresentOrElse(service -> {
            if (service.isDesktop()) {
                CSSFX.start();
            }
        }, () -> {
            CSSFX.start();
        });

        app.start(SessionManager.getDefault(app, stage));
    }

    public static void main(String args[]) {
        System.setProperty("prism.lcdtext", "false");
        launch(args);
    }
}
