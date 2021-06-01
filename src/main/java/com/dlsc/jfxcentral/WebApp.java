package com.dlsc.jfxcentral;

import com.dlsc.jfxcentral.views.page.JPROWebView;
import javafx.stage.Stage;


public class WebApp extends com.jpro.web.WebApp {

    WebApp(Stage stage) {
        super(stage);

        addRouteJava((s) -> {
            if(s.equals("") || s.equals("/")) {
                return new com.jpro.web.Redirect("/?page=/PEOPLE/a.almiray");
            } else {
                return null;
            }
        });

        addRouteJava((s) -> {
            if(s.startsWith("/?page=")) {
                return new JPROWebView(s);
            } else {
                return null;
            }
        });
    }
}