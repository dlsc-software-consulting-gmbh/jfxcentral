package com.dlsc.jfxcentral;

import com.dlsc.jfxcentral.data.DataRepository;
import com.dlsc.jfxcentral.util.PageUtil;
import javafx.geometry.Rectangle2D;
import javafx.stage.Stage;


public class WebApp extends com.jpro.web.WebApp {

    WebApp(Stage stage) {
        super(stage);

        addRouteJava((s) -> {
            if (s.equals("") || s.equals("/")) {
                return new com.jpro.web.Redirect("/home");
            } else {
                return null;
            }
        });

        addRouteJava((s) -> {
            if (s.startsWith("/memory")) {
                return new MemoryView();
            } else {
                return null;
            }
        });

        addRouteJava((s) -> {
            if (s.startsWith("/refresh")) {
                DataRepository.getInstance().refreshData();
                return new com.jpro.web.Redirect("/home");
            }
            return null;
        });

        addRouteJava((s) -> {
            if (s.startsWith("/")) {
                if (PageUtil.getViewFromURL(s) != null) {
                    webAPI().getHeaders().forEach((key, value) -> System.out.println(key + ": " + value));
                    Rectangle2D browserSize = webAPI().getBrowserSize();
                    System.out.println("browserSize: " + browserSize);
                    System.out.println("--------");
                    return new WebView(s, browserSize.getWidth() < 800);
                } else {
                    return null;
                }
            } else {
                return null;
            }
        });

        addRouteJava((s) -> new ErrorView(s));
    }
}