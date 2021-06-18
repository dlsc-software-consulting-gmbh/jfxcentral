package com.dlsc.jfxcentral;

import com.dlsc.jfxcentral.data.DataRepository;
import javafx.stage.Stage;


public class WebApp extends com.jpro.web.WebApp {

    WebApp(Stage stage) {
        super(stage);

        addRouteJava((s) -> {
            if(s.equals("") || s.equals("/")) {
                return new com.jpro.web.Redirect("/HOME");
            } else {
                return null;
            }
        });

        addRouteJava((s) -> {
            if(s.startsWith("/memory")) {
                return new MemoryView();
            } else {
                return null;
            }
        });

        addRouteJava((s) -> {
            if(s.startsWith("/refresh")) {
                DataRepository.getInstance().refreshData();
                return new com.jpro.web.Redirect("/HOME");
            }
            return null;
        });

        addRouteJava((s) -> {
            if(s.startsWith("/")) {
                return new WebView(s);
            } else {
                return null;
            }
        });
    }
}