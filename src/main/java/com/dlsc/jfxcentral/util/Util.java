package com.dlsc.jfxcentral.util;

import com.gluonhq.attach.browser.BrowserService;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import org.apache.commons.lang3.StringUtils;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class Util {

    public static void setLink(Node node, String link, String desc) {
        com.jpro.web.Util.setLink(node, link, desc);
    }
    public static void setLink(Node node, String link, String desc, ObservableList<Node> parentChildren) {
        com.jpro.web.Util.setLink(node, link, desc, parentChildren);
    }

    public static void browse(String link) {
        String url = StringUtils.deleteWhitespace(link);
        BrowserService.create().ifPresentOrElse(service -> {
            try {
                service.launchExternalBrowser(url);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
        }, () -> {
            if(Desktop.isDesktopSupported()) {
                Desktop desktop = Desktop.getDesktop();
                if (desktop.isSupported(Desktop.Action.BROWSE)) {
                    try {
                        desktop.browse(new URI(url));
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (URISyntaxException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }
}
