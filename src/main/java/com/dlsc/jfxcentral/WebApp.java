package com.dlsc.jfxcentral;

import javafx.stage.Stage;


public class WebApp extends com.jpro.web.WebApp {

    WebApp(Stage stage) {
        super(stage);

        addRouteJava((s) -> {
            if(s.equals("") || s.equals("/")) {
                return new com.jpro.web.Redirect("/?page=/HOME");
            } else {
                return null;
            }
        });

        addRouteJava((s) -> {
            if(s.startsWith("/?page=/memory")) {
                return new JMemoryBuddyView();
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