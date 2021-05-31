package com.dlsc.jfxcentral;

import com.dlsc.jfxcentral.page.DefaultPage;
import com.dlsc.jfxcentral.page.PageUtil;
import com.jpro.webapi.JProApplication;
import com.jpro.webapi.WebAPI;
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
                return new DefaultPage(s);
            } else {
                return null;
            }
        });
    }
}