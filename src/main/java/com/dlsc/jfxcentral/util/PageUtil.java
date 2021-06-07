package com.dlsc.jfxcentral.util;

import com.dlsc.jfxcentral.data.model.*;
import com.dlsc.jfxcentral.data.pull.PullRequest;
import com.dlsc.jfxcentral.views.View;
import com.dlsc.jfxcentral.views.detail.OpenJFXDetailView;

public class PageUtil {

    static public View getViewFromURL(String url) {
        if (!url.startsWith("/?page=/")) return null;
        int secondSlash = url.indexOf("/", "/?page=/".length());
        if (secondSlash == -1) secondSlash = url.length();
        String viewString = url.substring("/?page=/".length(), secondSlash);
        return View.valueOf(viewString);
    }

    static public String getIdFromURL(String url) {
        if (!url.startsWith("/?page=/")) return null;
        int secondSlash = url.indexOf("/", "/?page=/".length());
        if (secondSlash == -1) return null;
        String idString = url.substring(secondSlash + 1);
        return idString;
    }

    static public String getLink(View view) {
        return "/?page=/" + view.name();
    }

    static public String getLink(ModelObject obj) {
        return getLink(getViewOfObject(obj)) + "/" + obj.getId();
    }

    static public View getViewOfObject(ModelObject obj) {
        if(obj instanceof News) return View.NEWS;
        //if(obj instanceof PullRequest) return View.OPENJFX; // Hm???
        if(obj instanceof Person) return View.PEOPLE;
        //Tutorial
        if(obj instanceof RealWorldApp) return View.REAL_WORLD;
        if(obj instanceof Company) return View.COMPANIES;
        if(obj instanceof Tool) return View.TOOLS;
        if(obj instanceof Library) return View.LIBRARIES;
        if(obj instanceof Blog) return View.BLOGS;
        if(obj instanceof Book) return View.BOOKS;
        if(obj instanceof Video) return View.VIDEOS;
        if(obj instanceof Download) return View.DOWNLOADS;
        throw new RuntimeException("Couldn't find View for " + obj);
    }


}
