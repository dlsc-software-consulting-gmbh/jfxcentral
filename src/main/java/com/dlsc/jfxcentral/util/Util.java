package com.dlsc.jfxcentral.util;

import com.gluonhq.attach.browser.BrowserService;
import org.apache.commons.lang3.StringUtils;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class Util {

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
