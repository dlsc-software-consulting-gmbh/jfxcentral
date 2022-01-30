import org.kordamp.ikonli.IkonProvider;

open module jfxcentral {
    requires java.instrument;
    requires org.scenicview.scenicview;
    requires FXTrayIcon;
    requires org.eclipse.jgit;
    requires jfxcentral.data;
    requires com.rometools.rome;
    requires com.rometools.rome.utils;
    requires com.sandec.mdfx;
    requires com.google.gson;
    requires com.dlsc.gemsfx;
    requires jpro.webapi;
    requires java.logging;
    requires javafx.controls;
    requires javafx.web;
    requires javafx.media;
    requires javafx.swing;
    requires javafx.fxml;
    requires java.desktop;
    requires gson.javatime.serialisers;
    requires org.controlsfx.controls;
    requires org.kordamp.ikonli.materialdesign;
    requires com.gluonhq.attach.audio;
    requires com.gluonhq.attach.browser;
    requires com.gluonhq.attach.statusbar;
    requires com.gluonhq.attach.display;
    requires com.gluonhq.attach.orientation;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.ikonli.material;
    requires org.kordamp.ikonli.fontawesome;
    requires org.kordamp.ikonli.fontawesome5;
    requires org.apache.commons.lang3;
    requires jpro.web.core;
    requires de.sandec.jmemorybuddy;
    requires com.dlsc.showcasefx;

    exports com.dlsc.jfxcentral;

    requires fr.brouillard.oss.cssfx;
    requires org.kordamp.ikonli.core;
    requires jdk.xml.dom;
    requires java.prefs;

    uses IkonProvider;
}