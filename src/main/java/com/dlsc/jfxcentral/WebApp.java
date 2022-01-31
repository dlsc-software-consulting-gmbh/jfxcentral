package com.dlsc.jfxcentral;

import com.dlsc.jfxcentral.util.PageUtil;
import com.jpro.webapi.WebAPI;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.geometry.Rectangle2D;
import javafx.stage.Stage;


public class WebApp extends com.jpro.web.WebApp {

    WebApp(Stage stage) {
        super(stage);

        addRouteJava((s) -> {
            if (s.equals("") || s.equals("/")) {
                return new com.jpro.web.Redirect("/home");
            }
            return null;
        });

        addRouteJava((s) -> {
            if (s.startsWith("/memory")) {
                return new MemoryView();
            }
            return null;
        });

        addRouteJava((s) -> {
            if (s.startsWith("/showcase")) {
                return new ShowcaseView();
            }
            return null;
        });

        addRouteJava((s) -> {
            if (s.startsWith("/refresh")) {
                return new RefreshView();
            }
            return null;
        });

        addRouteJava((s) -> {
            if (s.startsWith("/")) {
                if (PageUtil.getViewFromURL(s) != null) {
                    WebAPI webAPI = webAPI();
                    boolean mobile = false;
                    if (webAPI != null) {
                        Rectangle2D browserSize = webAPI.getBrowserSize();
                        mobile = browserSize.getWidth() < 800;
                    }
                    WebView webView = new WebView(s, mobile);
                    webView.disableEffectsProperty().bind(disableEffectsProperty());
                    return webView;
                } else {
                    return null;
                }
            } else {
                return null;
            }
        });

        addRouteJava((s) -> new ErrorView(s));
    }

    private final BooleanProperty disableEffects = new SimpleBooleanProperty(this, "disableEffects", true);

    public boolean isDisableEffects() {
        return disableEffects.get();
    }

    public BooleanProperty disableEffectsProperty() {
        return disableEffects;
    }

    public void setDisableEffects(boolean disableEffects) {
        this.disableEffects.set(disableEffects);
    }
}