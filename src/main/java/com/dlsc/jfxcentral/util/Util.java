package com.dlsc.jfxcentral.util;

import com.gluonhq.attach.browser.BrowserService;
import com.jpro.webapi.WebAPI;
import javafx.collections.ObservableList;
import javafx.scene.Node;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class Util {

    public static void setLink(Node node, String link, String desc) {
        if (link == null) return;
        if (WebAPI.isBrowser()) {
            com.jpro.web.Util.setLink(node, link, desc);
        } else {
            browseOnClick(node, link);
        }
    }

    public static void setLink(Node node, String link, String desc, ObservableList<Node> parentChildren) {
        if (link == null) return;
        com.jpro.web.Util.setLink(node, link, desc, parentChildren);
    }

    private static void browseOnClick(Node node, String link) {
        node.setOnMouseClicked(e -> {
            browse(node, link);
        });
    }

    public static void browse(Node node, String link) {
        browse(node, link, true);
    }

    public static void browse(Node node, String link, boolean tab) {
        if (link == null) return;

        final String finalLink = link.trim();

        if (finalLink.startsWith("/")) {
            com.jpro.web.Util.gotoPage(node, finalLink);
        } else {
            if (WebAPI.isBrowser()) {
                WebAPI.getWebAPI(node.getScene()).openURLAsTab(finalLink);
            } else {
                BrowserService.create().ifPresentOrElse(service -> {
                    try {
                        service.launchExternalBrowser(finalLink);
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (URISyntaxException e) {
                        e.printStackTrace();
                    }
                }, () -> {

                    Desktop desktop = Desktop.getDesktop();
                    if (desktop.isSupported(Desktop.Action.BROWSE)) {
                        try {
                            desktop.browse(new URI(finalLink));
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (URISyntaxException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        }
    }
}
