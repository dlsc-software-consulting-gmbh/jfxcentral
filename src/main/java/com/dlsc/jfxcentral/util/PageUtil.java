package com.dlsc.jfxcentral.util;

import com.dlsc.jfxcentral.data.model.*;
import com.dlsc.jfxcentral.data.pull.PullRequest;
import com.dlsc.jfxcentral.views.View;

public class PageUtil {

    static public View getViewFromURL(String url) {
        if (!url.startsWith("/")) return null;
        int secondSlash = url.indexOf("/", "/".length());
        if (secondSlash == -1) secondSlash = url.length();
        String viewString = url.substring("/".length(), secondSlash);
        try {
            return View.valueOf(viewString.toUpperCase());
        } catch (IllegalArgumentException ex) {
            return null;
        }
    }

    static public String getIdFromURL(String url) {
        if (!url.startsWith("/")) return null;
        int secondSlash = url.indexOf("/", "/".length());
        if (secondSlash == -1) return null;
        String idString = url.substring(secondSlash + 1);
        return idString;
    }

    static public String getLink(View view) {
        return "/" + view.name().toLowerCase();
    }

    static public String getLink(ModelObject obj) {
        return getLink(getViewOfObject(obj)) + "/" + obj.getId();
    }

    static public View getViewOfObject(Object obj) {
        if (obj instanceof PullRequest) {
            return View.OPENJFX;
        } else if (obj instanceof Person) {
            return View.PEOPLE;
        } else if (obj instanceof Tutorial) {
            return View.TUTORIALS;
        } else if (obj instanceof RealWorldApp) {
            return View.REAL_WORLD;
        } else if (obj instanceof Company) {
            return View.COMPANIES;
        } else if (obj instanceof Tool) {
            return View.TOOLS;
        } else if (obj instanceof Library) {
            return View.LIBRARIES;
        } else if (obj instanceof Blog) {
            return View.BLOGS;
        } else if (obj instanceof Book) {
            return View.BOOKS;
        } else if (obj instanceof Video) {
            return View.VIDEOS;
        } else if (obj instanceof Download) {
            return View.DOWNLOADS;
        } else {
            return View.HOME;
        }
    }
}
