package com.dlsc.jfxcentral.page;

import com.dlsc.jfxcentral.View;

public class PageUtil {

    static public View getViewFromURL(String url) {
        if(!url.startsWith("/?page=/")) return null;
        int secondSlash = url.indexOf("/","/?page=/".length());
        if(secondSlash == -1) secondSlash = url.length();
        String viewString = url.substring("/?page=/".length(), secondSlash);
        return View.valueOf(viewString);
    }

    static public String getIdFromURL(String url) {
        if(!url.startsWith("/?page=/")) return null;
        int secondSlash = url.indexOf("/","/?page=/".length());
        if(secondSlash == -1) return null;
        String idString = url.substring(secondSlash+1);
        return idString;
    }

    static public String viewURL(View view) {
        return "/?page=/"+view.name();
    }
}
