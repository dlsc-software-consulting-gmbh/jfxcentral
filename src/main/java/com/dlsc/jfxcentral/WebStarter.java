package com.dlsc.jfxcentral;

import com.dlsc.jfxcentral.util.Util;
import com.dlsc.jfxcentral.views.Display;
import com.dlsc.jfxcentral.views.IntroView;
import com.dlsc.jfxcentral.views.RootPane;
import com.gluonhq.attach.display.DisplayService;
import com.jpro.web.sessionmanager.SessionManager;
import com.jpro.webapi.WebAPI;
import fr.brouillard.oss.cssfx.CSSFX;
import javafx.application.Application;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class WebStarter extends Application {

    /* workaround to load data before starting the application */
    static Node workaround = new RootPane();

    @Override
    public void start(Stage stage) {
        WebApp app = new WebApp(stage);

        Parent root =  app;
        if(!WebAPI.isBrowser()) {
            root = new IntroView(app);
        }

        Scene scene = new Scene(root, 1200, 800);
        scene.setFill(Color.TRANSPARENT);
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
