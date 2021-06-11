package com.dlsc.jfxcentral.views.page;

import com.dlsc.jfxcentral.data.model.*;
import org.kordamp.ikonli.Ikon;
import org.kordamp.ikonli.fontawesome.FontAwesome;
import org.kordamp.ikonli.fontawesome5.FontAwesomeBrands;
import org.kordamp.ikonli.material.Material;
import org.kordamp.ikonli.materialdesign.MaterialDesign;

public class StandardIcons {

    public static Ikon HOME = Material.HOME;
    public static Ikon OPENJFX = FontAwesomeBrands.JAVA;
    public static Ikon BOOK = FontAwesomeBrands.AMAZON;
    public static Ikon BLOG = FontAwesomeBrands.BLOGGER;
    public static Ikon TOOL = FontAwesome.GEAR;
    public static Ikon NEWS = Material.NOTES;
    public static Ikon LIBRARY = FontAwesomeBrands.GITHUB;
    public static Ikon VIDEO = FontAwesomeBrands.YOUTUBE;
    public static Ikon TUTORIAL = Material.SCHOOL;
    public static Ikon COMPANY = MaterialDesign.MDI_FACTORY;
    public static Ikon PERSON = Material.PERSON;
    public static Ikon REAL_WORLD = Material.APPS;
    public static Ikon DOWNLOAD = Material.FILE_DOWNLOAD;

    public static Ikon getIcon(Object obj) {
        Class cl = obj.getClass();
        if (cl.equals(Book.class)) {
            return BOOK;
        } else if (cl.equals(Blog.class)) {
            return BLOG;
        } else if (cl.equals(Tool.class)) {
            return TOOL;
        } else if (cl.equals(News.class)) {
            return NEWS;
        } else if (cl.equals(Library.class)) {
            return LIBRARY;
        } else if (cl.equals(Video.class)) {
            return VIDEO;
        } else if (cl.equals(Tutorial.class)) {
            return TUTORIAL;
        } else if (cl.equals(Company.class)) {
            return COMPANY;
        } else if (cl.equals(Person.class)) {
            return PERSON;
        } else if (cl.equals(RealWorldApp.class)) {
            return REAL_WORLD;
        } else if (cl.equals(Download.class)) {
            return DOWNLOAD;
        }

        throw new IllegalArgumentException("unsupported class");
    }
}
