module com.dlsc.jfxcentral {
    requires com.google.gson;
    requires javafx.controls;
    requires javafx.web;
    requires javafx.media;
    requires com.dlsc.gemsfx;
    requires com.gluonhq.attach.statusbar;
    requires com.gluonhq.attach.display;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.ikonli.material;
    requires org.kordamp.ikonli.fontawesome;

    opens com.dlsc.jfxcentral;

    requires fr.brouillard.oss.cssfx;
}